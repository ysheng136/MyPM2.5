package com.pm25.mypm25;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pm25.mypm25.gson.Weather;
import com.pm25.mypm25.gson3.Daily;
import com.pm25.mypm25.gson3.Forecast2;
import com.pm25.mypm25.gson3.Hourly;
import com.pm25.mypm25.service.AutoUpdateService;
import com.pm25.mypm25.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/29.
 */

public class WeatherFragment extends Fragment {

    //标题栏控件
    private TextView title_city;
    private TextView title_time;

    //Now控件
    private TextView degree_text;
    private TextView weather_info;
    private TextView wind_text1;
    private TextView wind_text2;
    private TextView wet_text;
    private TextView temp_text;
    private TextView qlty_text;

    //suggestion
    private TextView dress_text;
    private TextView ultraviolet_text;
    private TextView sport_text;
    private TextView wash_car_text;
    private TextView comfort_now_text;

    //下滑刷新控件
    private SwipeRefreshLayout swipeRefresh;

    private LinearLayout forecastLayout;

    private RecyclerView recyclerView;


    //地图信息
    private String city_data;    //城市
    private double latitude_data; //纬度
    private double longitude_data;  //经度
    private String district_data;  //地区


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view1, container, false);

        //初始化控件
        init(view);
        //注册EventBus
        EventBus.getDefault().register(this);

        return view;
    }

    /*
    初始化控件
     */
    public void init(View view) {
        title_city = (TextView) view.findViewById(R.id.title_city);
        title_time = (TextView) view.findViewById(R.id.title_time);
        degree_text = (TextView) view.findViewById(R.id.degree_text);
        weather_info = (TextView) view.findViewById(R.id.weather_info);
        wind_text1 = (TextView) view.findViewById(R.id.wind_text1);
        wind_text2 = (TextView) view.findViewById(R.id.wind_text2);
        wet_text = (TextView) view.findViewById(R.id.wet_text);
        temp_text = (TextView) view.findViewById(R.id.temp_text);
        qlty_text = (TextView) view.findViewById(R.id.qlty_info);
        dress_text = (TextView) view.findViewById(R.id.dress_text);
        ultraviolet_text = (TextView) view.findViewById(R.id.ultraviolet_text);
        sport_text = (TextView) view.findViewById(R.id.sport_text);
        wash_car_text = (TextView) view.findViewById(R.id.wash_car_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        comfort_now_text = (TextView)view.findViewById(R.id.comfort_now_text);

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

    }

    @Subscribe
    public void onEvent(MapMessage mapMessage) {

        //位置信息
        city_data = mapMessage.getCity_data();
        latitude_data = mapMessage.getLatitude_data();
        longitude_data = mapMessage.getLongitude_data();
        district_data = mapMessage.getDistrict_data();

        // SharedPreferences 存储 和风天气
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String weatherString = prefs.getString("weather", null);

        // SharedPreferences 存储阿里云天气
        SharedPreferences prefs3 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String forecastString2 = prefs3.getString("forecast2", null);

        /*
            天气情况
         */
        if (weatherString != null || forecastString2 != null) {
            //和风天气情况 有缓存时直接解析数据
            Weather weather = HttpUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);

            //阿里云天气预报 有缓存时直接解析数据
            Forecast2 forecast2 = HttpUtil.handleForecastResponse2(forecastString2);
            showResultInfo2(forecast2);

        } else {
            //天气情况 无缓存时根据经纬度去服务器查询天气
            requestWeather(longitude_data, latitude_data);

            //阿里云天气预报 无缓存时根据经纬度去服务器查询天气
            requestForecast2(longitude_data, latitude_data);
        }

        //设置城市地区
        title_city.setText(city_data + "|" + district_data);

        //下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //和风天气
                requestWeather(longitude_data, latitude_data);
                //阿里云天气
                requestForecast2(longitude_data, latitude_data);
            }
        });
    }

    /**
     * 根据经纬度请求和风城市天气信息
     */
    public void requestWeather(double longitude_data, double latitude_data) {
        //和风的天气接口
        final String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + longitude_data + "," +
                latitude_data + "&key=0e51a1c839234a8a84789c9f349cfe15";
        //解析接口数据
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseData = response.body().string();

                final Weather weather = HttpUtil.handleWeatherResponse(responseData);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("weather", responseData);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    /**
     * 根据经纬度请求阿里云城市天气预报信息
     */
    public void requestForecast2(double longitude_data, double latitude_data) {
        //阿里云的天气接口
        final String weatherUrl = "http://jisutqybmf.market.alicloudapi.com/weather/query?location=" + latitude_data + "," + longitude_data;
        //解析接口数据
        HttpUtil.sendOkHttpRequest2(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseData = response.body().string();
                final Forecast2 forecast2 = HttpUtil.handleForecastResponse2(responseData);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (forecast2 != null && "0".equals(forecast2.getStatus()) && "ok".equals(forecast2.getMsg())) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("forecast2", responseData);
                            editor.apply();
                            showResultInfo2(forecast2);
                        } else {
                            Toast.makeText(getContext(), "获取天气信息失败2", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取天气信息失败2", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 处理并展示和风Weather实体类中的数据
     */
    public void showWeatherInfo(Weather weather) {

        Intent intent = new Intent(getActivity(), AutoUpdateService.class);
        getActivity().startService(intent);

        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        String windDirection = weather.now.wind.direction;
        String windscreen = weather.now.wind.screen + "级";
        String wet = weather.now.humidity + "%";
        String feelsLike = weather.now.feelsLike + "℃";
        String comfort = weather.suggestion.comfort.info;
        String carWash = weather.suggestion.carWash.info;
        String dress = weather.suggestion.dress.info;
        String sport = weather.suggestion.sport.info;
        String ultraviolet = weather.suggestion.ultraviolet.info;

        degree_text.setText(degree);
        weather_info.setText(weatherInfo);
        wind_text1.setText(windDirection);
        wind_text2.setText(windscreen);
        wet_text.setText(wet);
        temp_text.setText(feelsLike);
        comfort_now_text.setText(comfort);
        wash_car_text.setText(carWash);
        dress_text.setText(dress);
        sport_text.setText(sport);
        ultraviolet_text.setText(ultraviolet);
    }

    /**
     * 处理并展示阿里云Result实体类中的数据
     */
    public void showResultInfo2(Forecast2 forecast2) {

        Intent intent = new Intent(getActivity(), AutoUpdateService.class);
        getActivity().startService(intent);

        String updatetime = forecast2.getResult().getUpdatetime().substring(11,16);
        title_time.setText(updatetime);

        String qlty = forecast2.getResult().getAqi().getQuality();
        qlty_text.setText(qlty);

        //设置aily预测
        showDailyForecast2(forecast2);

        //设置Hourly预测
        List<Hourly> hourly = forecast2.getResult().getHourly();
        //指定RecyclerView的布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        //适配器设置
        WeatherAdapter weatherAdapter = new WeatherAdapter(hourly);
        recyclerView.setAdapter(weatherAdapter);
    }

    /*
    处理并展示阿里云Daily实体类中的数据
     */
    public void showDailyForecast2(Forecast2 forecast2) {

        forecastLayout.removeAllViews();
        List<Daily> dailyList = forecast2.getResult().getDaily();

        for (Daily d : dailyList) {
            //将预测列表添加到预测布局
            View view = LayoutInflater.from(getContext()).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView date_text = (TextView) view.findViewById(R.id.date_text);
            TextView weather_text = (TextView) view.findViewById(R.id.weather_text);
            TextView max_text = (TextView) view.findViewById(R.id.Max_text);
            TextView min_text = (TextView) view.findViewById(R.id.Min_text);
            ImageView image = (ImageView)view.findViewById(R.id.iv_weather_png);

            date_text.setText(d.getDate());
            max_text.setText(d.getDay().getTemphigh() + "°");
            min_text.setText(d.getNight().getTemplow() + "°");
            weather_text.setText(d.getDay().getWeather());

            int img = 0;
            switch (d.getDay().getImg()){
                case "0": img = R.drawable.a0;break;
                case "1": img = R.drawable.a1;break;
                case "2": img = R.drawable.a2;break;
                case "3": img = R.drawable.a3;break;
                case "4": img = R.drawable.a4;break;
                case "5": img = R.drawable.a5;break;
                case "6": img = R.drawable.a6;break;
                case "7": img = R.drawable.a7;break;
                case "8": img = R.drawable.a8;break;
                case "9": img = R.drawable.a9;break;
                case "10": img = R.drawable.a10;break;
                case "11": img = R.drawable.a11;break;
                case "12": img = R.drawable.a12;break;
                case "13": img = R.drawable.a13;break;
                case "14": img = R.drawable.a14;break;
                case "15": img = R.drawable.a15;break;
                case "16": img = R.drawable.a16;break;
                case "17": img = R.drawable.a17;break;
                case "18": img = R.drawable.a18;break;
                case "19": img = R.drawable.a19;break;
                case "20": img = R.drawable.a20;break;
                case "21": img = R.drawable.a21;break;
                case "22": img = R.drawable.a22;break;
                case "23": img = R.drawable.a23;break;
                case "24": img = R.drawable.a24;break;
                case "25": img = R.drawable.a25;break;
                case "26": img = R.drawable.a26;break;
                case "27": img = R.drawable.a27;break;
                case "28": img = R.drawable.a28;break;
                case "29": img = R.drawable.a29;break;
                case "30": img = R.drawable.a30;break;
                case "31": img = R.drawable.a31;break;
                case "32": img = R.drawable.a32;break;
                case "49": img = R.drawable.a49;break;
                case "53": img = R.drawable.a53;break;
                case "54": img = R.drawable.a54;break;
                case "55": img = R.drawable.a55;break;
                case "56": img = R.drawable.a56;break;
                case "57": img = R.drawable.a57;break;
                case "58": img = R.drawable.a58;break;
                case "99": img = R.drawable.a99;break;
                case "301": img = R.drawable.a301;break;
                case "302": img = R.drawable.a302;break;
            }
            image.setImageResource(img);
            forecastLayout.addView(view);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}

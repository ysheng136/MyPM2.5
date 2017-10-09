package com.pm25.mypm25;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pm25.mypm25.gson.Weather;
import com.pm25.mypm25.gson2.Daily_Skycon;
import com.pm25.mypm25.gson2.Daily_aqi;
import com.pm25.mypm25.gson2.Daily_temperature;
import com.pm25.mypm25.gson2.Forecast;
import com.pm25.mypm25.gson2.Hourly_Skycon;
import com.pm25.mypm25.gson2.Hourly_aqi;
import com.pm25.mypm25.gson2.Hourly_temperature;
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

    private String TAG = "weather";
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

    //suggestion
    private TextView comfort_text;
    private TextView dress_text;
    private TextView ultraviolet_text;
    private TextView sport_text;
    private TextView wash_car_text;

    //forecast_item控件
    //    private TextView date_text;
    //    private TextView weather_text;
    //    private ImageView iv_weather_png;
    //    private TextView aqi_text;
    //    private TextView max_text;
    //    private TextView min_text;

    private SwipeRefreshLayout swipeRefresh;

    private LinearLayout forecastLayout;

    //地图信息
    private String city_data;
    private double latitude_data; //纬度
    private double longitude_data;  //经度
    private String district_data;

    //
    private TextView description_text;



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
        comfort_text = (TextView) view.findViewById(R.id.comfort_text);
        dress_text = (TextView) view.findViewById(R.id.dress_text);
        ultraviolet_text = (TextView) view.findViewById(R.id.ultraviolet_text);
        sport_text = (TextView) view.findViewById(R.id.sport_text);
        wash_car_text = (TextView) view.findViewById(R.id.wash_car_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        description_text = (TextView) view.findViewById(R.id.description_text);

        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


    }

    @Subscribe
    public void onEvent(MapMessage mapMessage) {
        city_data = mapMessage.getCity_data();
        latitude_data = mapMessage.getLatitude_data();
        longitude_data = mapMessage.getLongitude_data();
        district_data = mapMessage.getDistrict_data();

        Log.i(TAG, "onCreateView: " + latitude_data + " " + longitude_data);

        // SharedPreferences 存储
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String weatherString = prefs.getString("weather", null);

        SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String forecastString = prefs2.getString("forecast", null);
        Log.i(TAG, "forecastString: " + forecastString);

        /*
        天气情况
         */
        if (weatherString != null || forecastString != null) {
            //天气情况 有缓存时直接解析数据
            Weather weather = HttpUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);

            //天气预报 有缓存时直接解析数据
            Forecast forecast = HttpUtil.handleForecastResponse(forecastString);
            showResultInfo(forecast);

        } else {
            //天气情况 无缓存时根据经纬度去服务器查询天气
            requestWeather(longitude_data, latitude_data);

            //天气预报 无缓存时根据经纬度去服务器查询天气
            requestForecast(longitude_data, latitude_data);
        }

        title_city.setText(city_data + "|" + district_data);
        Log.i("weather", "onEvent: " + "经度" + longitude_data + "纬度" + latitude_data);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(longitude_data, latitude_data);
                requestForecast(longitude_data, latitude_data);
            }
        });


    }

    /**
     * 根据经纬度请求城市天气信息
     */
    public void requestWeather(double longitude_data, double latitude_data) {
        Log.i(TAG, "requestWeather: " + "经度" + longitude_data + "纬度" + latitude_data);
        final String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + longitude_data + "," +
                latitude_data + "&key=0e51a1c839234a8a84789c9f349cfe15";

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
     * 根据经纬度请求城市天气预报信息
     */
    public void requestForecast(double longitude_data, double latitude_data) {

        Log.i(TAG, "requestForecast: " + longitude_data + latitude_data);

        final String forecastUrl = "https://api.caiyunapp.com/v2/Nf8ohAGoXcBVU-Xj/" + longitude_data + "," +
                latitude_data + "/forecast.json";

        HttpUtil.sendOkHttpRequest(forecastUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //服务器返回的数据
                final String responseData = response.body().string();

                Log.i(TAG, "onResponse: " + responseData);

                final Forecast forecast = HttpUtil.handleForecastResponse(responseData);

                Log.i(TAG, "OnForecast : " + forecast);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (forecast != null && "ok".equals(forecast.getStatus())
                                && "ok".equals(forecast.getResult().getHourly().getStatus())
                                && "ok".equals(forecast.getResult().getDaily().getStatus())
                                ) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("forecast", responseData);
                            editor.apply();
                            showResultInfo(forecast);
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
                        Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    /**
     * 处理并展示Weather实体类中的数据
     */
    public void showWeatherInfo(Weather weather) {
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
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
        comfort_text.setText(comfort);
        wash_car_text.setText(carWash);
        dress_text.setText(dress);
        sport_text.setText(sport);
        ultraviolet_text.setText(ultraviolet);
        title_time.setText(updateTime);
    }


    /**
     * 处理并展示Result实体类中的数据
     */
    public void showResultInfo(Forecast forecast) {

        //Daily预报信息
        showDailyForecast(forecast);
        //Hourly预报信息
        showHourlyForecast(forecast);


    }

    public void showDailyForecast(Forecast forecast){

        //天气情况
        String skycon = null;
        List<Daily_Skycon> ids = forecast.getResult().getDaily().getSkycon();
        List<Daily_aqi> ida = forecast.getResult().getDaily().getAqi();
        List<Daily_temperature> idt = forecast.getResult().getDaily().getTemperature();

        forecastLayout.removeAllViews();
        for (Daily_Skycon s : ids) {
            switch (s.getValue()) {
                case "PARTLY_CLOUDY_DAY":
                case "PARTLY_CLOUDY_NIGHT":
                    skycon = "多云";
                    break;
                case "CLEAR_DAY":
                case "CLEAR_NIGHT":
                    skycon = "晴";
                    break;
                case "CLOUDY":
                    skycon = "阴";
                    break;
                case "RAIN":
                    skycon = "雨";
                    break;
                case "SNOW":
                    skycon = "雪";
                    break;
                case "WIND":
                    skycon = "风";
                    break;
                case "FOG":
                    skycon = "雾";
                    break;
            }

            for (Daily_temperature t : idt) {
                if (s.getDate().equals(t.getDate())) {
                    //将预测列表添加到预测布局
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.forecast_item, forecastLayout, false);
                    TextView date_text = (TextView) view.findViewById(R.id.date_text);
                    TextView weather_text = (TextView) view.findViewById(R.id.weather_text);
                    TextView max_text = (TextView) view.findViewById(R.id.Max_text);
                    TextView min_text = (TextView) view.findViewById(R.id.Min_text);

                    date_text.setText(t.getDate());
                    max_text.setText((int) t.getMax() + "°");
                    min_text.setText((int) t.getMin() + "°");
                    weather_text.setText(skycon);
                    forecastLayout.addView(view);
                }
            }
        }

    }

    public void showHourlyForecast(Forecast forecast){
        //预报信息
        String desription = forecast.getResult().getHourly().getDescription();
        List<Hourly_Skycon> ihs = forecast.getResult().getHourly().getSkycon();
        List<Hourly_temperature> iht = forecast.getResult().getHourly().getTemperature();
        List<Hourly_aqi> iha = forecast.getResult().getHourly().getAqi();

        description_text.setText(desription);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}

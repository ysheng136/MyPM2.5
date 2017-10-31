package com.pm25.mypm25.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pm25.mypm25.MapMessage;
import com.pm25.mypm25.gson.Weather;
import com.pm25.mypm25.gson2.Forecast;
import com.pm25.mypm25.gson3.Forecast2;
import com.pm25.mypm25.gson4.CityRank;
import com.pm25.mypm25.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class AutoUpdateService extends Service {

    //地图信息
    private String city_data;
    private double latitude_data; //纬度
    private double longitude_data;  //经度
    private String district_data;
    //    private MyLocationListener myLocationListener;

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //EventBus的注册
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(MapMessage mapMessage) {
        latitude_data = mapMessage.getLatitude_data();
        longitude_data = mapMessage.getLongitude_data();

        Log.i(TAG, "111onEvent: " + latitude_data + " " + longitude_data);

    }

    //    @Override
    //    public void onCreate() {
    //        super.onCreate();
    //        //获取位置管理对象
    //        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    //        //以最优的方式获得经纬度对象
    //        Criteria criteria = new Criteria();
    //        //不要求海拔信息
    //        criteria.setAltitudeRequired(false);
    //        //不要求方位信息
    //        criteria.setBearingRequired(false);
    //        //是否付费
    //        criteria.setCostAllowed(true);
    //        //对电量的要求
    //        criteria.setPowerRequirement(Criteria.POWER_LOW);
    //        //指定获取经纬度的精确度
    //        criteria.setAccuracy(Criteria.ACCURACY_FINE);
    //
    //        String bestProvider = locationManager.getBestProvider(criteria, true);
    //
    //        locationManager.requestLocationUpdates(bestProvider, 0, 0,
    //                new MyLocationListener());
    //
    //
    //    }

    //
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //更新城市排名
        updateCiteRank();

        //更新和风天气
        updateWeather();

        //更新阿里云天气
        updateForecast2();


        //更新彩云天气
        updateForecast();

        //更新阿里云天气
        updateForecast4();


        //半小时更新一次
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour = 1 * 30 * 60 * 1000; //半小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;

        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);

    }


    //更新城市排名
    private void updateCiteRank() {

        // SharedPreferences 存储
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String cityRankString = prefs.getString("cityRank", null);

        Log.i(TAG, "updateCiteRank: " + "1111111");

        if (cityRankString != null) {

            final String pm25Url = "https://ali-pm25.showapi.com/pm25-top";

            HttpUtil.sendOkHttpRequest2(pm25Url, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseData = response.body().string();

                    final CityRank cityRank = HttpUtil.handleCityRankResponse(responseData);


                    if (cityRank != null && 0 == cityRank.getShowapi_res_code() && "".equals(cityRank.getShowapi_res_error())) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("cityRank", responseData);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //更新和风天气
    private void updateWeather() {
        // SharedPreferences 存储
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        if (weatherString != null) {
            //天气情况 有缓存时直接解析数据
            Weather weather = HttpUtil.handleWeatherResponse(weatherString);

            String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + longitude_data + "," +
                    latitude_data + "&key=0e51a1c839234a8a84789c9f349cfe15";

            //根据经纬度请求和风城市天气信息
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseData = response.body().string();

                    final Weather weather = HttpUtil.handleWeatherResponse(responseData);

                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", responseData);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //更新阿里云天气
    private void updateForecast2() {

        SharedPreferences prefs3 = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastString2 = prefs3.getString("forecast2", null);

         /*
        天气情况
         */
        if (forecastString2 != null) {

            //天气预报 有缓存时直接解析数据
            Forecast2 forecast2 = HttpUtil.handleForecastResponse2(forecastString2);

            String forecast2Url = "http://jisutqybmf.market.alicloudapi.com/weather/query?location=" + latitude_data + "," + longitude_data;

            //根据经纬度请求阿里云城市天气信息
            HttpUtil.sendOkHttpRequest2(forecast2Url, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseData = response.body().string();

                    final Forecast2 forecast2 = HttpUtil.handleForecastResponse2(responseData);

                    if (forecast2 != null && "0".equals(forecast2.getStatus()) && "ok".equals(forecast2.getMsg())) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("forecast2", responseData);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //更新彩云天气
    public void updateForecast() {
        SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastString = prefs2.getString("forecast", null);

        if (forecastString != null) {
            //天气预报 有缓存时直接解析数据
            Forecast forecast = HttpUtil.handleForecastResponse(forecastString);

            String forecastUrl = "https://api.caiyunapp.com/v2/Nf8ohAGoXcBVU-Xj/" + longitude_data + "," +
                    latitude_data + "/forecast.json";

            // 根据经纬度请求彩云城市天气预报信息
            HttpUtil.sendOkHttpRequest(forecastUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    //服务器返回的数据
                    final String responseData = response.body().string();
                    final Forecast forecast = HttpUtil.handleForecastResponse(responseData);

                    if (forecast != null && "ok".equals(forecast.getStatus())
                            && "ok".equals(forecast.getResult().getHourly().getStatus())
                            && "ok".equals(forecast.getResult().getDaily().getStatus())
                            ) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("forecast", responseData);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }


    //更新阿里云天气
    private void updateForecast4() {

        SharedPreferences prefs3 = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastString2 = prefs3.getString("forecast4", null);

         /*
        天气情况
         */
        if (forecastString2 != null) {

            //天气预报 有缓存时直接解析数据
            Forecast2 forecast2 = HttpUtil.handleForecastResponse2(forecastString2);

            String forecast2Url = "http://jisutqybmf.market.alicloudapi.com/weather/query?location=" + latitude_data + "," + longitude_data;

            //根据经纬度请求阿里云城市天气信息
            HttpUtil.sendOkHttpRequest2(forecast2Url, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String responseData = response.body().string();

                    final Forecast2 forecast2 = HttpUtil.handleForecastResponse2(responseData);

                    if (forecast2 != null && "0".equals(forecast2.getStatus()) && "ok".equals(forecast2.getMsg())) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("forecast4", responseData);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

        @Override
        public void onDestroy () {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }
    }


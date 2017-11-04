package com.pm25.mypm25.util;

import com.google.gson.Gson;
import com.pm25.mypm25.gson.Weather;
import com.pm25.mypm25.gson2.Forecast;
import com.pm25.mypm25.gson3.Forecast2;
import com.pm25.mypm25.gson4.CityRank;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HttpUtil {

    /*
       解析接口
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        //创建一个OkhttpClient实例
        OkHttpClient client = new OkHttpClient();
        //创建一个Request对象
        Request request = new Request.Builder()
                .url(address)
                .build();
        //调用OkhttpClient的newcall()来创建一个call对象，
        // 并调用它的enqueue（）方法在子线程中来执行HTTP请求并将服务器返回的数据回调到 okhttp3.Callback中
        client.newCall(request).enqueue(callback);
    }


    /*
       解析接口
     */
    public static void sendOkHttpRequest2(String address,okhttp3.Callback callback){
        //创建一个OkhttpClient实例
        OkHttpClient client = new OkHttpClient();
        String appcode = "49817b8e3f7b42e99e3b0dd5b4acb311";
        //创建一个Request对象
        Request request = new Request.Builder()
                .url(address)
                .get()
                .addHeader("Authorization","APPCODE "+appcode)
                .build();
        //调用OkhttpClient的newcall()来创建一个call对象，
        // 并调用它的enqueue（）方法在子线程中来执行HTTP请求并将服务器返回的数据回调到 okhttp3.Callback中
        client.newCall(request).enqueue(callback);
    }


    /*
        解析和风天气JSON数据
     */
    public static Weather handleWeatherResponse(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Weather weather = new Gson().fromJson(weatherContent,Weather.class);
            return weather;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        解析彩云天气预测JSON数据
     */
    public static Forecast handleForecastResponse(String response){

        try {

            Gson gson = new Gson();
            return  gson.fromJson(response,Forecast.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
        解析阿里云天气预测JSON数据
     */
    public static Forecast2 handleForecastResponse2(String response){

        try {

            Gson gson = new Gson();
            return  gson.fromJson(response, Forecast2.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        解析城市排名JSON数据
     */
    public static CityRank handleCityRankResponse(String response){

        try {

            Gson gson = new Gson();
            return  gson.fromJson(response, CityRank.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

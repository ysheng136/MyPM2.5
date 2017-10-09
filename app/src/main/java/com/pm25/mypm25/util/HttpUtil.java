package com.pm25.mypm25.util;

import android.util.Log;

import com.google.gson.Gson;
import com.pm25.mypm25.gson.Weather;
import com.pm25.mypm25.gson2.Forecast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

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
        解析天气JSON数据
     */
    public static Weather handleWeatherResponse(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Log.i(TAG, "handleWeatherResponse:" + weatherContent);
            Weather weather = new Gson().fromJson(weatherContent,Weather.class);
            Log.i(TAG, "handleWeatherResponse: " + weather);
            return weather;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
        解析天气预测JSON数据
     */
    public static Forecast handleForecastResponse(String response){

        try {


//            JSONObject jsonObject = new JSONObject(response);
//            String forecastContent = jsonObject.getJSONObject("").toString();

            Log.i(TAG, "handleForecastResponse: " + response);
            Gson gson = new Gson();
            return  gson.fromJson(response,Forecast.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

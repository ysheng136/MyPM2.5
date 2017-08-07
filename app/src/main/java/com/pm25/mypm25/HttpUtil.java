package com.pm25.mypm25;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HttpUtil {
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
}

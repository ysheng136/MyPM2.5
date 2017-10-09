package com.pm25.mypm25.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/13.
 */

public class Now {

    //体感温度
    @SerializedName("fl")
    public String feelsLike;

    //相对湿度
    @SerializedName("hum")
    public String humidity;

    //温度
    @SerializedName("tmp")
    public String temperature;



    public Wind wind;

    @SerializedName("cond")
    public More more;


    public class More{
        @SerializedName("txt")
        public String info;
    }

    public class Wind{

        //风向
        @SerializedName("dir")
        public String direction;

        //风力
        @SerializedName("sc")
        public String screen;
    }


}

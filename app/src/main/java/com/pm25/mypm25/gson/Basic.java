package com.pm25.mypm25.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/13.
 */

public class Basic {

    public Update update;

    //更新时间
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}

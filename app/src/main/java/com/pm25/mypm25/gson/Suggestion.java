package com.pm25.mypm25.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/13.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("drsg")
    public Dress dress;

    public Sport sport;

    @SerializedName("uv")
    public Ultraviolet ultraviolet;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Dress{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }

    public class Ultraviolet{
        @SerializedName("txt")
        public String info;
    }
}

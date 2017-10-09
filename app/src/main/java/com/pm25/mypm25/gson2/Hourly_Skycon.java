package com.pm25.mypm25.gson2;

/**
 * Created by wys on 2017/10/7.
 */

public class Hourly_Skycon {

    private String value;
    private String datetime;



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "Hourly_Skycon{" +
                "value='" + value + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}

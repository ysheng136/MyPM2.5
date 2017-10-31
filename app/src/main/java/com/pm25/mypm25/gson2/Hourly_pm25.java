package com.pm25.mypm25.gson2;

/**
 * Created by wys on 2017/10/27.
 */

public class Hourly_pm25 {

    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Hourly_pm25{" +
                "value='" + value + '\'' +
                '}';
    }
}

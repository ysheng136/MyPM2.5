package com.pm25.mypm25.gson2;

/**
 * Created by wys on 2017/10/7.
 */

public class Hourly_temperature {

    private double value;
    private String datetime;



    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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
        return "Hourly_temperature{" +
                "value='" + value + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}

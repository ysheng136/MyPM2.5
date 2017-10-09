package com.pm25.mypm25.gson2;

/**
 * Created by wys on 2017/10/8.
 */

public class Forecast_Daily {

    private String date;
    private Daily_Skycon skycon;
    private Daily_temperature temperature;

    public Forecast_Daily(String date, Daily_Skycon skycon, Daily_temperature temperature) {
        this.date = date;
        this.skycon = skycon;
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Daily_Skycon getSkycon() {
        return skycon;
    }

    public void setSkycon(Daily_Skycon skycon) {
        this.skycon = skycon;
    }

    public Daily_temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Daily_temperature temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Forecast_Daily{" +
                "date='" + date + '\'' +
                ", skycon=" + skycon +
                ", temperature=" + temperature +
                '}';
    }
}

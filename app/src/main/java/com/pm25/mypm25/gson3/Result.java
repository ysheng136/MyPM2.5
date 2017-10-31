package com.pm25.mypm25.gson3;

import java.util.List;

/**
 * Created by wys on 2017/10/13.
 */

public class Result {

    private List<Daily> daily;
    private List<Hourly> hourly;

    private String weather;
    private AQI aqi;
    private String updatetime;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }



    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public void setHourly(List<Hourly> hourly) {
        this.hourly = hourly;
    }

    public AQI getAqi() {
        return aqi;
    }

    public void setAqi(AQI aqi) {
        this.aqi = aqi;
    }

    @Override
    public String toString() {
        return "Result{" +
                "daily=" + daily +
                ", hourly=" + hourly +
                ", weather='" + weather + '\'' +
                ", aqi=" + aqi +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}

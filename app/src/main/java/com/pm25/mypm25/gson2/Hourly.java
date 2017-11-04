package com.pm25.mypm25.gson2;

import java.util.List;

/**
 * Created by wys on 2017/10/6.
 */

public class Hourly {

    private String status;
    private String description;
    private List<Hourly_aqi> aqi;
    private List<Hourly_Date> temperature;
    private List<Hourly_pm25> pm25;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Hourly_aqi> getAqi() {
        return aqi;
    }

    public void setAqi(List<Hourly_aqi> aqi) {
        this.aqi = aqi;
    }

    public List<Hourly_Date> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<Hourly_Date> temperature) {
        this.temperature = temperature;
    }

    public List<Hourly_pm25> getPm25() {
        return pm25;
    }

    public void setPm25(List<Hourly_pm25> pm25) {
        this.pm25 = pm25;
    }

}

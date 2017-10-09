package com.pm25.mypm25.gson2;

import java.util.List;

/**
 * Created by wys on 2017/10/6.
 */

public class Hourly {

    private String status;
    private String description;
    private List<Hourly_Skycon> skycon;
    private List<Hourly_aqi> aqi;
    private List<Hourly_temperature> temperature;

    public Hourly(String status, String description, List<Hourly_Skycon> skycon, List<Hourly_aqi> aqi, List<Hourly_temperature> temperature) {
        this.status = status;
        this.description = description;
        this.skycon = skycon;
        this.aqi = aqi;
        this.temperature = temperature;
    }

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

    public List<Hourly_Skycon> getSkycon() {
        return skycon;
    }

    public void setSkycon(List<Hourly_Skycon> skycon) {
        this.skycon = skycon;
    }

    public List<Hourly_aqi> getAqi() {
        return aqi;
    }

    public void setAqi(List<Hourly_aqi> aqi) {
        this.aqi = aqi;
    }

    public List<Hourly_temperature> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<Hourly_temperature> temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Hourly{" +
                "status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", skycon=" + skycon +
                ", aqi=" + aqi +
                ", temperature=" + temperature +
                '}';
    }
}

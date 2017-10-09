package com.pm25.mypm25.gson2;

import java.util.List;

/**
 * Created by wys on 2017/10/6.
 */

public class Daily {

    private String status;
    private List<Daily_temperature> temperature;
    private List<Daily_Skycon> skycon;
    private List<Daily_aqi> aqi;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Daily_temperature> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<Daily_temperature> temperature) {
        this.temperature = temperature;
    }

    public List<Daily_Skycon> getSkycon() {
        return skycon;
    }

    public void setSkycon(List<Daily_Skycon> skycon) {
        this.skycon = skycon;
    }

    public List<Daily_aqi> getAqi() {
        return aqi;
    }

    public void setAqi(List<Daily_aqi> aqi) {
        this.aqi = aqi;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "status='" + status + '\'' +
                ", temperature=" + temperature +
                ", skycon=" + skycon +
                ", aqi=" + aqi +
                '}';
    }
}

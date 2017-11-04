package com.pm25.mypm25;

/**
 * Created by Administrator on 2017/8/13.
 */

public class MapMessage {

    private String city_data;
    private double latitude_data;
    private double longitude_data;
    private String district_data;

    public MapMessage(String city_data, double latitude_data, double longitude_data, String district_data) {
        this.city_data = city_data;
        this.latitude_data = latitude_data;
        this.longitude_data = longitude_data;
        this.district_data = district_data;
    }

    public String getCity_data() {
        return city_data;
    }

    public void setCity_data(String city_data) {
        this.city_data = city_data;
    }

    public double getLatitude_data() {
        return latitude_data;
    }

    public void setLatitude_data(double latitude_data) {
        this.latitude_data = latitude_data;
    }

    public double getLongitude_data() {
        return longitude_data;
    }

    public void setLongitude_data(double longitude_data) {
        this.longitude_data = longitude_data;
    }

    public String getDistrict_data() {
        return district_data;
    }

    public void setDistrict_data(String district_data) {
        this.district_data = district_data;
    }
}

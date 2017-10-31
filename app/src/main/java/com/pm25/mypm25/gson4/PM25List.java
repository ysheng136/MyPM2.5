package com.pm25.mypm25.gson4;

/**
 * Created by wys on 2017/10/19.
 */

public class PM25List {
    private String so2;
    private String o3;
    private String pm2_5;
    private String ct;
    private String primary_pollutant;
    private String num;
    private String co;
    private String area;
    private String no2;
    private String aqi;
    private String quality;
    private String pm10;
    private String o3_8h;

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(String pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getPrimary_pollutant() {
        return primary_pollutant;
    }

    public void setPrimary_pollutant(String primary_pollutant) {
        this.primary_pollutant = primary_pollutant;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getO3_8h() {
        return o3_8h;
    }

    public void setO3_8h(String o3_8h) {
        this.o3_8h = o3_8h;
    }

    @Override
    public String toString() {
        return "PM25List{" +
                "so2='" + so2 + '\'' +
                ", o3='" + o3 + '\'' +
                ", pm2_5='" + pm2_5 + '\'' +
                ", ct='" + ct + '\'' +
                ", primary_pollutant='" + primary_pollutant + '\'' +
                ", num='" + num + '\'' +
                ", co='" + co + '\'' +
                ", area='" + area + '\'' +
                ", no2='" + no2 + '\'' +
                ", aqi='" + aqi + '\'' +
                ", quality='" + quality + '\'' +
                ", pm10='" + pm10 + '\'' +
                ", o3_8h='" + o3_8h + '\'' +
                '}';
    }
}

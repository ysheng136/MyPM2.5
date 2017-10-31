package com.pm25.mypm25.gson3;

/**
 * Created by wys on 2017/10/31.
 */

public class AQI {
    private String so2;
    private String no2;
    private String co;
    private String o3;
    private String pm10;
    private String pm2_5;
    private String aqi;
    private String primarypollutant;
    private String quality;
    private String timepoint;
    private AQIInfo aqiinfo;

    public class AQIInfo{

        private String color;
        private String affect;
        private String measure;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getAffect() {
            return affect;
        }

        public void setAffect(String affect) {
            this.affect = affect;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }
    }

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(String pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPrimarypollutant() {
        return primarypollutant;
    }

    public void setPrimarypollutant(String primarypollutant) {
        this.primarypollutant = primarypollutant;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(String timepoint) {
        this.timepoint = timepoint;
    }

    public AQIInfo getAqiinfo() {
        return aqiinfo;
    }

    public void setAqiinfo(AQIInfo aqiinfo) {
        this.aqiinfo = aqiinfo;
    }

    @Override
    public String toString() {
        return "AQI{" +
                "so2='" + so2 + '\'' +
                ", no2='" + no2 + '\'' +
                ", co='" + co + '\'' +
                ", o3='" + o3 + '\'' +
                ", pm10='" + pm10 + '\'' +
                ", pm2_5='" + pm2_5 + '\'' +
                ", aqi='" + aqi + '\'' +
                ", primarypollutant='" + primarypollutant + '\'' +
                ", quality='" + quality + '\'' +
                ", timepoint='" + timepoint + '\'' +
                ", aqiinfo=" + aqiinfo +
                '}';
    }
}

package com.pm25.mypm25.gson2;

/**
 * Created by wys on 2017/10/6.
 */

public class Result {

    private  Hourly hourly;
    private  Daily daily;

    public Result() {

    }

    public Result(Hourly hourly, Daily daily) {
        this.hourly = hourly;
        this.daily = daily;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    @Override
    public String toString() {
        return "Result{" +
                "hourly=" + hourly +
                ", daily=" + daily +
                '}';
    }
}

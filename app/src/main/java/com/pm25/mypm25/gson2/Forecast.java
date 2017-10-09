package com.pm25.mypm25.gson2;

/**
 * Created by wys on 2017/10/6.
 */

public class Forecast {

    private String status;

    private Result result;

    public Forecast(String status, Result result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "status='" + status + '\'' +
                ", result=" + result +
                '}';
    }
}

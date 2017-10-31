package com.pm25.mypm25.gson2;

/**
 * Created by wys on 2017/10/6.
 */

public class Forecast {

    private String status;

    private Result result;

    private long server_time;

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
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


}

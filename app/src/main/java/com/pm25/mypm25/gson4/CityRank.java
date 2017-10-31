package com.pm25.mypm25.gson4;

/**
 * Created by wys on 2017/10/19.
 */

public class CityRank {

        private int showapi_res_code;
        private String showapi_res_error;
        private Show showapi_res_body;




    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public Show getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(Show showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    @Override
    public String toString() {
        return "CityRank{" +
                "showapi_res_code=" + showapi_res_code +
                ", showapi_res_error='" + showapi_res_error + '\'' +
                ", showapi_res_body=" + showapi_res_body +
                '}';
    }
}

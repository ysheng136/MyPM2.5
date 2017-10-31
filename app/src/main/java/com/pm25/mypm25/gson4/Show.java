package com.pm25.mypm25.gson4;

import java.util.List;

/**
 * Created by wys on 2017/10/23.
 */

public class Show {

        private int ret_code;
        private List<PM25List> list;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public List<PM25List> getList() {
            return list;
        }

        public void setList(List<PM25List> list) {
            this.list = list;
        }

    @Override
    public String toString() {
        return "Show{" +
                "ret_code=" + ret_code +
                ", list=" + list +
                '}';
    }
}

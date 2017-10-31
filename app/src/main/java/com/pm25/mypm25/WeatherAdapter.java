package com.pm25.mypm25;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm25.mypm25.gson3.Hourly;

import java.util.List;

/**
 * Created by wys on 2017/10/12.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{

    private List<Hourly> hourlyList;

    static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView skyconName;
        TextView tempName;
        TextView dateName;
        ImageView imgName;

        public ViewHolder(View view){
            super(view);
            skyconName = (TextView) view.findViewById(R.id.skycon_text);
            tempName = (TextView) view.findViewById(R.id.hourly_temp_text);
            dateName = (TextView) view.findViewById(R.id.hourly_date_text);
            imgName = (ImageView) view.findViewById(R.id.hourly_img_text);
        }
    }

    public  WeatherAdapter(List<Hourly> hourlyList){
        this.hourlyList = hourlyList;
    }

    //创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //对子项的数据进行赋值
    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {
        Hourly hourly = hourlyList.get(position);
        int img = 0;
        holder.skyconName.setText(hourly.getWeather());
        holder.dateName.setText(hourly.getTime());
        holder.tempName.setText(hourly.getTemp()+"°");
        switch (hourly.getImg()){
            case "0": img = R.drawable.a0;break;
            case "1": img = R.drawable.a1;break;
            case "2": img = R.drawable.a2;break;
            case "3": img = R.drawable.a3;break;
            case "4": img = R.drawable.a4;break;
            case "5": img = R.drawable.a5;break;
            case "6": img = R.drawable.a6;break;
            case "7": img = R.drawable.a7;break;
            case "8": img = R.drawable.a8;break;
            case "9": img = R.drawable.a9;break;
            case "10": img = R.drawable.a10;break;
            case "11": img = R.drawable.a11;break;
            case "12": img = R.drawable.a12;break;
            case "13": img = R.drawable.a13;break;
            case "14": img = R.drawable.a14;break;
            case "15": img = R.drawable.a15;break;
            case "16": img = R.drawable.a16;break;
            case "17": img = R.drawable.a17;break;
            case "18": img = R.drawable.a18;break;
            case "19": img = R.drawable.a19;break;
            case "20": img = R.drawable.a20;break;
            case "21": img = R.drawable.a21;break;
            case "22": img = R.drawable.a22;break;
            case "23": img = R.drawable.a23;break;
            case "24": img = R.drawable.a24;break;
            case "25": img = R.drawable.a25;break;
            case "26": img = R.drawable.a26;break;
            case "27": img = R.drawable.a27;break;
            case "28": img = R.drawable.a28;break;
            case "29": img = R.drawable.a29;break;
            case "30": img = R.drawable.a30;break;
            case "31": img = R.drawable.a31;break;
            case "32": img = R.drawable.a32;break;
            case "49": img = R.drawable.a49;break;
            case "53": img = R.drawable.a53;break;
            case "54": img = R.drawable.a54;break;
            case "55": img = R.drawable.a55;break;
            case "56": img = R.drawable.a56;break;
            case "57": img = R.drawable.a57;break;
            case "58": img = R.drawable.a58;break;
            case "99": img = R.drawable.a99;break;
            case "301": img = R.drawable.a301;break;
            case "302": img = R.drawable.a302;break;
        }

        holder.imgName.setImageResource(img);

    }

    //数据源的长度
    @Override
    public int getItemCount() {
        return hourlyList.size();
    }
}

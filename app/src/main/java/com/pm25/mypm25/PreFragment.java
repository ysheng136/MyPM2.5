package com.pm25.mypm25;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pm25.mypm25.gson2.Forecast;
import com.pm25.mypm25.gson2.Hourly_Date;
import com.pm25.mypm25.gson2.Hourly_pm25;
import com.pm25.mypm25.service.AutoUpdateService;
import com.pm25.mypm25.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/7/29.
 */

public class PreFragment extends Fragment {

    private LineChart lineChart;
    private double latitude_data; //纬度
    private double longitude_data;  //经度

    private XAxis xAxis;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view3, container, false);
        lineChart = (LineChart) view.findViewById(R.id.linechart);

        init();

        //进行EventBus的注册
        EventBus.getDefault().register(this);

        return view;
    }

    @Subscribe
    public void onEvent(MapMessage mapMessage) {

        latitude_data = mapMessage.getLatitude_data();
        longitude_data = mapMessage.getLongitude_data();

        SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String forecastString = prefs2.getString("forecast", null);

        if (forecastString != null) {
            //彩云天气预报 有缓存时直接解析数据
            Forecast forecast = HttpUtil.handleForecastResponse(forecastString);
            showPM25Info(forecast);
        } else {
            //彩云天气预报 无缓存时根据经纬度去服务器查询天气
            requestForecast(longitude_data, latitude_data);
        }
    }


    /**
     * 根据经纬度请求彩云城市天气预报信息
     */
    public void requestForecast(double longitude_data, double latitude_data) {

        final String forecastUrl = "https://api.caiyunapp.com/v2/Nf8ohAGoXcBVU-Xj/" + longitude_data + "," +
                latitude_data + "/forecast.json";

        HttpUtil.sendOkHttpRequest(forecastUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //服务器返回的数据
                final String responseData = response.body().string();

                final Forecast forecast = HttpUtil.handleForecastResponse(responseData);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (forecast != null && "ok".equals(forecast.getStatus())
                                && "ok".equals(forecast.getResult().getHourly().getStatus())
                                && "ok".equals(forecast.getResult().getDaily().getStatus())
                                ) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("forecast", responseData);
                            editor.apply();
                            showPM25Info(forecast);
                        } else {
                            Toast.makeText(getContext(), "更新天气信息失败，请开启网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "更新天气信息失败，请开启网络连接", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    public void init() {
        //1、基本设置
        lineChart.setMaxVisibleValueCount(60); //最大显示的个数。超过60个将不再显示
        lineChart.getDescription().setEnabled(false);//不显示标题

        //表格边框
        lineChart.setDrawBorders(false);//启用绘制图表边框（chart周围的线）

        lineChart.setTouchEnabled(true); // 设置为可以触摸
        lineChart.setDragEnabled(true);// 启用可以拖拽
        lineChart.setDragDecelerationEnabled(true);  //拖动 如果设置为true，手指滑动抛掷图表后继续减速滚动。默认值：true。
        lineChart.setScrollContainer(true); //设置滚动

        lineChart.setPinchZoom(true);//如果设置为true，没缩放功能。如果false，x轴和y轴可分别放大。

        lineChart.setScaleEnabled(false);// 禁用缩放图表上的两个轴
        lineChart.setHighlightPerDragEnabled(true);//拖拽超过图表绘制画布时高亮显示

        lineChart.setDrawGridBackground(false); // 是否显示表格颜色 如果启用，chart绘图区后面的背景矩形将绘制

        lineChart.animateX(1500); //数据从左到右动画依次显示
        lineChart.zoom(9.6f, 1f, 0, 0);

        //X轴标签设置
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);//设置轴启用或禁用。如果false，该轴的任何部分都不会被绘制（不绘制坐标轴/便签等）。
        xAxis.setDrawAxisLine(true); //绘制该行旁边的轴线
        xAxis.setDrawGridLines(true); //不绘制网格线
        xAxis.setDrawLabels(true);//设置为true，则绘制轴的标签。
        xAxis.setLabelCount(6);  //一个页面显示10个
        xAxis.setGranularity(1f);//设置最小间隔
        xAxis.setTextSize(15f);

        //Y轴样式
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);  //绘制y轴的网格线
        leftAxis.setDrawAxisLine(true);//设置为true，则绘制该行旁边的轴线（axis-line）。
        leftAxis.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));//绘制该行旁边的轴线的颜色
        leftAxis.setLabelCount(10);  //设置坐标轴的标签数量，当count>25时，count=25；当count<2时，count=2
        leftAxis.setTextSize(15f);
        leftAxis.setTextColor(Color.RED);
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(500);

        //隐藏右边的坐标轴
        lineChart.getAxisRight().setEnabled(false);

        //比例尺
        Legend legend = lineChart.getLegend();//隐藏比例尺
        legend.setEnabled(true);
        legend.setForm(Legend.LegendForm.LINE);// 样式
        legend.setTextSize(15f);
        legend.setFormSize(15f);// 字体大小
        legend.setTextColor(Color.RED);// 颜色

    }

    public void showPM25Info(Forecast forecast) {

        Intent intent = new Intent(getContext(), AutoUpdateService.class);
        getActivity().startService(intent);

        //x轴数据
        List<Hourly_pm25> pm25 = forecast.getResult().getHourly().getPm25();
        List<Hourly_Date> date = forecast.getResult().getHourly().getTemperature();

        Log.i(TAG, "111showPM25Info: "+ pm25);
        Log.i(TAG, "222showPM25Info: " + date);

//        List<Hourly_aqi> aqi = forecast.getResult().getHourly().getAqi();

        //x轴的数据
        final ArrayList<String> xValues = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            String d = date.get(i).getDatetime().substring(11, 16);
            xValues.add(d);
        }
        //设置数据格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int i = (int) value % xValues.size();
                return xValues.get(i);
            }
        });


        //y轴数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();

        for (int i = 0; i < pm25.size(); i++) {
            Integer it = Integer.valueOf(pm25.get(i).getValue().substring(0, 2));
            yValues.add(new Entry(i, it));
        }


        LineDataSet lineDataSet;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) lineChart.getLineData().getDataSetByIndex(0);

            lineDataSet.setValues(yValues);

            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();

        } else {
            lineDataSet = new LineDataSet(yValues, "pm2.5");
            // 设置曲线颜色
            lineDataSet.setColor(Color.parseColor("#000000"));
            // 设置平滑曲线
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // 显示坐标点的小圆点
            lineDataSet.setDrawCircles(true);
            // 显示坐标点的数据
            lineDataSet.setDrawValues(true);

            // 显示定位线
            lineDataSet.setHighlightEnabled(true);

            //设置y轴数据格式
            lineDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    int n = (int) value;
                    return n + "";
                }
            });

            LineData data = new LineData(lineDataSet);
            data.setValueTextSize(13f);
            data.setValueTextColor(Color.BLUE);
            lineChart.setData(data);
            lineChart.invalidate();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

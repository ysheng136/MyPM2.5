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
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pm25.mypm25.gson4.CityRank;
import com.pm25.mypm25.gson4.PM25List;
import com.pm25.mypm25.service.AutoUpdateService;
import com.pm25.mypm25.util.HttpUtil;

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

public class CityFragment extends Fragment {

    private BarChart barChart;
    private XAxis xAxis;
    private XAxis xAxis2;
    private YAxis leftAxis;
    private YAxis leftAxis2;
    private TextView title_rank;
    private BarChart barChart2;
    private TextView title_rank2;
    private TextView title_time;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view4, container, false);

        barChart = (BarChart) view.findViewById(R.id.barChart);
        title_rank = (TextView) view.findViewById(R.id.title_rank);
        barChart2 = (BarChart) view.findViewById(R.id.barChart2);
        title_rank2 = (TextView) view.findViewById(R.id.title_rank2);
        title_time = (TextView) view.findViewById(R.id.title_time);

        //柱形图初始化
        init();
        init2();
        // SharedPreferences 存储
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cityRankString = prefs.getString("cityRank", null);

         /*
        天气情况
         */
        if (cityRankString != null) {
            //城市排名 有缓存时直接解析数据
            CityRank cityRank = HttpUtil.handleCityRankResponse(cityRankString);
            showCityInfo(cityRank);

        } else {
            requestCityRank();
        }

        return view;

    }

    public void init() {

        //1、基本设置
        barChart.setMaxVisibleValueCount(60); //最大显示的个数。超过60个将不再显示
        //标题
        barChart.getDescription().setEnabled(false);//不显示标题

        //表格边框
        barChart.setDrawBorders(false);//启用绘制图表边框（chart周围的线）
        barChart.setBorderColor(Color.GRAY);//设置chart边框线的颜色
        barChart.setBorderWidth(1f);//设置chart边界线的宽度，单位dp

        barChart.setDrawValueAboveBar(true); //设置数字显示在柱形条上部
        barChart.setDrawBarShadow(false);     //表不要阴影, 设置是否显示全部柱形条，不填充部分显示灰色

        barChart.setTouchEnabled(false); // 设置为不可以触摸
        barChart.setDragEnabled(true);// 启用可以拖拽
        barChart.setPinchZoom(true);//如果设置为true，没缩放功能。如果false，x轴和y轴可分别放大。
        barChart.setScaleEnabled(false);// 禁用缩放图表上的两个轴

        barChart.setDrawGridBackground(false); // 是否显示表格颜色 如果启用，chart绘图区后面的背景矩形将绘制
        barChart.setHighlightFullBarEnabled(true);//设置是否高亮显示

        barChart.animateY(2500); //数据从下到上动画依次显示

        //X轴标签设置
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);//设置轴启用或禁用。如果false，该轴的任何部分都不会被绘制（不绘制坐标轴/便签等）。
        xAxis.setDrawAxisLine(true); //绘制该行旁边的轴线
        xAxis.setDrawGridLines(false); //不绘制网格线
        xAxis.setDrawLabels(true);//设置为true，则绘制轴的标签。
        xAxis.setLabelCount(10);
        xAxis.setTextSize(12f);
        xAxis.setLabelRotationAngle(-45);

        //Y轴样式
        leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);  //绘制y轴的网格线
        leftAxis.setDrawAxisLine(true);//设置为true，则绘制该行旁边的轴线（axis-line）。
        leftAxis.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));//绘制该行旁边的轴线的颜色
        leftAxis.setLabelCount(5);  //设置坐标轴的标签数量，当count>25时，count=25；当count<2时，count=2
        leftAxis.setTextSize(10f);
        leftAxis.setTextColor(Color.RED);
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(500);

        barChart.getAxisRight().setEnabled(false);

        //比例尺
        Legend legend = barChart.getLegend();//隐藏比例尺
        legend.setEnabled(false);
    }

    public void init2() {

        //1、基本设置
        barChart2.setMaxVisibleValueCount(60); //最大显示的个数。超过60个将不再显示
        //标题
        barChart2.getDescription().setEnabled(false);//不显示标题

        //表格边框
        barChart2.setDrawBorders(false);//启用绘制图表边框（chart周围的线）

        barChart2.setDrawValueAboveBar(true); //设置数字显示在柱形条上部
        barChart2.setDrawBarShadow(false);     //表不要阴影, 设置是否显示全部柱形条，不填充部分显示灰色

        barChart2.setTouchEnabled(false); // 设置为不可以触摸
        barChart2.setDragEnabled(true);// 启用可以拖拽
        barChart2.setPinchZoom(true);//如果设置为true，没缩放功能。如果false，x轴和y轴可分别放大。
        barChart2.setScaleEnabled(false);// 禁用缩放图表上的两个轴

        barChart2.setDrawGridBackground(false); // 是否显示表格颜色 如果启用，chart绘图区后面的背景矩形将绘制
        barChart2.setHighlightFullBarEnabled(true);//设置是否高亮显示

        barChart2.animateY(2500); //数据从下到上动画依次显示

        //X轴标签设置
        xAxis2 = barChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setEnabled(true);//设置轴启用或禁用。如果false，该轴的任何部分都不会被绘制（不绘制坐标轴/便签等）。
        xAxis2.setDrawAxisLine(true); //绘制该行旁边的轴线
        xAxis2.setDrawGridLines(false); //不绘制网格线
        xAxis2.setDrawLabels(true);//设置为true，则绘制轴的标签。
        xAxis2.setLabelCount(10);
        xAxis2.setLabelRotationAngle(-45);
        xAxis2.setTextSize(12f);

        //Y轴样式
        leftAxis2 = barChart2.getAxisLeft();
        leftAxis2.setDrawGridLines(true);  //绘制y轴的网格线
        leftAxis2.setDrawAxisLine(true);//设置为true，则绘制该行旁边的轴线（axis-line）。
        leftAxis2.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));//绘制该行旁边的轴线的颜色
        leftAxis2.setLabelCount(5);  //设置坐标轴的标签数量，当count>25时，count=25；当count<2时，count=2
        leftAxis2.setTextSize(10f);
        leftAxis2.setTextColor(Color.RED);
        leftAxis2.setAxisMinimum(0);
        leftAxis2.setAxisMaximum(500);
        barChart2.getAxisRight().setEnabled(false);

        //比例尺
        Legend legend = barChart2.getLegend();//隐藏比例尺
        legend.setEnabled(false);
    }


    /**
     * 根据经纬度请求城市天气预报信息
     */

    public void requestCityRank() {

        final String pm25Url = "https://ali-pm25.showapi.com/pm25-top";

        HttpUtil.sendOkHttpRequest2(pm25Url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseData = response.body().string();

                final CityRank cityRank = HttpUtil.handleCityRankResponse(responseData);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cityRank != null && 0 == cityRank.getShowapi_res_code() && "".equals(cityRank.getShowapi_res_error())) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("cityRank", responseData);
                            editor.apply();
                            showCityInfo(cityRank);
                        } else {
                            Toast.makeText(getContext(), "获取城市排名信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取城市排名信息失败", Toast.LENGTH_SHORT).show();
                        //                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    public void showCityInfo(CityRank cityRank){
        Intent intent = new Intent(getContext(), AutoUpdateService.class);
        getActivity().startService(intent);

        setData(cityRank);
        setData2(cityRank);
    }


    public void setData(CityRank cityRank) {



        //获取数据
        List<PM25List> pm25Lists = cityRank.getShowapi_res_body().getList();

        String time = pm25Lists.get(0).getCt();
        title_time.setText(time.substring(11,16));

        final List<PM25List> firstLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            firstLists.add(pm25Lists.get(i));
        }

        //x轴数据
        final ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < firstLists.size(); i++) {
            xValues.add(firstLists.get(i).getArea());
        }

        Log.i(TAG, "setData: " + xValues);

        //x轴数据格式

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int i = (int) value % xValues.size();
                return xValues.get(i);
            }
        });

        //y轴数据
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < firstLists.size(); i++) {
            yValues.add(new BarEntry(i, Integer.valueOf(firstLists.get(i).getAqi())));
        }

        BarDataSet set1;
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yValues);

            // 让图表知道它的基础数据发生更改，并执行所有必要的重新计算
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            //y轴的数据集合
            set1 = new BarDataSet(yValues, "城市排名");
            //设置多彩 也可以单一颜色
            set1.setColors(Color.GREEN);
            set1.setDrawValues(true);
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    int n = (int) value;
                    return n + "";
                }
            });
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(12f);
//            data.setBarWidth(0.5f);

            barChart.setData(data);
            barChart.setFitBars(true);

        }
        barChart.invalidate();
    }


    public void setData2(CityRank cityRank) {

        //获取数据
        List<PM25List> pm25Lists = cityRank.getShowapi_res_body().getList();

        //后十个数据
        final List<PM25List> lastValues = new ArrayList<>();

        int l = pm25Lists.size();
        //设置x轴数据
        for (int i = l - 1; i >= l - 10; i--) {
            lastValues.add(pm25Lists.get(i));
        }

        //x轴数据
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < lastValues.size(); i++) {
            xValues.add(lastValues.get(i).getArea());
        }

        //y轴数据
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < lastValues.size(); i++) {
            yValues.add(new BarEntry(i, Integer.valueOf(lastValues.get(i).getAqi())));
        }

        //x轴数据格式

        xAxis2.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int i = (int) value % lastValues.size();
                return lastValues.get(i).getArea();
            }
        });

        BarDataSet set1;
        if (barChart2.getData() != null && barChart2.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart2.getData().getDataSetByIndex(0);
            set1.setValues(yValues);
            barChart2.getData().notifyDataChanged();
            barChart2.notifyDataSetChanged();
        } else {
            //y轴的数据集合
            set1 = new BarDataSet(yValues, "城市排名");
            //设置多彩 也可以单一颜色

            set1.setColors(Color.RED);
            set1.setDrawValues(true);

            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    int n = (int) value;
                    return n + "";
                }
            });

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            barChart2.setData(data);
            data.setValueTextSize(12f);
            barChart2.setFitBars(true);
        }
        barChart2.invalidate();
    }


}



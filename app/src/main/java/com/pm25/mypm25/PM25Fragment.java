package com.pm25.mypm25;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.pm25.mypm25.gson3.Forecast2;
import com.pm25.mypm25.service.AutoUpdateService;
import com.pm25.mypm25.util.HttpUtil;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/7/29.
 */

public class PM25Fragment extends Fragment implements LocationSource, AMapLocationListener {
    //显示地图需要的变量
    private MapView mapView;//地图控件
    private AMap aMap;//地图对象

    //pm2.5控件信息
    private TextView aqi;
    private TextView co;
    private TextView pm10;
    private TextView o3;
    private TextView so2;
    private TextView no2;
    private TextView qlty_info;
    private TextView pm25;
    private TextView pollutant;
    private TextView advice;
    private TextView effect;
    private ImageView imageView;

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    //地图信息
    private String city_Data;
    private double latitude_data;
    private double longitude_data;
    private float accuracy_data;
    private String district_data;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view2, container, false);

        //初始化控件
        init(view);

        //显示地图
        mapView = (MapView) view.findViewById(R.id.map);
        //必须要写
        mapView.onCreate(savedInstanceState);
        //获取地图对象
        aMap = mapView.getMap();
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);


        //开始定位
        initLoc();

        return view;
    }

    public void init(View view) {
        aqi = (TextView) view.findViewById(R.id.aqiId);
        co = (TextView) view.findViewById(R.id.coNumber);
        pm10 = (TextView) view.findViewById(R.id.pm10Number);
        o3 = (TextView) view.findViewById(R.id.o3Number);
        so2 = (TextView) view.findViewById(R.id.so2Number);
        no2 = (TextView) view.findViewById(R.id.no2Number);
        qlty_info = (TextView) view.findViewById(R.id.qlty_info2);
        pm25 = (TextView) view.findViewById(R.id.pm25_info);
        advice = (TextView) view.findViewById(R.id.advice);
        effect = (TextView) view.findViewById(R.id.effect);
        pollutant = (TextView) view.findViewById(R.id.pollutant);
        imageView = (ImageView) view.findViewById(R.id.imageViewId);
    }

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                latitude_data = amapLocation.getLatitude();//获取纬度
                longitude_data = amapLocation.getLongitude();//获取经度
                accuracy_data = amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                city_Data = amapLocation.getCity();//城市信息
                district_data = amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

                //发送消息到天气界面
                EventBus.getDefault().post(new MapMessage(city_Data, latitude_data, longitude_data, district_data));

                // SharedPreferences 存储阿里云天气
                SharedPreferences prefs3 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String forecastString2 = prefs3.getString("forecast4", null);
                Log.i(TAG, "forecastString2: " + forecastString2);

            /*
             pm2.5情况
             */
                if (forecastString2 != null) {

                    //阿里云天气预报 有缓存时直接解析数据
                    Forecast2 forecast2 = HttpUtil.handleForecastResponse2(forecastString2);
                    showResultInfo2(forecast2);

                } else {
                    //阿里云天气预报 无缓存时根据经纬度去服务器查询天气
                    requestForecast2(longitude_data, latitude_data);
                }
            }

            // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
            if (isFirstLoc) {
                //设置缩放级别
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                //将地图移动到定位点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                //点击定位按钮 能够将地图的中心移动到定位点
                mListener.onLocationChanged(amapLocation);
                //添加图钉
                aMap.addMarker(getMarkerOptions(amapLocation));
                //获取定位信息
                StringBuffer buffer = new StringBuffer();
                buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                Toast.makeText(getActivity().getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                isFirstLoc = false;
            }
        }
    }


    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        //   options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;
    }

    /**
     * 根据经纬度请求阿里云城市天气预报信息
     */
    public void requestForecast2(double longitude_data, double latitude_data) {

        final String weatherUrl = "http://jisutqybmf.market.alicloudapi.com/weather/query?location=" + latitude_data + "," + longitude_data;
        HttpUtil.sendOkHttpRequest2(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseData = response.body().string();
                final Forecast2 forecast2 = HttpUtil.handleForecastResponse2(responseData);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (forecast2 != null && "0".equals(forecast2.getStatus()) && "ok".equals(forecast2.getMsg())) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("forecast4", responseData);
                            editor.apply();
                            showResultInfo2(forecast2);
                        } else {
                            Toast.makeText(getContext(), "获取天气信息失败2", Toast.LENGTH_SHORT).show();
                        }
                        //                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取天气信息失败2", Toast.LENGTH_SHORT).show();
                        //                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    /**
     * 处理并展示阿里云Result实体类中的数据
     */

    public void showResultInfo2(Forecast2 forecast2) {

        Intent intent = new Intent(getActivity(), AutoUpdateService.class);
        getActivity().startService(intent);

        aqi.setText(forecast2.getResult().getAqi().getAqi());
        co.setText(forecast2.getResult().getAqi().getCo());
        so2.setText(forecast2.getResult().getAqi().getSo2());
        no2.setText(forecast2.getResult().getAqi().getNo2());
        o3.setText(forecast2.getResult().getAqi().getO3());
        pm10.setText(forecast2.getResult().getAqi().getPm10());
        pollutant.setText(forecast2.getResult().getAqi().getPrimarypollutant());
        pm25.setText(forecast2.getResult().getAqi().getPm2_5());
        qlty_info.setText(forecast2.getResult().getAqi().getQuality());
        imageView.setBackgroundColor(Color.parseColor(forecast2.getResult().getAqi().getAqiinfo().getColor()));
        advice.setText(forecast2.getResult().getAqi().getAqiinfo().getMeasure());
        effect.setText(forecast2.getResult().getAqi().getAqiinfo().getAffect());
    }


    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}

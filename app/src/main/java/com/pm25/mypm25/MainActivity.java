package com.pm25.mypm25;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private static ViewPager viewPager;

    private LinearLayout ll_weather;
    private LinearLayout ll_pm25;
    private LinearLayout ll_pre;
    private LinearLayout ll_city;

    private ImageView iv_weather;
    private ImageView iv_pm25;
    private ImageView iv_pre;
    private ImageView iv_city;

    private TextView tv_weather;
    private TextView tv_pm25;
    private TextView tv_pre;
    private TextView tv_city;

    private ImageButton ib_city;

    public String city_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGPS();

        //初始化控件
        initView();

        //初始化监听器
        initEven();

        //设置ViewPager的缓存界面数
        viewPager.setOffscreenPageLimit(3);

        //添加Fragment数组
        fragmentList = new ArrayList<>();
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new PM25Fragment());
        fragmentList.add(new PreFragment());
        fragmentList.add(new CityFragment());
        //创建FragmentPagerAdapter适配器
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);

    }

    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(MainActivity.this, "请打开GPS",
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定",
                    new android.content.DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                    arg0.dismiss();
                }
            } );
            dialog.show();
        }
    }

    private void initView(){
        //底部菜单4个Linearlayout
        ll_weather = (LinearLayout) findViewById(R.id.ll_weather);
        ll_pm25 = (LinearLayout) findViewById(R.id.ll_pm25);
        ll_pre = (LinearLayout) findViewById(R.id.ll_pre);
        ll_city = (LinearLayout) findViewById(R.id.ll_city);
        //中间内容
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //底部菜单4个ImageView
        iv_weather = (ImageView) findViewById(R.id.iv_weather);
        iv_pm25 = (ImageView) findViewById(R.id.iv_pm25);
        iv_pre = (ImageView) findViewById(R.id.iv_pre);
        iv_city = (ImageView) findViewById(R.id.iv_city);
        //底部菜单4个标题
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_pm25 = (TextView) findViewById(R.id.tv_pm25);
        tv_pre = (TextView) findViewById(R.id.tv_pre);
        tv_city = (TextView) findViewById(R.id.tv_city);
    }

    //点击事件
    private void initEven(){
        ll_weather.setOnClickListener(this);
        ll_pm25.setOnClickListener(this);
        ll_pre.setOnClickListener(this);
        ll_city.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        restartButton();
        switch(v.getId()){
            case R.id.ll_weather:
                iv_weather.setImageResource(R.drawable.tab_weather_pressed);
                tv_weather.setTextColor(0xff63B8FF);
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_pm25:
                iv_pm25.setImageResource(R.drawable.tab_pm25_pressed);
                tv_pm25.setTextColor(0xff63B8FF);
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_pre:
                iv_pre.setImageResource(R.drawable.table_pre_pressed);
                tv_pre.setTextColor(0xff63B8FF);
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_city:
                iv_city.setImageResource(R.drawable.tab_city_pressed);
                tv_city.setTextColor(0xff63B8FF);
                viewPager.setCurrentItem(3);
                break;
            default:
                break;
        }

    }
    public void restartButton(){
        iv_weather.setImageResource(R.drawable.tab_weather_normal);
        iv_pm25.setImageResource(R.drawable.tab_pm25_normal);
        iv_pre.setImageResource(R.drawable.table_pre_normal);
        iv_city.setImageResource(R.drawable.tab_city_normal);

        tv_weather.setTextColor(0x40000000);
        tv_pm25.setTextColor(0x40000000);
        tv_pre.setTextColor(0x40000000);
        tv_city.setTextColor(0x40000000);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        restartButton();
        switch (position){
            case 0:
                iv_weather.setImageResource(R.drawable.tab_weather_pressed);
                tv_weather.setTextColor(0xff63B8FF);
                break;
            case 1:
                iv_pm25.setImageResource(R.drawable.tab_pm25_pressed);
                tv_pm25.setTextColor(0xff63B8FF);
                break;
            case 2:
                iv_pre.setImageResource(R.drawable.table_pre_pressed);
                tv_pre.setTextColor(0xff63B8FF);
                break;
            case 3:
                iv_city.setImageResource(R.drawable.tab_city_pressed);
                tv_city.setTextColor(0xff63B8FF);
                break;
            default:
                break;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}

package com.lijie.smartweather;

import android.content.Context;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lijie.smartweather.activity.AboutActivity;
import com.lijie.smartweather.activity.MultiCityActivity;
import com.lijie.smartweather.base.BaseApplication;
import com.lijie.smartweather.beans.Weather;
import com.lijie.smartweather.service.AutoUpdateService;
import com.lijie.smartweather.utils.DoubleClickExit;
import com.lijie.smartweather.utils.HttpUtil;
import com.lijie.smartweather.utils.ImageLoader;
import com.lijie.smartweather.utils.SharedPreferenceUtil;
import com.lijie.smartweather.utils.Utility;
import com.orhanobut.logger.Logger;
import com.zaaach.citypicker.CityPickerActivity;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
//    @Bind(R.id.viewPage)
//    ViewPager viewPage;
//    @Bind(R.id.tabLayout)
//    TabLayout tabLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    //now页面

    @Bind(R.id.toolTextView)
    TextView toolTextView;
    @Bind(R.id.weather_icon)
    ImageView weatherIcon;
    @Bind(R.id.weather_icon_text)
    TextView weatherIconText;
    @Bind(R.id.temp_flu)
    TextView tempFlu;
    @Bind(R.id.temp_max)
    TextView tempMax;
    @Bind(R.id.temp_min)
    TextView tempMin;
    @Bind(R.id.temp_pm)
    TextView tempPm;
    @Bind(R.id.temp_quality)
    TextView tempQuality;
    @Bind(R.id.last_upate_text)
    TextView lastTime;

    //main_hourlist

    //main_index
    @Bind(R.id.index_cloth_brief)
    TextView indexClothBrief;
    @Bind(R.id.index_cloth_txt)
    TextView indexClothTxt;
    @Bind(R.id.index_sport_brief)
    TextView indexSportBrief;
    @Bind(R.id.index_sport_txt)
    TextView indexSportTxt;
    @Bind(R.id.index_travel_brief)
    TextView indexTravelBrief;
    @Bind(R.id.index_travel_txt)
    TextView indexTravelTxt;
    @Bind(R.id.index_flu_brief)
    TextView indexFluBrief;
    @Bind(R.id.index_flu_txt)
    TextView indexFluTxt;




    public DrawerLayout mDrawerLayout;
    public SwipeRefreshLayout swipeRefresh;
    private ScrollView scrollView;
    public static String city;
    private static final int REQUEST_CODE_PICK_CITY = 233;
    private Handler mHandler = new Handler() {
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            switch (type) {
                case 1:
//                    mCityListAdapter.notifyDataSetChanged();//左侧菜单更新
//                    requestWeather(mCurrentCity);//请求天气
//                  /*  List<WeatherDB> weatherDBs= (List<WeatherDB>) msg.obj;
//                    for(WeatherDB weatherDB:weatherDBs){
//                        mListCity.add(weatherDB.getmCityName());
//                    }*/
//                    break;
                case 2:
                    requestWeather(city);//请求天气
                    swipeRefresh.setRefreshing(false);//请求后下拉刷新停止
                    Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiprefreshContent);
        scrollView = (ScrollView) findViewById(R.id.weather_scrollview_layout);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menus);
        }
        navView.setCheckedItem(R.id.nav_city);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_city:
                        startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),
                                REQUEST_CODE_PICK_CITY);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_multi_cities:
                        MultiCityActivity.launch(MainActivity.this);
                        break;
                    case R.id.nav_set:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_about:
                        AboutActivity.launch(MainActivity.this);
                        break;
                }
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
//                Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();
            }
        });
        //设置tablayout
//        FirstPageAdapter firstPageAdapter = new FirstPageAdapter(getSupportFragmentManager());
//        firstPageAdapter.addTab(new MainFragment(), "首页");
//        firstPageAdapter.addTab(new MultiCityFragment(), "多城市管理");
//        viewPage.setAdapter(firstPageAdapter);
//        tabLayout.setupWithViewPager(viewPage, false);
//        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(final int position) {
//                fab.post(new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
//                            @Override
//                            public void onHidden(FloatingActionButton fab) {
//                                //super.onHidden(fab);
//                                if (position==1){
//                                    fab.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Snackbar.make(v, "Data deleted", Snackbar.LENGTH_SHORT)
//                                                    .setAction("Undo", new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//                                                            Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    })
//                                                    .show();
//                                        }
//                                    });
//
//                                }
//                                else{
//                                    fab.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),
//                                                    REQUEST_CODE_PICK_CITY);
//
//                                        }
//                                    });
//                                }
//                                fab.show();
//                            }
//                        });
//                    }
//                }));
//                if (!fab.isShown()) {
//                    fab.show();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
        initIcon();

        scrollView.smoothScrollTo(0, 0);
        String weatherString = SharedPreferenceUtil.getInstance().getString("weather", null);
        //scrollView.setVisibility(View.INVISIBLE);
        if (weatherString != null) {
            toolbar.setTitle("");
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }
        else {
            // 无缓存时去服务器查询天气
           // mWeatherId = getIntent().getStringExtra("weather_id");
            //city = SharedPreferenceUtil.getInstance().getString("weather_name",null);
            //scrollView.setVisibility(View.INVISIBLE);
            requestWeather(city);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                //city = SharedPreferenceUtil.getInstance().getString("weather_name",null);
//                requestWeather(city);
//                Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
//
//                Logger.d(city + "city是什么~~");

                //判断是否有网络
                if(Utility.isNetworkConnected(getApplicationContext())){
                    Message message = Message.obtain();
                    message.what =2;
                    // requestWeather(mCurrentCity);
                    //延时2秒
                    mHandler.sendMessageDelayed(message,2000);
                }else{
                    Toast.makeText(getApplicationContext(),"请求失败,请检查网络状况",Toast.LENGTH_SHORT).show();
                    if(swipeRefresh.isRefreshing()){
                        swipeRefresh.setRefreshing(false);
                    }
                }

            }
        });
        //下拉刷新颜色设置
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //控件拉动是放大放小，起始位置，结束位置
        swipeRefresh.setProgressViewOffset(false, 150, 300);
        //初始化ScrollView
       // ScrollView mScrollView = (ScrollView) findViewById(R.id.weather_scrollview_layout);
       // mScrollView.smoothScrollTo(0, 0);
//     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();
//            }
//        });

    }


    private void initIcon() {
        if (SharedPreferenceUtil.getInstance().getIconType() == 0) {
            SharedPreferenceUtil.getInstance().putInt("未知", R.mipmap.none);
            SharedPreferenceUtil.getInstance().putInt("晴", R.mipmap.type_one_sunny);
            SharedPreferenceUtil.getInstance().putInt("阴", R.mipmap.type_one_cloudy);
            SharedPreferenceUtil.getInstance().putInt("多云", R.mipmap.type_one_cloudy);
            SharedPreferenceUtil.getInstance().putInt("少云", R.mipmap.type_one_cloudy);
            SharedPreferenceUtil.getInstance().putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
            SharedPreferenceUtil.getInstance().putInt("小雨", R.mipmap.type_one_light_rain);
            SharedPreferenceUtil.getInstance().putInt("中雨", R.mipmap.type_one_light_rain);
            SharedPreferenceUtil.getInstance().putInt("大雨", R.mipmap.type_one_heavy_rain);
            SharedPreferenceUtil.getInstance().putInt("阵雨", R.mipmap.type_one_thunderstorm);
            SharedPreferenceUtil.getInstance().putInt("雷阵雨", R.mipmap.type_one_thunder_rain);
            SharedPreferenceUtil.getInstance().putInt("霾", R.mipmap.type_one_fog);
            SharedPreferenceUtil.getInstance().putInt("雾", R.mipmap.type_one_fog);
        } else {
            SharedPreferenceUtil.getInstance().putInt("未知", R.mipmap.none);
            SharedPreferenceUtil.getInstance().putInt("晴", R.mipmap.type_two_sunny);
            SharedPreferenceUtil.getInstance().putInt("阴", R.mipmap.type_two_cloudy);
            SharedPreferenceUtil.getInstance().putInt("多云", R.mipmap.type_two_cloudy);
            SharedPreferenceUtil.getInstance().putInt("少云", R.mipmap.type_two_cloudy);
            SharedPreferenceUtil.getInstance().putInt("晴间多云", R.mipmap.type_two_cloudytosunny);
            SharedPreferenceUtil.getInstance().putInt("小雨", R.mipmap.type_two_light_rain);
            SharedPreferenceUtil.getInstance().putInt("中雨", R.mipmap.type_two_rain);
            SharedPreferenceUtil.getInstance().putInt("大雨", R.mipmap.type_two_rain);
            SharedPreferenceUtil.getInstance().putInt("阵雨", R.mipmap.type_two_rain);
            SharedPreferenceUtil.getInstance().putInt("雷阵雨", R.mipmap.type_two_thunderstorm);
            SharedPreferenceUtil.getInstance().putInt("霾", R.mipmap.type_two_haze);
            SharedPreferenceUtil.getInstance().putInt("雾", R.mipmap.type_two_fog);
            SharedPreferenceUtil.getInstance().putInt("雨夹雪", R.mipmap.type_two_snowrain);
        }

    }

    //加载toolbar的菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);

                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                 city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                //SharedPreferenceUtil.getInstance().putString("weather_name",city);
                Logger.d("helloooo");
                requestWeather(city);
            }
        }
    }

    public void requestWeather(final String cityName) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + cityName + "&key=f24d4c2e4088430080ba89d9c5ed5f7a";
        if (!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                //Logger.d(weather.basic.city + "该有天气了~~");
                Logger.d(weather.status + "status的状态");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferenceUtil.getInstance().putString("weather", responseText);
//                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            editor.putString("weather", responseText);
//                            editor.apply();
//                            MainFragment fragment = new MainFragment();
//                            fragment.showWeatherInfo(weather);
                            showWeatherInfo(weather);

                        } else {
                            Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.city;
        Logger.d("当前的城市"+cityName);
        //now页面
        toolbar.setTitle("");
        toolTextView.setText(cityName);
//        TextView weatherName = (TextView) findViewById(R.id.text);
//        weatherName.setText(cityName);
        //ImageView weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        ImageLoader.load(BaseApplication.getAppContext(), SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none),
                weatherIcon);
        //TextView weatherIconText = (TextView) findViewById(R.id.weather_icon_text);
        weatherIconText.setText(weather.now.cond.txt);
        //TextView tempFlu = (TextView) findViewById(R.id.temp_flu);
        tempFlu.setText(String.format("%s℃", weather.now.tmp));
       // TextView tempMax = (TextView) findViewById(R.id.temp_max);
        tempMax.setText(String.format("%s℃",weather.dailyForecast.get(0).tmp.max));
        //TextView tempMin = (TextView) findViewById(R.id.temp_min);
        tempMin.setText(String.format("%s℃",weather.dailyForecast.get(0).tmp.min));
       // TextView tempPm = (TextView) findViewById(R.id.temp_pm);
        tempPm.setText(String.format("PM2.5: %s μg/m³", weather.aqi.city.pm25));
        //TextView tempQuality = (TextView) findViewById(R.id.temp_quality);
        tempQuality.setText(Utility.safeText("空气质量:",weather.aqi.city.qlty));
        //TextView lastTime = (TextView) findViewById(R.id.last_upate_text);
        lastTime.setText(Utility.safeText("",weather.basic.update.loc));

        //main_hourlist
         LinearLayout itemHourInfoLayout;
         TextView[] mClock = new TextView[weather.hourlyForecast.size()];
         TextView[] mTemp = new TextView[weather.hourlyForecast.size()];
         TextView[] mHumidity = new TextView[weather.hourlyForecast.size()];
         TextView[] mWind = new TextView[weather.hourlyForecast.size()];
         itemHourInfoLayout = (LinearLayout)findViewById(R.id.item_hour_info_linearlayout);
         itemHourInfoLayout.removeAllViews();

        for (int i = 0; i < weather.hourlyForecast.size(); i++) {
            View view = View.inflate(BaseApplication.getAppContext(), R.layout.item_hour_info_line, null);
            mClock[i] = (TextView) view.findViewById(R.id.one_clock);
            mTemp[i] = (TextView) view.findViewById(R.id.one_temp);
            mHumidity[i] = (TextView) view.findViewById(R.id.one_humidity);
            mWind[i] = (TextView) view.findViewById(R.id.one_wind);
            itemHourInfoLayout.addView(view);
        }
        try {
            for (int i = 0; i < weather.hourlyForecast.size(); i++) {
                //s.subString(s.length-3,s.length);
                //第一个参数是开始截取的位置，第二个是结束位置。
                String mDate = weather.hourlyForecast.get(i).date;
                mClock[i].setText(
                        mDate.substring(mDate.length() - 5, mDate.length()));
                mTemp[i].setText(
                        String.format("%s℃", weather.hourlyForecast.get(i).tmp));
                mHumidity[i].setText(
                        String.format("%s%%", weather.hourlyForecast.get(i).hum)
                );
                mWind[i].setText(
                        String.format("%sKm/h", weather.hourlyForecast.get(i).wind.spd)
                );
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        //main_index
        //TextView indexClothBrief = (TextView) findViewById(R.id.index_cloth_brief);
        indexClothBrief.setText(String.format("穿衣指数---%s", weather.suggestion.drsg.brf));
        //TextView indexClothTxt = (TextView) findViewById(R.id.index_cloth_txt);
        indexClothTxt.setText(weather.suggestion.drsg.txt);
        //TextView indexSportBrief = (TextView) findViewById(R.id.index_cloth_brief);
        indexSportBrief.setText(String.format("运动指数---%s", weather.suggestion.sport.brf));
        //TextView indexSportTxt = (TextView) findViewById(R.id.index_sport_txt);
        indexSportTxt.setText(weather.suggestion.drsg.txt);
       // TextView indexTravelBrief = (TextView) findViewById(R.id.index_travel_brief);
        indexTravelBrief.setText(String.format("旅游指数---%s", weather.suggestion.trav.brf));
        //TextView indexTravelTxt = (TextView) findViewById(R.id.index_travel_txt);
        indexTravelTxt.setText(weather.suggestion.trav.txt);
       // TextView indexFluBrief = (TextView) findViewById(R.id.index_flu_brief);
        indexFluBrief.setText(String.format("感冒指数---%s", weather.suggestion.flu.brf));
       // TextView indexFluTxt = (TextView) findViewById(R.id.index_flu_txt);
        indexFluTxt.setText(weather.suggestion.flu.txt);

        //main_forecast
        LinearLayout forecastLayout = (LinearLayout) findViewById(R.id.forecast_linear);
        forecastLayout.removeAllViews();
        TextView[] forecastDate = new TextView[weather.dailyForecast.size()];
        TextView[] forecastTemp = new TextView[weather.dailyForecast.size()];
        TextView[] forecastTxt = new TextView[weather.dailyForecast.size()];
        ImageView[] forecastIcon = new ImageView[weather.dailyForecast.size()];
        for (int i = 0; i < weather.dailyForecast.size(); i++) {
            View view = View.inflate(BaseApplication.getAppContext(), R.layout.item_forecast_line, null);
            forecastDate[i] = (TextView) view.findViewById(R.id.forecast_dates);
            forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
            forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
            forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
            forecastLayout.addView(view);
        }
        try {
            //今日 明日
            forecastDate[0].setText("今日");
            forecastDate[0].getText();
            forecastDate[1].setText("明日");
            forecastDate[1].getText();
            Logger.d(forecastDate[0].getText()+"as");
            Logger.d(forecastDate[1].getText()+"as");
            for (int i = 0; i < weather.dailyForecast.size(); i++) {
                if (i > 1) {
                    try {
                        forecastDate[i].setText(
                                Utility.dayForWeek(weather.dailyForecast.get(i).date));
                        Logger.d(forecastDate[2].getText()+"as");
                    } catch (Exception e) {
                        Logger.e(e.toString());
                    }
                }
                ImageLoader.load(BaseApplication.getAppContext(),
                        SharedPreferenceUtil.getInstance().getInt(weather.dailyForecast.get(i).cond.txtD, R.mipmap.none),
                        forecastIcon[i]);

                forecastTxt[i].setText(
                        String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                                weather.dailyForecast.get(i).cond.txtD,
                                weather.dailyForecast.get(i).wind.sc,
                                weather.dailyForecast.get(i).wind.dir,
                                weather.dailyForecast.get(i).wind.spd,
                                weather.dailyForecast.get(i).pop));
                forecastTemp[i].setText(
                        String.format("%s℃ - %s℃",
                                weather.dailyForecast.get(i).tmp.min,
                                weather.dailyForecast.get(i).tmp.max));
                Logger.d(forecastTemp[0].getText()+"temp");
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        scrollView.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //双击退出程序
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!DoubleClickExit.check()) {
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}

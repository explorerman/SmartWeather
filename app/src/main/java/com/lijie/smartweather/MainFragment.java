package com.lijie.smartweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lijie.smartweather.base.BaseApplication;
import com.lijie.smartweather.beans.*;
import com.lijie.smartweather.utils.ImageLoader;
import com.lijie.smartweather.utils.SharedPreferenceUtil;
import com.lijie.smartweather.utils.SpUtils;
import com.lijie.smartweather.utils.Utility;
import com.orhanobut.logger.Logger;
import com.zaaach.citypicker.CityPickerActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.lijie.smartweather.utils.HttpUtil;

import java.io.IOException;

/**
 * Created by lijie on 17/5/18.
 */

public class MainFragment extends Fragment {
    //mainfragment页面控件

    @Bind(R.id.swiprefreshContent)
    SwipeRefreshLayout swipRefreshContent;
    @Bind(R.id.weather_scrollview_layout)
    ScrollView weatherScrollviewLayout;

    //main_now页面控件
    //main_now页面的外部布局
    @Bind(R.id.main_now_weather)
    RelativeLayout mainNowWeather;
    //天气图标
    @Bind(R.id.weather_icon)
    ImageView weatherIcon;
    //天气状态
    @Bind(R.id.weather_icon_text)
    TextView weatherIconText;
    //当天天气温度
    @Bind(R.id.temp_flu)
    TextView tempFlu;
    //当天最高气温
    @Bind(R.id.temp_max)
    TextView tempMax;
    //当天最低气温
    @Bind(R.id.temp_min)
    TextView tempMin;
    //最低，最高气温外部布局
    @Bind(R.id.temp_layout)
    LinearLayout tempLayout;
    //空气质量，指数，更新时间外部布局
    @Bind(R.id.linear_temp)
    LinearLayout linearTemp;
    //空气指数
    @Bind(R.id.temp_pm)
    TextView tempPm;
    //空气质量
    @Bind(R.id.temp_quality)
    TextView tempQuality;
    //最后更新时间
    @Bind(R.id.last_upate_text)
    TextView lastUpateText;


    //hourlist页面
//    @Bind(R.id.hourdata_recyclerview)
//    RecyclerView hourdataRecyclerview;

    //空气aqi页面
    @Bind(R.id.air_weather_condition)
    TextView mAirWeatherCondition;
    @Bind(R.id.air_pm2_5_index)
    TextView mAirPm25Index;
    @Bind(R.id.air_co_index)
    TextView mAirCoIndex;
    @Bind(R.id.air_pm10_index)
    TextView mAirPm10Index;
    @Bind(R.id.air_so2_index)
    TextView mAirSo2Index;
    @Bind(R.id.air_o3_index)
    TextView mAirO3Index;
    @Bind(R.id.air_no2_index)
    TextView mAirNo2Index;
    @Bind(R.id.aqi_primary_pollutant)
    TextView mAqiPrimaryPollutiant;


    //一周天气预报页面
//    @Bind(R.id.forecast_layout)
//    LinearLayout forecastLayout;


    //建议页面
    @Bind(R.id.index_cloth_txt)
    TextView indexClothTxt;
    @Bind(R.id.index_sport_txt)
    TextView indexSportTxt;
    @Bind(R.id.index_travel_txt)
    TextView indexTravelTxt;
    @Bind(R.id.index_flu_txt)
    TextView indexFluTxt;

    @Bind(R.id.index_cloth_brief)
    TextView indexClothBrief;
    @Bind(R.id.index_sport_brief)
    TextView indexSportBrief;
    @Bind(R.id.index_travel_brief)
    TextView indexTravelBrief;
    @Bind(R.id.index_flu_brief)
    TextView indexFluBrief;


    private View view;
    private Context mContext;
    //private  WeatherAdapter mAdapter;
    private static final int REQUEST_CODE_PICK_CITY = 0;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_content, container, false);
            ButterKnife.bind(this, view);
            Logger.d("dsfdsfdsfdsfdf");
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initView();
    }
//
//    private void initView() {
//        if (swipRefreshContent != null) {
//            swipRefreshContent.setColorSchemeResources(android.R.color.holo_blue_bright,
//                    android.R.color.holo_green_light,
//                    android.R.color.holo_orange_light,
//                    android.R.color.holo_red_light);
//            swipRefreshContent.setProgressViewOffset(false, 150, 300);
////            swipRefreshContent.setOnRefreshListener(
////                    () -> swipRefreshContent.postDelayed(this::load, 1000));
//        }
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mAdapter = new WeatherAdapter(mWeather);
//        mRecyclerView.setAdapter(mAdapter);
//    }
    public void showWeatherInfo(final Weather weather) {
        Logger.d("进来了");
        //String temple = weather.now.tmp;
        //tempFlu.setText(String.format("%s℃", weather.now.tmp));
       // tempFlu.setText("12");
        ImageLoader.load(BaseApplication.getAppContext(), SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none),
                weatherIcon);

    }

//    //从高德地图中返回城市，并查询天气
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == getActivity().RESULT_OK) {
//            if (data != null) {
//                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
//                MainActivity activity = (MainActivity) getActivity();
//                activity.requestWeather(city);
//                //requestWeather(city);
//
//            }
//        }
//    }

//    public static void requestWeather(final String cityName){
//        String weatherUrl = "https://free-api.heweather.com/v5/weather?city="+cityName+"&key=f24d4c2e4088430080ba89d9c5ed5f7a";
//        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//               final String responseText =response.body().string();
//                Weather weather = Utility.handleWeatherResponse(responseText);
//                Logger.d(weather.basic.city+"该有天气了~~");
//            }
//
//        });
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

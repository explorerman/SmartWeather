package com.lijie.smartweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lijie.smartweather.MainActivity;
import com.lijie.smartweather.R;
import com.lijie.smartweather.beans.Weather;
import com.zaaach.citypicker.CityPickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lijie on 17/6/18.
 */

public class MultiCityActivity extends AppCompatActivity {
    @Bind(R.id.toolTextView)
    TextView toolTextView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @Bind(R.id.cityedit_listview)
    SwipeMenuListView cityeditListview;
    @Bind(R.id.weather_scrollview_layout)
    ScrollView weatherScrollviewLayout;
    @Bind(R.id.swiprefreshContent)
    SwipeRefreshLayout swiprefreshContent;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    List<Weather> mCityList =new ArrayList<>();
    SwipeMenuListView mCityEditListview;
    private static final int REQUEST_CODE_PICK_CITY = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_multi_city);
        ButterKnife.bind(this);
        initData();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolTextView.setText("多城市管理");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MultiCityActivity.this, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initData() {
    }
    public static void launch(Context context) {
        context.startActivity(new Intent(context, MultiCityActivity.class));
    }
}

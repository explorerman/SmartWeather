package com.lijie.smartweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijie.smartweather.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lijie on 17/6/18.
 */

public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.banner)
    ImageView banner;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.coord)
    CoordinatorLayout coord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitleEnabled(false);

        toolbarLayout.setTitle(getString(R.string.toolbar_name));
        toolbar.setTitle(this.getString(R.string.toolbar_name));
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

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }
}

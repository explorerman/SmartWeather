package com.lijie.smartweather;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijie on 17/5/18.
 */

public class FirstPageAdapter extends FragmentPagerAdapter {
   // private List<Activity> activities = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    public FirstPageAdapter(FragmentManager fm) {
        super(fm);
    }
    public FirstPageAdapter(FragmentManager fm, TableLayout tableLayout){
        super(fm);
    }

    public void addTab(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }
//    public void addTab(Activity activity,String title){
//        activities.add(activity);
//        titles.add(title);
//    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}

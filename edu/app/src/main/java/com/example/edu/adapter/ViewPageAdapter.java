package com.example.edu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 扶摇 on 2017/6/23.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {
    List<Fragment> fagments;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fagments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fagments.get(i);
    }

    @Override
    public int getCount() {
        return fagments.size();
    }
}

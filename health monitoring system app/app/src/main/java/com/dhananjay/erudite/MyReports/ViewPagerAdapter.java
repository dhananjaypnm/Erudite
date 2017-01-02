package com.dhananjay.erudite.MyReports;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    String[] tabs={"Sugar Level","Blood Pressure","Pulse Rate","Temperature"};

    public ViewPagerAdapter(FragmentManager fm,int flag) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SugarLevelFragment();
            case 1:
                return new BloodPressureFragment();
            case 2:
                return new PulseRateFragment();
            case 3:
                return new TemperatureFragment();
        }
        return new SugarLevelFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
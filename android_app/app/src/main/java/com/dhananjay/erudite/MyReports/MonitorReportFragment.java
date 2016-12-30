package com.dhananjay.erudite.MyReports;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.erudite.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonitorReportFragment extends Fragment {


    public MonitorReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monitor_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager)view. findViewById(R.id.vitals_view_pager2);
        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(),1));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.vitals_tab_layout2);
        tabLayout.setupWithViewPager(viewPager);
    }
}

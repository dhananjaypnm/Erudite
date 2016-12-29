package com.dhananjay.erudite.MyReports;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.erudite.DatabaseHelper;
import com.dhananjay.erudite.R;
import com.dhananjay.erudite.VitalSignsReading;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodPressureFragment extends Fragment {

    DatabaseHelper helper;
    Dao<VitalSignsReading,Long> dao;
    int type=2;
    List<VitalSignsReading> vitalSignsReadingList;


    public BloodPressureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_pressure, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            helper= OpenHelperManager.getHelper(getContext(),DatabaseHelper.class);
            dao=helper.getDao();
            vitalSignsReadingList=dao.queryForEq("type",type);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.blood_pressure_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        VitalSignsReadingsRecyclerAdapter adapter=new VitalSignsReadingsRecyclerAdapter(getContext(),vitalSignsReadingList);
        recyclerView.setAdapter(adapter);
    }
}

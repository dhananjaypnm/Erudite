package com.dhananjay.erudite.MyReports;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.erudite.DatabaseHelper;
import com.dhananjay.erudite.MainActivity;
import com.dhananjay.erudite.R;
import com.dhananjay.erudite.VitalSignsReading;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class SugarLevelFragment extends Fragment {

    DatabaseHelper helper;
    Dao<VitalSignsReading,Long> dao;
    int type=1;
    List<VitalSignsReading> vitalSignsReadingList;
int flag=0;

    public SugarLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sugar_level, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            helper= OpenHelperManager.getHelper(getContext(),DatabaseHelper.class);
            dao=helper.getDao();

            QueryBuilder<VitalSignsReading,Long> queryBuilder=helper.getDao().queryBuilder();
            queryBuilder.orderBy("recordedTimestamp",false);
            queryBuilder.where().eq("type",type);
            vitalSignsReadingList=queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.sugar_level_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        VitalSignsReadingsRecyclerAdapter adapter=new VitalSignsReadingsRecyclerAdapter(getContext(),vitalSignsReadingList);
        recyclerView.setAdapter(adapter);
/*
        LineChart chart = (LineChart)view.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2,3));
        entries.add(new Entry(3,4));
        entries.add(new Entry(4,2));
        entries.add(new Entry(5,5));
        entries.add(new Entry(6,4.5f));

        LineDataSet dataSet = new LineDataSet(entries, "Sugar Levels");
        dataSet.setCircleColor(Color.RED);
        dataSet.setColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        Description description=new Description();
        description.setText("mMol/l");
        chart.setDescription(description);

        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawGridBackground(false);
        LimitLine limitLine = new LimitLine(5f, "Critical Blood Sugar");
        limitLine.setLineColor(Color.RED);
        limitLine.setTextColor(Color.BLACK);

        YAxis yAxisLeft=chart.getAxisLeft();
        yAxisLeft.addLimitLine(limitLine);
        yAxisLeft.setAxisMaximum(7);
        yAxisLeft.setAxisMinimum(0);
        chart.setVisibleXRange(0,4);

        YAxis yAxisRight=chart.getAxisRight();
        yAxisRight.addLimitLine(limitLine);
        yAxisRight.setAxisMaximum(7);
        yAxisRight.setAxisMinimum(0);
        chart.setVisibleXRange(0,4);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(9);
        xAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.animateXY(3000, 3000);
        chart.invalidate();
        */
    }
}

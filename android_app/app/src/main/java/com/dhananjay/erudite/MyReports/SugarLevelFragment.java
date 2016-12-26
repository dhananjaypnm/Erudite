package com.dhananjay.erudite.MyReports;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.erudite.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SugarLevelFragment extends Fragment {


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
    }
}

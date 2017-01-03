package com.dhananjay.erudite.MyReports;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.erudite.R;
import com.dhananjay.erudite.VitalSignsReading;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class VitalSignsReadingsRecyclerAdapter extends RecyclerView.Adapter<VitalSignsReadingViewHolder>{

    private static final String TAG="RecyclerAdapter";

    List<VitalSignsReading> vitalSignsReadingList;
    Context context;

    VitalSignsReadingsRecyclerAdapter(Context context,List<VitalSignsReading> vitalSignsReadingList){
        this.context=context;
        this.vitalSignsReadingList=vitalSignsReadingList;
        Log.d(TAG, "VitalSignsReadingsRecyclerAdapter: ");
    }

    @Override
    public VitalSignsReadingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_vital_sign_reading_item, parent, false);
        return new VitalSignsReadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VitalSignsReadingViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+vitalSignsReadingList.get(position).getType());
        holder.userId.setText((vitalSignsReadingList.get(position)).getUserId());
        long seconds=(vitalSignsReadingList.get(position)).getRecordedTimestamp();
        long millis=seconds*1000;
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        holder.timestamp.setText(formattedDate);
        holder.value.setText(String.valueOf((vitalSignsReadingList.get(position)).getValue()));
        holder.sync.setText((String.valueOf((vitalSignsReadingList.get(position)).getSyncedWithServer())));

    }

    @Override
    public int getItemCount() {
        return vitalSignsReadingList.size();
    }
}

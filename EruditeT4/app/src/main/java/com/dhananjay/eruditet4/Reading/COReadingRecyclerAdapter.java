package com.dhananjay.eruditet4.Reading;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.eruditet4.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by dhananjay on 1/1/17.
 */

public class COReadingRecyclerAdapter extends RecyclerView.Adapter<COReadingViewHolder> {

    List<COReading> coReadingList;

    COReadingRecyclerAdapter(List<COReading> coReadingList){
        this.coReadingList=coReadingList;
    }

    @Override
    public COReadingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_co_reading_item, parent, false);
        return new COReadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(COReadingViewHolder holder, int position) {
        holder.value.setText(coReadingList.get(position).value);
        long seconds=(coReadingList.get(position)).recordedTimestamp;
        long millis=seconds*1000;
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        holder.timestamp.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return coReadingList.size();
    }
}

package com.dhananjay.erudite.DietData;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.erudite.MyReports.VitalSignsReadingViewHolder;
import com.dhananjay.erudite.R;

import java.util.List;

/**
 * Created by dhananjay on 31/12/16.
 */

public class DietDataRecyclerAdapter extends RecyclerView.Adapter<DietDataViewHolder> {

    List<DietData> dietDataList;

    DietDataRecyclerAdapter(List<DietData> dietDataList){
        this.dietDataList=dietDataList;
    }

    @Override
    public DietDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_diet_data_item, parent, false);
        return new DietDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DietDataViewHolder holder, int position) {
        holder.dietData.setText(dietDataList.get(position).dietData);

    }

    @Override
    public int getItemCount() {
        return dietDataList.size();
    }
}

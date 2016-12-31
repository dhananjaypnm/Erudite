package com.dhananjay.erudite.DietData;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dhananjay.erudite.R;

/**
 * Created by dhananjay on 31/12/16.
 */

public class DietDataViewHolder extends RecyclerView.ViewHolder {

    TextView dietData;

    public DietDataViewHolder(View itemView) {
        super(itemView);
        dietData= (TextView) itemView.findViewById(R.id.diet_data);
    }
}

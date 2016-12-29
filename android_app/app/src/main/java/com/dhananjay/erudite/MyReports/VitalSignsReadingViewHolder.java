package com.dhananjay.erudite.MyReports;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dhananjay.erudite.R;

/**
 * Created by dhananjay on 28/12/16.
 */

public class VitalSignsReadingViewHolder extends RecyclerView.ViewHolder{

    TextView userId,value,timestamp,sync;


    public VitalSignsReadingViewHolder(View itemView) {
        super(itemView);
        userId= (TextView) itemView.findViewById(R.id.user_id);
        value= (TextView) itemView.findViewById(R.id.value);
        timestamp= (TextView) itemView.findViewById(R.id.timestamp);
        sync= (TextView) itemView.findViewById(R.id.sync);

    }
}

package com.dhananjay.eruditet4.Reading;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dhananjay.eruditet4.R;

/**
 * Created by dhananjay on 1/1/17.
 */

public class COReadingViewHolder extends RecyclerView.ViewHolder {

    TextView value;
    TextView timestamp;

    public COReadingViewHolder(View itemView) {
        super(itemView);
        value= (TextView) itemView.findViewById(R.id.value);
        timestamp= (TextView) itemView.findViewById(R.id.timestamp);

    }


}


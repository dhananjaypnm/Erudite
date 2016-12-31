package com.dhananjay.eruditet4.Reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.eruditet4.R;
import com.dhananjay.eruditet4.Reading.COReading;
import com.j256.ormlite.dao.Dao;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {



    DatabaseHelper helper;
    Dao<COReading,Long> dao;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

}

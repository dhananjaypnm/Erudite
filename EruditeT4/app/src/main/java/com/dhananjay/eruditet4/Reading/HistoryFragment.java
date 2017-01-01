package com.dhananjay.eruditet4.Reading;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.eruditet4.R;
import com.dhananjay.eruditet4.Reading.COReading;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {



    DatabaseHelper helper;
    Dao<COReading,Long> dao;
    List<COReading> coReadingList;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper= OpenHelperManager.getHelper(getContext(),DatabaseHelper.class);
        try {
            dao=helper.getDao();
            coReadingList=dao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.co_level_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        COReadingRecyclerAdapter adapter=new COReadingRecyclerAdapter(coReadingList);
        recyclerView.setAdapter(adapter);

    }
}

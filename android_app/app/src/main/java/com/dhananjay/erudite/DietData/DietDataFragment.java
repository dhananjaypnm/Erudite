package com.dhananjay.erudite.DietData;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhananjay.erudite.MyReports.VitalSignsReadingsRecyclerAdapter;
import com.dhananjay.erudite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietDataFragment extends Fragment {

    List<DietData> dietDataList;


    public DietDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet_data, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.diet_data_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        dietDataList=new ArrayList<>();
        String string="-->Diet for high sugar (6.0 and above):\n" +
                "1.Brown or wild rice.\n" +
                "2.High fiber low sugar cereal.\n" +
                "3.Peas, leafy greens.\n" +
                "4.Low sugar bran flakes.\n" +
                "5.whole wheat pasta.6.High fiber fruits and vegetables.\n" +
                "-->Diet for low sugar (4.5 and below):\n" +
                "1.Carbohydrate rich foods with a low glycemic index.\n" +
                "2.Low GI foods include bran cereals, large flake, pasta, milk, yogurt, soy beverages, apples, pears, oranges, dried apricots, nuts, seeds and legumes.";
        dietDataList.add(new DietData(string));
        string="-->Diet for High BP:(140/90 and above)\n" +
                "1.Fruits and vegetables as they are rich in potassium,magnesium and fiber .Low Sodium\n" +
                "2.Nuts,seeds,legumes,fish and poultry.\n" +
                "3.Whole grains,Low fat diary products for fiber and calcium.\n" +
                "-->Diet for low BP:(90/60) and below\n" +
                "1.Milk,Almond,lemon and salt, limited intake of high carbohydrate foods such as potatoes,rice,pasta and bread.\n" +
                "2.Drink more water.\n" +
                "3.Raisins,carrots and caffeinated foods.";
        dietDataList.add(new DietData(string));
        string="-->Diet for high pulse rate (100bpm and above):\n" +
                "1.Appropriate diet for this condition usually includes low-fat foods and a significant amount of fruits, vegetables and other foods rich in vitamins.\n"+
                "2.Banana, fish, tomato, avocado, garlic, pumpkin, milk , spinach\n"+
                "-->Diet for low pulse rate(60bpm and below):\n"+
                "Caffeine containing foods, chocolates, hot peppers";
        dietDataList.add(new DietData(string));
        string="-->Diet for high temperature (37.5\u00b0C and above):\n" +
                "1.One should drink lots of water when affected with fever.\n"+
                "-->Diet for low temperature (35\u00b0C and below):\n" +
                "2.Consume raw fruits and vegetables which can be peeled.\n"+
                "3.One should reduce or stop caffeine intake.\n"+
                "Providing warm beverages, such as hot chocolate or decaffeinated tea. Do not give the person caffeine or alcohol." ;
        dietDataList.add(new DietData(string));
        DietDataRecyclerAdapter adapter=new DietDataRecyclerAdapter(dietDataList);
        recyclerView.setAdapter(adapter);
    }
}

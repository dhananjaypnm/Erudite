package com.dhananjay.erudite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dhananjay.erudite.Map.GMapFragment;
import com.dhananjay.erudite.MyReports.MonitorFragment;
import com.dhananjay.erudite.MyReports.MonitorReportFragment;
import com.dhananjay.erudite.MyReports.ReportsFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG="MainActivity";
    DatabaseHelper helper;
    Dao<VitalSignsReading,Long> dao;
    List<VitalSignsReading> vitalSignsReadingList;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d(TAG, "onCreate: "+prefs.getString("public_key","lala"));

        userId=prefs.getString("user_id","lala");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        long time=1482957383;
        vitalSignsReadingList=new ArrayList<>();


        try {
            helper= OpenHelperManager.getHelper(MainActivity.this,DatabaseHelper.class);
            dao=helper.getDao();

            int count=0;
            for(int i=0;i<10;i++){
                vitalSignsReadingList.add(new VitalSignsReading(userId,time,50.5,1,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                   int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = "+id);
                    Log.d(TAG, "onCreate: "+count+++" "+i +" "+1);
                }else{
                    Log.d(TAG, "onCreate:1 list size"+list.size());
                }

                time=time-86400;
            }
            time+=86400*10+1;
            for(int i=10;i<20;i++){
                vitalSignsReadingList.add(new VitalSignsReading(userId,time,50.5,2,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                    int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = "+id);
                    Log.d(TAG, "onCreate: "+count+++" "+i+" "+2);

                }else{
                    Log.d(TAG, "onCreate:2 list size"+list.size());
                }
                time=time-86400;
            }
            time=time+86400*10+1;
            for(int i=20;i<30;i++){
                vitalSignsReadingList.add(new VitalSignsReading(userId,time,50.5,3,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                    int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = "+id);
                    Log.d(TAG, "onCreate: "+count+++" "+i+" "+3);

                }else{
                    Log.d(TAG, "onCreate:3 list size"+list.size());
                }
                time=time-86400;
            }
            time=time+86400*10+1;
            for(int i=30;i<40;i++){
                vitalSignsReadingList.add(new VitalSignsReading(userId,time,50.5,4,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                    int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = "+id);
                    Log.d(TAG, "onCreate: "+count+++" "+i+" "+4);

                }else{
                    Log.d(TAG, "onCreate:4 list size"+list.size());
                }
                time=time-86400;
            }
            time=time+86400*10+1;
            for(int i=40;i<50;i++){
                vitalSignsReadingList.add(new VitalSignsReading("notme",time,50.5,1,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                    int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = "+id);
                    Log.d(TAG, "onCreate: "+count+++" "+i +" "+1);

                }
                time=time-86400;
            }
            time+=86400*10+1;
            for(int i=50;i<60;i++){
                vitalSignsReadingList.add(new VitalSignsReading("notme",time,50.5,2,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                    int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = "+id);
                    Log.d(TAG, "onCreate: "+count+++" "+i+" "+2);

                }
                time=time-86400;
            }
            time=time+86400*10+1;
            for(int i=60;i<70;i++){
                vitalSignsReadingList.add(new VitalSignsReading("notme",time,50.5,3,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                    int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = "+id);
                    Log.d(TAG, "onCreate: "+count+++" "+i+" "+3);
                }
                time=time-86400;
            }
            time=time+86400*10+1;
            for(int i=70;i<80;i++){
                vitalSignsReadingList.add(new VitalSignsReading("notme",time,50.5,4,0,0));

                List<VitalSignsReading> list=new ArrayList<>();
                list=dao.queryForEq("recordedTimestamp",time);
                if(list.size()==0){
                    int id= dao.create(vitalSignsReadingList.get(i));
                    Log.d(TAG, "onCreate:inserted id = ");
                    Log.d(TAG, "onCreate: "+count+++" "+i+" "+4);

                }
                time=time-86400;
            }

            dao.queryForAll();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_reports) {
            ReportsFragment reportsFragment=new ReportsFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,reportsFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("My Reports");


        } else if (id == R.id.nav_monitor) {

            MonitorFragment monitorFragment=new MonitorFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,monitorFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Request Monitor");

        } else if (id == R.id.nav_diet) {

        } else if (id == R.id.nav_map) {
            GMapFragment gMapFragment=new GMapFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,gMapFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Find in Map");

        } else if (id == R.id.nav_receive_from_modules) {
            //listen
            ReceiveFragment receiveFragment=new ReceiveFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,receiveFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Receive from bluetooth");



        } else if (id == R.id.nav_sync) {
            //sync with server
            SyncFragment syncFragment=new SyncFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,syncFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Sync with server");


        }else if(id==R.id.nav_sign_out){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("logged_in",false);
            editor.commit();
            Log.d(TAG, "onNavigationItemSelected: logged out");
            Toast.makeText(this, "logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void grantedPermission(View view) {
        MonitorReportFragment monitorReportFragment=new MonitorReportFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,monitorReportFragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Monitor Reports");


    }
}
//freepik.com
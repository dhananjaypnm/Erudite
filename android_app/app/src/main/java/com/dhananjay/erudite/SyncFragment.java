package com.dhananjay.erudite;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SyncFragment extends Fragment implements View.OnClickListener, Callback<Result> {

    private static final String TAG="SyncFragment";
    private static final String BASE_URL="http://1bae0161.ngrok.io/";
    Retrofit retrofit;

    DatabaseHelper helper;
    Dao<VitalSignsReading,Long> dao;
    List<VitalSignsReading> vitalSignsReadingList;
    Button buttonSync;
    ProgressBar progressBarSync;
    String publicKeyString;
    PublicKey publicKey;
    byte[] encodedBytes = null;
    String encoded="";
    int syncSize=0;
    int count=0;


    public SyncFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sync, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonSync= (Button) view.findViewById(R.id.button_sync);
        progressBarSync= (ProgressBar) view.findViewById(R.id.progress_sync);

        buttonSync.setOnClickListener(this);

        Gson gson=new Gson();
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        try {
            helper= OpenHelperManager.getHelper(getContext(),DatabaseHelper.class);
            dao=helper.getDao();
            vitalSignsReadingList=dao.queryForEq("syncedWithServer",0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        publicKeyString=prefs.getString("public_key","not available");

        try {
            publicKey=loadPublicKey(publicKeyString);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


    }

    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = base64Decode(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }

    private static byte[] base64Decode(String stored) {
        return Base64.decode(stored, Base64.DEFAULT);
    }



    @Override
    public void onClick(View view) {

        progressBarSync.setVisibility(View.VISIBLE);




        Cipher c1 = null;
        try {
            c1 = Cipher.getInstance("RSA");
            c1.init(Cipher.ENCRYPT_MODE, publicKey);
            syncSize=vitalSignsReadingList.size();

            for(int i=0;i<vitalSignsReadingList.size();i++){
                VitalSignsReading readingitem=vitalSignsReadingList.get(i);
                readingitem.setSyncedWithServer(1);
                readingitem.setSyncTime(System.currentTimeMillis()/1000);

                double reading=readingitem.getValue();
                String readingString=String.valueOf(reading);

                encodedBytes = c1.doFinal(readingString.getBytes());
                encoded= Base64.encodeToString(encodedBytes, Base64.DEFAULT);
                Log.d(TAG, "crypto: encoded :"+encoded);

                API api=retrofit.create(API.class);
                Call<Result> call =api.syncRequest(3,readingitem.getUserId(),readingitem.getRecordedTimestamp(),encoded,readingitem.getType(),readingitem.getSyncedWithServer(),readingitem.getSyncTime());
                call.enqueue(this);


                try {
                    dao.update(readingitem);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {

        Result result=response.body();
        if(result!=null){

            Log.d(TAG, "onResponse: success:"+result.success+" result:"+result.message);
            if(result.success.equals(String.valueOf(1))){
               count++;
            }
        }else{
            Log.d(TAG, "onResponse: result null");
        }

        if(count==syncSize){
            Toast.makeText(getContext(), "synced", Toast.LENGTH_SHORT).show();
            progressBarSync.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        progressBarSync.setVisibility(View.GONE);
        Toast.makeText(getContext(), "failed to sync", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFailure: ");
    }
}

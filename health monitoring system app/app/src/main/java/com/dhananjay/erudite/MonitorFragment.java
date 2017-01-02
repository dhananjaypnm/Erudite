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
import android.widget.EditText;
import android.widget.Toast;

import com.dhananjay.erudite.API;
import com.dhananjay.erudite.DatabaseHelper;
import com.dhananjay.erudite.Keys;
import com.dhananjay.erudite.R;
import com.dhananjay.erudite.Result;
import com.dhananjay.erudite.TargetVitalSignsReading;
import com.dhananjay.erudite.VitalSignsReading;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.SQLException;
import java.util.Arrays;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MonitorFragment extends Fragment implements View.OnClickListener, Callback<TargetVitalSignsReading> {

    private static final String TAG="MonitorFragment";


    EditText userIdMonitor;
    Button buttonMonitor;
    private static  String BASE_URL;
    Retrofit retrofit;

    DatabaseHelper helper;
    Dao<VitalSignsReading,Long> dao;
    String userId;
    List<VitalSignsReading> vitalSignsReadingList;
    String privateKeyString;
    PrivateKey privateKey;

    public MonitorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monitor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BASE_URL=getActivity().getResources().getString(R.string.baseurl);

        userIdMonitor= (EditText) view.findViewById(R.id.user_id_monitor);
        buttonMonitor= (Button) view.findViewById(R.id.button_monitor);
        buttonMonitor.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        Gson gson=new Gson();
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        helper= OpenHelperManager.getHelper(getContext(),DatabaseHelper.class);
        try {
            dao=helper.getDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userId=prefs.getString("user_name","NIL");

        API api=retrofit.create(API.class);
        Call<TargetVitalSignsReading> call =api.monitorRequest(4,userId,userIdMonitor.getText().toString());
        call.enqueue(this);

    }

    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = base64Decode(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }

    private static byte[] base64Decode(String stored) {
        return Base64.decode(stored, Base64.DEFAULT);
    }

    @Override
    public void onResponse(Call<TargetVitalSignsReading> call, Response<TargetVitalSignsReading> response) {
        TargetVitalSignsReading targetVitalSignsReading=response.body();
        Keys targetKeys=targetVitalSignsReading.getKeys();
        privateKeyString=targetKeys.getPrivateKey();
        try {
            privateKey=loadPrivateKey(privateKeyString);
            Log.d(TAG, "onResponse: private key loaded: "+privateKeyString);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResponse: "+targetVitalSignsReading.getVitalSignsReadingArray().length);
        VitalSignsReading[] vitalSignsReadings=targetVitalSignsReading.getVitalSignsReadingArray();
        vitalSignsReadingList=Arrays.asList(vitalSignsReadings);
        for(int i=0;i<vitalSignsReadingList.size();i++){
            VitalSignsReading reading=vitalSignsReadingList.get(i);
            String encoded=reading.getValue();
            byte[] encodedBytes=Base64.decode(encoded,Base64.DEFAULT);
            byte[] decodedBytes=null;
            String decoded;
            Cipher c2 = null;
            try {
                c2 = Cipher.getInstance("RSA");
                c2.init(Cipher.DECRYPT_MODE, privateKey);
                decodedBytes = c2.doFinal(encodedBytes);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

            decoded=new String(decodedBytes);

            reading.setValue(decoded);
            Log.d(TAG, "crypto: decoded "+decoded);

            try {

                dao.create(reading);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(Call<TargetVitalSignsReading> call, Throwable t) {
        Toast.makeText(getContext(), "failed to download data", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFailure: "+t);
    }
}

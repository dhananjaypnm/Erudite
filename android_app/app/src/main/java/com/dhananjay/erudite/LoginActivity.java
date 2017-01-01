package com.dhananjay.erudite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResult> {

    EditText user_name_login,password_login;
    TextView sign_up_login;
    Button button_login;
    String userName="",password="";
    private static final String TAG="LoginActivity";
    private static  String BASE_URL;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BASE_URL=getResources().getString(R.string.baseurl);


        Gson gson=new Gson();
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        user_name_login= (EditText) findViewById(R.id.user_name_login);
        password_login= (EditText) findViewById(R.id.password_login);
        sign_up_login= (TextView) findViewById(R.id.sign_up_login);
        button_login= (Button) findViewById(R.id.button_login);
        sign_up_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });
        button_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


         userName=user_name_login.getText().toString();
        password=password_login.getText().toString();
        password=md5(password);
        API api=retrofit.create(API.class);
        Call<LoginResult> call =api.loginAuth(2,userName,password);
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
        LoginResult loginResult=response.body();
        String pubKey=loginResult.keys.publicKey;
        String privKey=loginResult.keys.privateKey;

        Log.d(TAG, "onResponse: success:"+loginResult.success+" result "+loginResult.message);
        if(loginResult.success.equals(String.valueOf(1))){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("logged_in",true);
            editor.putString("user_id",userName);
            editor.putString("public_key",pubKey);
            editor.putString("private_key",privKey);
            editor.commit();
            Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onFailure(Call<LoginResult> call, Throwable t) {
        Log.d(TAG, "onFailure: failed"+t);
        Toast.makeText(this,"Failed!"+t,Toast.LENGTH_LONG).show();
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

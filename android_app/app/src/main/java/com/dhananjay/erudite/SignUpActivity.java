package com.dhananjay.erudite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, Callback<Result> {

    EditText name_sign_up,user_name_sign_up,password_sign_up,retype_password_sign_up,phone_sign_up;
    String name,userName,password,retypePassword,phone;
    Button button_sign_up;
    private static final String TAG="SignUpActivity";
    private static final String BASE_URL="http://10.42.0.1/";
    Retrofit retrofit;

    String publicKey="",privateKey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Gson gson=new Gson();
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        name_sign_up= (EditText) findViewById(R.id.name_sign_up);
        user_name_sign_up= (EditText) findViewById(R.id.user_name_sign_up);
        password_sign_up= (EditText) findViewById(R.id.password_sign_up);
        retype_password_sign_up= (EditText) findViewById(R.id.retype_password_sign_up);
        phone_sign_up= (EditText) findViewById(R.id.phone_sign_up);
        button_sign_up= (Button) findViewById(R.id.button_sign_up);
        button_sign_up.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        name=name_sign_up.getText().toString();
        userName=user_name_sign_up.getText().toString();
        password=password_sign_up.getText().toString();
        retypePassword=retype_password_sign_up.getText().toString();
        phone=phone_sign_up.getText().toString();
        if(password.equals(retypePassword)){
            API api=retrofit.create(API.class);
            Call<Result> call =api.signUpRequest(1,name,userName,password,phone,publicKey);
            call.enqueue(this);
        }else{
            Toast.makeText(this, "passwords don't match", Toast.LENGTH_SHORT).show();
        }

        /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("public_key",publicKey);
            editor.putString("private_key",privateKey);
            editor.commit();

            */


    }

    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {
        Result result=response.body();
        Log.d(TAG, "onResponse: success:"+result.success+" result:"+result.message);
        if(result.success.equals(String.valueOf(1))){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("logged_in",true);
            editor.commit();
            Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        Log.d(TAG, "onFailure: failed"+t);
        Toast.makeText(this,"Failed! Make sure you have proper internet connection",Toast.LENGTH_LONG).show();

    }
}

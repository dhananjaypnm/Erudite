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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<Result> {

    EditText user_name_login,password_login;
    TextView sign_up_login;
    Button button_login;
    String userName="",password="";
    private static final String TAG="LoginActivity";
    private static final String BASE_URL="http://1bae0161.ngrok.io/";
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        API api=retrofit.create(API.class);
        Call<Result> call =api.loginAuth(2,userName,password);
        call.enqueue(this);


    }

    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {
        Result result=response.body();
        Log.d(TAG, "onResponse: success:"+result.success+" result"+result.message);
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
        Toast.makeText(this,"Failed!"+t,Toast.LENGTH_LONG).show();
    }
}

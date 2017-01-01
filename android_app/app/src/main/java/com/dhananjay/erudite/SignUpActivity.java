package com.dhananjay.erudite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

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
    private static  String BASE_URL;
    Retrofit retrofit;

    String pubKey;
    String privKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        BASE_URL=getResources().getString(R.string.baseurl);

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
        String test="this is a test text";
        Log.d(TAG, "crypto: "+test);
        KeyPair pair=null;
        byte[] encodedBytes = null;
        byte[] decodedBytes = null;
        String encoded="";
        String decoded="";

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            pair = kpg.genKeyPair();

            pubKey = savePublicKey(pair.getPublic());
            Log.d(TAG, "crypto: public key string :");
            PublicKey pubSaved = loadPublicKey(pubKey);
            Log.d(TAG, "crypto: public key :"+pubSaved);

            privKey = savePrivateKey(pair.getPrivate());
            Log.d(TAG, "crypto: private key string :"+privKey);
            PrivateKey privSaved = loadPrivateKey(privKey);
            Log.d(TAG, "crypto: private key :"+privSaved);

            Cipher c1 = Cipher.getInstance("RSA");
            c1.init(Cipher.ENCRYPT_MODE, pubSaved);
            encodedBytes = c1.doFinal(test.getBytes());
            encoded= Base64.encodeToString(encodedBytes, Base64.DEFAULT);
            Log.d(TAG, "crypto: encoded :"+encoded);

            Cipher c2 = Cipher.getInstance("RSA");
            c2.init(Cipher.DECRYPT_MODE, privSaved);
            decodedBytes = c2.doFinal(encodedBytes);

            decoded=new String(decodedBytes);
            Log.d(TAG, "crypto: decoded "+decoded);



        }catch (Exception e){
            Log.d(TAG, "crypto: "+e);
        }



        name=name_sign_up.getText().toString();
        userName=user_name_sign_up.getText().toString();
        password=password_sign_up.getText().toString();
        retypePassword=retype_password_sign_up.getText().toString();
        phone=phone_sign_up.getText().toString();



        if(password.equals(retypePassword)){

            password=md5(password);

            API api=retrofit.create(API.class);
            Call<Result> call =api.signUpRequest(1,name,userName,password,phone,pubKey,privKey);
            call.enqueue(this);
        }else{
            Toast.makeText(this, "passwords don't match", Toast.LENGTH_SHORT).show();
        }




    }


    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = base64Decode(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
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

    public static String savePrivateKey(PrivateKey priv) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
                PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = base64Encode(packed);

        Arrays.fill(packed, (byte) 0);
        return key64;
    }

    private static String base64Encode(byte[] packed) {
        return Base64.encodeToString(packed, Base64.DEFAULT);
    }


    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = fact.getKeySpec(publ,
                X509EncodedKeySpec.class);
        return base64Encode(spec.getEncoded());
    }


    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {
        Result result=response.body();
        if(result!=null){



            Log.d(TAG, "onResponse: success:"+result.success+" result:"+result.message);
            if(result.success.equals(String.valueOf(1))){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_id",userName);
                editor.putBoolean("logged_in",true);
                editor.putString("public_key",pubKey);
                editor.putString("private_key",privKey);
                editor.commit();
                Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }else{
            Log.d(TAG, "onResponse: result null");
        }
        

    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        Log.d(TAG, "onFailure: failed"+t);
        Toast.makeText(this,"Failed! Make sure you have proper internet connection",Toast.LENGTH_LONG).show();

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

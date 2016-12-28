package com.dhananjay.erudite;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    @FormUrlEncoded()
    @POST("request.php")
    Call<Result> loginAuth(
            @Field("request_code") int requestCode,
            @Field("user_name") String userName,
            @Field("password") String password
    );

    @FormUrlEncoded()
    @POST("request.php")
    Call<Result> signUpRequest(
            @Field("request_code") int requestCode,
            @Field("name") String name,
            @Field("user_name") String user_name,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("public_key") String public_key
    );

}
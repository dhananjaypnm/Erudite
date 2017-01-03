package com.dhananjay.erudite;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    @FormUrlEncoded()
    @POST("request.php")
    Call<LoginResult> loginAuth(
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
            @Field("public_key") String public_key,
            @Field("private_key")String private_key
    );

    @FormUrlEncoded()
    @POST("request.php")
    Call<Result> syncRequest(
            @Field("request_code") int requestCode,
            @Field("user_id") String userId,
            @Field("recorded_timestamp") long recordedTimestamp,
            @Field("value") String value,
            @Field("type") int type,
            @Field("in_sync") int inSync,
            @Field("sync_timestamp") long syncTimestamp

    );
    @FormUrlEncoded()
    @POST("request.php")
    Call<TargetVitalSignsReading> monitorRequest(
            @Field("request_code") int requestCode,
            @Field("monitor_user_id") String requesterUserId,
            @Field("target_user_id") String requested
    );

}
package com.dhananjay.eruditet4.Map;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapsAPI {
    /*
    * Retrofit get annotation with our URL
    * And our method that will return us details of student.
    */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyCvCfcxcSVzebXSs3_ufS62i_fKjeR6f4E")
    Call<ResponseObject> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
    //AIzaSyADh3e0sb3JFCJC6l5cYZ6wtaPuWcpcyKk
//AIzaSyCvCfcxcSVzebXSs3_ufS62i_fKjeR6f4E---final
}

package com.dhananjay.erudite;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhananjay on 31/12/16.
 */

public class LoginResult{


    @SerializedName("keys")
    Keys keys;

    @SerializedName("success")
    String success;

    @SerializedName("message")
    String message;

}



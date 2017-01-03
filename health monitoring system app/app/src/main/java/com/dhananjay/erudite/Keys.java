package com.dhananjay.erudite;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhananjay on 31/12/16.
 */

public class Keys {
    @SerializedName("public_key")
    String publicKey;

    @SerializedName("private_key")
    String privateKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}

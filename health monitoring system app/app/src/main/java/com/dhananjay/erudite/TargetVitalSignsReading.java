package com.dhananjay.erudite;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhananjay on 31/12/16.
 */

public class TargetVitalSignsReading {

    @SerializedName("keys")
    Keys keys;

    @SerializedName("request_vitals_result_array")
    VitalSignsReading[] vitalSignsReadingArray;

    public Keys getKeys() {
        return keys;
    }

    public VitalSignsReading[] getVitalSignsReadingArray() {
        return vitalSignsReadingArray;
    }
}

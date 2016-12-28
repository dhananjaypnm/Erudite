package com.dhananjay.erudite;


import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "VitalSignsReadings")
public class VitalSignsReading {

    @DatabaseField(generatedId = true)
    @SerializedName("id")
    long id;

    @DatabaseField
    @SerializedName("user_id")
    String userId;

    @DatabaseField
    @SerializedName("recorded_timestamp")
    long recordedTimestamp;

    @DatabaseField
    @SerializedName("value")
    double value;

    @DatabaseField
    @SerializedName("type")
    int type;

    @DatabaseField
    @SerializedName("sync_with_server")
    int syncedWithServer;

    @DatabaseField
    @SerializedName("sync_time")
    long syncTime;

}

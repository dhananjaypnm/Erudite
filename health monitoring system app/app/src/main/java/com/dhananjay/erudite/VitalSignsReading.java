package com.dhananjay.erudite;


import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "VitalSignsReadings")
public class VitalSignsReading {


    public VitalSignsReading(){}


    public VitalSignsReading(String userId, long recordedTimestamp, String value, int type, int syncedWithServer, long syncTime){
        this.userId=userId;
        this.recordedTimestamp=recordedTimestamp;
        this.value=value;
        this.type=type;
        this.syncedWithServer=syncedWithServer;
        this.syncTime=syncTime;

    }

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
    String value;

    @DatabaseField
    @SerializedName("type")
    int type;

    @DatabaseField
    @SerializedName("sync_with_server")
    int syncedWithServer;

    @DatabaseField
    @SerializedName("sync_time")
    long syncTime;

    public String getUserId() {
        return userId;
    }

    public long getRecordedTimestamp() {
        return recordedTimestamp;
    }

    public String getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public int getSyncedWithServer() {
        return syncedWithServer;
    }

    public long getSyncTime() {
        return syncTime;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public void setSyncedWithServer(int syncedWithServer) {
        this.syncedWithServer = syncedWithServer;
    }

    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }
}


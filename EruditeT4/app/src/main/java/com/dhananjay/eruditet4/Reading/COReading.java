package com.dhananjay.eruditet4.Reading;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by dhananjay on 30/12/16.
 */

public class COReading {

    COReading(long recordedTimestamp,double value){
        this.recordedTimestamp=recordedTimestamp;
        this.value=value;
    }

    @DatabaseField(generatedId = true)
    long id;

    @DatabaseField
    long recordedTimestamp;

    @DatabaseField
    double value;

}

package com.dhananjay.erudite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper  extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "VitalSignsReadings";
    private static final int DATABASE_VERSION = 1;
    private Dao<VitalSignsReading,Long> dao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, VitalSignsReading.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, VitalSignsReading.class,false);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<VitalSignsReading, Long> getDao() throws SQLException {
        if(dao == null) {
            dao = getDao(VitalSignsReading.class);
        }
        return dao;
    }

}

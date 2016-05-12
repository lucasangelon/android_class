package com.example.lucas.sampleapplication.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lucas on 12/05/2016.
 */

// Based on: http://developer.android.com/guide/topics/providers/content-provider-creating.html
// http://developer.android.com/guide/topics/data/data-storage.html#db
// http://developer.android.com/training/basics/data-storage/databases.html
public class DatabaseHelper extends SQLiteOpenHelper {

    // Defining database variables.
    private static final String DATABASE_NAME = "android_class.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor for this helper.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // This will generate a table if it does not already exists.
        db.execSQL(DatabaseContract.Weather.WEATHER_TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

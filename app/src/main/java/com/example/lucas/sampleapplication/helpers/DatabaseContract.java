package com.example.lucas.sampleapplication.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by lucas on 12/05/2016.
 */

// Based on: http://developer.android.com/guide/topics/providers/content-provider-creating.html
// http://developer.android.com/guide/topics/data/data-storage.html#db
// http://developer.android.com/training/basics/data-storage/databases.html
public class DatabaseContract {

    // Empty constructor in case someone tries to instantiate this.
    public DatabaseContract() {}

    // Defining the Weather table alongside all the columns and the base create query required.
    public static abstract class Weather implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_MAX = "max";
        public static final String COLUMN_NAME_MIN = "min";
        public static final String IF_NOT_EXISTS = "IF NOT EXISTS";

        public static final String WEATHER_TABLE_CREATE =
                "CREATE TABLE " + IF_NOT_EXISTS + " " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME_DAY + " TEXT NOT NULL, " +
                        COLUMN_NAME_TYPE + " TEXT NOT NULL, " +
                        COLUMN_NAME_MAX + " REAL NOT NULL, " +
                        COLUMN_NAME_MIN + " REAL NOT NULL);";
    }
}

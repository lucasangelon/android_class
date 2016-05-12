package com.example.lucas.sampleapplication.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by lucas on 12/05/2016.
 */
public class DatabaseContract {

    public DatabaseContract() {}

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

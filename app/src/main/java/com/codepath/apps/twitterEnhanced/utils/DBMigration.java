package com.codepath.apps.twitterEnhanced.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codepath.apps.twitterEnhanced.dao.MyDatabase;

/**
 * Created by Saranu on 3/23/17.
 */

public class DBMigration extends SQLiteOpenHelper {

    public static final String TABLE_1 = "User";
    public static final String TABLE_2 = "Tweet";


    private static final String DATABASE_NAME = MyDatabase.NAME;
    private static final int DATABASE_VERSION = MyDatabase.VERSION;


    public DBMigration(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        onCreate(db);
    }

}
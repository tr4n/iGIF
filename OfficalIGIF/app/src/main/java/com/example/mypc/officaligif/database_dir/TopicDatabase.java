package com.example.mypc.officaligif.database_dir;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class TopicDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "igif_2.db";
    private static final int DATABASE_VERSION = 1;


    public TopicDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}

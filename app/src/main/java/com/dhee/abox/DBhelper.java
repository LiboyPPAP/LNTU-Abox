package com.dhee.abox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {
    // name in string
    public static final String TABLE_NAME           = "datalv";
    public static final String COLUMN_TIMES         = "times";
    public static final String COLUMN_TEMPERATURE   = "temperature";
    public static final String COLUMN_HUMIDITY      = "humidity";

    public DBhelper(@Nullable Context context){

        super(context,"my.db",null,1);
    }


    // CREATE_TABLE
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_TIMES + " TEXT PRIMARY KEY," +
                    COLUMN_TEMPERATURE + " TEXT," +
                    COLUMN_HUMIDITY + " TEXT" +
                    ")";



    public void onCreate(SQLiteDatabase db) {
        // CREATE_TABLE()
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}

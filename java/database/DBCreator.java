package com.example.erick.rrq.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by erick on 19/02/17.
 */

public class DBCreator extends SQLiteOpenHelper {


    private final String table = "grupo";
    private final String script =
            "create table if not exists "+table+"(" +
                    "id integer primary key autoincrement," +
                    "nome text);";



    public DBCreator(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+table+";");
        onCreate(db);
    }
}

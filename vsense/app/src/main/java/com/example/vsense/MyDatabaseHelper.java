package com.example.vsense;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Murphy on 16/7/6.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_VSENSE = "create table Vsense ("
            + "username text primary key, "
            + "password text, "
            + "phoneNum integer, "
            + "email text, "
            + "id integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VSENSE);
        Toast.makeText(mContext, "Create database succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
package com.example.admin.myapplication.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2017-01-23.
 */

public class DBHelper  extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME ="IDBD_MOBILE";
    // Contacts table name
    private static final String CHAT_LIST ="GR_LIST";


    private static final String GR_ID = "gr_id";

    private static final String WRITER = "writer";


    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_gr_list = "CREATE TABLE " + CHAT_LIST+ "("+GR_ID +" INTEGER UNSIGNED ,"+WRITER+" INTEGER UNSIGNED, content TEXT , send_time timestamp);";
        sqLiteDatabase.execSQL(create_gr_list);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}

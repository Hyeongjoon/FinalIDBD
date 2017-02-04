package com.example.admin.myapplication.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2017-01-23.
 */

public class DBHelper  extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME ="IDBD_MOBILE";
    // Contacts table name
    static final String CHAT_LIST ="GR_LIST";


    static final String GR_ID = "gr_id";

    static final String WRITER = "writer";


    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_gr_list = "CREATE TABLE " + CHAT_LIST+ "("+GR_ID +" INTEGER UNSIGNED ,"+WRITER+" INTEGER UNSIGNED, content TEXT );";
        sqLiteDatabase.execSQL(create_gr_list);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor findChat(String gid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT writer , content FROM GR_LIST WHERE gr_id = " + gid , null);
        return cursor;
    }
}

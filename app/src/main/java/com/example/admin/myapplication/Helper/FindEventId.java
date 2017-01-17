package com.example.admin.myapplication.Helper;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by admin on 2017-01-10.
 */

public class FindEventId {
    public static int find(Calendar c){
        int result = (c.get(Calendar.DAY_OF_WEEK) -2)*24 +c.get(Calendar.HOUR_OF_DAY);
        return result;
    }
}

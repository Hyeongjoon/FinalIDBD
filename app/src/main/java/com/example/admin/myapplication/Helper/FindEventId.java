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

    public static Calendar idToCalender(int id){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR , 2017);
        cal.set(Calendar.MONTH , 0);
        cal.set(Calendar.DATE , 9);
        cal.add(Calendar.DATE , id/24);
        cal.set(Calendar.HOUR_OF_DAY , id%24);
        cal.set(Calendar.MINUTE ,0);
        cal.set(Calendar.SECOND ,0);
        cal.set(Calendar.MILLISECOND ,0);
        return cal;
    }
}

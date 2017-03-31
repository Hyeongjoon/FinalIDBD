package com.idbd.admin.myapplication.Helper;

import java.util.Random;

/**
 * Created by Admin on 2017-03-23.
 */

public class MakeRandomNum {
    public static String makeNum(){
        Random generator = new Random();
        String temp = "";
        for(int i = 0 ; i < 3 ; i++){
            temp = temp + generator.nextInt(10);
        }
        return temp;
    }
}

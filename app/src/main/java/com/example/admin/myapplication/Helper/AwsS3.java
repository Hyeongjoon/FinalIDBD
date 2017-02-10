package com.example.admin.myapplication.Helper;

/**
 * Created by Admin on 2017-02-10.
 */

public class AwsS3 {
    private static String accesskey;
    private static String secretkey;

    public static void setKey(String a , String b){
        accesskey = a;
        secretkey = b;
    }

    public static String getAccesskey(){
        return accesskey;
    }

    public static String getSecretkey(){
        return secretkey;
    }
}

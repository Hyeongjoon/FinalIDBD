package com.example.admin.myapplication.Helper;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by admin on 2017-01-13.
 */

public class TokenInfo extends FirebaseInstanceIdService {
    private static String TokenId;
    private static String userEmail;
    private static String userName;

    public static void setTokenId(String id){
        TokenId = id;
    }

    public static String getTokenId(){
        return TokenId;
    }

    public static void setUserInfo ( String email , String name){
        userEmail = email;
        userName = name;
    }

    //token refresh 됐을때 작성할것

    public static String getUserEmail(){
        return userEmail;
    }

    public static String getUserName(){
        return userName;
    }
}

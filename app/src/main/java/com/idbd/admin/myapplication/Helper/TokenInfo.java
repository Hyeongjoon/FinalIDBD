package com.idbd.admin.myapplication.Helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * Created by admin on 2017-01-13.
 */


public class TokenInfo extends FirebaseInstanceIdService {
    private static String TokenId;
    private static String userEmail;
    private static String userName;

    private String refreshURI = "http://52.78.18.19/refresh?token=";

    public static void setTokenId(String id){
        TokenId = id;
    }

    public static String getTokenId(){
        return TokenId;
    }

    public static void setUserInfo (String email , String name){
        userEmail = email;
        userName = name;
    }

    public static String getUserEmail(){
        return userEmail;
    }

    public static String getUserName(){
        return userName;
    }

    @Override
    public void onTokenRefresh() {

            FirebaseAuth.getInstance().signOut();
    }
}

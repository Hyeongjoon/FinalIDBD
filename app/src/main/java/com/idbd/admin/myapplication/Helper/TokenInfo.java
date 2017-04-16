package com.idbd.admin.myapplication.Helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        // Get updated InstanceID token.
        if(FirebaseAuth.getInstance()!=null){
            FirebaseAuth.getInstance().signOut();
        };
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }


}

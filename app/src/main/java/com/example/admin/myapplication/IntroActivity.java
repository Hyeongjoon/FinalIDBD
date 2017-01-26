package com.example.admin.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.admin.myapplication.Helper.TokenInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


/**
 * Created by admin on 2016-12-20.
 */
@EActivity(R.layout.activity_intro)
public class IntroActivity extends Activity {
    private Handler handler;


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LoginActivity_.intent(IntroActivity.this).start().withAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
};

    @AfterViews
    void goMain(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                user.getToken(true)
                                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                if (task.isSuccessful()) {
                                                    String idToken = task.getResult().getToken();  //id 토큰 알아오는곳
                                                    TokenInfo.setTokenId(idToken);
                                                    goMainActivity();
                                                    Log.d("msg" , ""+FirebaseInstanceId.getInstance().getToken());
                                                } else {
                                                    init();
                                                    handler.postDelayed(runnable, 1000);
                                                }
                        }
                    });
        } else {
            init();
            handler.postDelayed(runnable, 1000);
        }
    }

    void goMainActivity(){
        MainActivity_.intent(this).start();
        finish();
    }

    public void init() {
        handler = new Handler();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}

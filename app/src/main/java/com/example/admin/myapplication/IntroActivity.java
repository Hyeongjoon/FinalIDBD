package com.example.admin.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
        init();
        handler.postDelayed(runnable, 1000);
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

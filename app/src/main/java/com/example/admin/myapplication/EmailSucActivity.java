package com.example.admin.myapplication;

import android.app.Activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by admin on 2016-12-22.
 */
@EActivity(R.layout.activity_email_suc)
public class EmailSucActivity extends Activity{
    @Click(R.id.eamil_suc_go_login_btn)
    public void goToLogin(){
        LoginActivity_.intent(this).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
    }
}

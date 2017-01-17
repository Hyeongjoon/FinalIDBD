package com.example.admin.myapplication;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myapplication.Helper.TokenInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 2017-01-12.
 */

@EFragment(R.layout.fragment_content3)
public class MainSub3Fragment extends Fragment{
    @ViewById(R.id.main_sub3_user_email)
    TextView userEmail;

    @ViewById(R.id.main_sub3_user_name)
    TextView userName;

    @AfterViews
    public void init(){
        userEmail.setText(TokenInfo.getUserEmail());
        userName.setText(TokenInfo.getUserName());
    }
}

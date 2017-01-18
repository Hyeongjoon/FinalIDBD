package com.example.admin.myapplication;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myapplication.Helper.Get;
import com.example.admin.myapplication.Helper.TokenInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by admin on 2017-01-12.
 */

@EFragment(R.layout.fragment_content3)
public class MainSub3Fragment extends Fragment{

    private String getScheURL = "http://52.78.18.19/my_option/getSche/";

    @ViewById(R.id.main_sub3_user_email)
    TextView userEmail;

    @ViewById(R.id.main_sub3_user_name)
    TextView userName;

    @AfterViews
    public void init(){
        userEmail.setText(TokenInfo.getUserEmail());
        userName.setText(TokenInfo.getUserName());
    }

    @Click(R.id.main_sub3_my_schedule_btn)
    public void goMyschedule(){
        getSche();
    }

    @Background
    public void getSche(){
        try {
            Log.d("msg" , Get.get(getScheURL + TokenInfo.getTokenId())); //서버에서 스케쥴 정보 넘겨오는것까지함 그리고 mySchedule Activity로 보내는거 할것
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

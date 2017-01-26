package com.example.admin.myapplication;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myapplication.Helper.Get;
import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.TokenInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;

/**
 * Created by admin on 2017-01-12.
 */

@EFragment(R.layout.fragment_content3)
public class MainSub3Fragment extends Fragment{

    private String getScheURL = "http://52.78.18.19/my_option/getSche/";
    private ProgressDialog pDialog;

    @ViewById(R.id.main_sub3_user_email)
    TextView userEmail;

    @ViewById(R.id.main_sub3_user_name)
    TextView userName;

    @AfterViews
    public void Init(){
      Long start = System.currentTimeMillis();
        while (true) {
            if(TokenInfo.getUserEmail()!=null){
                userEmail.setText(TokenInfo.getUserEmail());
                userName.setText(TokenInfo.getUserName());
                if(System.currentTimeMillis()-start >1000){
                    break; //시작시간보다 1초지나면
                }
                break;
            }
        }
    }


    @Click(R.id.main_sub3_my_schedule_btn)
    public void goMyschedule(){
        pDialog = ProgressDialog.show(getActivity(), "서버와 통신중입니다.", "Please wait", true, false);
        getSche();
    }

    @Background
    public void getSche(){
        try {
            JSONObject result =  new JSONObject(Get.get(getScheURL + TokenInfo.getTokenId()));
            if(result.get("result").equals("success")){
                pDialog.cancel();
                MyScheduleActivity_.intent(getActivity()).extra("sche_info" , result.get("content").toString()).start();
            } else{
                pDialog.cancel();
                makeDialog("내부 서버 오류입니다 잠시후에 시도해 주세요");
            }
        } catch (IOException e){
            makeDialog("내부 서버 오류입니다 잠시후에 시도해 주세요");
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(getActivity() , content);
    }

}

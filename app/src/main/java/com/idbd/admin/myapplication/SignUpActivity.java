package com.idbd.admin.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.Post;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by admin on 2016-12-20.
 */
@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends Activity{

    ProgressDialog pDialog;
    private String sign_up_url = "http://52.78.18.19/signUp";

    @ViewById(R.id.sign_up_email)
    EditText emailText;

    @ViewById(R.id.sign_up_name)
    EditText nameText;

    @ViewById(R.id.sign_up_pwd)
    EditText pwdText;

    @ViewById(R.id.sign_up_individual)
    TextView individual;

    @ViewById(R.id.sign_up_service)
    TextView service;

    @ViewById(R.id.sign_up_pwd_confirm)
    EditText pwd_cofirmText;


    @AfterViews
    public void init(){
        individual.setClickable(true);
        individual.setMovementMethod(LinkMovementMethod.getInstance());
        service.setClickable(true);
        service.setMovementMethod(LinkMovementMethod.getInstance());
        String indi_link = "<a href='http://idbd.co.kr/policy/individual'>개인정보 취급 방침 동의</a>";
        String service_link = "<a href='http://idbd.co.kr/policy/service'>서비스 이용약관 동의</a>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            individual.setText(Html.fromHtml(indi_link, Html.FROM_HTML_MODE_LEGACY));
            service.setText(Html.fromHtml(service_link, Html.FROM_HTML_MODE_LEGACY));
        } else {
            individual.setText(Html.fromHtml(indi_link));
            service.setText(Html.fromHtml(service_link));
        }
    }

    boolean validCheck(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+$";
        String email = emailText.getText().toString();
        String pwd = pwdText.getText().toString();
        String pwd_con = pwd_cofirmText.getText().toString();
        String name = nameText.getText().toString();
        if(!email.matches(emailPattern)){
            makeDialog("올바른 이메일을 입력해 주세요");
            return false;
        } else if(name.length()==0 || name.length()>30){
            makeDialog("이름을 입력하거나 30자 이하로 설정해 주세요");
            return false;
        } else if(pwd.length()<8 ||pwd.length()>16){
            makeDialog("비밀번호는 8자리 이상 16자리 이하로 설정해주세요");
            return false;
        } else if(!pwd.equals(pwd_con)){
            makeDialog("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return false;
        } else{
            return  true;
        }
    }

    @Click(R.id.sign_up_sign_up_btn)
    void sign_up(){
        if(validCheck()){
            pDialog = ProgressDialog.show(this, "회원가입 중입니다...", "Please wait", true, false);
            signUp();
        }
    }

    @Click(R.id.sign_up_go_login_btn)
    void go_to_login(){
        LoginActivity_.intent(this).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
        finish();
    }

    @Background
    void signUp() {
        try {
            String email = emailText.getText().toString();
            String pwd = pwdText.getText().toString();
            String pwd_con = pwd_cofirmText.getText().toString();
            String name = nameText.getText().toString();
            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .add("pwd", pwd)
                    .add("pwd_con", pwd_con)
                    .add("name", name)
                    .build();

            JSONObject jsonObject = new JSONObject(Post.post(sign_up_url , formBody));
            String result = jsonObject.get("result").toString();
            if(result.equals("success")){
                pDialog.cancel();
                EmailSucActivity_.intent(this).start();
                finish();
            } else {
                pDialog.cancel();
                String content = jsonObject.get("content").toString();
                makeDialog(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(this , content);
    }
}

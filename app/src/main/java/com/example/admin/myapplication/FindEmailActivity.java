package com.example.admin.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.EditText;

import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;


import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


/**
 * Created by admin on 2016-12-27.
 */

@EActivity(R.layout.activity_find_email)
public class FindEmailActivity extends Activity{

    ProgressDialog pDialog;
    private String find_url = "http://52.78.18.19/findPwd";

    @ViewById(R.id.find_email_input)
    EditText emailText;

    @Click(R.id.find_go_login_btn)
    void go_to_login(){
        LoginActivity_.intent(this).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
        finish();
    }

    @Click(R.id.find_email_btn)
    void email(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+$";
        String email = emailText.getText().toString();
        if(!email.matches(emailPattern)){
            makeDialog("올바른 이메일을 입력해 주세요");
        }else{
            pDialog = ProgressDialog.show(this, "메일 발송 중입니다...", "Please wait", true, false);
            chk_email();
        }
    }

    @Background
    void chk_email(){
        try {
            String email = emailText.getText().toString();

            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .build();

            JSONObject jsonObject = new JSONObject(Post.post(find_url, formBody));
            String result = jsonObject.get("result").toString();
            if(result.equals("success")){
                pDialog.cancel();
                ModifyPwdActivity_.IntentBuilder_ intent = ModifyPwdActivity_.intent(this);
                intent.extra("email" , jsonObject.getString("email"));
                intent.start();
                finish();
            } else{
                pDialog.cancel();
                String content = jsonObject.get("content").toString();
                makeDialog(content);
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(this , content);
    }
}

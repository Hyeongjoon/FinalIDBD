package com.idbd.admin.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.EditText;

import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.Post;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by Admin on 2017-04-14.
 */

@EActivity(R.layout.activity_fb_email)
public class FbEmailActivity extends Activity {

    String fb_email_url = "http://52.78.18.19/android_mail_fb";
    ProgressDialog pDialog;

    @Extra
    String fb_id;

    @Extra
    String uid;

    @ViewById(R.id.fb_email_text_box)
    EditText editText;

    @Click(R.id.fb_email_verify)
    public void mailing(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+$";
        String email = editText.getText().toString();
        if(!email.matches(emailPattern)){
            makeDialog("올바르지 못한 이메일입니다.");
        } else{
            pDialog = ProgressDialog.show(this, "로그인 중입니다...", "Please wait", true, false);
            verify(email);
        }
    }

    @Click(R.id.fb_go_login_btn)
    public void goLogin(){
        LoginActivity_.intent(this).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
        finish();
    }

    @Background
    public void verify(String email){
        RequestBody formBody = new FormBody.Builder()
                .add("facebook_id", fb_id)
                .add("uid", uid)
                .add("email" , email)
                .build();
        try {
            JSONObject jsonObject = new JSONObject(Post.post(fb_email_url, formBody));
            if(jsonObject.getString("result").equals("true")){
                pDialogCancle();
                EmailSucActivity_.intent(this).start();
                finish();
            } else{
                pDialogCancle();
                makeDialog(jsonObject.getString("content"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void pDialogCancle(){
        if(pDialog!=null){
            pDialog.cancel();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(this , content);
    }
}

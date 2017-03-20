package com.idbd.admin.myapplication;

import android.app.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.idbd.admin.myapplication.Helper.Get;
import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.Post;
import com.idbd.admin.myapplication.Helper.TokenInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

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


@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity{
    ProgressDialog pDialog;
    private String login_url = "http://52.78.18.19/login";

    private FirebaseAuth mAuth;

    boolean goMain = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // your custom code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @AfterViews
    protected void init() {
        mAuth = FirebaseAuth.getInstance();
    }


    @ViewById(R.id.login_email)
    EditText emailText;

    @ViewById(R.id.login_pwd)
    EditText pwdText;

    @Click(R.id.login_sign_up_btn)
    void goSignUpActivity(){
        SignUpActivity_.intent(this).start();
    }

    void goSaveActivity(){
        SaveActivity_.intent(this).start();
        finish();
    }

    void goMainActivity(){
        pDialog.dismiss();
        MainActivity_.intent(this).start();
        finish();
    }

    boolean login_chk(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+$";
        String email = emailText.getText().toString();
        String pwd = pwdText.getText().toString();
        if(!email.matches(emailPattern)){
            makeDialog( "올바른 이메일을 입력해 주세요");
            return false;
        }  else if(pwd.length()<1){
            makeDialog("비밀번호를 입력해 주세요");
            return false;
        } else{
            return true;
        }
    }

    @Click(R.id.login_login_btn)
    void login_btn(){
        if(login_chk()){
            pDialog = ProgressDialog.show(this, "로그인 중입니다...", "Please wait", true, false);
            login();
        }
    }

    @Click(R.id.login_find_pwd)
    void find_pwd(){
        FindEmailActivity_.intent(this).start();
    }

    @Background
    void login(){
        try {
            String email = emailText.getText().toString();
            String pwd = pwdText.getText().toString();
            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .add("pwd", pwd)
                    .build();
            JSONObject jsonObject = new JSONObject(Post.post(login_url, formBody));
            String result = jsonObject.get("result").toString();
            if(result.equals("success")){
                String mCustomToken = jsonObject.get("content").toString();
                goMain = jsonObject.getBoolean("goMain");
                mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                 Log.d("msg", "signInWithCustomToken:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()){
                            Log.w("msg", "signInWithCustomToken", task.getException());
                                makeDialog("로그인 실패 잠시후에 시도해주세요");
                            } else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.getToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()) {
                                                String idToken = task.getResult().getToken();  //id 토큰 알아오는곳
                                                TokenInfo.setTokenId(idToken);
                                                if(goMain){
                                                    goMainActivity();
                                                } else{
                                                    goSaveActivity();
                                                }
                                            } else {
                                                makeDialog("로그인 실패 잠시후에 시도해주세요");
                                            }
                                        }
                                    });
                        }
                    }
                });
            } else {
                pDialog.cancel();
                String content = jsonObject.get("content").toString();
                makeDialog(content);
            }
        } catch (IOException e){
            e.printStackTrace();
            pDialog.cancel();
            makeDialog("로그인 실패 잠시후에 시도해주세요");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Background
    protected void getGrInfo(String url){
        try {
            Get.get(url);
        } catch (IOException e) {
            e.printStackTrace();
            makeDialog("내부 서버 오류입니다. 잠시후에 시도해주세요");
            pDialog.cancel();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(this , content);
    }
}

package com.idbd.admin.myapplication;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity{
    ProgressDialog pDialog;
    private String login_url = "http://52.78.18.19/login";
    private String loging_fb = "http://52.78.18.19/login/fb";

    private FirebaseAuth mAuth;

    private LoginButton loginButton;
    private CallbackManager facebookCallbackManager;

    @ViewById(R.id.login_text_view)
    TextView login_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // your custom code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        facebookCallbackManager= CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("msg"  ,""+loginResult.getAccessToken().getPermissions());
                GraphRequest request =GraphRequest.newMeRequest(loginResult.getAccessToken() ,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    registerFb(object.getString("name") , object.getString("id"));
                                    LoginManager.getInstance().logOut();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                // App code
            }

            @Override
            public void onError(FacebookException exception) {

                // App code
            }
        });
    }

    @UiThread
    public void makePdialog(){
        pDialog = ProgressDialog.show(this, "로그인 중입니다...", "Please wait", true, false);
    }


    @Background
    public void registerFb(String name , String fb_code){
        makePdialog();
        RequestBody formBody = new FormBody.Builder()
                .add("facebook_id", fb_code)
                .add("name", name)
                .build();
        try {
            JSONObject jsonObject = new JSONObject(Post.post(loging_fb, formBody));
            if(jsonObject.getString("result").equals("true")){
                if(jsonObject.getString("email").equals("null")){
                    pDialog.cancel();
                    FbEmailActivity_.intent(this).extra("fb_id" , fb_code).extra("uid" , jsonObject.getString("uid")).start();
                } else{
                    String mCustomToken = jsonObject.getString("token");
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
                                                            pDialog.cancel();
                                                            goMainActivity();
                                                        } else {
                                                            makeDialog("로그인 실패 잠시후에 시도해주세요");
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                };
            } else {
                pDialog.cancel();
                makeDialog("내부 서버오류입니다. 잠시후에 다시 시도해 주세요");
            }
        } catch (JSONException e) {
            makeDialog("내부 서버오류입니다. 잠시후에 다시 시도해 주세요");
        } catch (IOException e) {
            makeDialog("내부 서버오류입니다. 잠시후에 다시 시도해 주세요");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @AfterViews
    protected void init() {
        mAuth = FirebaseAuth.getInstance();
        login_text.setClickable(true);
        login_text.setMovementMethod(LinkMovementMethod.getInstance());
        String login = "로그인을 함으로써 idbd의 <a href='http://idbd.co.kr/policy/individual'>개인정보취급방침</a>,"+ "\n"+"<a href='http://idbd.co.kr/policy/service'>서비스 이용약관에</a> 동의 하시게 됩니다.";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            login_text.setText(Html.fromHtml(login, Html.FROM_HTML_MODE_LEGACY));
        } else {
            login_text.setText(Html.fromHtml(login));
        }
    }

    @ViewById(R.id.login_email)
    EditText emailText;

    @ViewById(R.id.login_pwd)
    EditText pwdText;

    @Click(R.id.login_sign_up_btn)
    void goSignUpActivity(){
        SignUpActivity_.intent(this).start();
    }

    void goMainActivity(){
        if(pDialog!=null){
            pDialog.dismiss();
        }
        MainActivity_.intent(this).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
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
                                                    goMainActivity();
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

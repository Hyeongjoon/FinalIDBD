package com.idbd.admin.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.EditText;

import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.Post;

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

/**
 * Created by admin on 2016-12-28.
 */

@EActivity(R.layout.activity_modify_pwd)
public class ModifyPwdActivity extends Activity{

    Long time;
    ProgressDialog pDialog;
    private String modify_url = "http://52.78.18.19/findPwd/modify";
    private String find_url = "http://52.78.18.19/findPwd";


    @ViewById(R.id.find_code_input)
    EditText codeText;

    @ViewById(R.id.find_pwd_input)
    EditText pwdText;

    @ViewById(R.id.find_pwd_con_input)
    EditText conText;

    @Click(R.id.find_go_login)
    void goToLogin(){
        LoginActivity_.intent(this).start();
        finish();
    }

    boolean validCheck(){
        String code = codeText.getText().toString();
        String pwd = pwdText.getText().toString();
        String pwd_con = conText.getText().toString();
        if(code.length()!=7){
            makeDialog("올바른 코드를 입력해 주세요");
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

    @Click(R.id.find_modify_pwd_btn)
    void modify(){
        if(validCheck()){
            pDialog = ProgressDialog.show(this, "비밀번호 변경중입니다...", "Please wait", true, false);
            pwdModify();
        }
    }

    @Click(R.id.find_re_email_btn)
    void reEmail(){
        mail();
    }

    @Background
    void pwdModify(){
        try {
            String code = codeText.getText().toString();
            String pwd = pwdText.getText().toString();
            String pwd_con = conText.getText().toString();

            RequestBody formBody = new FormBody.Builder()
                    .add("code", code)
                    .add("pwd", pwd)
                    .add("pwd_con", pwd_con)
                    .build();
            JSONObject jsonObject = new JSONObject(Post.post(modify_url, formBody));
        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Background
    void mail(){
        if(time == null||(System.currentTimeMillis()-time)/1000>60){
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("email", getIntent().getStringExtra("email"))
                        .build();
                JSONObject jsonObject = new JSONObject(Post.post(find_url, formBody));
                String result = jsonObject.get("result").toString();
                if(result.equals("success")){
                    makeDialog("인증메일 재발송이 완료되었습니다. 다시 확인 후 진행해 주세요");
                } else {
                    String content = jsonObject.get("content").toString();
                    makeDialog(content);
                }
            } catch (JSONException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            time=System.currentTimeMillis();
        } else {
            makeDialog("메일 발송중입니다. 잠시후에 시도해 주세요");
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(this , content);
    }

}

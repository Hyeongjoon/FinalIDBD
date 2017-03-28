package com.idbd.admin.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.MakeRandomNum;
import com.idbd.admin.myapplication.Helper.Post;
import com.idbd.admin.myapplication.Helper.TokenInfo;
import com.google.firebase.iid.FirebaseInstanceId;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by admin on 2017-01-12.
 */



@EFragment(R.layout.fragment_content1)
public class MainSub1Fragment extends Fragment{
    private String confirm = "http://52.78.18.19/confirm/";

    ProgressDialog pDialog;

    @ViewById(R.id.main_sub1_input)
    EditText codeInput;

    @Click(R.id.addCode)
    public void addCodeBtn(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            builder.setCancelable(true)
                    .setTitle(R.string.dialog_title)
                    .setMessage("코드입력을 하려면 로그인이 필요합니다\n로그인 하시겠습니까?")
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LoginActivity_.intent(getActivity()).start();
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            builder.show();
        } else{
            String code = codeInput.getText().toString().trim();
            codeInput.setText("");
            if(code.length()!=5){
                makeDialog(getString(R.string.worng_code_input));
            } else{
                pDialog = ProgressDialog.show(getActivity(), "확인중입니다....", "Please wait", true, false);
                addCode(code);
            }
        }
    }

    @Click(R.id.random_btn)
    public void randomInput(){
        codeInput.setText(MakeRandomNum.makeNum());
    }

    @Background
    public void addCode(String code){
        RequestBody formBody = new FormBody.Builder()
                .add("code" , code)
                .build();
        try {
                JSONObject result =  new JSONObject(Post.post(confirm + TokenInfo.getTokenId(),formBody));
                if(result.get("result").equals("true")){
                    pDialog.cancel();
                    makeNotCancleDialog(getString(R.string.won_prize));
                } else if(result.get("content").equals("non")){
                    pDialog.cancel();
                    String content= getString(R.string.main_sub1_wrong_code_dialog) +"\n" +"남은 list" + "\n";
                    JSONArray temp = result.getJSONArray("list");
                    for(int i = 0 ; i < temp.length() ; i++){
                        content = content + temp.getJSONObject(i).getString("pname") +"\n";
                    }
                    makeDialog(content);
                        //코드가 틀린코드를 입력했을때
                } else if(result.get("content").equals("selected")){
                    pDialog.cancel();
                    String content= getString(R.string.main_sub1_selected_code) +"\n" +"남은 list" + "\n";
                    JSONArray temp = result.getJSONArray("list");
                    for(int i = 0 ; i < temp.length() ; i++){
                        content = content + temp.getJSONObject(i).getString("pname") +"\n";
                    }
                    makeDialog(content);
                } else if(result.get("content").equals("server")){
                    pDialog.cancel();
                    makeDialog(getString(R.string.server_wrong));
                }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(getActivity() , content);
    }

    @UiThread
    public void makeNotCancleDialog(String content){
        MakeDialog.oneBtnNotCancleDialog(getActivity() , content);
    }
}

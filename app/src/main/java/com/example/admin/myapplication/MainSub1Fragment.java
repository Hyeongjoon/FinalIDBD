package com.example.admin.myapplication;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by admin on 2017-01-12.
 */

@EFragment(R.layout.fragment_content1)
public class MainSub1Fragment extends Fragment{
    private String addCode = "http://52.78.18.19/gr/addByCode/";

    ProgressDialog pDialog;

    @ViewById(R.id.main_sub1_input)
    EditText codeInput;

    @Click(R.id.addCode)
    public void addCodeBtn(){
        String code = codeInput.getText().toString().trim();
        if(code.length()!=5){
            makeDialog(getString(R.string.main_sub1_wrong_code_dialog));
        } else{
            addCode(code);
        }
    }

    @Background
    public void addCode(String code){
        RequestBody formBody = new FormBody.Builder()
                .add("code" , code)
                .build();
        try {
            Post.post(addCode + TokenInfo.getTokenId(),formBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(getActivity() , content);
    }
}

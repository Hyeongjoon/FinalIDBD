package com.example.admin.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

// 이거 서버쪽 들어가는것까지 완료했고 get 결과물 json으로 받는거 확인부터하면 될듯

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
            makeInputDialog(code);
        }
    }

    @Background
    public void addCode(String code , String name){
        RequestBody formBody = new FormBody.Builder()
                .add("code" , code)
                .add("name" , name)
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

    @UiThread
    public void makeInputDialog(final String code){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        final View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        builder.setView(mView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String gr_title = ((EditText)mView.findViewById(R.id.userInputDialog)).getText().toString().trim();
                        if(gr_title.length()==0){
                            MakeDialog.oneBtnDialog(getActivity() , "1글자 이상 입력해주세요");
                        } else{
                            addCode(code , gr_title );
                            pDialog = ProgressDialog.show(getActivity(), "그룹 추가중입니다.", "Please wait", true, false);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        TextView titleText = (TextView)mView.findViewById(R.id.dialogTitle);
        titleText.setText(R.string.main_sub2_gr_create_dialog_title);
        builder.show();  //Edit Text에서 불러오는법 몰라서 일단 여기다가 넣어놓음................후........
    }

}

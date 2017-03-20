package com.idbd.admin.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.Post;
import com.idbd.admin.myapplication.Helper.TokenInfo;
import com.google.firebase.iid.FirebaseInstanceId;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
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
    private String addCode = "http://52.78.18.19/gr/addByCode/";

    ProgressDialog pDialog;

    @ViewById(R.id.main_sub1_input)
    EditText codeInput;

    @Click(R.id.addCode)
    public void addCodeBtn(){
        String code = codeInput.getText().toString().trim();
        codeInput.setText("");
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
                .add("reg_id", FirebaseInstanceId.getInstance().getToken())
                .build();
        try {
                JSONObject result =  new JSONObject(Post.post(addCode + TokenInfo.getTokenId(),formBody));
                if(result.get("result").equals("true")){
                    pDialog.cancel();
                    toRight();
                    ((MainSub2Fragment)((MainActivity)getActivity()).getFragmentItem(1)).InsertGr(result); //2번에 붙어있는 어뎁터 불러와서 데이터 0번에 삽입
                        //추가 정상적으로 되고 넘어왔을때
                } else if(result.get("content").equals("code")){
                    pDialog.cancel();
                    makeDialog(getString(R.string.main_sub1_wrong_code_dialog));
                        //코드가 틀린코드를 입력했을때
                } else if(result.get("content").equals("duplication")){
                    pDialog.cancel();
                    makeDialog(getString(R.string.main_sub1_dupli_code));
                    //코드가 이미 있을때
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
    public void toRight(){
        ((MainActivity)getActivity()).mViewPager.arrowScroll(View.FOCUS_RIGHT);
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
                            MakeDialog.oneBtnDialog(getActivity() , getString(R.string.wrong_gr_name_input));
                        } else{
                            pDialog = ProgressDialog.show(getActivity(), "그룹 추가중입니다.", "Please wait", true, false);
                            addCode(code , gr_title );
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

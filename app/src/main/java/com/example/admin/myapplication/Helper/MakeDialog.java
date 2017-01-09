package com.example.admin.myapplication.Helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.admin.myapplication.R;

import org.androidannotations.annotations.UiThread;


/**
 * Created by admin on 2016-12-22.
 */

public class MakeDialog {

    public static void oneBtnDialog(Activity a , String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(a);     // 여기서 this는 Activity의 this
        // 여기서 부터는 알림창의 속성 설정
        builder.setTitle(R.string.dialog_title)        // 제목 설정
                .setMessage(content)        // 메세지 설정
                .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}

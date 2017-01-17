package com.example.admin.myapplication.Helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public static void twoBtn(Activity a , String content , DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(a);     // 여기서 this는 Activity의 this
        // 여기서 부터는 알림창의 속성 설정
        builder.setTitle(R.string.dialog_title)        // 제목 설정
                .setMessage(content)        // 메세지 설정
                .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton(R.string.dialog_ok, listener)
                .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    public static void oneInput(Activity a , String title , DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(a);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        builder.setView(mView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_ok , listener)
                .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        TextView titleText = (TextView)mView.findViewById(R.id.dialogTitle);
        //EditText input = new EditText(a);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        //input.setSelectAllOnFocus(true);
        titleText.setText(title);
        builder.show();
    }
}

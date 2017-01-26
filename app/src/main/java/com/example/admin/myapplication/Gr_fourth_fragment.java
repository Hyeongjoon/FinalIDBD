package com.example.admin.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.myapplication.Helper.Get;
import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;

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



@EFragment(R.layout.gr_layout4)
public class Gr_fourth_fragment extends Fragment{

    private String temp = "http://52.78.18.19/temp/";

    @Click(R.id.tempbutton)
    public void temp(){
     asd();
    }

    @Background
    public void asd(){
        try {
            Get.get(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

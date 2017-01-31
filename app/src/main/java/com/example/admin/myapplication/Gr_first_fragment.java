package com.example.admin.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;

import org.androidannotations.annotations.AfterViews;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by admin on 2017-01-12.
 */



@EFragment(R.layout.gr_layout1)
public class Gr_first_fragment extends Fragment{

    @ViewById(R.id.gr_layout1_master_name)
    TextView master_name_view;

    @AfterViews
    public void init(){
        master_name_view.setText(Gr_info_Activity_.master_name);
    }
}

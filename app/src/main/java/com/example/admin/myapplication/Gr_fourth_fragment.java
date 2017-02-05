package com.example.admin.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.myapplication.Helper.DBHelper;
import com.example.admin.myapplication.Helper.Get;
import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;
import com.example.admin.myapplication.adapter.ChatAdapter;

import org.androidannotations.annotations.AfterViews;
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

    private String send = "http://52.78.18.19/chat?token=";
    private String resetNum = "http://52.78.18.19/chat/reset_num/";

    private DBHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @ViewById(R.id.text_content)
    EditText editText;

    @Click(R.id.sned_chat)
    public void send_chat(){
        if(editText.getText().toString().trim().length()==0){

        } else{
            send();
            editText.setText("");
        }
    }

    @AfterViews
    public void init(){
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.gr_layout4_list);
        mDbHelper = new DBHelper(getContext() , 1);
        Cursor chat = mDbHelper.findChat(Gr_info_Activity_.gid+"");
        mAdapter = new ChatAdapter(chat , getActivity());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        ChatAdapter.adapter = (ChatAdapter)mAdapter;
    }

    @Background
    public void send(){
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("text", editText.getText().toString())
                    .add("gid", Gr_info_Activity.gid+"")
                    .build();
            Post.post(send+TokenInfo.getTokenId(), formBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mDbHelper.close();
    }

    @Override
    public void onStart(){
        super.onStart();
        resetTalkNum();
    }

    @Background
    public void resetTalkNum(){
        try {
            Get.get(resetNum+TokenInfo.getTokenId()+"/"+Gr_info_Activity_.gid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

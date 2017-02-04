package com.example.admin.myapplication;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;
import com.example.admin.myapplication.adapter.UserAdapter;

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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @ViewById(R.id.gr_layout1_master_name)
    TextView master_name_view;

    @ViewById(R.id.gr_layout1_see_code)
    Button see_code_btn;

    @AfterViews
    public void init(){
        master_name_view.setText(Gr_info_Activity_.master_name);
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.gr_layout1_list);
        mAdapter = new UserAdapter(Gr_info_Activity_.user_list , getActivity());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Click(R.id.gr_layout1_see_code)
    public void see_code(){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(10000);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        see_code_btn.startAnimation(animation);
        see_code_btn.setText(Gr_info_Activity_.gr_code);
    }

    @Click(R.id.gr_layout1_share)
    public void copy_code(){
        ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text",Gr_info_Activity_.gr_code);
        clipboard.setPrimaryClip(clip);
        Toast.makeText( getActivity() ,"코드가 복사되었습니다." ,  Toast.LENGTH_SHORT).show();
    }
}

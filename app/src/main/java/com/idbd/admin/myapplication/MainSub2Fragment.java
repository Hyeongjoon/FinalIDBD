package com.idbd.admin.myapplication;



import android.support.v4.app.Fragment;

import android.util.Log;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idbd.admin.myapplication.Helper.Post;



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

@EFragment(R.layout.fragment_content2)               //EFragment에서 왜 UI Thread라던가 어노테이션 아무것도 안먹는 이유 모르겠음 ㅠㅠㅠㅠㅠㅠ !!!!!!!!시발 왜안먹는지알아???레이아웃 정해줬냐 ㅄ새끼야.......아오...........
public class MainSub2Fragment extends Fragment {

    private String init_url = "http://52.78.18.19/main";

    @ViewById(R.id.main_image1)
    ImageView imageView1;

    @ViewById(R.id.main_image2)
    ImageView imageView2;

    @AfterViews
    public void init(){
        getImage();
    }

    @Background
    public void getImage(){
        setImageView("https://s3.ap-northeast-2.amazonaws.com/sendwitchtracer/mainImage/1.png" , imageView1);
        setImageView("https://s3.ap-northeast-2.amazonaws.com/sendwitchtracer/mainImage/2.png" , imageView2);
    }

    @Click(R.id.go_mining)
    public void go_mining(){
        ((MainActivity_) getActivity()).selectPage(1);
    }

    @UiThread
    public void setImageView(String url ,ImageView imageView){
        Glide.with(getActivity()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
}



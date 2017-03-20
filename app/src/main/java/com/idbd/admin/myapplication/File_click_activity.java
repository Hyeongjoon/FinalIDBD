package com.idbd.admin.myapplication;

<<<<<<< HEAD
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
=======
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.idbd.admin.myapplication.Helper.Get;
import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.TokenInfo;
import com.idbd.admin.myapplication.Helper.ZoomOutPageTransformer;
import com.idbd.admin.myapplication.adapter.FileClickAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Admin on 2017-02-19.
 */

@EActivity
public class File_click_activity extends AppCompatActivity{

    private String get_flie_list = "http://52.78.18.19/file/get_file";

    ViewPager mViewPager;			// View pager를 지칭할 변수
    private FileClickAdapter pagerAdapter;

    @Extra
    String date;

    @AfterViews
    public void init(){
        get_file_list();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_click);
<<<<<<< HEAD

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.high_blue, this.getTheme())));
        } else{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.high_blue)));
        }
        getSupportActionBar().setLogo(R.drawable.actionbar_logo_white);
=======
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Background
    public void get_file_list(){
        try {
            final JSONObject result = new JSONObject(Get.get(get_flie_list+"/"+ TokenInfo.getTokenId()+"/"+Gr_info_Activity_.gid+"/"+date));
            if(result.getString("result").equals("true")){
                mViewPager = (ViewPager)findViewById(R.id.file_click_list);
                pagerAdapter = new FileClickAdapter(getSupportFragmentManager() , result.getJSONArray("file_list"));
                setAdapterInPager(pagerAdapter);
            } else{
                makeDialog("서버 오류입니다. 잠시후에 다시 시도해주세요");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void setAdapterInPager(FileClickAdapter pagerAdapter){
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    //손쉬운 그룹공유를 통한 파일공유
    @UiThread
    public void makeDialog(String contents){
        MakeDialog.oneBtnDialog(this , contents);
    }

    @UiThread
    public void deleteFile(int fid){
        if(pagerAdapter.deleteItem(fid)){
            Log.d("msg" , fid+"");
            pagerAdapter.notifyDataSetChanged();
        };
    }
}

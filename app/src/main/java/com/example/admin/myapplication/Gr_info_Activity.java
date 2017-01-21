package com.example.admin.myapplication;



import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.widget.EditText;


import com.example.admin.myapplication.Helper.Get;
import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.TokenInfo;
import com.example.admin.myapplication.Helper.ZoomOutPageTransformer;


import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;


@EActivity
public class Gr_info_Activity extends AppCompatActivity  {

    private int NUM_PAGES = 4;		// 최대 페이지의 수

    /* Fragment numbering */
    public final static int FRAGMENT_PAGE1 = 0;
    public final static int FRAGMENT_PAGE2 = 1;
    public final static int FRAGMENT_PAGE3 = 2;
    public final static int FRAGMENT_PAGE4 = 3;


    ViewPager mViewPager;			// View pager를 지칭할 변수
    private pagerAdapter pagerAdapter;

    @Extra
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ViewPager를 검색하고 Adapter를 달아주고, 첫 페이지를 선정해준다.
        mViewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new pagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        switch (page){
            case 1 : {
                mViewPager.setCurrentItem(FRAGMENT_PAGE1);
                break;
            }case 2 : {
                mViewPager.setCurrentItem(FRAGMENT_PAGE2);
                break;
            }case  3 :{
                mViewPager.setCurrentItem(FRAGMENT_PAGE3);
                break;
            }case 4:{
                mViewPager.setCurrentItem(FRAGMENT_PAGE4);
                break;
            } default: {
                mViewPager.setCurrentItem(FRAGMENT_PAGE2);
                break;
            }
        }
        getSupportActionBar().setElevation(0);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case FRAGMENT_PAGE1 :{
                        getSupportActionBar().setSubtitle("");
                        break;
                    } case FRAGMENT_PAGE2 :{
                        getSupportActionBar().setSubtitle("");
                        break;
                    } case FRAGMENT_PAGE3 :{
                        getSupportActionBar().setSubtitle("마이페이지 / 설정"); //요기서 select 된상태면 이름 셋팅하라고 이거 넣어놨는데 가로세로 전환될때 오류나서 어캐바꿔야할지 생각할것
                        break;                                                  //로드가 느리면 이름이랑 이메일 null로 나옴
                    }case FRAGMENT_PAGE4 :{
                        break;
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    public Fragment getFragmentItem(int position){
        return pagerAdapter.getItem(position);
    };


    // FragmentPageAdater : Fragment로써 각각의 페이지를 어떻게 보여줄지 정의한다.
    private class pagerAdapter extends FragmentPagerAdapter{
        Gr_first_fragment_ gr_first_fragment;
        Gr_second_fragment_ gr_second_fragment;
        Gr_third_fragment_ gr_third_fragment;
        Gr_fourth_fragment_ gr_fourth_fragment;

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override               // 특정 위치에 있는 Fragment를 반환해준다.
        public Fragment getItem(int position) {
            switch(position){
                case 0:{
                    if(gr_first_fragment==null){
                        gr_first_fragment = new Gr_first_fragment_();
                    }
                    return gr_first_fragment;
                }
                case 1: {
                    if(gr_second_fragment==null){
                        gr_second_fragment= new Gr_second_fragment_();
                    }
                    return gr_second_fragment;
                }
                case 2:{
                    if(gr_third_fragment==null){
                        gr_third_fragment= new Gr_third_fragment_();
                    }
                    return gr_third_fragment;
                }
                case 3 :{
                    if(gr_fourth_fragment==null){
                        gr_fourth_fragment= new Gr_fourth_fragment_();
                    }
                    return gr_fourth_fragment;
                }
                default:
                    return null;
            }
        }

        // 생성 가능한 페이지 개수를 반환해준다.
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return NUM_PAGES;
        }
    }


}



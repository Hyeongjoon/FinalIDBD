package com.idbd.admin.myapplication;



import android.graphics.Typeface;
import android.media.tv.TvContract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;

import android.view.ViewGroup;
import android.widget.TextView;


import com.idbd.admin.myapplication.Helper.ZoomOutPageTransformer;


import org.androidannotations.annotations.EActivity;
import org.w3c.dom.Text;


@EActivity
public class MainActivity extends AppCompatActivity  {

    private int NUM_PAGES = 3;		// 최대 페이지의 수

    /* Fragment numbering */
    public final static int FRAGMENT_PAGE1 = 1;
    public final static int FRAGMENT_PAGE2 = 0;
    public final static int FRAGMENT_PAGE3 = 2;



    ViewPager mViewPager;			// View pager를 지칭할 변수
    private pagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ViewPager를 검색하고 Adapter를 달아주고, 첫 페이지를 선정해준다.
        mViewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new pagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setCurrentItem(FRAGMENT_PAGE2);
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
                        getSupportActionBar().setSubtitle("");
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
        MainSub1Fragment_ mainSub1Fragment;
        MainSub2Fragment mainSub2Fragment;
        MainSub3Fragment mainSub3Fragment;
        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override               // 특정 위치에 있는 Fragment를 반환해준다.
        public Fragment getItem(int position) {
            switch(position){
                case FRAGMENT_PAGE1 :{
                    if(mainSub1Fragment==null){
                        mainSub1Fragment = new MainSub1Fragment_();
                    }
                    return mainSub1Fragment;
                }
                case FRAGMENT_PAGE2 : {
                    if(mainSub2Fragment==null){
                        mainSub2Fragment = new MainSub2Fragment_();
                    }
                    return mainSub2Fragment;
                }
                case FRAGMENT_PAGE3 : {
                    if(mainSub3Fragment==null){
                        mainSub3Fragment = new MainSub3Fragment_();
                    }
                    return mainSub3Fragment;
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



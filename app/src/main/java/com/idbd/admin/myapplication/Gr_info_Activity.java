package com.idbd.admin.myapplication;



import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;


import com.idbd.admin.myapplication.Helper.ZoomOutPageTransformer;



import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@EActivity
public class Gr_info_Activity extends AppCompatActivity  implements TabLayout.OnTabSelectedListener{

    public final static int MY_PERMISSIONS_REQUEST= 1;
<<<<<<< HEAD
    public final static int MY_STORAGE_REQUEST= 2;
=======
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609

    private int NUM_PAGES = 4;		// 최대 페이지의 수

    /* Fragment numbering */
    public final static int FRAGMENT_PAGE1 = 0;
    public final static int FRAGMENT_PAGE2 = 1;
    public final static int FRAGMENT_PAGE3 = 2;
    public final static int FRAGMENT_PAGE4 = 3;


    ViewPager mViewPager;			// View pager를 지칭할 변수
    private pagerAdapter pagerAdapter;

    private TabLayout tabLayout; //tab

    @Extra
    int page;

    @Extra
    public static Long gid; //extra는 프라이빗 못쓴데 그래서 직접 갔다써야함 ㅠㅠ

    @Extra
    public static String gr_sche;

    public static Long temp_gid;

    public static JSONArray user_list;

    @Extra
    public static int master;

    public static String master_name;

    @Extra
    public static String gr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.high_blue, this.getTheme())));
        } else{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.high_blue)));
        }
        getSupportActionBar().setLogo(R.drawable.actionbar_logo_white);
        temp_gid = gid;
        setContentView(R.layout.activity_gr_main);
        for(int i = 0 ; i < user_list.length() ; i++){
            try {
                if(((JSONObject)user_list.get(i)).getInt("uid") == master){
                    master_name = ((JSONObject)user_list.get(i)).getString("name");
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        tabLayout = (TabLayout) findViewById(R.id.gr_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("팀원"));
        tabLayout.addTab(tabLayout.newTab().setText("파일"));
        tabLayout.addTab(tabLayout.newTab().setText("일정"));
        tabLayout.addTab(tabLayout.newTab().setText("채팅"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // ViewPager를 검색하고 Adapter를 달아주고, 첫 페이지를 선정해준다.
        mViewPager = (ViewPager)findViewById(R.id.gr_pager);

        pagerAdapter = new pagerAdapter(getSupportFragmentManager() , tabLayout.getTabCount());
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        tabLayout.addOnTabSelectedListener(this);

        switch (page){
            case 1 : {
                onTabSelected(tabLayout.getTabAt(0));
                break;
            }case 2 : {
                onTabSelected(tabLayout.getTabAt(1));
                break;
            }case  3 :{
                onTabSelected(tabLayout.getTabAt(2));
                break;
            }case 4:{
                onTabSelected(tabLayout.getTabAt(3));
                break;
            } default: {
                onTabSelected(tabLayout.getTabAt(1));
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    public Fragment getFragmentItem(int position){
        return pagerAdapter.getItem(position);
    };

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //액션바 바꾸고 싶으면 요기서 바꿀것
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    // FragmentPageAdater : Fragment로써 각각의 페이지를 어떻게 보여줄지 정의한다.
    private class pagerAdapter extends FragmentStatePagerAdapter {
        int tabNum;

        Gr_first_fragment_ gr_first_fragment;
        Gr_second_fragment_ gr_second_fragment;
        Gr_third_fragment_ gr_third_fragment;
        Gr_fourth_fragment_ gr_fourth_fragment;

        public pagerAdapter(FragmentManager fm , int tabNum) {
            super(fm);
            this.tabNum = tabNum;
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
            return tabNum;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        temp_gid = null;
    }

    @Override
    public void onStart(){
        super.onStart();
        temp_gid = gid;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //프레그먼트에서 안불림 엑티비티에서 처리해야함
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                            Log.d("msg" , "여긴 1");
                        }
                    if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
                        Log.d("msg" , "여긴 2");
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
<<<<<<< HEAD
            case MY_STORAGE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        Log.d("msg" , "여긴 1");
                    }
                    if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
                        Log.d("msg" , "여긴 2");
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
=======

>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}



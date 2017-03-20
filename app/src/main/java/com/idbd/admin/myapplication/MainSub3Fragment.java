package com.idbd.admin.myapplication;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.idbd.admin.myapplication.Helper.Get;
import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.TokenInfo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by admin on 2017-01-12.
 */

@EFragment(R.layout.fragment_content3)
public class MainSub3Fragment extends Fragment{

    private String getScheURL = "http://52.78.18.19/my_option/getSche/";
    private ProgressDialog pDialog;

    @ViewById(R.id.main_sub3_user_email)
    TextView userEmail;

    @ViewById(R.id.main_sub3_user_name)
    TextView userName;

    @AfterViews
    public void Init(){
      Long start = System.currentTimeMillis();
        boolean temp = true;
        while (temp) {
            if(TokenInfo.getUserEmail()!=null){
                userEmail.setText(TokenInfo.getUserEmail());
                userName.setText(TokenInfo.getUserName());
                if(System.currentTimeMillis()-start >1000){
                    temp = false;
                }
                break;
            }
        }
    }

    @Click(R.id.main_sub3_fourth_menu)
    public void click_fifth(){
<<<<<<< HEAD
            FirebaseAuth.getInstance().getCurrentUser().delete();
=======
            FirebaseAuth.getInstance().signOut();
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
            LoginActivity_.intent(this).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
            getActivity().finish();

    }

    @Click(R.id.main_sub3_fifth_menu)
    public void click_fourth(){

    }

    @Click(R.id.main_sub3_third_menu)
    public void click_third(){

    }

    @Click(R.id.main_sub3_second_menu)
    public void click_second(){

    }

    @Click(R.id.main_sub3_first_menu)
    public void click_first(){

    }

    @Click(R.id.main_sub3_my_schedule_btn)
    public void goMyschedule(){
        pDialog = ProgressDialog.show(getActivity(), "서버와 통신중입니다.", "Please wait", true, false);
        getSche();
    }

    @Background
    public void getSche(){
        try {
            JSONObject result =  new JSONObject(Get.get(getScheURL + TokenInfo.getTokenId()));
            if(result.get("result").equals("success")){
                pDialog.cancel();
                MyScheduleActivity_.intent(getActivity()).extra("sche_info" , result.get("content").toString()).start();
            } else{
                pDialog.cancel();
                makeDialog("내부 서버 오류입니다 잠시후에 시도해 주세요");
            }
        } catch (IOException e){
            makeDialog("내부 서버 오류입니다 잠시후에 시도해 주세요");
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(getActivity() , content);
    }

}

package com.example.admin.myapplication;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.admin.myapplication.Helper.DBHelper;
import com.example.admin.myapplication.Helper.Get;
import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;
import com.example.admin.myapplication.adapter.MainSub2Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

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
import okhttp3.RequestBody;

/**
 * Created by admin on 2017-01-12.
 */

@EFragment               //EFragment에서 왜 UI Thread라던가 어노테이션 아무것도 안먹는 이유 모르겠음 ㅠㅠㅠㅠㅠㅠ !!!!!!!!시발 왜안먹는지알아???레이아웃 정해줬냐 ㅄ새끼야.......아오...........
public class MainSub2Fragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ProgressDialog pDialog;

    private String add_gr_url = "http://52.78.18.19/gr/add";

    private String get_gr_url = "http://52.78.18.19/gr?token=";

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            makeInputDialog();
        }
    } ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_content2, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_sub2_group_list);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MainSub2Adapter(new JSONArray() , getActivity());
        mRecyclerView.setAdapter(mAdapter);
        Button btn = (Button)rootView.findViewById(R.id.main_sub2_add_group);
        btn.setOnClickListener(listener);
        return rootView;
        }

    @Override
    public void onResume(){
        super.onResume();
        initMain();
    }

    public void initMain(){                     //이거 더러운거 인정한다 근데 android annotation 먹는거 확인한 후 부터 진짜 손쉽게 바꿀수있음 ㅠㅠㅠㅠㅠㅠㅠㅠㅠ
        new Thread(){
          @Override
          public void run(){
              try {
                  final JSONObject jsonObject = new JSONObject(Get.get(get_gr_url +TokenInfo.getTokenId()));
                  if(jsonObject.getString("result").equals("success")){
                      JSONObject userInfo = (JSONObject)jsonObject.getJSONArray("userInfo").get(0);
                      TokenInfo.setUserInfo(userInfo.getString("email") , userInfo.getString("name"));
                      if(getActivity()!=null){
                      getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  mAdapter = new MainSub2Adapter(jsonObject.getJSONArray("grInfo") , getActivity());
                                  mRecyclerView.swapAdapter(mAdapter , false);
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      });}
                  }else{
                        //앱 강제종료 넣어야함
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              } catch(JSONException e){
                  e.printStackTrace();
              }
          }
        }.start();
    }


    @UiThread
    public void makeInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        final View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        builder.setView(mView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String gr_title = ((EditText)mView.findViewById(R.id.userInputDialog)).getText().toString().trim();
                        if(gr_title.length()==0){
                            MakeDialog.oneBtnDialog(getActivity() , "1글자 이상 입력해주세요");
                        } else{
                            addGr(gr_title , dialogInterface);
                            makePDialog();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        TextView titleText = (TextView)mView.findViewById(R.id.dialogTitle);
        titleText.setText(R.string.main_sub2_gr_create_dialog_title);
        builder.show();  //Edit Text에서 불러오는법 몰라서 일단 여기다가 넣어놓음................후........
    }


    void addGr(final String title , final DialogInterface dialogInterface){
        new Thread(){
            public void run(){
                RequestBody formBody = new FormBody.Builder()
                        .add("title" , title)
                        .add("token" , TokenInfo.getTokenId())
                        .add("reg_id", FirebaseInstanceId.getInstance().getToken())
                        .build();
                try {
                    JSONObject jsonObject = new JSONObject(Post.post(add_gr_url , formBody));
                    pDialog.cancel();
                    String result = jsonObject.get("result").toString();
                    if(result.equals("success")){
                        InsertGr(jsonObject);
                    } else{
                        makeDialog("내부 서버 오류입니다. 잠시후에 시도해주세요");
                        dialogInterface.cancel();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    makeDialog("내부 서버 오류입니다. 잠시후에 시도해주세요");
                    dialogInterface.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void makeDialog(final String title){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MakeDialog.oneBtnDialog(getActivity() , title);
            }
        });
    }

    public void makePDialog(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pDialog=ProgressDialog.show(getActivity(), "그룹 추가중입니다.", "Please wait", true, false);
            }
        });
    }

    public void notifyInsert(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyItemInserted(0);
                mRecyclerView.scrollToPosition(0);
            }
        });
    }

    public void InsertGr(JSONObject jsonObject){
        ((MainSub2Adapter)mAdapter).addGroup(jsonObject);
        notifyInsert();
    }

}



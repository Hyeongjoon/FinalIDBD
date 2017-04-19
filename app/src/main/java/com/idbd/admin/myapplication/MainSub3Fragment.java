package com.idbd.admin.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.idbd.admin.myapplication.Helper.Get;
import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.MakeRandomNum;
import com.idbd.admin.myapplication.Helper.Post;
import com.idbd.admin.myapplication.Helper.TokenInfo;
import com.idbd.admin.myapplication.adapter.Item_list_adapter;

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
import okhttp3.RequestBody;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by admin on 2017-01-12.
 */



@EFragment(R.layout.fragment_content3)
public class MainSub3Fragment extends Fragment{

    private String getList = "http://52.78.18.19/getList/";
    private String delete_id_url = "http://52.78.18.19/delete/";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content3, container, false);
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.main_sub3_linear_btn);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Button login = makeBtn("로그인");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity_.intent(getActivity()).start();
                }
            });
            layout.addView(login);
        } else{
            Button modify_pwd = makeBtn("비밀번호 변경");
            modify_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeDialog("비밀번호 변경은 웹사이트에서 진행해 주세요\nhttp://idbd.co.kr");
                }
            });

            Button sign_out = makeBtn("로그아웃");
            sign_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    TokenInfo.setTokenId(null);
                    MainActivity_.intent(getActivity()).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
                    getActivity().finish();
                }
            });
            Button delete_id = makeBtn("계정탈퇴");
            delete_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeDeleteDialog();
                }
            });
            layout.addView(modify_pwd);
            layout.addView(sign_out);
            layout.addView(delete_id);
        }
        return view;
    }

    public Button makeBtn(String text){
        Button btn = new Button(getContext());
        btn.setGravity(Gravity.CENTER);
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()); //xml dp단위 변환
        final int margin_top = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT , height);
        p.setMargins(0,margin_top,0,0);
        btn.setBackgroundResource(R.drawable.setting_selector);
        btn.setText(text);
        btn.setTextColor(ContextCompat.getColor(getContext(), R.color.blacky));
        btn.setLayoutParams(p);
        return btn;
    }

    @AfterViews
    public void init(){
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Item_list_adapter(new JSONArray());
        mRecyclerView.setAdapter(mAdapter);
        getItem();

    }

    @Background
    public void getItem(){
        try {
            final JSONObject jsonObject = new JSONObject(Get.get(getList +TokenInfo.getTokenId()));
            mAdapter = new Item_list_adapter(jsonObject.getJSONArray("content"));
            swap();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void swap(){
        mRecyclerView.swapAdapter(mAdapter , false);
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(getActivity() , content);
    }

    @UiThread
    public void canclePdialog(){
        if(pDialog!=null){
            pDialog.cancel();
        }
    }

    @UiThread
    public void makeDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true)
                .setTitle("알  림")
                .setMessage(getString(R.string.delete_id_dialog_content))
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete_id();
                        ProgressDialog.show(getActivity(), "확인중입니다....", "Please wait", true, false);
                    }
                })
                .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    @UiThread
    public void goMainActivity(){
        FirebaseAuth.getInstance().signOut();
        TokenInfo.setTokenId(null);
        MainActivity_.intent(getActivity()).flags(FLAG_ACTIVITY_CLEAR_TOP).start();
        getActivity().finish();
    }

    @Background
    public void delete_id(){
        RequestBody formBody = new FormBody.Builder()
                .build();
        try {
            JSONObject jsonObject = new JSONObject(Post.post(delete_id_url+TokenInfo.getTokenId(), formBody));
            if(jsonObject.getString("result").equals("true")){
                canclePdialog();
                goMainActivity();
            } else{
                canclePdialog();
                makeDialog("내부 서버오류입니다. 잠시후에 시도해주세요");
            }
        } catch (JSONException e) {
            canclePdialog();
            makeDialog("내부 서버오류입니다. 잠시후에 시도해주세요");
        } catch (IOException e) {
            canclePdialog();
            makeDialog("내부 서버오류입니다. 잠시후에 시도해주세요");
        }
    }

    @Click(R.id.main_sub3_service)
    public void service(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://idbd.co.kr/policy/service"));
        startActivity(intent);
    }

    @Click(R.id.main_sub3_individual)
    public void individual(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://idbd.co.kr/policy/individual"));
        startActivity(intent);
    }
}

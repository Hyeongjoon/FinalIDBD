package com.idbd.admin.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idbd.admin.myapplication.Gr_info_Activity_;
import com.idbd.admin.myapplication.Helper.MakeDialog;
import com.idbd.admin.myapplication.Helper.Post;
import com.idbd.admin.myapplication.Helper.TokenInfo;
import com.idbd.admin.myapplication.R;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by admin on 2017-01-12.
 */


public class MainSub2Adapter extends RecyclerView.Adapter <MainSub2Adapter.ViewHolder>  {
    private List<JSONObject> mList= new ArrayList<>();
    private Activity a;
    private String change_gr_name_urI = "http://52.78.18.19/gr/changeGrName/";
    private String delete_belong_gr = "http://52.78.18.19/gr/delete/";
    private String change_gr_color_urI = "http://52.78.18.19/gr/change_color/";
    private String get_gr_info_uri = "http://52.78.18.19/gr_info?token=";



    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView groupName;
        public TextView groupNum;
        public Button groupNewFileNum;
        public Button groupNewChatNum;
        public View groupColor;

        public LinearLayout linearLayout;
        public RelativeLayout layout2;
        public LinearLayout layout3;
        public LinearLayout layout4;

        public ViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout)v.findViewById(R.id.main_sub2_item_layout);
            groupName = (TextView)v.findViewById(R.id.main_sub_2_gr_name);
            groupNum = (TextView)v.findViewById(R.id.main_sub_2_gr_num);
            groupNewChatNum = (Button)v.findViewById(R.id.new_chat_num_btn);
            groupNewFileNum = (Button)v.findViewById(R.id.new_file_num_btn);
            groupColor = v.findViewById(R.id.main_sub2_gr_color);
            layout2 = (RelativeLayout)v.findViewById(R.id.main_sub2_item_layout2);
            layout3 = (LinearLayout)v.findViewById(R.id.main_sub2_item_layout3);
            layout4 = (LinearLayout)v.findViewById(R.id.main_sub2_item_layout4);
        };


        public void setData(JSONObject group){
            //데이터 셋팅하는곳
            try {
                groupName.setText(group.getString("name"));
                groupNum.setText(group.getString("member_num")+"명");
                groupNewFileNum.setText(group.getString("new_file_num"));
                groupNewChatNum.setText(group.getString("new_talk_num"));
                int color = group.getInt("color");
                int id = R.color.gr_color1;

                switch (color){
                    case 1:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color1);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color1;
                        break;
                    }case 2:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color2);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color2;
                        break;
                    }case 3:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color3);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color3;
                        break;
                    }case 4:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color4);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color4;
                        break;
                    }case 5:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color5);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color5;
                        break;
                    }case 6:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color6);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color6;
                        break;
                    }case 7:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color7);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color7;
                        break;
                    }case 8:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color8);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color8;
                        break;
                    }case 9:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color9);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color9;
                        break;
                    }case 10:{
                        /*Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext() , R.drawable.border_color10);
                        linearLayout.setBackground(drawable);
                        layout2.setBackground(drawable);
                        layout3.setBackground(drawable);
                        layout4.setBackground(drawable);*/
                        id=R.color.gr_color10;
                        break;
                    }
                }
                groupColor.setBackgroundColor(ContextCompat.getColor(linearLayout.getContext() , id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public MainSub2Adapter(JSONArray jsonArrayGroup , Activity a){
        int[] temp_gr_arr = new int[jsonArrayGroup.length()];
        for(int i =0 ; i<jsonArrayGroup.length() ; i++){
            try {
                mList.add(jsonArrayGroup.getJSONObject(i));
                temp_gr_arr[i] = jsonArrayGroup.getJSONObject(i).getInt("gid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.a = a;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_sub2_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {  //리스트 아이템에있는 클릭 이벤트 만드는곳
        JSONObject group= mList.get(position);
        holder.setData(group);
        TextView gr_name = (TextView)holder.linearLayout.findViewById(R.id.main_sub_2_gr_name);
        gr_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_gr_info(1 , getItemId(position));
                //Gr_info_Activity_.intent(a).extra("page" , 1).extra("gid" , getItemId(position)).start();
            }
        });
        holder.groupNewFileNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_gr_info(2 , getItemId(position));
                //Gr_info_Activity_.intent(a).extra("page" , 2).extra("gid" , getItemId(position)).start();
            }
        });
        holder.groupNewChatNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_gr_info(4 , getItemId(position));
                //Gr_info_Activity_.intent(a).extra("page" , 4).extra("gid" , getItemId(position)).start();
            }
        });
        ImageView imageView = (ImageView)holder.linearLayout.findViewById(R.id.main_sub2_gr_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context c = holder.linearLayout.getContext();
                AlertDialog.Builder ab = new AlertDialog.Builder(c);
                ab.setTitle(holder.groupName.getText());
                ab.setItems(R.array.main_sub2_gr_btn_click, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0 : {
                                final AlertDialog.Builder wrongGroupName = new AlertDialog.Builder(a);
                                wrongGroupName.setTitle(R.string.wrong_gr_name_input);
                                wrongGroupName.setPositiveButton(R.string.dialog_ok , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog.Builder builder = new AlertDialog.Builder(a);
                                builder.setTitle(R.string.main_sub2_gr_name_change_dialog_title);
                                final EditText input = new EditText(a);
                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                                input.setText(holder.groupName.getText());
                                input.setSelectAllOnFocus(true);
                                builder.setView(input);
                                builder.setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String groupName = input.getText().toString().trim();
                                        if (groupName.length() == 0) {
                                            wrongGroupName.show();
                                        } else {
                                            changeName(groupName, getItemId(holder.getAdapterPosition()) , holder.getAdapterPosition());
                                        }
                                    }
                                });
                                AlertDialog mDialog = builder.create();
                                mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                                mDialog.show();
                                return;
                            }case 1 :{
                                AlertDialog.Builder change = new AlertDialog.Builder(c);
                                change.setTitle(c.getString(R.string.main_sub2_color_change_tilte));
                                change.setItems(R.array.main_sub2_gr_color_pick_list, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int color) {
                                        changeColor(getItemId(holder.getAdapterPosition()) , holder.getAdapterPosition() , color+1);
                                    }
                                });
                                AlertDialog mDialog = change.create();
                                mDialog.show();
                                return;
                            }case 2 : {
                                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteBelong(getItemId(position) , holder.getAdapterPosition());
                                    }
                                };
                                MakeDialog.twoBtn(a, a.getString(R.string.main_sub2_gr_delete_confirm) , listener);
                                return;
                            }
                        }
                    }
                });
                ab.show();
            }
        });
    }

    private void deleteBelong(final Long gid , final int position){
        new Thread(){
            @Override
            public void run(){
                try {
                    RequestBody formBody = new FormBody.Builder()
                        .add("gid" , gid+"")
                        .add("reg_id", FirebaseInstanceId.getInstance().getToken())
                        .add("notify_key" , mList.get(position).getString("notify_key"))
                        .build();
                    JSONObject result = new JSONObject(Post.post(delete_belong_gr+TokenInfo.getTokenId() , formBody));
                    if(result.get("result").equals("success")){
                        deleteGroup(position);
                    } else{
                        makeDailog("그룹 탈퇴에 실패하였습니다. 잠시후에 다시 시도해 주세요");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    makeDailog("내부 서버 오류입니다.");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void changeName(final String name , final Long gid , final int position){
        new Thread(){
            @Override
            public void run(){
                RequestBody formBody = new FormBody.Builder()
                        .add("name", name)
                        .add("gid" , gid+"")
                        .build();
                try {
                    JSONObject result = new JSONObject(Post.post(change_gr_name_urI+ TokenInfo.getTokenId() , formBody));
                    if(result.get("result").equals("success")){
                        changeNameData(name , position);
                    } else{
                        makeDailog("내부 서버 오류입니다.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e){
                    makeDailog("내부 서버 오류입니다.");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void changeColor(final Long gid , final int position , final int color){
         new Thread(){
             @Override
             public void run(){
                 RequestBody formBody = new FormBody.Builder()
                         .add("color", color+"")
                         .add("gid" , gid+"")
                         .build();
                 try {
                     JSONObject result = new JSONObject(Post.post(change_gr_color_urI+ TokenInfo.getTokenId() , formBody));
                     if(result.get("result").equals("success")){
                         changeColorData(color , position);
                     } else{
                         makeDailog("내부 서버 오류입니다.");
                     }

                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (JSONException e){
                     makeDailog("내부 서버 오류입니다.");
                     e.printStackTrace();
                 }
             }
         }.start();;
    }

    private void makeDailog(final String string){
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MakeDialog.oneBtnDialog(a , string);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        if(mList.size()==0){
            return 0;
        } else{
            try {
                JSONObject targetJSON = mList.get(position);
                return targetJSON.getInt("gid");
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mList.size()==0){
            return 0;
        } else {
            return  mList.size();
        }
    }


    public void addGroup(JSONObject jsonObject){
        mList.add(0 , jsonObject);
    }

    public void deleteGroup(final int position){
        mList.remove(position);
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainSub2Adapter.super.notifyItemRemoved(position);
            }
        });
    }

    public void changeColorData(int color, final int postion){
        try {
            JSONObject temp = mList.get(postion);
            temp.put("color" , color);
            mList.set(postion,temp);
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainSub2Adapter.super.notifyItemChanged(postion);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeNameData(String name, final int postion){
        try {
            JSONObject temp = mList.get(postion);
            temp.put("name" , name);
            mList.set(postion,temp);
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainSub2Adapter.super.notifyItemChanged(postion);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void get_gr_info(final int page , final Long gid){
        new Thread(){
            @Override
            public void run(){
                RequestBody formBody = new FormBody.Builder()
                        .add("gid", gid+"")
                        .build();
                try {
                    JSONObject result = new JSONObject(Post.post(get_gr_info_uri+TokenInfo.getTokenId() , formBody));
                    if(result.getString("result").equals("success")){
                        Gr_info_Activity_.user_list = result.getJSONArray("user_list");
                        Gr_info_Activity_.intent(a).extra("page" , page).extra("gid" , gid).extra("master" , result.getInt("gr_master")).extra("gr_code" , result.getString("gr_code")).extra("gr_sche" , result.getString("gr_sche")).start();
                    } else{
                        //불러오기 실패했을때 어캐할지 처리하는곳
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

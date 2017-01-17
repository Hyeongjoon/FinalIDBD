package com.example.admin.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.admin.myapplication.R;

import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017-01-12.
 */


public class MainSub2Adapter extends RecyclerView.Adapter <MainSub2Adapter.ViewHolder>  {
    private List<JSONObject> mList= new ArrayList<>();;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView groupName;
        public TextView groupNum;
        public Button groupNewFileNum;
        public Button groupNewChatNum;

        public LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout)v.findViewById(R.id.main_sub2_item_layout);
            groupName = (TextView)v.findViewById(R.id.main_sub_2_gr_name);
            groupNum = (TextView)v.findViewById(R.id.main_sub_2_gr_num);
            groupNewChatNum = (Button)v.findViewById(R.id.new_chat_num_btn);
            groupNewFileNum = (Button)v.findViewById(R.id.new_file_num_btn);
        };

        public void setData(JSONObject group){
            //데이터 셋팅하는곳
            try {
                groupName.setText(group.getString("name"));
                groupNum.setText(group.getString("member_num"));
                groupNewFileNum.setText(group.getString("new_file_num"));
                groupNewChatNum.setText(group.getString("new_talk_num"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public MainSub2Adapter(JSONArray jsonArrayGroup){
        for(int i =0 ; i<jsonArrayGroup.length() ; i++){
            try {
                mList.add(jsonArrayGroup.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_sub2_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final JSONObject group= mList.get(position);
        holder.setData(group);
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

    public JSONArray deleteGroup(int position){
        JSONArray temp = new JSONArray();
        for(int i = position ; i < mList.size() ; i++){
            try {
                temp.put(mList.get(i).getInt("gid"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mList.remove(position);
        return temp;
    }

    public void changeNameData(String name, int postion){
        try {
            JSONObject temp = mList.get(postion);
            temp.put("name" , name);
            mList.set(postion,temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.example.admin.myapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.myapplication.File_click_activity;
import com.example.admin.myapplication.File_click_activity_;
import com.example.admin.myapplication.Gr_info_Activity_;
import com.example.admin.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017-02-15.
 */

public class FileListAdapter extends RecyclerView.Adapter <FileListAdapter.ViewHolder> {

    private List<JSONArray> mList= new ArrayList<>();
    private Activity a;


    public FileListAdapter(JSONArray jsonArray , Activity a ){
        for(int i =0 ; i<jsonArray.length() ; i++){
            try {
                mList.add(jsonArray.getJSONArray(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.a = a;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gr_file_list_item, parent, false);
        FileListAdapter.ViewHolder vh = new FileListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONArray target = mList.get(position);
        holder.setData(target);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView date;
        public ImageView[] imageViewArr = new ImageView[4];
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.gr_file_list_date);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.file_list_item_layout);
            imageViewArr[0] = (ImageView) itemView.findViewById(R.id.gr_file_list_first);
            imageViewArr[1] = (ImageView) itemView.findViewById(R.id.gr_file_list_second);
            imageViewArr[2] =  (ImageView) itemView.findViewById(R.id.gr_file_list_third);
            imageViewArr[3] =  (ImageView) itemView.findViewById(R.id.gr_file_list_fourth);
        }

        public void setData(JSONArray jsonArray){
            try {
                final JSONObject temp = jsonArray.getJSONObject(0);
                date.setText(temp.getString("d"));
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            File_click_activity_.intent(a).extra("date" , temp.getString("d")).start();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                for(int i = 0 ; i<jsonArray.length() ; i++){
                    JSONObject tempObject = jsonArray.getJSONObject(i);
                    if(tempObject.getInt("image")==1) {
                        Glide.with(imageViewArr[i].getContext()).load(tempObject.getString("location")).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(imageViewArr[i]);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public int addItem(JSONObject jsonObject){
        try {
            if(mList.size() ==0){
                JSONArray insert = new JSONArray();
                insert.put(jsonObject);
                mList.add(insert);
                return 1;
            } else{
            JSONArray temp = mList.get(mList.size()-1);
            JSONArray insert = new JSONArray();
            insert.put(jsonObject);
            if(temp.getJSONObject(0).getString("d").equals(jsonObject.getString("d"))){
                for(int i = 0 ; temp.length()==4? i<3 : i < temp.length();i++){
                    insert.put(temp.get(i));
                }
                mList.set(mList.size()-1 , insert);
                return 0;
            } else{
                mList.add(insert);
                return 1;
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

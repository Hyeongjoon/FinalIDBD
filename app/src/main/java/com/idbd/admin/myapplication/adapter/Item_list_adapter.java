package com.idbd.admin.myapplication.adapter;

import android.app.Activity;
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
import com.idbd.admin.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017-03-27.
 */

public class Item_list_adapter extends RecyclerView.Adapter <Item_list_adapter.ViewHolder> {

    private List<JSONObject> mList= new ArrayList<>();
    private Activity a;

    public Item_list_adapter(JSONArray target){
        try {
        for(int i = 0 ; i < target.length() ; i++){
                mList.add(target.getJSONObject(i));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        Item_list_adapter.ViewHolder vh = new Item_list_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject target = mList.get(position);
        holder.setData(target);
    }

    @Override
    public int getItemCount() {
        if(mList==null){
            return 0;
        } else{
            return mList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ItemName = (TextView)itemView.findViewById(R.id.item_name);
        }

        public void setData(JSONObject data){
            try {
                ItemName.setText(data.getString("pname"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

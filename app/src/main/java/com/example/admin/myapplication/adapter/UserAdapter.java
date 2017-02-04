package com.example.admin.myapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myapplication.Gr_info_Activity_;
import com.example.admin.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017-01-31.
 */

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.ViewHolder> {

    private Activity a;
    private List<JSONObject> mList= new ArrayList();

    public UserAdapter(JSONArray jsonArray , Activity a ){
        for(int i =0 ; i<jsonArray.length() ; i++){
            try {
                mList.add(jsonArray.getJSONObject(i));
                if(jsonArray.getJSONObject(i).getInt("uid")== Gr_info_Activity_.master){
                    mList.remove(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.a = a;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gr_user_name;

        public ViewHolder(View itemView) {
            super(itemView);
            gr_user_name = (TextView) itemView.findViewById(R.id.gr_layout1_gr_name);
        }

        public void setData(JSONObject jsonObject) {
            try {
                this.gr_user_name.setText(jsonObject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gr_user_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject group= mList.get(position);
        holder.setData(group);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

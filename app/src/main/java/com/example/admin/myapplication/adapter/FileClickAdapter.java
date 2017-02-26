package com.example.admin.myapplication.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.myapplication.Gr_file_list_fragment;
import com.example.admin.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017-02-19.
 */

public class FileClickAdapter extends FragmentStatePagerAdapter {

    JSONArray jsonArray;

    public FileClickAdapter(FragmentManager fm , JSONArray jsonArray) {
        super(fm);
        this.jsonArray = jsonArray;
    }


    @Override
    public Fragment getItem(int position) {
        try {
            JSONObject data  = jsonArray.getJSONObject(position);
            return Gr_file_list_fragment.crate(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return Gr_file_list_fragment.crate(new JSONObject());
        }
    }

    @Override
    public int getCount() {
        if(jsonArray==null){
            return 0;
        } else{
            return jsonArray.length();
        }
    }

    public boolean deleteItem(int fid){
        int index = -1;
        try {
        for(int i = 0 ; i <jsonArray.length() ; i++){
                if(jsonArray.getJSONObject(i).getInt("fid") == fid){
                    index = i;
                    break;
                }
            }
            Log.d("msg" , index+"여긴 어댑터 안");
            if(index==-1){
                return false;
            } else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    jsonArray.remove(index);
                    Log.d("msg" , jsonArray.getJSONObject(0).getInt("fid")+"여기는 els문안");
                } else{
                    JSONArray newArr = new JSONArray();
                    for(int i = 0 ; i <index ; i++){
                        newArr.put(i , jsonArray.getJSONObject(i));
                    }
                    for(int i = index +1 ; i<jsonArray.length() ; i++ ){
                        newArr.put(i , jsonArray.getJSONObject(i));
                    }
                    jsonArray = newArr;
                    Log.d("msg" , jsonArray.getJSONObject(0).getInt("fid")+"여기는 els문안");
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}

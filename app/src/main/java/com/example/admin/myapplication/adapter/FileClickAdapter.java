package com.example.admin.myapplication.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
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

    private Activity a;

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
}

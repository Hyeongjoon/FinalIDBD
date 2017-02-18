package com.example.admin.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 2017-02-19.
 */

@EFragment
public class Gr_file_list_fragment extends Fragment {

    private String location;
    private String d;
    private int fid;
    private int uid;
    private int image;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        location = getArguments().getString("location");
        d = getArguments().getString("d");
        fid = getArguments().getInt("fid");
        uid = getArguments().getInt("uid");
        image = getArguments().getInt("image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.file_item, container, false);
        TextView date = (TextView)rootView.findViewById(R.id.file_upload_date);

        date.setText(d);

        TextView writer = (TextView)rootView.findViewById(R.id.file_upload_name);
        try {
            for(int i = 0 ; i<Gr_info_Activity_.user_list.length() ; i++){
                if(Gr_info_Activity_.user_list.getJSONObject(i).getInt("uid")==uid){
                    writer.setText(Gr_info_Activity_.user_list.getJSONObject(i).getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(image==1){
            ImageView imageView = (ImageView)rootView.findViewById(R.id.file_image_view);
            Glide.with(getActivity()).load(location).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        } else{
            ImageView imageView = (ImageView)rootView.findViewById(R.id.file_image_view);
            Glide.with(getActivity()).load(R.drawable.file).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
        return rootView;
    }

    public static  Gr_file_list_fragment crate(JSONObject jsonObject){
        Gr_file_list_fragment fragment = new Gr_file_list_fragment();
        Bundle args = new Bundle();
        try {
            args.putString("location" , jsonObject.getString("location"));
            args.putString("d" , jsonObject.getString("d"));
            args.putInt("fid" , jsonObject.getInt("fid"));
            args.putInt("uid" , jsonObject.getInt("uid"));
            args.putInt("image" , jsonObject.getInt("image"));
            fragment.setArguments(args);
        } catch (JSONException e){
                e.printStackTrace();
        }
        return fragment;
    };
}

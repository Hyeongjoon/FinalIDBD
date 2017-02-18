package com.example.admin.myapplication;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Admin on 2017-02-19.
 */

@EFragment
public class Gr_file_list_fragment extends Fragment {

    private String location;
    private String d;
    private String fileName;
    private int fid;
    private int uid;
    private int image;

    private DownloadManager mDownloadManager; //다운로드 매니저.
    private long mDownloadQueueId; //다운로드 큐 아이디..

    /**
     * 다운로드 완료 액션을 받을 리시버.
     */
    private BroadcastReceiver mCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            }
        }
    };

    public void download(String url) {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        DownloadManager.Request request = new DownloadManager.Request( Uri.parse(url) );
        request.setTitle("==타이틀==");
        request.setDescription("==설명==");
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/IDBD").getAbsolutePath();
        File dir_IDBD = new File(dirPath);
        if(!dir_IDBD.exists()){
            dir_IDBD.mkdir();
        } else{
            String filePath = dirPath +"/" + fileName;
            File file = new File(filePath);
            if(file.exists()){
                String temp = fileName.substring(0 , fileName.lastIndexOf("."));
                String fileType = fileName.substring(fileName.lastIndexOf("."));
                fileName = temp + System.currentTimeMillis() + fileType;
            }
        }

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/IDBD/", fileName );

        mDownloadQueueId = mDownloadManager.enqueue(request);
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        location = getArguments().getString("location");
        d = getArguments().getString("d");
        fileName = getArguments().getString("fileName");
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

        ImageView downBtn= (ImageView)rootView.findViewById(R.id.file_down_image);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(location);
            }
        });

        if(image==1){
            ImageView imageView = (ImageView)rootView.findViewById(R.id.file_image_view);
            Glide.with(getActivity()).load(location).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        } else{
            ImageView imageView = (ImageView)rootView.findViewById(R.id.file_image_view);
            Glide.with(getActivity()).load(R.drawable.file).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mCompleteReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(mCompleteReceiver, completeFilter);
    }


    public static  Gr_file_list_fragment crate(JSONObject jsonObject){
        Gr_file_list_fragment fragment = new Gr_file_list_fragment();
        Bundle args = new Bundle();
        try {
            args.putString("location" , jsonObject.getString("location"));
            args.putString("d" , jsonObject.getString("d"));
            args.putString("fileName" , jsonObject.getString("fileName"));
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

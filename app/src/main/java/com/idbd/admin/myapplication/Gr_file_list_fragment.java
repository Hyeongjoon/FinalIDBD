package com.idbd.admin.myapplication;

<<<<<<< HEAD
import android.*;
import android.Manifest;
=======
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
=======
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idbd.admin.myapplication.Helper.AwsS3;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
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

    private AmazonS3 s3;

    private DownloadManager mDownloadManager; //다운로드 매니저.
    private long mDownloadQueueId; //다운로드 큐 아이디..

<<<<<<< HEAD
    private static final String[] STORAGE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

=======
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
    /**
     * 다운로드 완료 액션을 받을 리시버.
     */
    private BroadcastReceiver mCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Toast.makeText(context, "DownLoad Complete.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void download(String url) {
<<<<<<< HEAD

        //요기 권한설정
=======
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        DownloadManager.Request request = new DownloadManager.Request( Uri.parse(url) );
        request.setTitle("파일을 다운로드 중입니다.");
        request.setDescription("다운로드중입니다.");
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/IDBD_download").getAbsolutePath();
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

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/IDBD_download/", fileName );

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
        s3 = new AmazonS3Client(new BasicAWSCredentials(AwsS3.getAccesskey() , AwsS3.getSecretkey()));
        s3.setRegion(com.amazonaws.regions.Region.getRegion(Regions.AP_NORTHEAST_2));
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

        //다운로드 버튼 셋팅
        ImageView downBtn= (ImageView)rootView.findViewById(R.id.file_down_image);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    download(location);
                } else{
                    if(ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                            ||ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(), STORAGE_PERMISSION, Gr_info_Activity_.MY_STORAGE_REQUEST);
                    } else{
                        download(location);
                    }
                }
=======
                download(location);
>>>>>>> 7e730503479d66aff7bc73f7fcac21b60eb5f609
            }
        });


        //정보보기 버튼
        ImageView dialogBtn = (ImageView)rootView.findViewById(R.id.file_dialog_image);
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog(fid , uid);
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

    @UiThread
    public void makeDialog(final int fid , int uid){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(fileName);
        if(Integer.parseInt(FirebaseAuth.getInstance().getCurrentUser().getUid())==uid){
         builder.setItems(R.array.file_click_my_file, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
               if(which == 0){
                    //정보 넣는 다이아로그 띄울것
               } else{
                    dialog.cancel();
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
                    deleteDialog.setTitle("정말로 삭제하시겠습니까?");
                    deleteDialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((File_click_activity_)getActivity()).deleteFile(fid);
                        }
                    });
                   deleteDialog.setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                       }
                   });
                   deleteDialog.show();
               }
             }
         });
        } else{
            builder.setItems(R.array.file_click_not_my_file, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //정보 넣는 다이아로그 띄울것
                }
            });
        }
        builder.show();
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

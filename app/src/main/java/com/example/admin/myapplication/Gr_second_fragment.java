package com.example.admin.myapplication;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.admin.myapplication.Helper.AwsS3;
import com.example.admin.myapplication.Helper.Get;
import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;
import com.example.admin.myapplication.adapter.FileListAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.Manifest.*;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by admin on 2017-01-12.
 */



@EFragment(R.layout.gr_layout2)
public class Gr_second_fragment extends Fragment{


    private AmazonS3 s3;
    private TransferUtility transferUtility;

    private String upload = "http://52.78.18.19/file/upload/";
    private String getList = "http://52.78.18.19/file/";

    private Uri mImageCaptureUri;
    private final int CameraRequestCode = 1;
    private static final String[] CAMERA_PERMISSIONS = {
            permission.CAMERA,
            permission.READ_EXTERNAL_STORAGE,
            permission.WRITE_EXTERNAL_STORAGE,
    };

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @AfterViews
    public void init(){
        s3 = new AmazonS3Client(new BasicAWSCredentials(AwsS3.getAccesskey() , AwsS3.getSecretkey()));
        s3.setRegion(com.amazonaws.regions.Region.getRegion(Regions.AP_NORTHEAST_2));
        transferUtility =  new TransferUtility(s3, getContext());
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.gr_layout2_list);
        mAdapter = new FileListAdapter(new JSONArray(), getActivity());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getFileList();
    }

    @Background
    public void getFileList(){
        try {
            final JSONObject result = new JSONObject(Get.get(getList+TokenInfo.getTokenId()+"/" + Gr_info_Activity.gid));
            if(result.getString("result").equals("true")){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mAdapter = new FileListAdapter(result.getJSONArray("file_list"), getActivity());
                            mRecyclerView.swapAdapter(mAdapter , false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else{
                //내부서버 오류났을때 앱 처리코드 적을것
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Click(R.id.gr_layout2_camera)
    public void camera(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
            //권환체크 필요없는 버전일때
        } else{
            if(ContextCompat.checkSelfPermission(getActivity() , permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity() , permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(getActivity() , permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), CAMERA_PERMISSIONS , Gr_info_Activity_.MY_PERMISSIONS_REQUEST);
            } else{
                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IDBD";
                File dir_tracer = new File(dirPath);
                if(!dir_tracer.exists()){
                    dir_tracer.mkdir();
                }
                Date d = new Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String filePath = dirPath +"/" + df.format(d) +".jpg";
                File image = new File(filePath);
                mImageCaptureUri = Uri.fromFile(image);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT , mImageCaptureUri);
                startActivityForResult(intent , CameraRequestCode);
            }
        }
    }

    @OnActivityResult(CameraRequestCode)
    void onResult(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,mImageCaptureUri));
            Log.d("msg" , mImageCaptureUri.getPath());
            String[] temp = mImageCaptureUri.getPath().split("/");
            String fileName = temp[temp.length-1];
            String fileKey = "file/" + Gr_info_Activity_.gid +"/"+fileName;
            PutObjectRequest por = new PutObjectRequest( getString(R.string.bucket), fileKey , new File(mImageCaptureUri.getPath()));
            this.upload(por , fileKey);
        }
    }

    @Background
    public void upload(PutObjectRequest por , String fileKey){
        try{
            s3.putObject(por);
            RequestBody formBody = new FormBody.Builder()
                    .add("location", fileKey)
                    .add("gid", Gr_info_Activity.gid+"")
                    .add("image" , true+"")
                    .build();
            JSONObject result = new JSONObject(Post.post(upload+TokenInfo.getTokenId(), formBody));

        }catch ( JSONException e){
           // Toast.makeText( getActivity() ,"내부 서버 오류 입니다." ,  Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch (Exception e){
           // Toast.makeText( getActivity() ,"파일전송중 오류가 생겼습니다." ,  Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}




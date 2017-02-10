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
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Semaphore;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.Manifest.*;
import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 2017-01-12.
 */



@EFragment(R.layout.gr_layout2)
public class Gr_second_fragment extends Fragment{
    private Uri mImageCaptureUri;
    private final int CameraRequestCode = 1;
    private static final String[] CAMERA_PERMISSIONS = {
            permission.CAMERA,
            permission.READ_EXTERNAL_STORAGE,
            permission.WRITE_EXTERNAL_STORAGE,
    };

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
                String fileKey = "file/" + Gr_info_Activity_.gid +"/"+df.format(d) +".jpg";
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
        }
    }
}




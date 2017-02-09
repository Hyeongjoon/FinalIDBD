package com.example.admin.myapplication;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.myapplication.Helper.MakeDialog;
import com.example.admin.myapplication.Helper.Post;
import com.example.admin.myapplication.Helper.TokenInfo;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.Manifest.*;

/**
 * Created by admin on 2017-01-12.
 */



@EFragment(R.layout.gr_layout2)
public class Gr_second_fragment extends Fragment{

    int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 2;
    int[] requst;

    @Click(R.id.gr_layout2_camera)
    public void camera(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            //권환체크 필요없는 버전일때
        } else{
            if(ContextCompat.checkSelfPermission(getActivity() , permission.CAMERA)!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity() , permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                Log.d("msg" , "여긴오냐");
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission.CAMERA , permission.WRITE_EXTERNAL_STORAGE}, Gr_info_Activity_.MY_PERMISSIONS_REQUEST);
            } else{
                Log.d("msg" , "여긴 허용 됐을때");
                CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
            }
        }
    }


}

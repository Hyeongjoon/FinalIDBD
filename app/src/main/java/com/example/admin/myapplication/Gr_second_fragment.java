package com.example.admin.myapplication;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.policy.conditions.BooleanCondition;
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
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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


    private String upload = "http://52.78.18.19/file/upload/";
    private String getList = "http://52.78.18.19/file/";

    private Uri mImageCaptureUri;
    private final int CameraRequestCode = 1;
    private final int TAKE_PHOTO = 2;
    private final int TAKE_DOC = 3;

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
            String[] temp = mImageCaptureUri.getPath().split("/");
            String fileName = temp[temp.length-1];
            String fileKey = "file/" + Gr_info_Activity_.gid +"/"+fileName;
            PutObjectRequest por = new PutObjectRequest( getString(R.string.bucket), fileKey , new File(mImageCaptureUri.getPath()));
            this.upload(por , fileKey , true);
        }
    }

    @Background
    public void upload(PutObjectRequest por , String fileKey, Boolean image){
        try{
            s3.putObject(por);
            RequestBody formBody = new FormBody.Builder()
                    .add("location", fileKey)
                    .add("gid", Gr_info_Activity.gid+"")
                    .add("image" , image+"")
                    .build();
            JSONObject result = new JSONObject(Post.post(upload+TokenInfo.getTokenId(), formBody));
            if(result.getString("result").equals("true")){
                notifyAddFile(((FileListAdapter)mAdapter).addItem(result.getJSONObject("input")));
            } else{
                //result false일때
            }
        }catch ( JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @UiThread
    public void notifyAddFile(int code){
        if(code == 1){
            mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        } else{
            mAdapter.notifyItemChanged(mAdapter.getItemCount()-1);
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    @OnActivityResult(TAKE_PHOTO)
    void onTakeResult(int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            String filePath = getImagePath(data.getData());
            if(filePath==null){
                makeDialog("클라우드 저장소에 있는 이미지는 기기에 다운후에 올려주세요");
            } else{
                String[] fileTemp = filePath.split("/");
                String fileName = fileTemp[fileTemp.length-1];
                String fileType = fileName.substring(fileName.lastIndexOf("."));
                Calendar current = Calendar.getInstance();
                current.get(Calendar.YEAR);
                String insertTime = ""+current.get(Calendar.YEAR) +current.get(Calendar.MONTH) + current.get(Calendar.DATE) + current.get(Calendar.HOUR_OF_DAY) + current.get(Calendar.MINUTE) + current.get(Calendar.SECOND);
                String insertName  = insertTime+fileType;
                String fileKey = "file/" + Gr_info_Activity_.gid +"/"+insertName;
                PutObjectRequest por = new PutObjectRequest( getString(R.string.bucket), fileKey , new File(filePath));
                this.upload(por , fileKey , true);
            }
        } else{

        }
    }

    public String getImagePath(Uri uri){
        Log.d("msg"  , uri.getPath());
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        String  path;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
        try {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        } catch (CursorIndexOutOfBoundsException e){
            return null;
        }
            cursor.close();
            return path;

    }

    @OnActivityResult(TAKE_DOC)
    void onDocTakeResult(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
/*            Calendar current = Calendar.getInstance();
            current.get(Calendar.YEAR);
            String insertTime = ""+current.get(Calendar.YEAR) +current.get(Calendar.MONTH) + current.get(Calendar.DATE) + current.get(Calendar.HOUR_OF_DAY) + current.get(Calendar.MINUTE) + current.get(Calendar.SECOND);
            String fileKey = "file/" + Gr_info_Activity_.gid +"/"+insertTime;
            Log.d("msg" , "여긴오냐");*/

            Log.d("msg" , data.getData().getAuthority());
            String filePath = getPath(data.getData());
            String[] fileTemp = filePath.split("/");
            String fileName = fileTemp[fileTemp.length-1];
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            Calendar current = Calendar.getInstance();
            current.get(Calendar.YEAR);
            String insertTime = ""+current.get(Calendar.YEAR) +current.get(Calendar.MONTH) + current.get(Calendar.DATE) + current.get(Calendar.HOUR_OF_DAY) + current.get(Calendar.MINUTE) + current.get(Calendar.SECOND);
            String insertName  = insertTime+"."+fileType;
            String fileKey = "file/" + Gr_info_Activity_.gid +"/"+insertName;
            PutObjectRequest por = new PutObjectRequest( getString(R.string.bucket), fileKey , new File(filePath));
            if(fileType.equals("bmp") ||fileType.equals("gif") || fileType.equals("jpg") || fileType.equals("png") || fileType.equals("webp") || fileType.equals("jpeg")){
                this.upload(por , fileKey , true);
                // 문서선택기로 이미지파일 선택했을때
            } else{
                this.upload(por , fileKey , false);
            }
            //PutObjectRequest por = new PutObjectRequest( getString(R.string.bucket), fileKey , new File(data.getData().getPath()));
            } else{

        }
    }


    private String getPath(final Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // MediaStore (and general)
                return getForApi19(uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @RequiresApi(19)
    private String getForApi19(Uri uri) {

        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {

                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {

                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {

                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {

                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {


            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {

            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Click(R.id.gr_layout2_upload)
    public void fileUpload(){
        final LayoutInflater inflater= getActivity().getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.upload_dialog, null);
        final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        Button doc_btn = (Button)dialogView.findViewById(R.id.upload_dialog_doc_btn);
        Button photo_btn = (Button)dialogView.findViewById(R.id.upload_dialog_photo_btn);
        Button cancel_btn = (Button)dialogView.findViewById(R.id.upload_dialog_cancel_btn);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , TAKE_PHOTO);
                //사진 pick
            }
        });

        doc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                //String[] mimetypes = {"application/pdf", "application/msword" , "application/vnd.ms-excel" , "application/mspowerpoint" , "application/zip" , "text/plain" , "application/hwp"};
               // intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(intent, TAKE_DOC);
                //파일탐색기
            }
        });

        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
        makeUploadDialog(dialog);
    }

    @UiThread
    public void makeUploadDialog(AlertDialog dialog){
        dialog.show();
    }

    @UiThread
    public void makeDialog(String content){
        MakeDialog.oneBtnDialog(getActivity() , content);
    }
}




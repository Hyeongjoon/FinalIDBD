package com.idbd.admin.myapplication.Helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.idbd.admin.myapplication.IntroActivity_;
import com.idbd.admin.myapplication.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by admin on 2017-01-28.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    DBHelper mDbHelper;
    private String add_new_num = "http://52.78.18.19/chat/add_num/";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // [START_EXCLUDE]

        // There are two types of messages data messages and notification messages. Data messages are handled

        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type

        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app

        // is in the foreground. When the app is in the background an automatically generated notification is displayed.

        // When the user taps on the notification they are returned to the app. Messages containing both notification

        // and data payloads are treated as notification messages. The Firebase console always sends notification

        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options

        // [END_EXCLUDE]


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("msg", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        // Check if message contains a notification payload.

        if (remoteMessage.getNotification() != null) {
            Log.d("msg", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }



        // Also if you intend on generating your own notifications as a result of a received FCM

        // message, here is where that should be initiated. See sendNotification method below.

    }

    private void sendPushNotification(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, IntroActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("text"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void insertMessage(Context context , RemoteMessage remoteMessage){
        mDbHelper = new DBHelper(context , 1);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.GR_ID, remoteMessage.getData().get("gid"));
        values.put(DBHelper.WRITER, remoteMessage.getData().get("writer"));
        values.put("content" , remoteMessage.getData().get("text"));
        db.insert(DBHelper.CHAT_LIST, null, values);
    }


    private void add_num( String temp , String gid){
                final RequestBody formBody = new FormBody.Builder().add("gid" , gid).build();
                String idToken = temp;
                if(idToken==null) {
                    idToken = FirebaseInstanceId.getInstance().getToken();
                }
                final String finalIdToken = idToken;
                new Thread(){
                        @Override
                        public void run() {
                            try {
                                Post.post(add_new_num + finalIdToken, formBody);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
    }
}

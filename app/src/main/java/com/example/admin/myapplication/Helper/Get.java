package com.example.admin.myapplication.Helper;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017-01-13.
 */

public class Get {
    private static OkHttpClient client = new OkHttpClient();

    public static String get(String url ) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        Log.d("msg" , "여긴오냐");
        return response.body().string();
    }
}

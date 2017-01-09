package com.example.admin.myapplication.Helper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2016-12-28.
 */

public class Post {

    private static OkHttpClient client = new OkHttpClient();

    public static String post(String url , RequestBody formBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

}

package com.lijie.smartweather.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/24.
 */
public class HttpUtil {
//    public final static String appid = "28198";
//    public final static String secret = "bd9ad7a172ee4a5a8c57618a248c63e9";


    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}

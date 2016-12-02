package com.trimph.everything.utils;

import android.util.Log;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tao on 2016/8/11.
 */

public class LogIntercepter implements Interceptor {

    public String TAG = LogIntercepter.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e(TAG, "------------chain----------");
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.e("trimph loggInterceptor" , String.format("发送请求: [%s] %s%n%s",
                request.url(),  request.method(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.e("trimph loggInterceptor" , String.format("接收响应: [%s] %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return chain.proceed(request);
    }
}

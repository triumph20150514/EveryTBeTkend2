package com.trimph.everything.net;

import android.os.Environment;
import android.util.Log;

import com.trimph.everything.log.okHttpLog.HttpLogInterceptor;
import com.trimph.everything.log.okHttpLog.HttpLoggingInterceptorM;
import com.trimph.everything.utils.LogIntercepter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: Trimph
 * data: 2016/11/9.
 * description:
 */

public class RetrifitNet {

    public static OkHttpClient okHttpClient;
    public static File dir = Environment.getExternalStorageDirectory();
    public static int maxSize = 5 * 1024 * 1024;

    public static MediaAPi getMediaApi() {
        initHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MediaAPi.HOST)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MediaAPi.class);
    }

    public static void initHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /**
         * 缓存
         */
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                return null;
            }
        };
        File file = new File(dir, "cache");
        Cache cache = new Cache(file, maxSize);
//        builder.addInterceptor(interceptor);
        HttpLoggingInterceptorM httpLoggingInterceptor = new HttpLoggingInterceptorM(new HttpLogInterceptor("HttpLogging"));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptorM.Level.BASIC);
        builder.addNetworkInterceptor(httpLoggingInterceptor);
        builder.cache(cache);
//        builder.addInterceptor(cacheInterceptor);
        builder.addInterceptor(new LogIntercepter());
//        builder.connectTimeout(20, TimeUnit.SECONDS);
        okHttpClient = builder.build();
    }


}

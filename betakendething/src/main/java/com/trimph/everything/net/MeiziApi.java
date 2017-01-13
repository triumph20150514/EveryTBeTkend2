package com.trimph.everything.net;

import android.os.Build;

import com.trimph.everything.model.GankHttpResponse;
import com.trimph.everything.model.GankItemBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public interface MeiziApi {

    String HOST = "http://gank.io/api/";
    ///http://gank.io/api/data/福利/1/1
    /**
     * 福利列表
     */
    @Headers("Cache-Control:public,max-age=3600")
    @GET("data/福利/{num}/{page}")
    Observable<GankHttpResponse<List<GankItemBean>>> getGirlList(@Path("num") int num, @Path("page") int page);


}

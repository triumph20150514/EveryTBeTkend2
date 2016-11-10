package com.trimph.everything.net;

import com.trimph.everything.model.VideoHttpResponse;
import com.trimph.everything.model.VideoRes;
import com.trimph.everything.model.VideoResult;

import retrofit2.http.GET;
import rx.Observable;

/**
 * author: Trimph
 * data: 2016/11/9.
 * description:
 */

public interface MediaAPi {
    String HOST = "http://api.svipmovie.com/front/";
    /**
     * 首页
     *
     * @return
     */
    @GET("homePageApi/homePage.do")
    Observable<VideoHttpResponse<VideoRes>> getHomePage();

}

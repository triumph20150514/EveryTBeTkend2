package com.trimph.everything.net;

import com.trimph.everything.model.VideoDetail;
import com.trimph.everything.model.VideoHttpResponse;
import com.trimph.everything.model.VideoRes;
import com.trimph.everything.model.VideoResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
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
     * http://api.svipmovie.com/front/homePageApi/homePage.do
     *
     * @return
     */
    @GET("homePageApi/homePage.do")
    Observable<VideoHttpResponse<VideoRes>> getHomePage();


    /**
     * 影片详情
     * http://api.svipmovie.com/front/videoDetailApi/videoDetail.do?mediaId=CMCC_00000000000000001_618545932
     *
     * @param mediaId 影片id
     * @return
     */
    @GET("videoDetailApi/videoDetail.do")
    Observable<VideoHttpResponse<VideoDetail>> getVideoInfo(@Query("mediaId") String mediaId);

}

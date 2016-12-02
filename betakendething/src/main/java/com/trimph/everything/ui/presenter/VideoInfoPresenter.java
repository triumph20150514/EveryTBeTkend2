package com.trimph.everything.ui.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.trimph.everything.base.RxPresenter;
import com.trimph.everything.model.VideoDetail;
import com.trimph.everything.model.VideoHttpResponse;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.net.RetrifitNet;
import com.trimph.everything.ui.contact.VideoInfoContact;
import com.trimph.everything.ui.fragment.FravoriteFragment;
import com.trimph.everything.utils.RxUtil;

import org.simple.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * author: Trimph
 * data: 2016/11/28.
 * description:
 */

public class VideoInfoPresenter extends RxPresenter implements VideoInfoContact.Persenter {

    public VideoInfoContact.View videoView;
    public VideoInfo videoInfo;
    public String TAG = VideoInfoPresenter.class.getSimpleName();

    private String dataId;

    public VideoInfoPresenter(VideoInfoContact.View view, VideoInfo info) {
        this.videoView = view;
        videoView.setPersenter(this);
        this.videoInfo = info;
        this.dataId = videoInfo.getDataId();
        Log.e(TAG + "VideoInfoPresenter::", "构造函数");
        getDetailData(dataId);
    }

    @Override
    public void getDetailData(String dataId) {
        if (TextUtils.isEmpty(dataId)) {
            Log.e(TAG + "VideoInfoPresenter::", "数据Id为空呢");
            return;
        }
        videoView.showDialog();


        /*
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.svipmovie.com/front/videoDetailApi/videoDetail.do?mediaId=CMCC_00000000000000001_621948077") //携带参数
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                videoView.closeDialog();
                Log.e("Response", response.body().toString());
            }
        });
*/

//        OkHttpClient.Builder okhttp=new OkHttpClient().newBuilder();


        Subscription subscription = RetrifitNet.getMediaApi().getVideoInfo(dataId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtil.<VideoDetail>handleResult())
                .subscribe(new Action1<VideoDetail>() {
                               @Override
                               public void call(VideoDetail videoDetail) {
                                   if (videoDetail != null) {
                                       Log.e(TAG, "VideoDetail:" + videoDetail.toString());
                                       videoView.closeDialog();
                                       videoView.showContent(videoDetail, videoInfo);
                                       EventBus.getDefault().post(videoDetail, FravoriteFragment.TAG); //
                                   }
                               }
                           }
                );

        addComposite(subscription);

    }
}

package com.trimph.everything.ui.presenter;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.trimph.everything.base.RxPresenter;
import com.trimph.everything.model.GankHttpResponse;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.model.VideoHttpResponse;
import com.trimph.everything.model.VideoRes;
import com.trimph.everything.net.MeiziNet;
import com.trimph.everything.net.RetrifitNet;
import com.trimph.everything.ui.contact.HomeContact;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Trimph
 * data: 2016/11/11.
 * description:
 */

public class HomePresenter extends RxPresenter implements HomeContact.presenter {

    HomeContact.view view;
    public boolean isBanner = false, recycler = false;

    public HomePresenter(HomeContact.view view) {
        this.view = view;
        Log.e("HomePresenter --------", "HomePresenter++++++++");
        view.setPersenter(this);
        start();
    }

    @Override
    public void getVideo() {
        requestData();
    }

    @Override
    public void start() {
        Log.e("HomePresenter --------", "start");
        requestData();
    }

    private void requestData() {
        Log.e("HomePresenter --------", "requestData");
        view.showDialog();
        Log.e("VideoInfo::", "come in");
        final RetrifitNet retrifitNet = new RetrifitNet();

        Subscription subscription = retrifitNet.getMediaApi().getHomePage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoHttpResponse<VideoRes>>() {
                    @Override
                    public void call(VideoHttpResponse<VideoRes> videoResVideoHttpResponse) {
                        if (videoResVideoHttpResponse != null) {
                            Log.e("VideoInfo::", videoResVideoHttpResponse.getRet().list.get(0).childList.toString());
                            if (videoResVideoHttpResponse.getCode() == 200) {
                                recycler = true;
                                if (isBanner && recycler) {
                                    Log.e("HomePresenter--------", "load view close");
                                    view.closeDialog();
                                }
                                view.showContent(videoResVideoHttpResponse.getRet());

                            }
                        }
                    }
                });

        MeiziNet meiziNet = new MeiziNet();
        Subscription subscription1 = meiziNet.getMeiZiApi().getGirlList(3, 2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankHttpResponse<List<GankItemBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GankHttpResponse<List<GankItemBean>> listGankHttpResponse) {
                        if (listGankHttpResponse.getError() == false) {
                            Log.e("HomePresenter--------", listGankHttpResponse.getResults().get(0).toString());
                            view.showBannerImage(listGankHttpResponse.getResults());
                            view.showImage(listGankHttpResponse.getResults().get(0));
                            isBanner = true;
//                            view.closeDialog();
                            if (isBanner && recycler) {
                                Log.e("HomePresenter--------", "load view close");
                                view.closeDialog();
                            }
                        }
                    }
                });


        addComposite(subscription);
        addComposite(subscription1);
    }

    @Override
    public void detachView() {
        super.detachView();
        view.stopBanner();
    }
}

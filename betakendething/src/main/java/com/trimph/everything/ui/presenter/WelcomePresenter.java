package com.trimph.everything.ui.presenter;

import android.util.Log;

import com.trimph.everything.base.RxPresenter;
import com.trimph.everything.model.GankHttpResponse;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.model.VideoHttpResponse;
import com.trimph.everything.model.VideoRes;
import com.trimph.everything.net.MeiziNet;
import com.trimph.everything.net.RetrifitNet;
import com.trimph.everything.ui.contact.WelcomeContact;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: Trimph
 * data: 2016/11/9.
 * description:
 */

public class WelcomePresenter extends RxPresenter implements WelcomeContact.WelcomePresenter {

    public WelcomeContact.WelcomeView welcomeView;

    /***
     * 通过构造函数把 View 视图注入
     *
     * @param welcomeView
     */
    public WelcomePresenter(WelcomeContact.WelcomeView welcomeView) {
        this.welcomeView = welcomeView;
        //注入进去
        welcomeView.setPersenter(this);  ///什么
    }

    @Override
    public void getWelcomeData() {
        //先获取数据 然后显示到界面中
        MeiziNet meiziNet = new MeiziNet();
        Subscription subscription = meiziNet.getMeiZiApi().getGirlList(2, 1)
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
                        Log.e("NET", listGankHttpResponse.getError() + "");
                        welcomeView.showContent(listGankHttpResponse.getResults().get(1));
                    }
                });
        addComposite(subscription);
    }

    @Override
    public void start() {

    }
}

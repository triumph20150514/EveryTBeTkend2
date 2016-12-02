package com.trimph.everything.utils;

import android.text.TextUtils;

import com.trimph.everything.model.VideoHttpResponse;
import com.trimph.everything.net.ApiException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public class RxUtil {

    public static <T> Observable.Transformer<VideoHttpResponse<T>, T> handleResult() {   //compose判断结果
        return new Observable.Transformer<VideoHttpResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<VideoHttpResponse<T>> httpResponseObservable) {
                return httpResponseObservable.flatMap(new Func1<VideoHttpResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(VideoHttpResponse<T> videoHttpResponse) {
                        if (videoHttpResponse.getCode() == 200) {
                            return createData(videoHttpResponse.getRet());
                        } else if (!TextUtils.isEmpty(videoHttpResponse.getMsg())) {
                            return Observable.error(new ApiException("*" + videoHttpResponse.getMsg()));
                        } else {
                            return Observable.error(new ApiException("*" + "服务器返回error"));
                        }
                    }
                });
            }
        };
    }


    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static <T> Observable<T> createDate(final T t) {

        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(t);
            }
        });


    }

    public static <T> Observable.Transformer<T, T> getCommentThread() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public static <T> Observable.Transformer<T, T> resultDeth() {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return null;
            }
        };
    }

}

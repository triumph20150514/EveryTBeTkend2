package com.trimph.everything.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public class RxUtil {

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



    public static <T> Observable.Transformer<T, T>  resultDeth(){

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return null;
            }
        };
    }

}

package com.trimph.everything.base;

import android.util.Log;

import rx.Completable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public class RxPresenter<T> implements BasePresenter<T> {

    public T view;
    CompositeSubscription compositeSubscription;

    public void addComposite(Subscription subscription) {
        if (compositeSubscription == null) {
            Log.e("detachView", "addComposite");
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    public void start() {

    }

    @Override
    public void attachView(T view) {
        this.view = view;
        Log.e("detachView", "attachView");
    }

    @Override
    public void detachView() {
        Log.e("detachView", "detachView");
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

}

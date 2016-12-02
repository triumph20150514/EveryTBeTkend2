package com.trimph.everything.base;

import android.view.View;

/**
 * Created by tao on 2016/11/8.
 */

public interface BasePresenter<T> {
    void start();

    public void attachView(T view);

    public void detachView();
}

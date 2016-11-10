package com.trimph.everything.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public abstract class RootView<T extends BasePresenter> extends LinearLayout {

    public T rPresenter;
    public Unbinder binder;

    public RootView(Context context) {
        super(context);
        init(context);
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
//        rPresenter.attachView(this);
        if (rPresenter != null) {
            rPresenter.attachView(this);
        }
        binder = ButterKnife.bind(this);// 绑定视图
    }

    @Override
    protected void onDetachedFromWindow() {
        if (rPresenter!=null){
            rPresenter.detachView();
        }
        binder.unbind();  //解除绑定
    }

    protected abstract void init(Context context);


}

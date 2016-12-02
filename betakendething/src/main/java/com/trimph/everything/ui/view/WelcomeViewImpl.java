package com.trimph.everything.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trimph.everything.R;
import com.trimph.everything.base.RootView;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.ui.activity.WelcomeActivity;
import com.trimph.everything.ui.contact.WelcomeContact;
import com.trimph.everything.utils.JumpToUtils;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * author: Trimph
 * data: 2016/11/9.
 * description:
 */

public class WelcomeViewImpl extends RootView<WelcomeContact.WelcomePresenter> implements WelcomeContact.WelcomeView {

    public Context context;

    @BindView(R.id.startImage)
    ImageView startImage;
    @BindView(R.id.tv)
    TextView tv;

    public WelcomeViewImpl(Context context) {
        this(context, null);
    }

    public WelcomeViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void showContent(GankItemBean videoRes) {
        Log.e("NET", videoRes.getUrl());
        tv.setText(videoRes.getUrl());
//        Glide.with(context).load("http://ww4.sinaimg.cn/large/610dc034jw1f6yq5xrdofj20u00u0aby.jpg").crossFade().into(startImage);

        Glide.with(context).load("file:///android_asset/b.jpg").crossFade().into(startImage);

//        GlideUtils.DisplayImage(context, startImage, videoRes.getUrl());
        startImage.animate().scaleX(1.15f).scaleY(1.15f).setDuration(2000).setInterpolator(new LinearInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (active) {
                    jumpToMain(context);
                }
            }
        }).start();
    }

    @Override
    public void jumpToMain(final Context context) {
        Subscription subscription = Observable.timer(2000, java.util.concurrent.TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (active) {
                            JumpToUtils.JumpToMain(context);
                            ((WelcomeActivity) context).finish();
                        }
                    }
                });

    }

    @Override
    public void setPersenter(WelcomeContact.WelcomePresenter persenter) {
        rPresenter = persenter;
    }

    @Override
    protected void init(Context context) {
        this.context = context;
        getLayout(context);
    }

    private void getLayout(Context context) {
        inflate(context, R.layout.welcome_view, this);
    }
}

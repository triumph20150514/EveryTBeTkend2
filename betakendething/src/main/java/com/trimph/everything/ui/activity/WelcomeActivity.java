package com.trimph.everything.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.trimph.everything.R;
import com.trimph.everything.base.BaseActivity;
import com.trimph.everything.ui.contact.WelcomeContact;
import com.trimph.everything.ui.presenter.WelcomePresenter;
import com.trimph.everything.ui.view.WelcomeViewImpl;
import com.trimph.everything.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public class WelcomeActivity extends BaseActivity<WelcomeContact.WelcomePresenter> {

    @BindView(R.id.welcome_view)
    WelcomeViewImpl welcomeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        presenter = new WelcomePresenter(welcomeView);
        init();
    }

    private void init() {
        presenter.getWelcomeData();
    }

}

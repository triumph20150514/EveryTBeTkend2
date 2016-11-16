package com.trimph.everything.ui.contact;

import android.content.Context;

import com.trimph.everything.base.BasePresenter;
import com.trimph.everything.base.BaseView;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.model.VideoRes;

/**
 * author: Trimph
 * data: 2016/11/9.
 * description:欢迎页
 */

public interface WelcomeContact {

    interface WelcomeView extends BaseView<WelcomePresenter> {
        void showContent(GankItemBean videoRes);
        void jumpToMain(Context context);
    }

    
    interface WelcomePresenter extends BasePresenter {
        void getWelcomeData();
    }

}

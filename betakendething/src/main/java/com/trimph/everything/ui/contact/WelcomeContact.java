package com.trimph.everything.ui.contact;

import com.trimph.everything.base.BasePresenter;
import com.trimph.everything.base.BaseView;

/**
 * author: Trimph
 * data: 2016/11/9.
 * description:欢迎页
 */

public interface WelcomeContact {

    interface WelcomeView extends BaseView<WelcomePresenter> {
        void showContent();
    }

    interface WelcomePresenter extends BasePresenter {
        void getWelcomeData();
    }

}

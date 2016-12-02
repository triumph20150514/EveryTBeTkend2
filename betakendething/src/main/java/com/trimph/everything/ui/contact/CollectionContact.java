package com.trimph.everything.ui.contact;

import com.trimph.everything.base.BasePresenter;
import com.trimph.everything.base.BaseView;

/**
 * author: Trimph
 * data: 2016/11/24.
 * description:
 */

public interface CollectionContact {

    interface View extends BaseView<Persenter> {
        void showContent();
    }

    interface Persenter extends BasePresenter {

    }
}

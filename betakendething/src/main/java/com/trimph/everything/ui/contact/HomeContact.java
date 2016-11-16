package com.trimph.everything.ui.contact;

import com.trimph.everything.base.BasePresenter;
import com.trimph.everything.base.BaseView;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.model.VideoRes;

import java.util.List;

/**
 * author: Trimph
 * data: 2016/11/11.
 * description:
 */

public interface HomeContact {
    interface view extends BaseView<presenter> {
        void showContent(VideoRes videoRes);

        void showBannerImage(List<GankItemBean> gankItemBeanList);

        void showImage(GankItemBean gankItemBeanList);

        void showDialog();

        void closeDialog();

        void stopBanner();
    }

    interface presenter extends BasePresenter {
        void getVideo();
    }
}

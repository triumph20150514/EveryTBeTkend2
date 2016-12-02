package com.trimph.everything.ui.contact;

import com.trimph.everything.base.BasePresenter;
import com.trimph.everything.base.BaseView;
import com.trimph.everything.model.VideoDetail;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.model.VideoRes;

/**
 * author: Trimph
 * data: 2016/11/28.
 * description:
 */

public interface VideoInfoContact {
    interface View extends BaseView<Persenter> {

        void showDialog();

        void closeDialog();

        void showContent(VideoDetail videoRes, VideoInfo videoInfo);
    }

    interface Persenter extends BasePresenter {
        /**
         * 根据id获取详情
         *
         * @param dataId
         */
        void getDetailData(String dataId);
    }
}

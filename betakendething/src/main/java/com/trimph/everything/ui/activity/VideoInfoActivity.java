package com.trimph.everything.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.trimph.everything.R;
import com.trimph.everything.base.BaseActivity;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.ui.contact.VideoInfoContact;
import com.trimph.everything.ui.presenter.VideoInfoPresenter;
import com.trimph.everything.ui.view.VideoInfoViewImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Trimph
 * data: 2016/11/28.
 * description:
 */

public class VideoInfoActivity extends BaseActivity<VideoInfoContact.Persenter> {
    @BindView(R.id.videoInfo)
    VideoInfoViewImpl videoInfoView;
    public String TAG = VideoInfoActivity.class.getSimpleName();

    public VideoInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);
        unbinder = ButterKnife.bind(this);

        info = getIntent().getParcelableExtra(VideoInfo.class.getSimpleName());
        Log.e(TAG, "-----------------info" + info.toString());
        if (info != null) {
            Log.e(TAG, "-----------------" + info.toString());
            presenter = new VideoInfoPresenter(videoInfoView, info);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.trimph.everything.ui.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.trimph.everything.R;
import com.trimph.everything.base.RootView;
import com.trimph.everything.model.VideoDetail;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.ui.activity.VideoInfoActivity;
import com.trimph.everything.ui.contact.VideoInfoContact;
import com.trimph.everything.ui.fragment.FravoriteFragment;
import com.trimph.everything.utils.GlideUtils;
import com.trimph.everything.weight.LVGhost;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * author: Trimph
 * data: 2016/11/28.
 * description:
 */

public class VideoInfoViewImpl extends RootView<VideoInfoContact.Persenter> implements VideoInfoContact.View {

    public Context mContext;
    @BindView(R.id.videoPlayer)
    JCVideoPlayerStandard videoPlayer;

    @BindView(R.id.lvHost)
    LVGhost lvHost;

    @BindView(R.id.smartLayout)
    SmartTabLayout smartLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public VideoInfoViewImpl(Context context) {
        this(context, null);
    }

    public VideoInfoViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPersenter(VideoInfoContact.Persenter persenter) {
        this.rPresenter = persenter;
    }

    @Override
    protected void init(Context context) {
        this.mContext = context;
        //加载布局
        inflate(context, R.layout.video_info_impl, this);

        Log.e("VideoInfoViewImpl:","init-------------");

    }

    @Override
    public void showDialog() {
        if (lvHost != null) {
            lvHost.setVisibility(VISIBLE);
        }
    }

    @Override
    public void closeDialog() {
        if (lvHost != null) {
            lvHost.setVisibility(GONE);
        }
    }

    @Override
    public void showContent(VideoDetail videoRes, VideoInfo videoInfo) {
        Log.e("VideoInfoViewImpl:","showContent-------------");

        FragmentPagerItemAdapter fragmentPagerItemAdapter = new FragmentPagerItemAdapter(((VideoInfoActivity) mContext).getSupportFragmentManager(),
                FragmentPagerItems.with(mContext)
                        .add(R.string.video_about_fravorite, FravoriteFragment.class)
                        .add(R.string.video_about_review, FravoriteFragment.class)
                        .create()
        );

        if (viewPager != null) {
            viewPager.setAdapter(fragmentPagerItemAdapter);
            if (smartLayout != null) {
                smartLayout.setLayoutMode(TabLayout.MODE_FIXED);
                smartLayout.setViewPager(viewPager);
            }
        } else {
            Toast.makeText(mContext, "怎么为空呢！", Toast.LENGTH_SHORT).show();
        }

        if (videoPlayer != null) {
            videoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            videoPlayer.backButton.setVisibility(View.GONE);
            videoPlayer.titleTextView.setVisibility(View.GONE);
            videoPlayer.tinyBackImageView.setVisibility(View.GONE);
        } else {
            Toast.makeText(mContext, "videoPlayer 怎么为空呢！", Toast.LENGTH_SHORT).show();
        }

        if (videoInfo != null) {
            GlideUtils.DisplayImage(mContext, videoPlayer.thumbImageView, videoInfo.getPic());
        }
        if (videoRes != null) {
            if (!TextUtils.isEmpty(videoRes.getHDURL())){
                videoPlayer.setUp(videoRes.getHDURL(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, videoRes.getTitle());
            }
        }
    }

}

package com.trimph.everything.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.trimph.everything.R;
import com.trimph.everything.base.RootView;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.model.VideoRes;
import com.trimph.everything.ui.adapter.HomeViewAdapter;
import com.trimph.everything.ui.contact.HomeContact;
import com.trimph.everything.weight.BannerIndicator;
import com.trimph.everything.weight.LVGhost;

import java.util.List;

import butterknife.BindView;

/**
 * author: Trimph
 * data: 2016/11/11.
 * description:
 */

public class HomeViewImpl extends RootView<HomeContact.presenter> implements HomeContact.view {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    HomeViewAdapter homeViewAdapter;

    public Context mContext;
    @BindView(R.id.banner)
    BannerIndicator banner;

    @BindView(R.id.load_view)
    LVGhost loadView;

    public HomeViewImpl(Context context) {
        this(context, null);
    }

    public HomeViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        this.mContext = context;
        inflate(context, R.layout.home_vew, this);

        homeViewAdapter = new HomeViewAdapter(context);
    }

    @Override
    public void showContent(VideoRes videoRes) {
        if (videoRes != null) {
            homeViewAdapter.setVideoInfos(videoRes.list.get(0).childList);
            Log.e("VideoInfo", videoRes.list.get(0).childList.toString());
            recycler.setAdapter(homeViewAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mContext));
        }
    }

    @Override
    public void showBannerImage(List<GankItemBean> gankItemBeanList) {
//        View handerView = LayoutInflater.from(mContext).inflate(R.layout.home_viepager_title, null, false);
//        banner = (BannerIndicator) handerView.findViewById(R.id.banner);
        Log.e("showBannerImage-------", gankItemBeanList.get(0).toString());
        if (gankItemBeanList == null) {
            rPresenter.getVideo();
        }

        if (gankItemBeanList != null) {
            Log.e("showBannerImage-------", "size" + gankItemBeanList.size());
            banner.setSource(gankItemBeanList).startScroll(); // 添加 startScroll 不然图片就出不来
        }
    }

    @Override
    public void showImage(GankItemBean gankItemBeanList) {
        Log.e("GankitemBean", gankItemBeanList.toString());
    }

    @Override
    public void showDialog() {
        if (loadView != null) {
            loadView.setVisibility(VISIBLE);
//            loadView.startAnim();
        }
    }

    @Override
    public void closeDialog() {
        if (loadView != null) {
//            loadView.stopAnim();
            Log.e("Dialog", "closeDialog");
            loadView.setVisibility(GONE);
        }
    }

    @Override
    public void stopBanner() {
        if (banner != null) {
            banner.pauseScroll();
        }
    }

    @Override
    public void setPersenter(HomeContact.presenter persenter) {
        this.rPresenter = persenter;
    }
}

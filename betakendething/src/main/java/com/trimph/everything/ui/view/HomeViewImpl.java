package com.trimph.everything.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trimph.everything.R;
import com.trimph.everything.base.RootView;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.model.VideoRes;
import com.trimph.everything.ui.adapter.HomeViewAdapter;
import com.trimph.everything.ui.contact.HomeContact;
import com.trimph.everything.utils.GlideUtils;
import com.trimph.everything.weight.BannerIndicator;
import com.trimph.everything.weight.LVGhost;
import com.yalantis.taurus.PullToRefreshView;

import java.util.List;

import butterknife.BindView;

/**
 * author: Trimph
 * data: 2016/11/11.
 * description:
 */

public class HomeViewImpl extends RootView<HomeContact.presenter> implements HomeContact.view, PullToRefreshView.OnRefreshListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    HomeViewAdapter homeViewAdapter;

    public Context mContext;

    BannerIndicator banner;

    MyAdapter myAdapter;

    View handerView;
    @BindView(R.id.load_view)
    LVGhost loadView;
    @BindView(R.id.pullToRefresh)
    PullToRefreshView pullToRefresh;

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

        handerView = LayoutInflater.from(mContext).inflate(R.layout.home_viepager_title, null, false);

        banner = (BannerIndicator) handerView.findViewById(R.id.banner);

        homeViewAdapter = new HomeViewAdapter(context);
        pullToRefresh.setOnRefreshListener(this);

    }

    @Override
    public void showContent(VideoRes videoRes) {

        if (videoRes != null) {
            myAdapter = new MyAdapter(mContext, R.layout.home_item_view_layout, videoRes.list.get(0).childList);
            myAdapter.addHeaderView(handerView);
            recycler.setAdapter(myAdapter);
            recycler.setLayoutManager(new LinearLayoutManager(mContext));
        }

    }

    @Override
    public void onRefresh() {

    }

    /**
     * BaseRecycleViewAdapter
     */
    public class MyAdapter extends BaseQuickAdapter<VideoInfo> {

        public MyAdapter(Context context, int layoutResId, List<VideoInfo> data) {
            super(context, layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, VideoInfo videoInfo) {
            baseViewHolder.setText(R.id.video_title, videoInfo.title);
            GlideUtils.DisplayImage(mContext, (ImageView) baseViewHolder.convertView.findViewById(R.id.video_iamge), videoInfo.pic);
        }
    }


    @Override
    public void showBannerImage(List<GankItemBean> gankItemBeanList) {
        Log.e("showBannerImage-------", gankItemBeanList.get(0).toString());
        if (gankItemBeanList == null) {
            rPresenter.getVideo();
        }
        if (gankItemBeanList != null) {
            Log.e("showBannerImage-------", "size" + gankItemBeanList.size());
            if (banner != null) {
                Log.e("showBannerImage-------", "banner" + gankItemBeanList.size());
                banner.setSource(gankItemBeanList).startScroll(); // 添加 startScroll 不然图片就出不来
                myAdapter.notifyDataSetChanged();
            }
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

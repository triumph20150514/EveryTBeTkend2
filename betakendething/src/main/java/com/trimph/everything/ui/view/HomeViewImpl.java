package com.trimph.everything.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trimph.everything.R;
import com.trimph.everything.base.RootView;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.model.SectionVideoInfo;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.model.VideoRes;
import com.trimph.everything.ui.activity.MainActivity;
import com.trimph.everything.ui.adapter.HomeVideoTypeAdapter;
import com.trimph.everything.ui.adapter.HomeViewAdapter;
import com.trimph.everything.ui.contact.HomeContact;
import com.trimph.everything.utils.GlideUtils;
import com.trimph.everything.utils.JumpToUtils;
import com.trimph.everything.weight.BannerIndicator;
import com.trimph.everything.weight.LVGhost;

import java.util.ArrayList;
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

    MyAdapter myAdapter;
    View handerView;
    @BindView(R.id.load_view)
    LVGhost loadView;

//    @BindView(R.id.pullToRefresh)
//    PullToRefreshView pullToRefresh;

    HomeVideoTypeAdapter videoTypeAdapter;

    public List<SectionVideoInfo> videoInfos = new ArrayList<>();
    public List<SectionVideoInfo> noTitleVideoInfos = new ArrayList<>();

    @BindView(R.id.banner)
    BannerIndicator banner;

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

//        handerView = ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.home_viepager_title, null, false);
//        banner = (BannerIndicator) findViewById(R.id.banner);

//        homeViewAdapter = new HomeViewAdapter(context);
    }

    @Override
    public void showContent(VideoRes videoRes) {

        if (videoRes != null) {

            initData(videoRes);

            videoInfos.addAll(noTitleVideoInfos);

            if (videoTypeAdapter == null) {
                videoTypeAdapter = new HomeVideoTypeAdapter(mContext, R.layout.home_item_view_layout,
                        R.layout.home_item_title, videoInfos);
//              myAdapter = new MyAdapter(mContext, R.layout.home_item_view_layout, videoRes.list.get(0).childList);
            }

            Log.e("TypeAdapter-------", videoTypeAdapter.getData().size() + "");
            Log.e("TypeAdapter--------", ((SectionVideoInfo) videoTypeAdapter.getData().get(0)).isHeader + "");

//            videoTypeAdapter.addHeaderView(handerView);
            if (recycler != null) {
                recycler.setAdapter(videoTypeAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(mContext));
            }

            initListener();
        }
    }

    private void initListener() {
        videoTypeAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Log.e("OnClickPositionChild", i + "");
            }
        });

        videoTypeAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Log.e("OnClickPosition", i + "");
                VideoInfo videoInfo = videoInfos.get(i).t;
                Log.e("OnClickPosition", videoInfo.toString() + "");
                JumpToUtils.JupmToVidoInfo(mContext, videoInfo);
            }
        });

    }


    /**
     * 分类
     *
     * @param videoRes
     */
    private void initData(VideoRes videoRes) {
        List<VideoInfo> list;

        for (int i = 0; i < videoRes.list.size(); i++) {
            if (videoRes.list.get(i).title.equals("免费推荐")) {
//                Log.e("HomeView size 免费推荐:" + i, ":" + videoRes.list.get(i).childList + "");
                list = videoRes.list.get(i).childList;
                for (int j = 0; j < list.size(); j++) {
                    VideoInfo videoInfo = list.get(j);
                    if (j == 0) {
                        videoInfos.add(new SectionVideoInfo(true, "免费推荐"));
                        Log.e("HomeView 免费推荐*********:" + i, ":" + videoInfos.get(0).isHeader + "");
                        videoInfos.add(new SectionVideoInfo(videoInfo));
//                        Log.e("HomeView size 免费推荐---:" + i, ":" + videoInfos.get(0).isHeader + "");
                    } else {
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    }
                }
            }

//            Log.e("HomeView size 免费推荐---:" + i, ":" + videoRes.list.get(i).childList.toString() + "");

            else if (videoRes.list.get(i).title.equals("热点资讯")) {
                Log.e("HomeView size 热点资讯:" + i, ":" + videoRes.list.get(i).childList + "");
                list = videoRes.list.get(i).childList;
                for (int j = 0; j < list.size(); j++) {
                    VideoInfo videoInfo = list.get(j);
                    SectionVideoInfo sectionVideoInfo;
                    if (j == 0) {
                        videoInfos.add(new SectionVideoInfo(true, "热点资讯"));
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    } else {
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    }
                }
//                videoInfos.addAll(videoRes.list.get(i).getChildList());
            } else if (videoRes.list.get(i).title.equals("精彩推荐")) {
                Log.e("HomeView size 精彩推荐:" + i, ":" + videoRes.list.get(i).childList + "");
                list = videoRes.list.get(i).childList;
                for (int j = 0; j < list.size(); j++) {
                    VideoInfo videoInfo = list.get(j);
                    if (j == 0) {
//                    videoInfo.header = "精彩推荐";
//                    videoInfo.isHeader = true;
                        videoInfos.add(new SectionVideoInfo(true, "精彩推荐"));
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    } else {
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    }
                }
//                videoInfos.addAll(videoRes.list.get(i).getChildList());
            } else if (videoRes.list.get(i).title.equals("好莱坞")) {
                Log.e("HomeView size 好莱坞:" + i, ":" + videoRes.list.get(i).childList + "");
                list = videoRes.list.get(i).childList;
                for (int j = 0; j < list.size(); j++) {
                    VideoInfo videoInfo = list.get(j);
                    if (j == 0) {
//                    videoInfo.header = "好莱坞";
//                    videoInfo.isHeader = true;
                        videoInfos.add(new SectionVideoInfo(true, "好莱坞"));
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    } else {
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    }
                }
//                videoInfos.addAll(videoRes.list.get(i).getChildList());
            } else if (videoRes.list.get(i).title.equals("专题")) {
                Log.e("HomeView size 专题:" + i, ":" + videoRes.list.get(i).childList + "");
                list = videoRes.list.get(i).childList;
                for (int j = 0; j < list.size(); j++) {
                    VideoInfo videoInfo = list.get(j);
                    if (j == 0) {
//                        videoInfo.header = "专题";
//                        videoInfo.isHeader = true;
                        videoInfos.add(new SectionVideoInfo(true, "专题"));
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    } else {
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    }
                }
//                videoInfos.addAll(videoRes.list.get(i).getChildList());
            } else if (videoRes.list.get(i).title.equals("直播专区")) {
                Log.e("HomeView size 直播专区:" + i, ":" + videoRes.list.get(i).childList + "");
                list = videoRes.list.get(i).childList;
                for (int j = 0; j < list.size(); j++) {
                    VideoInfo videoInfo = list.get(j);
                    if (j == 0) {
//                        videoInfo.header = "直播专区";
//                        videoInfo.isHeader = true;
                        videoInfos.add(new SectionVideoInfo(true, "直播专区"));
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    } else {
                        videoInfos.add(new SectionVideoInfo(videoInfo));
                    }
                }
//                videoInfos.addAll(videoRes.list.get(i).getChildList());
            } else {
                Log.e("HomeView size else:" + i, ":" + videoRes.list.get(i).childList + "");
                list = videoRes.list.get(i).childList;
                for (int j = 0; j < list.size(); j++) {
                    VideoInfo videoInfo = list.get(j);
//                    videoInfo.header = "直播专区";
//                    videoInfo.isHeader = true;
                    noTitleVideoInfos.add(new SectionVideoInfo(videoInfo));
                }
//                videoInfos.addAll(videoRes.list.get(i).getChildList());
            }

        }

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

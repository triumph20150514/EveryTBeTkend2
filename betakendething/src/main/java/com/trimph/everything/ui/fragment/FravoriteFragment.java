package com.trimph.everything.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trimph.everything.R;
import com.trimph.everything.base.VBaseFragment;
import com.trimph.everything.model.VideoDetail;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.ui.view.HomeViewImpl;
import com.trimph.everything.utils.GlideUtils;
import com.trimph.everything.weight.TextViewExpandableAnimation;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: Trimph
 * data: 2016/11/28.
 * description:ViewPager 下 推荐视频
 */

public class FravoriteFragment extends VBaseFragment {

    public static String TAG = "FRAVORITE_DATA";
    public Unbinder unbinder;

    TextViewExpandableAnimation tExpandable;

    @BindView(R.id.favar_recycler)
    RecyclerView favarRecycler;
    ExpandAdapter expandAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fravoritefragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }


    private void initView(View view) {

    }

    @Subscriber(tag = "FRAVORITE_DATA")
    public void showData(VideoDetail videoDetail) {
        Log.e("bus VideoDetail::", videoDetail.toString());

        View view = getActivity().getLayoutInflater().inflate(R.layout.video_info_text_title, null);
        tExpandable = (TextViewExpandableAnimation) view.findViewById(R.id.tExpandable);

        String author = "主演：" + videoDetail.getActors();
        String str = "导演：" + videoDetail.getDirector();
        String desc = "描述：" + videoDetail.getDescription();
        if (tExpandable != null)
            tExpandable.setText(author + "\n" + str + "\n" + desc);

        if (expandAdapter == null) {
            expandAdapter = new ExpandAdapter(getContext(), R.layout.home_item_view_layout,
                    videoDetail.getList().get(0).getChildList());
        }

        expandAdapter.addHeaderView(view);

        if (favarRecycler != null) {
            favarRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
            favarRecycler.setAdapter(expandAdapter);
        }

    }

    /**
     *
     */
    public class ExpandAdapter extends BaseQuickAdapter<VideoDetail.ListBean.ChildListBean> {

        public ExpandAdapter(Context context, int layoutResId, List<VideoDetail.ListBean.ChildListBean> data) {
            super(context, layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, VideoDetail.ListBean.ChildListBean childListBean) {
            baseViewHolder.setText(R.id.video_title, TextUtils.isEmpty(childListBean.getTitle()) ? "" : childListBean.getTitle());
            GlideUtils.DisplayImage(mContext, (ImageView) baseViewHolder.convertView.findViewById(R.id.video_iamge), childListBean.getPic());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}

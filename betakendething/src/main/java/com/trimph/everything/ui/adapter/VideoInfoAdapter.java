package com.trimph.everything.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimph.everything.R;
import com.trimph.everything.model.SectionVideoInfo;
import com.trimph.everything.model.VideoDetail;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.utils.GlideUtils;

import java.util.List;

/**
 * author: Trimph
 * data: 2016/12/15.
 * description:
 */

public class VideoInfoAdapter extends RecyclerView.Adapter {
    public static int TITLE_TYPE = 1;
    public static int CONTENT_VIDEO_TYPE = 0;

    public Context mContext;

    public VideoInfoAdapter(Context context) {
        this.mContext = context;
    }

    public List<VideoDetail.ListBean.ChildListBean> sectionVideoInfos;
    public View handerView;

    public List<VideoDetail.ListBean.ChildListBean> getSectionVideoInfos() {
        return sectionVideoInfos;
    }

    public void setSectionVideoInfos(List<VideoDetail.ListBean.ChildListBean> sectionVideoInfos) {
        this.sectionVideoInfos = sectionVideoInfos;
    }

    public View getHanderView() {
        return handerView;
    }

    public void setHanderView(View handerView) {
        this.handerView = handerView;
        notifyItemChanged(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TITLE_TYPE) {
            if (handerView != null) {
                return new DescriptionViewHolder(handerView);
            }
        } else if (viewType == CONTENT_VIDEO_TYPE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_item_view_layout, parent, false);
            return new VideoInfoDetailHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VideoDetail.ListBean.ChildListBean sectionVideoInfo = sectionVideoInfos.get(position);
        if (sectionVideoInfo == null) {
            return;
        }

        if (holder instanceof VideoInfoDetailHolder) {
            VideoInfoDetailHolder videoInfoDetailHolder = (VideoInfoDetailHolder) holder;

            videoInfoDetailHolder.video_title.setText(sectionVideoInfo.getTitle());
            GlideUtils.DisplayImage(mContext, videoInfoDetailHolder.video_iamge, sectionVideoInfo.getPic());
        }
    }

    @Override
    public int getItemCount() {
        return sectionVideoInfos.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (sectionVideoInfos.get(position).getType() == TITLE_TYPE) {
            return TITLE_TYPE;
        } else if (sectionVideoInfos.get(position).getType() == CONTENT_VIDEO_TYPE) {
            return CONTENT_VIDEO_TYPE;
        }
        return -100;
    }

    public class DescriptionViewHolder extends RecyclerView.ViewHolder {

        public DescriptionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class VideoInfoDetailHolder extends RecyclerView.ViewHolder {
        TextView video_title;
        ImageView video_iamge;

        public VideoInfoDetailHolder(View itemView) {
            super(itemView);
            video_title = (TextView) itemView.findViewById(R.id.video_title);
            video_iamge = (ImageView) itemView.findViewById(R.id.video_iamge);

        }
    }

}

package com.trimph.everything.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimph.everything.R;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Trimph
 * data: 2016/11/11.
 * description:
 */
public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.HomeViewHolder> {

    public Context mContext;
    public List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();


    public List<VideoInfo> getVideoInfos() {
        return videoInfos;
    }

    public void setVideoInfos(List<VideoInfo> videoInfos) {
        this.videoInfos = videoInfos;
    }

    public HomeViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        HomeViewHolder homeViewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.home_item_view_layout, parent, false);
            homeViewHolder = new HomeViewHolder(view);
            view.setTag(homeViewHolder);
        } else {
            homeViewHolder = (HomeViewHolder) view.getTag();
        }
        return homeViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        VideoInfo videoInfo = videoInfos.get(position);
        if (videoInfo==null){
            return;
        }
        holder.videoTitle.setText(videoInfo.title);
        GlideUtils.DisplayImage(mContext, holder.videoIamge, videoInfo.pic);
    }


    @Override
    public int getItemCount() {
        return videoInfos.size() == 0 ? 0 : videoInfos.size();
    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_iamge)
        ImageView videoIamge;
        @BindView(R.id.video_title)
        TextView videoTitle;

        public HomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }


}

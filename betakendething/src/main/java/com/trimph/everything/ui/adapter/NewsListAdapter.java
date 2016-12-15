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
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.utils.GlideUtils;

import java.util.List;

/**
 * Created by laucherish on 16/3/16.
 */
public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_NEWS = 0;
    public static final int ITEM_NEWS_DATE = 1;
    public static final int ITEM_NEWS_HEADER = -1;

    private Context mContext;
    private List<SectionVideoInfo> mNewsList;
    private long lastPos = -1;
    private boolean isAnim = true;

    private View headerView;
    OnItemClickListener itemClickListener;

    public NewsListAdapter(Context context, List<SectionVideoInfo> newsList) {
        this.mContext = context;
        this.mNewsList = newsList;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemChanged(0);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, SectionVideoInfo bean, int pos);
    }

    public void setOnItemListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && mNewsList.get(position).getType() == -1) {
            return ITEM_NEWS_HEADER;
        } else if (mNewsList.get(position).getType() == 0) {
            return ITEM_NEWS;
        } else if (mNewsList.get(position).getType() == 1) {
            return ITEM_NEWS_DATE;
        }
        return -100;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_NEWS_HEADER) {
            return new NewsHeaderViewHolder(headerView);
        } else if (viewType == ITEM_NEWS_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_title, parent, false);  //toubu
            return new TitleViewHolder(view);
        } else if (viewType == ITEM_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_view_layout, parent, false);
            return new VideoViewHloder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SectionVideoInfo news = mNewsList.get(position);

        if (news == null) {
            return;
        }

//        if ( (Boolean) SPUtils.get(mContext,ZhiHuApi.SET_ANIM,true)) {
//            startAnimator(holder.itemView, position);
//        }

        if (holder instanceof TitleViewHolder) {
            TitleViewHolder dateHolder = (TitleViewHolder) holder;
            String dateFormat = null;
            dateHolder.tv_type.setText(news.header + "");
//            dateFormat = DateUtil.formatDate(news.getDate());
//            dateHolder.mTvNewsDate.setText(dateFormat);

        } else if (holder instanceof NewsHeaderViewHolder) {
//            bindViewHolder(holder, position, news);
        } else if (holder instanceof VideoViewHloder) {
            bindViewHolder((VideoViewHloder) holder, position, news);
        }
    }

    private void bindViewHolder(final VideoViewHloder holder, final int position, final SectionVideoInfo news) {

        VideoInfo videoInfo = news.t;

        if (videoInfo == null) {
            return;
        }

        holder.video_title.setText(videoInfo.getTitle());

        GlideUtils.DisplayImage(mContext, holder.video_iamge, videoInfo.getPic());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, news, position);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    private void startAnimator(View view, long position) {
//        if (position > lastPos) {
//            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.item_bottom_in));
//            lastPos = position;
//        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        if (holder != null && holder instanceof NewsViewHolder && ((NewsViewHolder) holder).mCvItem != null)
//            ((NewsViewHolder) holder).mCvItem.clearAnimation();
    }

    public void changeData(List<SectionVideoInfo> newsList) {
        mNewsList = newsList;
        notifyDataSetChanged();
    }

    public void addData(List<SectionVideoInfo> newsList) {
        if (mNewsList == null) {
            changeData(newsList);
        } else {
            mNewsList.addAll(newsList);
            notifyDataSetChanged();
        }
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }

    public void setmNewsList(List<SectionVideoInfo> mNewsList) {
        this.mNewsList = mNewsList;
    }

    public List<SectionVideoInfo> getmNewsList() {
        return mNewsList;
    }


    class VideoViewHloder extends RecyclerView.ViewHolder {
        TextView video_title;
        ImageView video_iamge;

        public VideoViewHloder(View itemView) {
            super(itemView);
            video_title = (TextView) itemView.findViewById(R.id.video_title);
            video_iamge = (ImageView) itemView.findViewById(R.id.video_iamge);

        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

    class NewsHeaderViewHolder extends RecyclerView.ViewHolder {

        public NewsHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}

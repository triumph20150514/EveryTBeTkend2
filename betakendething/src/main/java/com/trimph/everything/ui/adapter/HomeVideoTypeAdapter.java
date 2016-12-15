package com.trimph.everything.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.trimph.everything.R;
import com.trimph.everything.model.SectionVideoInfo;
import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Trimph
 * data: 2016/11/24.
 * description:
 */

public class HomeVideoTypeAdapter extends BaseSectionQuickAdapter<SectionVideoInfo> {

    public List<SectionVideoInfo> infoList = new ArrayList<>();

    public HomeVideoTypeAdapter(Context context, int layoutResId, int sectionHeadResId, List<SectionVideoInfo> data) {
        super(context, layoutResId, sectionHeadResId, data);
    }

    public List<SectionVideoInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<SectionVideoInfo> list) {
        this.infoList = list;
        mData = infoList;
        notifyDataSetChanged();
    }

    public void appendList(List<SectionVideoInfo> list) {
        if (infoList != null) {
            infoList.addAll(list);
            mData = infoList;
            notifyDataSetChanged();
        }
    }


    @Override
    protected int getDefItemViewType(int position) {
        if (this.mData.size() > position) {
            Log.e("ItemType type:", (mData.get(position).isHeader) + "");
            return ((SectionEntity) mData.get(position)).isHeader ? 1092 : 0;
        } else {
            return 0;
        }
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, SectionVideoInfo sectionVideoInfo) {
        Log.e("VideoInfo::header1", sectionVideoInfo.header + "");
        if (sectionVideoInfo.isHeader) {
            Log.e("VideoInfo::header2", sectionVideoInfo.header);
            baseViewHolder.setText(R.id.tv_type, "类型：：" + sectionVideoInfo.header);
        }
    }

    /**
     * 内容
     *
     * @param baseViewHolder
     * @param videoInfo
     */
    @Override
    protected void convert(BaseViewHolder baseViewHolder, SectionVideoInfo videoInfo) {
        Log.e("HomeView convert------", ":" + "");
        if (videoInfo.t != null) {
            Log.e("HomeView BaseViewHolder", ":" + videoInfo.t.toString() + "");
            baseViewHolder.setText(R.id.video_title, TextUtils.isEmpty(videoInfo.t.title) ? "" : videoInfo.t.title);
            GlideUtils.DisplayImage(mContext, (ImageView) baseViewHolder.convertView.findViewById(R.id.video_iamge), videoInfo.t.pic);
        }
    }


    public class HeaderViewHolder extends BaseViewHolder {
        protected HeaderViewHolder(Context context, View view) {
            super(context, view);
        }
    }
}

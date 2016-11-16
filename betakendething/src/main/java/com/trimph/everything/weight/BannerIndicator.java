package com.trimph.everything.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flyco.banner.widget.Banner.BaseIndicaorBanner;
import com.orhanobut.logger.Logger;
import com.trimph.everything.R;
import com.trimph.everything.model.GankItemBean;
import com.trimph.everything.weight.indicator.RoundCornerIndicaor;

/**
 * @description: Project TrimphJuneFifty
 * Created by Trimph on
 */
public class BannerIndicator extends BaseIndicaorBanner<GankItemBean, BannerIndicator> {

    public Context mContext;

    public BannerIndicator(Context context) {
        this(context, null);
    }

    public BannerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    @Override
    public View onCreateItemView(int position) {

        Log.e("onCreateItemView url:::", list.get(0).getUrl());

        View view = View.inflate(context, R.layout.banner_item, null);
//        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_view_layout, this, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
        Glide.with(context)
                .load(list.get(position).getUrl())
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
        return view;
    }

    RoundCornerIndicaor roundCornerIndicaor;

    @Override
    public View onCreateIndicator() {
        roundCornerIndicaor = new RoundCornerIndicaor(context);
        roundCornerIndicaor.setViewPager(vp);
        return roundCornerIndicaor;
    }

    @Override
    public void setCurrentIndicator(int position) {
        Logger.e(position + "position");
//        roundCornerIndicaor.setCurrentItem(position);
        roundCornerIndicaor.setCurrentItem(position);
    }
}

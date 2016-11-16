package com.trimph.everything.weight;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.trimph.everything.R;
import com.trimph.everything.model.GankItemBean;

/**
 * author: Trimph
 * data: 2016/11/14.
 * description:
 */

public class Banner extends BaseBanner<GankItemBean, Banner> {

    public Context mContext;

    public Banner(Context context) {
        super(context);
        this.mContext = context;
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View onCreateItemView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null, false);



        return null;
    }

    @Override
    public View onCreateIndicator() {
        return null;
    }

    @Override
    public void setCurrentIndicator(int position) {

    }
}

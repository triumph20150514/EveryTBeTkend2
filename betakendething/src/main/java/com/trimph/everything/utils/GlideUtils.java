package com.trimph.everything.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trimph.everything.R;

/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public class GlideUtils {

    public static void DisplayImage(Context activity, ImageView imageView, String url) {
        Glide.with(activity)
//                 .placeholder(R.mipmap.ic_launcher)
                .load(url)
                .centerCrop()
                .error(R.mipmap.loading)
                .placeholder(R.mipmap.loading) //加载过程中显示的图片
                .into(imageView);
    }
}

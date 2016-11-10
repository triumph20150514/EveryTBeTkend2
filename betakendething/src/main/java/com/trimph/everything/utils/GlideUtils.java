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
                .load(url)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }
}

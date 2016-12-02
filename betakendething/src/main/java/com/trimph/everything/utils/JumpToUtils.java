package com.trimph.everything.utils;

import android.content.Context;
import android.content.Intent;

import com.trimph.everything.model.VideoInfo;
import com.trimph.everything.ui.activity.MainActivity;
import com.trimph.everything.ui.activity.VideoInfoActivity;

/**
 * author: Trimph
 * data: 2016/11/10.
 * description:
 */

public class JumpToUtils {

    /**
     * 跳转到主页
     * @param context
     */
    public static void JumpToMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转到视频播放界面
     * @param context
     * @param videoInfo
     */
    public static void JupmToVidoInfo(Context context, VideoInfo videoInfo) {
        Intent intent = new Intent(context, VideoInfoActivity.class);
        intent.putExtra(VideoInfo.class.getSimpleName(),videoInfo);
        context.startActivity(intent);
    }
}

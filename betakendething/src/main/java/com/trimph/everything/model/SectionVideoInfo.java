package com.trimph.everything.model;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * author: Trimph
 * data: 2016/11/25.
 * description:
 */

public class SectionVideoInfo extends SectionEntity<VideoInfo> {

    public SectionVideoInfo(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionVideoInfo(VideoInfo videoInfo) {
        super(videoInfo);
    }
}

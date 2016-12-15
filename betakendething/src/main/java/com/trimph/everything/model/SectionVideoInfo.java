package com.trimph.everything.model;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * author: Trimph
 * data: 2016/11/25.
 * description:
 */

public class SectionVideoInfo extends SectionEntity<VideoInfo> {
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public SectionVideoInfo(boolean isHeader, String header) {
        super(isHeader, header);
    }
    public SectionVideoInfo(VideoInfo videoInfo) {
        super(videoInfo);
    }
}

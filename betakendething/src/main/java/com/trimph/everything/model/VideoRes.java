package com.trimph.everything.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description:
 * Creator: yxc
 * date: $date $time
 */
public class VideoRes {
    public
    @SerializedName("list")
    List<VideoType> list;
    public String title;
    public String score;
    public String videoType;
    public String region;
    public String airTime;
    public String director;
    public String actors;
    public String pic;
    public String description;
    public String smoothURL;
    public String SDURL;
    public String HDURL;

    @Override
    public String toString() {
        return "VideoRes{" +
                "list=" + list +
                ", title='" + title + '\'' +
                ", score='" + score + '\'' +
                ", videoType='" + videoType + '\'' +
                ", region='" + region + '\'' +
                ", airTime='" + airTime + '\'' +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", pic='" + pic + '\'' +
                ", description='" + description + '\'' +
                ", smoothURL='" + smoothURL + '\'' +
                ", SDURL='" + SDURL + '\'' +
                ", HDURL='" + HDURL + '\'' +
                '}';
    }

    public List<VideoType> getList() {
        return list;
    }

    public void setList(List<VideoType> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAirTime() {
        return airTime;
    }

    public void setAirTime(String airTime) {
        this.airTime = airTime;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSmoothURL() {
        return smoothURL;
    }

    public void setSmoothURL(String smoothURL) {
        this.smoothURL = smoothURL;
    }

    public String getSDURL() {
        return SDURL;
    }

    public void setSDURL(String SDURL) {
        this.SDURL = SDURL;
    }

    public String getHDURL() {
        return HDURL;
    }

    public void setHDURL(String HDURL) {
        this.HDURL = HDURL;
    }

    public String getVideoUrl() {
        if (!TextUtils.isEmpty(HDURL))
            return HDURL;
        else if (!TextUtils.isEmpty(SDURL))
            return SDURL;
        else if (!TextUtils.isEmpty(smoothURL))
            return smoothURL;
        else
            return "";
    }
}

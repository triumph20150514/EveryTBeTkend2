package com.trimph.everything.model;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description: VideoType
 * Creator: yxc
 * date: $date $time
 */
public class VideoType {
    public String title;
    public String moreURL;
    public String pic;
    public String dataId;
    public String airTime;
    public String score;
    public String description;
    public String msg;
    public String phoneNumber;
    public String userPic;
    public String time;
    public String likeNum;
    public
    @SerializedName("childList")
    List<VideoInfo> childList;


    @Override
    public String toString() {
        return "VideoType{" +
                "title='" + title + '\'' +
                ", moreURL='" + moreURL + '\'' +
                ", pic='" + pic + '\'' +
                ", dataId='" + dataId + '\'' +
                ", airTime='" + airTime + '\'' +
                ", score='" + score + '\'' +
                ", description='" + description + '\'' +
                ", msg='" + msg + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userPic='" + userPic + '\'' +
                ", time='" + time + '\'' +
                ", likeNum='" + likeNum + '\'' +
                ", childList=" + childList +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoreURL() {
        return moreURL;
    }

    public void setMoreURL(String moreURL) {
        this.moreURL = moreURL;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getAirTime() {
        return airTime;
    }

    public void setAirTime(String airTime) {
        this.airTime = airTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public List<VideoInfo> getChildList() {
        return childList;
    }

    public void setChildList(List<VideoInfo> childList) {
        this.childList = childList;
    }
}

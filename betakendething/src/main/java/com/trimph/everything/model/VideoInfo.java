package com.trimph.everything.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.io.Serializable;

/**
 * Description: 影片详情
 * Creator: yxc
 * date: 2016/9/29 9:39
 */
public class VideoInfo implements Parcelable {
    public String title;
    public String pic;
    public String dataId;
    public String score;
    public String airTime;
    public String moreURL;
    public String loadType;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAirTime() {
        return airTime;
    }

    public void setAirTime(String airTime) {
        this.airTime = airTime;
    }

    public String getMoreURL() {
        return moreURL;
    }

    public void setMoreURL(String moreURL) {
        this.moreURL = moreURL;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public static Creator<VideoInfo> getCREATOR() {
        return CREATOR;
    }

    protected VideoInfo(Parcel in) {
        title = in.readString();
        pic = in.readString();
        dataId = in.readString();
        score = in.readString();
        airTime = in.readString();
        moreURL = in.readString();
        loadType = in.readString();
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    @Override
    public String toString() {
        return "VideoInfo{" +
                "title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", dataId='" + dataId + '\'' +
                ", score='" + score + '\'' +
                ", airTime='" + airTime + '\'' +
                ", moreURL='" + moreURL + '\'' +
                ", loadType='" + loadType + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(pic);
        dest.writeString(dataId);
        dest.writeString(score);
        dest.writeString(airTime);
        dest.writeString(moreURL);
        dest.writeString(loadType);
    }
}

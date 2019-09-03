package com.yt.moment.beans;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 点赞bean
 */
public class PraiseBean implements Parcelable {
    private String praiseUserName;
    private String praiseUserId;

    public PraiseBean() {
    }

    protected PraiseBean(Parcel in) {
        praiseUserName = in.readString();
        praiseUserId = in.readString();
    }

    public static final Creator<PraiseBean> CREATOR = new Creator<PraiseBean>() {
        @Override
        public PraiseBean createFromParcel(Parcel in) {
            return new PraiseBean(in);
        }

        @Override
        public PraiseBean[] newArray(int size) {
            return new PraiseBean[size];
        }
    };

    public String getPraiseUserName() {
        return praiseUserName;
    }

    public void setPraiseUserName(String praiseUserName) {
        this.praiseUserName = praiseUserName;
    }

    public String getPraiseUserId() {
        return praiseUserId;
    }

    public void setPraiseUserId(String praiseUserId) {
        this.praiseUserId = praiseUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(praiseUserName);
        dest.writeString(praiseUserId);
    }

}

package com.yt.moment.beans;

import android.os.Parcel;
import android.os.Parcelable;


public class OtherInfoBean implements Parcelable{

    private String source;

    private String time;

    public OtherInfoBean() {
    }

    protected OtherInfoBean(Parcel in) {
        source = in.readString();
        time = in.readString();
    }

    public static final Creator<OtherInfoBean> CREATOR = new Creator<OtherInfoBean>() {
        @Override
        public OtherInfoBean createFromParcel(Parcel in) {
            return new OtherInfoBean(in);
        }

        @Override
        public OtherInfoBean[] newArray(int size) {
            return new OtherInfoBean[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(source);
        dest.writeString(time);
    }

}

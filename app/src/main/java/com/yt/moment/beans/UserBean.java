package com.yt.moment.beans;

import android.os.Parcel;
import android.os.Parcelable;


public class UserBean implements Parcelable {
    private String userAvatarUrl;

    private String userName;

    private int userId;

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        userAvatarUrl = in.readString();
        userName = in.readString();
        userId = in.readInt();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userAvatarUrl);
        dest.writeString(userName);
        dest.writeInt(userId);
    }

}

package com.yt.moment.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;

import com.yt.moment.enums.TranslationState;
import com.yt.moment.utils.Utils;
import java.util.List;


public class FriendCircleBean implements Parcelable {

    private int viewType;

    private String content;

    private List<CommentBean> commentBeans;

    private List<PraiseBean> praiseBeans;

    private List<String> imageUrls;

    private UserBean userBean;

    private OtherInfoBean otherInfoBean;

    private boolean isShowPraise;

    private boolean isExpanded;

    private boolean isShowComment;

    private boolean isShowCheckAll;

    private TranslationState translationState = TranslationState.START;

    private SpannableStringBuilder contentSpan;

    private SpannableStringBuilder praiseSpan;

    public FriendCircleBean() {
    }

    protected FriendCircleBean(Parcel in) {
        viewType = in.readInt();
        content = in.readString();
        commentBeans = in.createTypedArrayList(CommentBean.CREATOR);
        praiseBeans = in.createTypedArrayList(PraiseBean.CREATOR);
        imageUrls = in.createStringArrayList();
        userBean = in.readParcelable(UserBean.class.getClassLoader());
        otherInfoBean = in.readParcelable(OtherInfoBean.class.getClassLoader());
        isShowPraise = in.readByte() != 0;
        isExpanded = in.readByte() != 0;
        isShowComment = in.readByte() != 0;
        isShowCheckAll = in.readByte() != 0;
    }

    public static final Creator<FriendCircleBean> CREATOR = new Creator<FriendCircleBean>() {
        @Override
        public FriendCircleBean createFromParcel(Parcel in) {
            return new FriendCircleBean(in);
        }

        @Override
        public FriendCircleBean[] newArray(int size) {
            return new FriendCircleBean[size];
        }
    };

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isShowCheckAll() {
        return isShowCheckAll;
    }

    public void setShowCheckAll(boolean showCheckAll) {
        isShowCheckAll = showCheckAll;
    }


    public void setTranslationState(TranslationState translationState) {
        this.translationState = translationState;
    }

    public TranslationState getTranslationState() {
        return translationState;
    }

    public boolean isShowComment() {
        return isShowComment;
    }

    public boolean isShowPraise() {
        return isShowPraise;
    }

    public OtherInfoBean getOtherInfoBean() {
        return otherInfoBean;
    }

    public void setOtherInfoBean(OtherInfoBean otherInfoBean) {
        this.otherInfoBean = otherInfoBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getContent() {
        return content;
    }

    public SpannableStringBuilder getContentSpan() {
        return contentSpan;
    }

    public void setContentSpan(SpannableStringBuilder contentSpan) {
        this.contentSpan = contentSpan;
        this.isShowCheckAll = Utils.calculateShowCheckAllText(contentSpan.toString());
    }


    public void setContent(String content) {
        this.content = content;
        setContentSpan(new SpannableStringBuilder(content));
    }

    public List<CommentBean> getCommentBeans() {
        return commentBeans;
    }

    public void setCommentBeans(List<CommentBean> commentBeans) {
        isShowComment = commentBeans != null && commentBeans.size() > 0;
        this.commentBeans = commentBeans;
    }

    public List<PraiseBean> getPraiseBeans() {
        return praiseBeans;
    }

    public void setPraiseBeans(List<PraiseBean> praiseBeans) {
        isShowPraise = praiseBeans != null && praiseBeans.size() > 0;
        this.praiseBeans = praiseBeans;
    }


    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setPraiseSpan(SpannableStringBuilder praiseSpan) {
        this.praiseSpan = praiseSpan;
    }

    public SpannableStringBuilder getPraiseSpan() {
        return praiseSpan;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewType);
        dest.writeString(content);
        dest.writeTypedList(commentBeans);
        dest.writeTypedList(praiseBeans);
        dest.writeStringList(imageUrls);
        dest.writeParcelable(userBean, flags);
        dest.writeParcelable(otherInfoBean, flags);
        dest.writeByte((byte) (isShowPraise ? 1 : 0));
        dest.writeByte((byte) (isExpanded ? 1 : 0));
        dest.writeByte((byte) (isShowComment ? 1 : 0));
        dest.writeByte((byte) (isShowCheckAll ? 1 : 0));
    }

}

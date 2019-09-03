package com.yt.moment.beans;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;

import com.yt.moment.Constants;
import com.yt.moment.enums.TranslationState;
import com.yt.moment.utils.SpanUtils;
public class CommentBean  implements Parcelable {

    private int commentType;

    private String parentUserName;

    private String childUserName;

    private int parentUserId;

    private int childUserId;

    private String commentContent;

    private TranslationState translationState = TranslationState.START;

    public CommentBean() {
    }

    protected CommentBean(Parcel in) {
        commentType = in.readInt();
        parentUserName = in.readString();
        childUserName = in.readString();
        parentUserId = in.readInt();
        childUserId = in.readInt();
        commentContent = in.readString();
    }

    public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
        @Override
        public CommentBean createFromParcel(Parcel in) {
            return new CommentBean(in);
        }

        @Override
        public CommentBean[] newArray(int size) {
            return new CommentBean[size];
        }
    };

    public void setTranslationState(TranslationState translationState) {
        this.translationState = translationState;
    }

    public TranslationState getTranslationState() {
        return translationState;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getParentUserName() {
        return parentUserName;
    }

    public void setParentUserName(String parentUserName) {
        this.parentUserName = parentUserName;
    }

    public String getChildUserName() {
        return childUserName;
    }

    public void setChildUserName(String childUserName) {
        this.childUserName = childUserName;
    }

    public int getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(int parentUserId) {
        this.parentUserId = parentUserId;
    }

    public int getChildUserId() {
        return childUserId;
    }

    public void setChildUserId(int childUserId) {
        this.childUserId = childUserId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }


    /**
     * 富文本内容
     */
    private SpannableStringBuilder commentContentSpan;

    public SpannableStringBuilder getCommentContentSpan() {
        return commentContentSpan;
    }

    public void build(Context context) {
        if (commentType == Constants.CommentType.COMMENT_TYPE_SINGLE) {
            commentContentSpan = SpanUtils.makeSingleCommentSpan(context, childUserName, commentContent);
        } else {
            commentContentSpan = SpanUtils.makeReplyCommentSpan(context, parentUserName, childUserName, commentContent);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentType);
        dest.writeString(parentUserName);
        dest.writeString(childUserName);
        dest.writeInt(parentUserId);
        dest.writeInt(childUserId);
        dest.writeString(commentContent);
    }

}

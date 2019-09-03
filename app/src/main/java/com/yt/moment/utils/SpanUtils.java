package com.yt.moment.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.yt.moment.span.TextClickSpan;


/**
 * @author yangtao
 * @date 2019/8/15
 */
public class SpanUtils {

    public static SpannableStringBuilder makeSingleCommentSpan(Context context, String childUserName, String commentContent) {
        String richText = String.format("%s: %s", childUserName, commentContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        if (!TextUtils.isEmpty(childUserName)) {
            builder.setSpan(new TextClickSpan(context, childUserName), 0, childUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static SpannableStringBuilder makeReplyCommentSpan(Context context, String parentUserName, String childUserName, String commentContent) {
        String richText = String.format("%s回复%s: %s", parentUserName, childUserName, commentContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        int parentEnd = 0;
        if (!TextUtils.isEmpty(parentUserName)) {
            parentEnd = parentUserName.length();
            builder.setSpan(new TextClickSpan(context, parentUserName), 0, parentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(childUserName)) {
            int childStart = parentEnd + 2;
            int childEnd = childStart + childUserName.length();
            builder.setSpan(new TextClickSpan(context, childUserName), childStart, childEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}

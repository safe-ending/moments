package com.yt.moment.span;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.yt.moment.R;


/**
 * @author yangtao
 * @date 2019/8/15
 */
public class TextClickSpan extends ClickableSpan {

    private Context mContext;

    private String mUserName;
    private boolean mPressed;

    public TextClickSpan(Context context, String userName) {
        this.mContext = context;
        this.mUserName = userName;
    }

    public void setPressed(boolean isPressed) {
        this.mPressed = isPressed;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.bgColor = mPressed ? ContextCompat.getColor(mContext, R.color.base_B5B5B5) : Color.TRANSPARENT;
        ds.setColor(ContextCompat.getColor(mContext, R.color.base_697A9F));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        //TODO 也可在这里获取到灰度的用户名
//        Toast.makeText(mContext, "You Click " + mUserName, Toast.LENGTH_SHORT).show();
    }
}

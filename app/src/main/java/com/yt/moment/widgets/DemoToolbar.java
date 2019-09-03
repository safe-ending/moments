package com.yt.moment.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;


import com.yt.moment.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DemoToolbar extends Toolbar {

    @Bind(R.id.tvTitle)
    AppCompatTextView tvTitle;

    @Bind(R.id.ivTitleRight)
    AppCompatImageView ivTitleRight;

    @Bind(R.id.ivLeft)
    AppCompatImageView ivLeft;
    @Bind(R.id.tvLeft)
    AppCompatTextView tvLeft;

    @Bind(R.id.ivRight)
    AppCompatImageView ivRight;
    @Bind(R.id.tvRight)
    AppCompatTextView tvRight;

    @Bind(R.id.line)
    View line;

    private String mTitleStr;

    public DemoToolbar(Context context) {
        super(context);
    }

    public DemoToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(getLayoutRes(), this, true);
        ButterKnife.bind(this);

        setContentInsetsAbsolute(0, 0);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DemoToolbar);
        tvTitle.setText(typedArray.getString(R.styleable.DemoToolbar_toolbar_title));
        tvRight.setText(typedArray.getString(R.styleable.DemoToolbar_toolbar_text_right));
    }


    protected int getLayoutRes() {
        return R.layout.toolbar_demo;
    }

    public AppCompatTextView getTvRight() {
        return tvRight;
    }

    public void setTvTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setTvTitle(CharSequence resId) {
        tvTitle.setText(resId);
    }

    public void setTvTitle(String strTitle) {
        tvTitle.setText(strTitle);
    }

    public void setTvLeft(int resId) {
        tvLeft.setText(resId);
    }

    public void setTvRight(int resId) {
        tvRight.setText(resId);
    }

    public void setIvLeftClickListener(OnClickListener listener) {
        ivLeft.setOnClickListener(listener);
    }

    public void setIvRight(int resId) {
        ivRight.setImageResource(resId);
    }

    public void setIvRightClickListener(OnClickListener listener) {
        ivRight.setOnClickListener(listener);
    }

    public void setTvRightClickListener(OnClickListener listener) {
        tvRight.setOnClickListener(listener);
    }


    public void setBottomLineVisible(int visible) {
        line.setVisibility(visible);
    }

    public void setShowTitleRightImage(boolean isShow) {
        ivTitleRight.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setShowTitleRightIext(boolean isShow) {
        tvRight.setVisibility(isShow ? VISIBLE : GONE);
    }
}

package com.yt.moment.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.yt.moment.utils.Utils;


public class RoundImageView extends AppCompatImageView {


    float width, height;

    public RoundImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= 12 && height > 12) {
            Path path = new Path();
            float raids = Utils.dp2px(4f);
            //四个圆角
            path.moveTo(raids, 0);
            path.lineTo(width - raids, 0);
            path.quadTo(width, 0, width, raids);
            path.lineTo(width, height - raids);
            path.quadTo(width, height, width - raids, height);
            path.lineTo(raids, height);
            path.quadTo(0, height, 0, height - raids);
            path.lineTo(0, raids);
            path.quadTo(0, 0, raids, 0);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

}
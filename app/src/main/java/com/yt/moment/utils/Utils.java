package com.yt.moment.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;
import android.widget.PopupMenu;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yt.moment.MyApplication;
import com.yt.moment.R;
import com.yt.moment.enums.TranslationState;
import com.yt.moment.interfaces.OnItemClickPopupMenuListener;
import com.yt.moment.interfaces.OnStartSwipeRefreshListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author KCrason
 * @date 2018/5/6
 */
public class Utils {

    public static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, MyApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static int getScreenWidth() {
        return MyApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return MyApplication.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    public static int calcStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static boolean calculateShowCheckAllText(String content) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(Utils.dp2px(16f));
        float textWidth = textPaint.measureText(content);
        float maxContentViewWidth = Utils.getScreenWidth() - Utils.dp2px(32f);
        float maxLines = textWidth / maxContentViewWidth;
        return maxLines > 4;
    }


    public static void showSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, OnStartSwipeRefreshListener onStartSwipeRefreshListener) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(() -> {
                swipeRefreshLayout.setRefreshing(true);
                Single.timer(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            if (onStartSwipeRefreshListener != null) {
                                onStartSwipeRefreshListener.onStartRefresh();
                            }
                        });
            });
        }
    }

    public static void hideSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        }
    }

    public static void showPopupMenu(Context context, OnItemClickPopupMenuListener onItemClickPopupMenuListener,
                                     int position, View view, TranslationState translationState) {
        if (translationState != null) {
            PopupMenu popup = new PopupMenu(context, view);
            popup.getMenuInflater().inflate(R.menu.popup_menu_start, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.copy:
                        if (onItemClickPopupMenuListener != null) {
                            onItemClickPopupMenuListener.onItemViewCopy(position);
                        }
                        break;
                    case R.id.collection:
                        if (onItemClickPopupMenuListener != null) {
                            onItemClickPopupMenuListener.onItemViewCollection(position);
                        }
                        break;

                    default:
                        break;
                }
                return true;
            });
            popup.show(); //showing popup menu
        }
    }


    public static void startAlphaAnimation(View view, boolean isShowTranslation) {
        if (isShowTranslation && view != null) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.5f, 1f);
            valueAnimator.addUpdateListener(animation -> view.setAlpha((Float) animation.getAnimatedValue()));
            valueAnimator.setDuration(500).start();
        }
    }

}

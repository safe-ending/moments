package com.yt.moment.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.yt.moment.MyApplication;
import com.yt.moment.R;
import com.yt.moment.utils.DensityBox;
import com.yt.moment.widgets.DemoToolbar;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG;

    private Window window;
    private View decorView;
    private DemoToolbar demoToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DensityBox.setCustomDensity(this, MyApplication.getInstance());
        TAG = getClass().getSimpleName();

        window = getWindow();
        decorView = window.getDecorView();

        beforeViews();

        setContentView(contentLayout());
        ButterKnife.bind(this);

        configureToolBar();

        afterViews();
    }

    @LayoutRes
    protected abstract int contentLayout();

    protected abstract void beforeViews();

    protected abstract void afterViews();

    private void configureToolBar() {
        demoToolbar = findViewById(R.id.zhigeToolbar);
        if (demoToolbar != null) {
            demoToolbar.setIvLeftClickListener(l -> onBackPressed());
        }
    }

    protected DemoToolbar getDemoToolbar() {
        return demoToolbar;
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInputMethod();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void transparentStatusBar(boolean isDarkFont) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isDarkFont) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                            .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    //window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
                }
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                        .SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }
}

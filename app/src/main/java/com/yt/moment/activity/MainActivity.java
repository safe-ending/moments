package com.yt.moment.activity;

import android.content.Intent;

import com.yt.moment.R;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Override
    protected int contentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeViews() {

    }

    @Override
    protected void afterViews() {


    }

    @OnClick(R.id.btPub)
    void clickPub(){
        Intent intent = new Intent(MainActivity.this, PublishDynamicActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btMoments)
    void clickMoments(){
        Intent intent = new Intent(MainActivity.this, MomentsActivity.class);
        startActivity(intent);
    }
}

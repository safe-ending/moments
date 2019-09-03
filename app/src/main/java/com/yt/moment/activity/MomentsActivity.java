package com.yt.moment.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.yt.moment.R;
import com.yt.moment.adapters.FriendCircleAdapter;
import com.yt.moment.beans.FriendCircleBean;
import com.yt.moment.interfaces.OnCommentItemClickListener;
import com.yt.moment.interfaces.OnFriendCircleAdapterListener;
import com.yt.moment.interfaces.OnItemClickPopupMenuListener;
import com.yt.moment.others.DataCenter;
import com.yt.moment.others.FriendsCircleAdapterDivideLine;
import com.yt.moment.others.GlideSimpleLoader;
import com.yt.moment.utils.Utils;
import com.yt.moment.widgets.imagewatcher.ImageWatcher;
import java.util.List;

import butterknife.Bind;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MomentsActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        ImageWatcher.OnPictureLongPressListener,
        OnFriendCircleAdapterListener,
        OnItemClickPopupMenuListener, OnCommentItemClickListener {

    private Disposable mDisposable;
    private FriendCircleAdapter mFriendCircleAdapter;

    @Bind(R.id.swpie_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.image_watcher)
    ImageWatcher mImageWatcher;

    @Override
    protected int contentLayout() {
        return R.layout.activity_moments;
    }

    @Override
    protected void beforeViews() {
        transparentStatusBar(true);
    }

    @Override
    protected void afterViews() {
        getDemoToolbar().setTvTitle("朋友圈");
        getDemoToolbar().setIvRight(R.mipmap.ic_add_black);
        getDemoToolbar().setIvRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MomentsActivity.this, PublishDynamicActivity.class);
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(MomentsActivity.this).resumeRequests();
                } else {
                    Glide.with(MomentsActivity.this).pauseRequests();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new FriendsCircleAdapterDivideLine());
        mFriendCircleAdapter = new FriendCircleAdapter(this, recyclerView, mImageWatcher, true);
        mFriendCircleAdapter.setFriendCircleAdapterListener(this);
        mFriendCircleAdapter.setCommentItemClickListener(this);

        recyclerView.setAdapter(mFriendCircleAdapter);
        mImageWatcher.setTranslucentStatus(Utils.calcStatusBarHeight(this));
        mImageWatcher.setErrorImageRes(R.mipmap.error_picture);
        mImageWatcher.setOnPictureLongPressListener(this);
        mImageWatcher.setLoader(new GlideSimpleLoader());
        Utils.showSwipeRefreshLayout(mSwipeRefreshLayout, this::asyncMakeData);
    }

    private List<FriendCircleBean> beans;

    private void asyncMakeData() {
        mDisposable = Single.create((SingleOnSubscribe<List<FriendCircleBean>>) emitter ->
                emitter.onSuccess(DataCenter.makeFriendCircleBeans(this)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((friendCircleBeans, throwable) -> {
                    Utils.hideSwipeRefreshLayout(mSwipeRefreshLayout);
                    if (friendCircleBeans != null && throwable == null) {
                        mFriendCircleAdapter.setFriendCircleBeans(friendCircleBeans);
                        beans = friendCircleBeans;
                    }
                });
    }

    @Override
    public void onRefresh() {
        asyncMakeData();
    }

    @Override
    public void onItemViewLocation(int position) {
        Toast.makeText(MomentsActivity.this, "定位" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemViewComments(int position) {
        Toast.makeText(MomentsActivity.this, "评论楼主" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemViewPraise(int position) {
        Toast.makeText(MomentsActivity.this, "点赞" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemViewRemove(int position) {
        Toast.makeText(MomentsActivity.this, "删除" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemViewCopy(int position) {
        Toast.makeText(MomentsActivity.this, "复制" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemViewCollection(int position) {
        Toast.makeText(MomentsActivity.this, "收藏" + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPictureLongPress(ImageView imageView, Uri uri, int i) {
        Toast.makeText(MomentsActivity.this, "长按第" + i + "张图", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        if (!mImageWatcher.handleBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


    @Override
    public void messageClick(int position) {
        Toast.makeText(MomentsActivity.this, "评论层主" + position, Toast.LENGTH_SHORT).show();

    }
}

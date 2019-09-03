package com.yt.moment.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yt.moment.R;
import com.yt.moment.adapters.PublishDynamicAdapter;
import com.yt.moment.beans.ImageUploadBean;
import com.yt.moment.others.GlideSimpleLoader;
import com.yt.moment.others.OnRecyclerItemClickListener;
import com.yt.moment.utils.Utils;
import com.yt.moment.widgets.SelectDialog;
import com.yt.moment.widgets.imagewatcher.ImageWatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class PublishDynamicActivity extends BaseActivity implements PublishDynamicAdapter.AddImageItemListener,
        ImageWatcher.OnPictureLongPressListener {
    @Bind(R.id.etContent)
    EditText etContent;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.watcher)
    ImageWatcher mImageWatcher;

    private List<ImageUploadBean> listImgData = new ArrayList<>();//展示集合
    ImageUploadBean add = new ImageUploadBean("add");//加号

    @Override
    protected int contentLayout() {
        return R.layout.activity_publish_dynamic;
    }

    @Override
    protected void beforeViews() {
        transparentStatusBar(true);
    }

    PublishDynamicAdapter adapter;

    @Override
    protected void afterViews() {
        getDemoToolbar().setTvTitle("发布动态");
        getDemoToolbar().setTvRight(R.string.publish);

        getDemoToolbar().setTvRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    Toast.makeText(PublishDynamicActivity.this, "动态内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(PublishDynamicActivity.this, "发布", Toast.LENGTH_SHORT).show();

            }
        });

        adapter = new PublishDynamicAdapter();
        adapter.onAddImageItemListener(this);
        helper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //如果item不是最后一个，则执行拖拽
                if (vh.getLayoutPosition() != listImgData.size() - 1) {
                    helper.startDrag(vh);
                } else if (!listImgData.get(listImgData.size() - 1).equals(add)) {
                    helper.startDrag(vh);
                }
            }
        });
        etContent.clearFocus();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        mImageWatcher.setTranslucentStatus(Utils.calcStatusBarHeight(this));
        mImageWatcher.setErrorImageRes(R.mipmap.error_picture);
        mImageWatcher.setOnPictureLongPressListener(this);
        mImageWatcher.setLoader(new GlideSimpleLoader());

        listImgData.add(add);
        adapter.setNewData(listImgData);
    }


    private ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //设置监听拖拽的方向
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();//得到item原来的position
            int toPosition = target.getAdapterPosition();//得到目标position
            if ((toPosition == listImgData.size() - 1 || listImgData.size() - 1 == fromPosition) && listImgData.get(listImgData.size() - 1).equals(add)) {
                return true;
            }
            //滑动事件
            Collections.swap(listImgData, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public boolean isLongPressDragEnabled() {
            //是否可拖拽
            return false;
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setScaleX(1.1f);
                viewHolder.itemView.setScaleY(1.1f);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (!recyclerView.isComputingLayout()) {
                //拖拽结束后恢复view的状态
                viewHolder.itemView.setScaleX(1.0f);
                viewHolder.itemView.setScaleY(1.0f);
            }
        }
    });


    @Override
    public void removeItem(int position) {
        selectList.remove(position);
        listImgData.remove(position);
        ls.remove(position);
        //为9张图片后删除时补回添加按钮
        if (listImgData.size() < 9 && !listImgData.contains(add)) {
            listImgData.add(add);
        }
        adapter.setNewData(listImgData);
    }

    List<Uri> ls = new ArrayList<>();//要放大的图片地址   放微信加载器需要
    SparseArray<ImageView> viewSparseArray = new SparseArray<>();//要放大的图片的图片组

    @Override
    public void zoomItem(int position) {
        //仿微信全屏预览
        for (int i = 0; i < ls.size(); i++) {
            RelativeLayout layout = (RelativeLayout) recyclerView.getChildAt(i);
            ImageView imageView = layout.findViewById(R.id.image);
            viewSparseArray.put(i, imageView);
        }
        mImageWatcher.show(position, viewSparseArray, ls);
    }


    @Override
    public void addItem() {
        hideInputMethod();
        SelectDialog.show(this, -1, new SelectDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int which) {
                switch (which) {
                    case 0:
                        PictureSelector.create(PublishDynamicActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .enableCrop(false)
                                .compress(true)
                                .selectionMode(PictureConfig.SINGLE)
                                .showCropFrame(false)
                                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                                .minimumCompressSize(300)// 小于300kb的图片不压缩
                                .forResult(PictureConfig.REQUEST_CAMERA);

                        break;
                    case 1:
                        PictureSelector.create(PublishDynamicActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .enableCrop(false)
                                .compress(true)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .showCropFrame(false)
                                .selectionMedia(selectList)
                                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                                .minimumCompressSize(300)// 小于300kb的图片不压缩
                                .maxSelectNum(9)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                }
            }
        }, "拍照", "从相册中选择");
    }

    List<LocalMedia> selectList = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : list) {
                        listImgData.remove(add);
                        selectList.add(media);
                        ImageUploadBean bean = new ImageUploadBean();
                        bean.originPic = media.getPath();
                        bean.compressPic = media.getCompressPath();

                        listImgData.add(bean);
                        listImgData.add(add);
                    }
                    if (listImgData.size() > 9 && listImgData.contains(add)) {
                        listImgData.remove(add);
                    }
                    ls.clear();
                    for (ImageUploadBean url : listImgData) {
                        if (!url.equals(add)) {
                            ls.add(Uri.parse(url.originPic));
                        }
                    }
                    adapter.setNewData(listImgData);

                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    listImgData.clear();

                    for (LocalMedia media : selectList) {
                        listImgData.remove(add);
                        ImageUploadBean bean = new ImageUploadBean();
                        bean.originPic = media.getPath();
                        bean.compressPic = media.getCompressPath();

                        listImgData.add(bean);
                        listImgData.add(add);
                    }
                    if (listImgData.size() > 9 && listImgData.contains(add)) {
                        listImgData.remove(add);
                    }
                    ls.clear();
                    for (ImageUploadBean url : listImgData) {
                        if (!url.equals(add)) {
                            ls.add(Uri.parse(url.originPic));
                        }
                    }
                    adapter.setNewData(listImgData);

                    break;
            }
        }
    }

    @Override
    public void onPictureLongPress(ImageView imageView, Uri uri, int i) {

    }

    @Override
    public void onBackPressed() {
        if (!mImageWatcher.handleBackPressed()) {
            super.onBackPressed();
        }
    }
}

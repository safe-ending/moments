package com.yt.moment.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yt.moment.R;
import com.yt.moment.beans.ImageUploadBean;
import com.yt.moment.utils.Utils;
import com.yt.moment.widgets.RoundImageView;

/**
 * @author yangtao
 * @date 2019/8/19
 */
public class PublishDynamicAdapter extends BaseQuickAdapter<ImageUploadBean, BaseViewHolder> {

    private AddImageItemListener addImageItemListener;
    private ImageUploadBean addBean = new ImageUploadBean("add");

    public PublishDynamicAdapter() {
        super(R.layout.item_recycler_public_dynamic);
    }

    public void onAddImageItemListener(AddImageItemListener addImageItemListener) {
        this.addImageItemListener = addImageItemListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageUploadBean item) {
        RoundImageView imageView = (RoundImageView) helper.getView(R.id.image);
        int itemSize = (Utils.getScreenWidth() - 6 * Utils.dp2px(2) - Utils.dp2px(32)) / 3;

        RelativeLayout layout = helper.getView(R.id.layout);
        LinearLayout.LayoutParams la = new LinearLayout.LayoutParams(itemSize, itemSize);
        la.bottomMargin = Utils.dp2px(4);
        la.leftMargin = Utils.dp2px(2);
        la.rightMargin = Utils.dp2px(2);
        layout.setLayoutParams(la);

        if (item.equals(addBean)) {
            helper.getView(R.id.llDel).setVisibility(View.GONE);
            layout.setBackgroundColor(Color.parseColor("#F5F6FA"));

            Glide.with(mContext).load(R.mipmap.ic_add_gray)
                    .apply(new RequestOptions().override(Utils.dp2px(30), Utils.dp2px(30)))
                    .into(imageView);
        } else {
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            int size = (Utils.getScreenWidth() - 2 * Utils.dp2px(4) - Utils.dp2px(32)) / 3;

            Glide.with(mContext).load(item.originPic)
                    .apply(new RequestOptions().override(size, itemSize))
                    .into(imageView);
            helper.getView(R.id.llDel).setVisibility(View.VISIBLE);
        }

        helper.getView(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.equals(addBean)) {
                    addImageItemListener.addItem();
                } else {
                    //如果item不是最后一个
                    addImageItemListener.zoomItem(helper.getAdapterPosition());
                }
            }
        });

        helper.getView(R.id.llDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageItemListener.removeItem(helper.getAdapterPosition());
            }
        });
    }

    public interface AddImageItemListener {
        //移除
        void removeItem(int position);

        //放大
        void zoomItem(int position);

        //添加
        void addItem();
    }
}
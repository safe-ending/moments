package com.yt.moment.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.yt.moment.R;
import com.yt.moment.utils.Utils;
import com.yt.moment.widgets.NineGridView;
import com.yt.moment.widgets.RoundImageView;

import java.util.List;

/**
 * @author yangtao
 * @date 2019/8/15
 */
public class NineImageAdapter implements NineGridView.NineGridAdapter<String> {

    private List<String> mImageBeans;

    private Context mContext;

    private RequestOptions mRequestOptions;

    private DrawableTransitionOptions mDrawableTransitionOptions;


    public NineImageAdapter(Context context, RequestOptions requestOptions, DrawableTransitionOptions drawableTransitionOptions, List<String> imageBeans) {
        this.mContext = context;
        this.mDrawableTransitionOptions = drawableTransitionOptions;
        this.mImageBeans = imageBeans;
        int itemSize = (Utils.getScreenWidth() - 2 * Utils.dp2px(4) - Utils.dp2px(54)) / 3;

        this.mRequestOptions = requestOptions
                .override(itemSize, itemSize)
                .placeholder(R.color.base_F2F2F2);
    }

    @Override
    public int getCount() {
        return mImageBeans == null ? 0 : mImageBeans.size();
    }

    @Override
    public String getItem(int position) {
        return mImageBeans == null ? null :
                position < mImageBeans.size() ? mImageBeans.get(position) : null;
    }

    @Override
    public View getView(int position, View itemView) {
        RoundImageView imageView;
        if (itemView == null) {
            imageView = new RoundImageView(mContext);
//            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.base_F2F2F2));
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            imageView = (RoundImageView) itemView;
        }
        String url = mImageBeans.get(position);
        if ("add".equals(url)){
            imageView.setImageResource(R.mipmap.ic_add_gray);
        }else {
            Glide.with(mContext)
                    .load(url)
                    .apply(mRequestOptions)
                    .transition(mDrawableTransitionOptions)
                    .into(imageView);
        }
        return imageView;
    }
}

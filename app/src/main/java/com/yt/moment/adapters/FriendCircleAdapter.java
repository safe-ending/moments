package com.yt.moment.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.yt.moment.Constants;
import com.yt.moment.R;
import com.yt.moment.beans.FriendCircleBean;
import com.yt.moment.beans.OtherInfoBean;
import com.yt.moment.beans.UserBean;
import com.yt.moment.enums.TranslationState;
import com.yt.moment.interfaces.OnCommentItemClickListener;
import com.yt.moment.interfaces.OnFriendCircleAdapterListener;
import com.yt.moment.span.TextMovementMethod;
import com.yt.moment.utils.Utils;
import com.yt.moment.widgets.NineGridView;
import com.yt.moment.widgets.VerticalCommentWidget;
import com.yt.moment.widgets.imagewatcher.ImageWatcher;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author yangtao
 * @date 2019/8/15
 */
public class FriendCircleAdapter extends RecyclerView.Adapter<FriendCircleAdapter.BaseFriendCircleViewHolder> {

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<FriendCircleBean> mFriendCircleBeans;

    private RequestOptions mRequestOptions;

    private int mAvatarSize;

    private DrawableTransitionOptions mDrawableTransitionOptions;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private ImageWatcher mImageWatcher;

    private OnFriendCircleAdapterListener onFriendCircleAdapterListener;

    private OnCommentItemClickListener onCommentItemClickListener;
    private boolean isShowDetails = false;

    public FriendCircleAdapter(Context context, RecyclerView recyclerView, ImageWatcher imageWatcher, boolean isShowDetails) {
        this.mContext = context;
        this.mImageWatcher = imageWatcher;
        mRecyclerView = recyclerView;
        this.mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        this.mAvatarSize = Utils.dp2px(40f);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRequestOptions = new RequestOptions().centerCrop();
        this.mDrawableTransitionOptions = DrawableTransitionOptions.withCrossFade();
        this.isShowDetails = isShowDetails;
    }

    public void setFriendCircleAdapterListener(OnFriendCircleAdapterListener listener1) {
        this.onFriendCircleAdapterListener = listener1;
    }

    public void setCommentItemClickListener(OnCommentItemClickListener listener2) {
        this.onCommentItemClickListener = listener2;
    }

    public void setFriendCircleBeans(List<FriendCircleBean> friendCircleBeans) {
        this.mFriendCircleBeans = friendCircleBeans;
        notifyDataSetChanged();
    }

    public void addFriendCircleBeans(List<FriendCircleBean> friendCircleBeans) {
        if (friendCircleBeans != null) {
            if (mFriendCircleBeans == null) {
                mFriendCircleBeans = new ArrayList<>();
            }
            this.mFriendCircleBeans.addAll(friendCircleBeans);
            notifyItemRangeInserted(mFriendCircleBeans.size(), friendCircleBeans.size());
        }
    }

    @Override
    public BaseFriendCircleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.FriendCircleType.FRIEND_CIRCLE_TYPE_ONLY_WORD) {
            //全文字
            return new OnlyWordViewHolder(mLayoutInflater.inflate(R.layout.item_recycler_firend_circle_only_word, parent, false));
        } else if (viewType == Constants.FriendCircleType.FRIEND_CIRCLE_TYPE_WORD_AND_URL) {
            //分享的预留
            return new WordAndUrlViewHolder(mLayoutInflater.inflate(R.layout.item_recycler_firend_circle_word_and_url, parent, false));
        } else if (viewType == Constants.FriendCircleType.FRIEND_CIRCLE_TYPE_WORD_AND_IMAGES) {
            //文字加图
            return new WordAndImagesViewHolder(mLayoutInflater.inflate(R.layout.item_recycler_firend_circle_word_and_images, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseFriendCircleViewHolder holder, int position) {
        if (holder != null && mFriendCircleBeans != null && position < mFriendCircleBeans.size()) {
            FriendCircleBean friendCircleBean = mFriendCircleBeans.get(position);
            makeUserBaseData(holder, friendCircleBean, position);
            if (holder instanceof OnlyWordViewHolder) {
                OnlyWordViewHolder onlyWordViewHolder = (OnlyWordViewHolder) holder;
            } else if (holder instanceof WordAndUrlViewHolder) {
                WordAndUrlViewHolder wordAndUrlViewHolder = (WordAndUrlViewHolder) holder;
                wordAndUrlViewHolder.layoutUrl.setOnClickListener(v -> Toast.makeText(mContext, "You Click Layout Url", Toast.LENGTH_SHORT).show());
            } else if (holder instanceof WordAndImagesViewHolder) {
                WordAndImagesViewHolder wordAndImagesViewHolder = (WordAndImagesViewHolder) holder;
                List<Uri> ls = new ArrayList<>();
                for (String url : friendCircleBean.getImageUrls()){
                    ls.add(Uri.parse(url));
                }
                //图片组点击
                wordAndImagesViewHolder.nineGridView.setOnImageClickListener((position1, view) ->
                        mImageWatcher.show((ImageView) view, wordAndImagesViewHolder.nineGridView.getImageViews(),
                                ls));

                wordAndImagesViewHolder.nineGridView.setAdapter(new NineImageAdapter(mContext, mRequestOptions,
                        mDrawableTransitionOptions, friendCircleBean.getImageUrls()));
            }
        }
    }

    /**
     * 公用模块
     *
     * @param holder
     * @param friendCircleBean
     * @param position
     */
    private void makeUserBaseData(BaseFriendCircleViewHolder holder, FriendCircleBean friendCircleBean, int position) {
        friendCircleBean.setContentSpan(new SpannableStringBuilder(friendCircleBean.getContent()));
        holder.txtContent.setText(friendCircleBean.getContentSpan());
        setContentShowState(holder, friendCircleBean);
        holder.txtContent.setOnLongClickListener(v -> {
            TranslationState translationState = friendCircleBean.getTranslationState();
            //文字过长可收起 默认4行内不隐藏
            if (translationState == TranslationState.END) {
                Utils.showPopupMenu(mContext, onFriendCircleAdapterListener, position, v, TranslationState.END);
            } else {
                Utils.showPopupMenu(mContext, onFriendCircleAdapterListener, position, v, TranslationState.START);
            }
            return true;
        });

        UserBean userBean = friendCircleBean.getUserBean();
        if (userBean != null) {
            holder.txtUserName.setText(userBean.getUserName());
            Glide.with(mContext).load(userBean.getUserAvatarUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //避免加载圆形图第一次失败
                            holder.imgAvatar.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .apply(mRequestOptions.override(mAvatarSize, mAvatarSize).placeholder(R.mipmap.default_header))
                    .transition(mDrawableTransitionOptions)
                    .into(holder.imgAvatar);
        }

        OtherInfoBean otherInfoBean = friendCircleBean.getOtherInfoBean();

        if (otherInfoBean != null) {
            holder.txtSource.setText(otherInfoBean.getSource());
            holder.txtPublishTime.setText(otherInfoBean.getTime());
        }

        holder.layoutPraiseAndComment.setVisibility(View.GONE);
        if (isShowDetails) {
            holder.layoutPraiseAndComment.setVisibility(View.VISIBLE);
            holder.viewLine.setVisibility(View.GONE);
            holder.txtPraiseContent.setVisibility(View.GONE);

            if (friendCircleBean.isShowComment()) {
                holder.verticalCommentWidget.setVisibility(View.VISIBLE);
                if (onCommentItemClickListener != null) {
                    holder.verticalCommentWidget.setCommentItemClickListener(onCommentItemClickListener);
                }
                if (onFriendCircleAdapterListener != null) {
                    holder.verticalCommentWidget.setItemClickPopupMenuListener(onFriendCircleAdapterListener);
                }
                holder.verticalCommentWidget.addComments(friendCircleBean.getCommentBeans(), false);
            } else {
                holder.verticalCommentWidget.setVisibility(View.GONE);
            }

        }
        holder.txtLocation.setOnClickListener(v -> {
            onFriendCircleAdapterListener.onItemViewLocation(position);
        });
        holder.txtPraise.setOnClickListener(v -> {
            onFriendCircleAdapterListener.onItemViewPraise(position);
        });

        holder.txtComment.setOnClickListener(v -> {
            onFriendCircleAdapterListener.onItemViewComments(position);
        });

        holder.txtDelete.setOnClickListener(v -> {
            onFriendCircleAdapterListener.onItemViewRemove(position);
        });
    }

    private void setTextState(BaseFriendCircleViewHolder holder, boolean isExpand) {
        if (isExpand) {
            holder.txtContent.setMaxLines(Integer.MAX_VALUE);
            holder.txtState.setText("收起");
        } else {
            holder.txtContent.setMaxLines(4);
            holder.txtState.setText("全文");
        }
    }


    private void setContentShowState(BaseFriendCircleViewHolder holder, FriendCircleBean friendCircleBean) {
        if (friendCircleBean.isShowCheckAll()) {
            holder.txtState.setVisibility(View.VISIBLE);
            setTextState(holder, friendCircleBean.isExpanded());
            holder.txtState.setOnClickListener(v -> {
                if (friendCircleBean.isExpanded()) {
                    friendCircleBean.setExpanded(false);
                } else {
                    friendCircleBean.setExpanded(true);
                }
                setTextState(holder, friendCircleBean.isExpanded());
            });
        } else {
            holder.txtState.setVisibility(View.GONE);
            holder.txtContent.setMaxLines(Integer.MAX_VALUE);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mFriendCircleBeans.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mFriendCircleBeans == null ? 0 : mFriendCircleBeans.size();
    }

    static class WordAndImagesViewHolder extends BaseFriendCircleViewHolder {

        NineGridView nineGridView;

        public WordAndImagesViewHolder(View itemView) {
            super(itemView);
            nineGridView = itemView.findViewById(R.id.nine_grid_view);
        }
    }


    static class WordAndUrlViewHolder extends BaseFriendCircleViewHolder {

        LinearLayout layoutUrl;

        public WordAndUrlViewHolder(View itemView) {
            super(itemView);
            layoutUrl = itemView.findViewById(R.id.layout_url);
        }
    }

    static class OnlyWordViewHolder extends BaseFriendCircleViewHolder {

        public OnlyWordViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class BaseFriendCircleViewHolder extends RecyclerView.ViewHolder {

        public VerticalCommentWidget verticalCommentWidget;
        public TextView txtUserName;
        public View viewLine;
        public TextView txtPraiseContent;
        public CircleImageView imgAvatar;
        public TextView txtSource;
        public TextView txtPublishTime;
        public ImageView imgPraiseOrComment;
        public TextView txtLocation;
        public TextView txtContent;
        public TextView txtState;
        public LinearLayout layoutTranslation;
        public TextView txtTranslationContent;
        public View divideLine;
        public ImageView translationTag;
        public TextView translationDesc;
        public LinearLayout layoutPraiseAndComment;

        public TextView txtPraise;
        public TextView txtComment;
        public TextView txtDelete;

        public BaseFriendCircleViewHolder(View itemView) {
            super(itemView);
            verticalCommentWidget = itemView.findViewById(R.id.vertical_comment_widget);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtPraiseContent = itemView.findViewById(R.id.praise_content);
            viewLine = itemView.findViewById(R.id.view_line);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtSource = itemView.findViewById(R.id.txt_source);
            txtPublishTime = itemView.findViewById(R.id.txt_publish_time);
            imgPraiseOrComment = itemView.findViewById(R.id.img_click_praise_or_comment);
            txtLocation = itemView.findViewById(R.id.txt_location);
            txtContent = itemView.findViewById(R.id.txt_content);
            txtState = itemView.findViewById(R.id.txt_state);
            txtTranslationContent = itemView.findViewById(R.id.txt_translation_content);
            layoutTranslation = itemView.findViewById(R.id.layout_translation);
            layoutPraiseAndComment = itemView.findViewById(R.id.layout_praise_and_comment);
            divideLine = itemView.findViewById(R.id.view_divide_line);
            translationTag = itemView.findViewById(R.id.img_translating);
            translationDesc = itemView.findViewById(R.id.txt_translation_desc);
            txtPraiseContent.setMovementMethod(new TextMovementMethod());

            txtPraise = itemView.findViewById(R.id.txt_praise);
            txtComment = itemView.findViewById(R.id.txt_comment);
            txtDelete = itemView.findViewById(R.id.txt_delete);

        }
    }
}

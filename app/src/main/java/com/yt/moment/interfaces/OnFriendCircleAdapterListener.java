package com.yt.moment.interfaces;

/**
 * 列表中的点赞评论删除  继承 OnItemClickPopupMenuListener  弹窗中的复制收藏属性
 */
public interface OnFriendCircleAdapterListener extends  OnItemClickPopupMenuListener{

    void onItemViewLocation(int position);

    void onItemViewComments(int position);

    void onItemViewPraise(int position);

    void onItemViewRemove(int position);

}

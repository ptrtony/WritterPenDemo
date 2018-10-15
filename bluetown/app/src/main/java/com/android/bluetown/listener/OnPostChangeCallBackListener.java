package com.android.bluetown.listener;

import com.android.bluetown.bean.PostBean;

/**
 * 帖子改变的监听器
 * 
 * @author shenyz
 * 
 */
public interface OnPostChangeCallBackListener {
	/**
	 * 帖子跟帖数量改变的时候（被收藏，点赞，关注、评论、加楼主为好友）
	 * 
	 * @param position
	 * @param post
	 */
	public void onPostCommentChange(int position, PostBean post);

}

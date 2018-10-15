package com.android.bluetown.listener;

import com.android.bluetown.bean.CircleBean;

/**
 * 圈子改变的监听器
 * 
 * @author shenyz
 * 
 */
public interface OnCircleChangeCallBackListener {
	/**
	 * 圈子改变的时候（被收藏，点赞，关注之后）
	 * 
	 * @param position
	 * @param circle
	 */
	public void onCircleChange(int position, CircleBean circle);

}

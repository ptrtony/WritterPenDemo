package com.android.bluetown.listener;

import com.android.bluetown.bean.RecommendDish;

/**
 * 
 * 
 * @author shenyz
 * 
 */
public interface OnFoodCallBackListener {
	/**
	 * 添加菜的数量
	 * 
	 * @param dish
	 */
	public void addFoodListener(RecommendDish dish, int operation);

	/**
	 * 删除菜的数量
	 * 
	 * @param dish
	 */
	public void minusFoodListener(RecommendDish dish, int operation);

	/**
	 * 清除某一道菜
	 * 
	 * @param dish
	 */
	public void dleFoodListener(RecommendDish dish);

}

package com.android.bluetown.listener;

import com.android.bluetown.result.OrderListResult.Order;

/**
 * 
 * 订单操作
 * 
 * @author shenyz
 * 
 */
public interface OrderCallBackListener {
	/**
	 * 订单状态改变
	 * 
	 * @param position 当前订单的索引
	 * @param order 订单
	 */
	void onOrderStatusChange(int position, Order order);

}

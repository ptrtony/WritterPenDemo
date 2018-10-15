package com.android.bluetown.bean;

/**
 * 订单参数
 * 
 * @author shenyz
 * 
 */
public class OrderParams {
	/**
	 * 全部
	 */
	public static final String ORDER_ALL = "0";
	/**
	 * 交易中
	 */
	public static final String ORDER_TRADE = "1";
	/**
	 * 已关闭
	 */
	public static final String ORDER_CLOSED = "2";
	/**
	 * 已完成
	 */
	public static final String ORDER_FINISHED = "3";
	
	
	/**
	 * 取消订单
	 */
	public static final int CANCEL_ORDER = 1;
	
	/**
	 * 支付成功
	 */
	public static final int PAY_SUCCESS = 2;
	
	

}

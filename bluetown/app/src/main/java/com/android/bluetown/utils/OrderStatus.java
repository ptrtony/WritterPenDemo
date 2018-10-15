package com.android.bluetown.utils;

public class OrderStatus {
	// 订单状态（0未付款，1已付款，2已完成，3已关闭，4退款）
	/**
	 * 订单状态 - 0未付款
	 */
	public static final int STATUS_UNPAY = 0;
	/**
	 * 订单状态 - 1已付款
	 */
	public static final int STATUS_PAID = 1;
	/**
	 * /** 订单状态 - 已完成
	 */
	public static final int STATUS_FINISHED = 2;
	/**
	 * 订单状态 - 已关闭
	 */
	public static final int STATUS_CLOSED = 3;
	/**
	 * 退款
	 */
	public static final int STATUS_BACKMONEY = 4;
	// 未支付
	public static final int ST_UNPAID = 0;
	// 已支付
	public static final int ST_PAID = 1;
	// 已完成
	public static final int ST_FINISHED = 2;
	// 已关闭
	public static final int ST_CLOSED = 3;
	/**
	 * 退款
	 */
	public static final int ST_BACKMOENY = 4;

	/**
	 * 订单类型 - 预定
	 */
	public static final int TYPE_SCHEDULE = 10;
	/**
	 * 订单类型 - 点菜
	 */
	public static final int TYPE_DISHES = 21;

	public int convertServerStatus(int status) {
		switch (status) {
		case ST_UNPAID:
			return OrderStatus.STATUS_UNPAY;
		case ST_PAID:
			return OrderStatus.STATUS_PAID;
		case ST_FINISHED:
			return OrderStatus.STATUS_FINISHED;
		default:
			return OrderStatus.STATUS_CLOSED;
		}
	}

	public String convertLocalStatus(int[] statuses) {
		if (statuses != null && statuses.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < statuses.length; i++) {
				switch (statuses[i]) {
				case OrderStatus.STATUS_UNPAY:
					buffer.append(Integer.toString(ST_UNPAID));
					break;
				case OrderStatus.STATUS_PAID:
					buffer.append(Integer.toString(ST_PAID));
					break;
				case OrderStatus.STATUS_FINISHED:
					buffer.append(Integer.toString(ST_FINISHED));
					break;
				default:
					buffer.append(Integer.toString(ST_CLOSED));
					break;
				}
				if (i < statuses.length - 1) {
					buffer.append(",");
				}
			}
		}
		return null;
	}

}

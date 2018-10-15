package com.android.bluetown.result;
/**
 * 下订单
 * @author shenyz
 *
 */
public class PlaceOrderResult extends Result {
	private PlaceOrder data;

	public PlaceOrder getData() {
		return data;
	}

	public void setData(PlaceOrder data) {
		this.data = data;
	}

	public class PlaceOrder {
		private String orderNum;

		public String getOrderNum() {
			return orderNum;
		}

		public void setOrderNum(String orderNum) {
			this.orderNum = orderNum;
		}

	}

}

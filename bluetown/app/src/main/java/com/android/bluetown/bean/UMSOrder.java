package com.android.bluetown.bean;

public class UMSOrder {
	public static final int UMS_STATUS_NEW = 0;
	public static final int UMS_STATUS_SUCCESS = 1;
	public static final int UMS_STATUS_FAILD = 2;
	public static final int UMS_STATUS_NOW = 3;
	public String MerOrderUuid;
	public String ChrCode;
	public String Sign;
	public String TransId;
	public String MerId;
	public String mchantUserCode;
	// TransState：订单状态0:新订单、1 成功、2 失败、3 支付中
	public String TransState;
}
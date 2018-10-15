package com.android.bluetown.bean;

//cid:订单id
//createTime：创建时间
//orderStatus:0未付款，1已付款，2已完成，3已关闭，4退款
//orderTag:  退单状态（0未退单，1已退单，2申请退单， 3退单中，4拒绝退单，默认是0）
//(当orderStatus =1  时 backTag=0 显示 已付款，    backTag=1 显示退款成功 backTag=2 显示申请退单    backTag=3 显示退单中    backTag=4 显示拒绝退单 )
//tableNum:餐桌的类型
//orderTime:预约时间
//amount:金额
//orderType:订单类型（0只订座，1订座订菜）
public class OrderModel {

	private String cid;
	private String createTime;
	private String orderStatus;
	private String orderTag;
	private String tableNum;
	private String orderTime;
	private String amount;
	private String orderType;
	private String merchantName;
	private String backTag;
	

	public String getBackTag() {
		return backTag;
	}

	public void setBackTag(String backTag) {
		this.backTag = backTag;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderTag() {
		return orderTag;
	}

	public void setOrderTag(String orderTag) {
		this.orderTag = orderTag;
	}

	public String getTableNum() {
		return tableNum;
	}

	public void setTableNum(String tableNum) {
		this.tableNum = tableNum;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

}

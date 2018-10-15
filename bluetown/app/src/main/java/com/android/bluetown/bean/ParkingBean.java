package com.android.bluetown.bean;

import java.io.Serializable;

public class ParkingBean implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;
	// 主键
	public String poid;
	// 所属园区
	public String garden;
	// 停车场名称
	public String parkingName;
	// 停车场名称
	public String parkingNo;
	// 停车场ID
	public String parkingId;
	// 区域
	public String region;
	// 车位位置
	public String parkingSpace;
	// 车位类型
	public String parkingType;
	// 开始时间
	public String startTime;
	// 结束时间
	public String endTime;
	// 包月时长
	public String mouthNumber;
	// 车牌号
	public String carNumber;
	// 金额
	public Double amount;
	// 订单编号
	public String orderNum;
	// 订单类型:0食堂消费、1停车费、2商户消费、3其他
	public String orderType;
	// 订单类型字符
	public String orderTypeStr;
	// 交易类型：0充值，1支付
	public String tradeType;
	// 交易类型字符
	public String tradeTypeStr;
	// 支付方式（0现金，1支付宝，2微信，3平台支付）
	public String paymentType;
	// 支付方式字符
	public String paymentTypeStr;
	// 客户ID
	public String userId;
	// 客户名称
	public String userName;
	// 联系电话
	public String phoneNumber;
	//是否续包(0未续包，1已续包)
	public String isXubao;

	// 订单状态:（0未付款，1已付款，2已生效，3已关闭，4已退款）
	public String orderStatus;
	// 订单状态字符
	public String orderStatusStr;
	// 支付流水号
	public String paymentNum;
	// 创建时间
	public String createTime;
	// 修改时间
	public String updateTime;
	// 是否删除
	public String isDelete;

	// 关联固定车订单编号
	public String relationOrderNum;
	
	public String getIsXubao() {
		return isXubao;
	}

	public void setIsXubao(String isXubao) {
		this.isXubao = isXubao;
	}

	public String getParkingId() {
		return parkingId;
	}

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}

	public String getParkingNo() {
		return parkingNo;
	}

	public void setParkingNo(String parkingNo) {
		this.parkingNo = parkingNo;
	}

	public String getRelationOrderNum() {
		return relationOrderNum;
	}

	public void setRelationOrderNum(String relationOrderNum) {
		this.relationOrderNum = relationOrderNum;
	}

	public String getPoid() {
		return poid;
	}

	public void setPoid(String poid) {
		this.poid = poid;
	}

	public String getGarden() {
		return garden;
	}

	public void setGarden(String garden) {
		this.garden = garden;
	}

	public String getParkingName() {
		return parkingName;
	}

	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getParkingSpace() {
		return parkingSpace;
	}

	public void setParkingSpace(String parkingSpace) {
		this.parkingSpace = parkingSpace;
	}

	public String getParkingType() {
		return parkingType;
	}

	public void setParkingType(String parkingType) {
		this.parkingType = parkingType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMouthNumber() {
		return mouthNumber;
	}

	public void setMouthNumber(String mouthNumber) {
		this.mouthNumber = mouthNumber;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderTypeStr() {
		return orderTypeStr;
	}

	public void setOrderTypeStr(String orderTypeStr) {
		this.orderTypeStr = orderTypeStr;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeTypeStr() {
		return tradeTypeStr;
	}

	public void setTradeTypeStr(String tradeTypeStr) {
		this.tradeTypeStr = tradeTypeStr;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentTypeStr() {
		return paymentTypeStr;
	}

	public void setPaymentTypeStr(String paymentTypeStr) {
		this.paymentTypeStr = paymentTypeStr;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusStr() {
		return orderStatusStr;
	}

	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}

	public String getPaymentNum() {
		return paymentNum;
	}

	public void setPaymentNum(String paymentNum) {
		this.paymentNum = paymentNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
}
package com.android.bluetown.bean;

import java.io.Serializable;

/**
 * @author hedi
 * @data: 2016年5月5日 下午2:33:29
 * @Description: 账单列表实例
 */
public class BillBean implements Serializable {
	// ID
	public String bid;
	// 账单编号
	public String transactionNumber;
	// 用户交易类型：0充值，1支付，2退款,3收款,4转账
	public String tradeType;
	// 用户交易类型字符
	public String tradeTypeStr;
	// 商户交易类型：1退款,2提现,3收款，4转账
	public String tradeTypeM;
	// 商户交易类型字符
	public String tradeTypeStrM;
	// 交易方式：0现金支付，1支付宝，2微信，3平台支付,4银行转账
	public String payType;
	// 交易方式字符
	public String payTypeStr;
	// 订单ID
	public String orderId;
	// 订单编号
	public String orderNumber;
	// 账单状态:0食堂消费、1停车费、2商户消费、3充值、4提现、5退款、6转账、7其他
	public String billStatus;
	// 账单状态字符
	public String billStatusStr;
	// 金额
	public Double amount;
	// 商户ID
	public String merchantId;
	// 商户名称
	public String merchantName;
	// 客户ID
	public String customerId;
	// 客户名称
	public String customerName;
	// 账单状态：0未付款，1已付款，2已完成，3已关闭，4退款
	public String billType;
	// 账单状态字符
	public String billTypeStr;
	// 二维码信息
	public String QRCode;
	// 支付时间
	public String payTime;
	// 创建时间
	public String createTime;
	// 更新时间
	public String updateTime;
	// 支付流水号
	public String paymentNum;
	// 是否删除（用户）
	public String isDelete;

	private String month;
	
	public String balance;
	// 商品信息
	public String commodityInformation;
	
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCommodityInformation() {
		return commodityInformation;
	}

	public void setCommodityInformation(String commodityInformation) {
		this.commodityInformation = commodityInformation;
	}

	public String getTradeTypeM() {
		return tradeTypeM;
	}

	public void setTradeTypeM(String tradeTypeM) {
		this.tradeTypeM = tradeTypeM;
	}

	public String getTradeTypeStrM() {
		return tradeTypeStrM;
	}

	public void setTradeTypeStrM(String tradeTypeStrM) {
		this.tradeTypeStrM = tradeTypeStrM;
	}

	public String getPaymentNum() {
		return paymentNum;
	}

	public void setPaymentNum(String paymentNum) {
		this.paymentNum = paymentNum;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTypeStr() {
		return payTypeStr;
	}

	public void setPayTypeStr(String payTypeStr) {
		this.payTypeStr = payTypeStr;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getBillStatusStr() {
		return billStatusStr;
	}

	public void setBillStatusStr(String billStatusStr) {
		this.billStatusStr = billStatusStr;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillTypeStr() {
		return billTypeStr;
	}

	public void setBillTypeStr(String billTypeStr) {
		this.billTypeStr = billTypeStr;
	}

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
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

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}

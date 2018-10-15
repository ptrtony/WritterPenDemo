package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.SeleteDish;

/**
 * 订单列表
 */
public class OrderListResult extends Result {
	private OrderListData data;
	
	public OrderListData getData() {
		return data;
	}

	public void setData(OrderListData data) {
		this.data = data;
	}

	public class OrderListData extends Data {
		private List<Order> rows;

		/**
		 * @Title: getRows <BR>
		 * @Description: please write your description <BR>
		 * @return: List<ActionCenterItemBean> <BR>
		 */

		public List<Order> getRows() {
			return rows;
		}

		/**
		 * @Title: setRows <BR>
		 * @Description: please write your description <BR>
		 * @Param:rows
		 */

		public void setRows(List<Order> rows) {
			this.rows = rows;
		}
	}

	/**
	 * cid:订单idcreateTime：创建时间orderStatus:0未付款，1已付款，2已完成，3已关闭，4退款tableNum:
	 * 餐桌的类型orderTime:预约时间amount:金额
	 * 
	 * @author shenyz
	 * 
	 */
	public class Order {
		private List<SeleteDish> menuList;
		private String merchantTell;
		private String merchantName;
		private String merchantAddress;
		private String type;
		private String description;
		private String userId;
		private String cid;
		private String createTime;
		private String orderType;
		private String amount;
		private String orderRemark;
		private String orderStatus;
		private String contactName;
		private String depositOrderNum;
		private String depositType;
		private String completeTime;
		private String backDescription;
		private String closeReasonFlag;
		private String refuseReason;
		private String merchantId;
		private String orderNum;
		private String backTime;
		private String tableNum;
		private String contactTel;
		private String orderTime;
		private String verifyCode;
		private String backReson;
		private String backTag;
		private String orderTag;
		private String contactSex;
		private String payTime;
		// 订单状态 0 未接单 1已接单 2拒绝接单
		private String status;
	
		/**
		 * menuList:订菜列表 （dishesCount：份数， dishesPrice：单价 dishesName：菜名）
		 * 
		 * @author shenyz
		 * 
		 */
		
		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public List<SeleteDish> getMenuList() {
			return menuList;
		}

		public void setMenuList(List<SeleteDish> menuList) {
			this.menuList = menuList;
		}

		public String getMerchantTell() {
			return merchantTell;
		}

		public void setMerchantTell(String merchantTell) {
			this.merchantTell = merchantTell;
		}

		public String getMerchantName() {
			return merchantName;
		}

		public void setMerchantName(String merchantName) {
			this.merchantName = merchantName;
		}
		
		public String getMerchantAddress() {
			return merchantAddress;
		}

		public void setMerchantAddress(String merchantAddress) {
			this.merchantAddress = merchantAddress;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
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

		public String getOrderType() {
			return orderType;
		}

		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getOrderRemark() {
			return orderRemark;
		}

		public void setOrderRemark(String orderRemark) {
			this.orderRemark = orderRemark;
		}

		public String getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public String getDepositOrderNum() {
			return depositOrderNum;
		}

		public void setDepositOrderNum(String depositOrderNum) {
			this.depositOrderNum = depositOrderNum;
		}

		public String getDepositType() {
			return depositType;
		}

		public void setDepositType(String depositType) {
			this.depositType = depositType;
		}

		public String getCompleteTime() {
			return completeTime;
		}

		public void setCompleteTime(String completeTime) {
			this.completeTime = completeTime;
		}

		public String getBackDescription() {
			return backDescription;
		}

		public void setBackDescription(String backDescription) {
			this.backDescription = backDescription;
		}

		public String getCloseReasonFlag() {
			return closeReasonFlag;
		}

		public void setCloseReasonFlag(String closeReasonFlag) {
			this.closeReasonFlag = closeReasonFlag;
		}

		public String getRefuseReason() {
			return refuseReason;
		}

		public void setRefuseReason(String refuseReason) {
			this.refuseReason = refuseReason;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getOrderNum() {
			return orderNum;
		}

		public void setOrderNum(String orderNum) {
			this.orderNum = orderNum;
		}

		public String getBackTime() {
			return backTime;
		}

		public void setBackTime(String backTime) {
			this.backTime = backTime;
		}

		public String getTableNum() {
			return tableNum;
		}

		public void setTableNum(String tableNum) {
			this.tableNum = tableNum;
		}

		public String getContactTel() {
			return contactTel;
		}

		public void setContactTel(String contactTel) {
			this.contactTel = contactTel;
		}

		public String getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
		}

		public String getVerifyCode() {
			return verifyCode;
		}

		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}

		public String getBackReson() {
			return backReson;
		}

		public void setBackReson(String backReson) {
			this.backReson = backReson;
		}

		public String getBackTag() {
			return backTag;
		}

		public void setBackTag(String backTag) {
			this.backTag = backTag;
		}

		public String getOrderTag() {
			return orderTag;
		}

		public void setOrderTag(String orderTag) {
			this.orderTag = orderTag;
		}

		public String getContactSex() {
			return contactSex;
		}

		public void setContactSex(String contactSex) {
			this.contactSex = contactSex;
		}

		public String getPayTime() {
			return payTime;
		}

		public void setPayTime(String payTime) {
			this.payTime = payTime;
		}

	}

	
		

}

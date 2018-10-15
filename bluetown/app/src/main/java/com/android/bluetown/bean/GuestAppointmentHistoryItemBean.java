package com.android.bluetown.bean;

import java.io.Serializable;

public class GuestAppointmentHistoryItemBean implements Serializable{

	/** 创建时间 */
	private String createTime;
	/** 车牌 */
	private String busNumber;
	/** 访客时间 */
	private String makingTime;

	/**预约状态(1成功 0失败)*/
	private String orderType;
	private String mid;

	public GuestAppointmentHistoryItemBean() {
		// TODO Auto-generated constructor stub
	}
	
	public GuestAppointmentHistoryItemBean(String createTime, String busNumber, String makingTime, String orderType,String mid) {
		super();
		this.createTime = createTime;
		this.busNumber = busNumber;
		this.makingTime = makingTime;
		this.orderType = orderType;
		this.mid=mid;
	}



	/**  
	 * @Title:  getCreateTime <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getCreateTime() {
		return createTime;
	}

	/**  
	 * @Title:  setCreateTime <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:createTime
	 */
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**  
	 * @Title:  getBusNumber <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getBusNumber() {
		return busNumber;
	}

	/**  
	 * @Title:  setBusNumber <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:busNumber
	 */
	
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
	}

	/**  
	 * @Title:  getMakingTime <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getMakingTime() {
		return makingTime;
	}

	/**  
	 * @Title:  setMakingTime <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:makingTime
	 */
	
	public void setMakingTime(String makingTime) {
		this.makingTime = makingTime;
	}

	/**  
	 * @Title:  getOrderType <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getOrderType() {
		return orderType;
	}

	/**  
	 * @Title:  setOrderType <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:orderType
	 */
	
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**  
	 * @Title:  getMid <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getMid() {
		return mid;
	}

	/**  
	 * @Title:  setMid <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:mid
	 */
	
	public void setMid(String mid) {
		this.mid = mid;
	}

	
}

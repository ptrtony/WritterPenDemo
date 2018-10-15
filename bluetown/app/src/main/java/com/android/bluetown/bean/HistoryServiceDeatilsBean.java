package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

public class HistoryServiceDeatilsBean implements Serializable {
	private static final long serialVersionUID = 2008244649304615261L;
	private String address;
	private String state;
	private String type;
	private String time;
	private String userId;
	private List<String> pictruesList;
	private String selection;
	private String pid;
	private String rid;
	private String businessId;
	private String seatNumber;
	private String grade;
	private String tell;
	private String remark;
	private String createTime;
	private String pictures;
	private String typeName;
	private String manageDate;

	public HistoryServiceDeatilsBean() {
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getPictruesList() {
		return pictruesList;
	}

	public void setPictruesList(List<String> pictruesList) {
		this.pictruesList = pictruesList;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getTell() {
		return tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	/**  
	 * @Title:  getTypeName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getTypeName() {
		return typeName;
	}

	/**  
	 * @Title:  setTypeName <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:typeName
	 */
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	
	
	/**  
	 * @Title:  getManageDate <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getManageDate() {
		return manageDate;
	}

	/**  
	 * @Title:  setManageDate <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:manageDate
	 */
	
	public void setManageDate(String manageDate) {
		this.manageDate = manageDate;
	}

	public HistoryServiceDeatilsBean(String address, String state, String type, String time, String userId, List<String> pictruesList,
			String selection, String pid, String rid, String businessId, String seatNumber, String grade, String tell, String remark,
			String createTime, String pictures,String typeName) {
		super();
		this.address = address;
		this.state = state;
		this.type = type;
		this.time = time;
		this.userId = userId;
		this.pictruesList = pictruesList;
		this.selection = selection;
		this.pid = pid;
		this.rid = rid;
		this.businessId = businessId;
		this.seatNumber = seatNumber;
		this.grade = grade;
		this.tell = tell;
		this.remark = remark;
		this.createTime = createTime;
		this.pictures = pictures;
		this.typeName=typeName;
	}

}

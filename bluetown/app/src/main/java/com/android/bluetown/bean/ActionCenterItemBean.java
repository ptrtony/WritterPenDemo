package com.android.bluetown.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: CompanyShowItemBean
 * @Description:TODO(企业展示列表Item的javabean)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:14:32
 * 
 */
public class ActionCenterItemBean implements Serializable{
	
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */   
	
	private static final long serialVersionUID = 1L;
	private String content;
	private String state;
	private String number;
	private String endTime;
	private String startTime;
	private String aid;
	private String actionName;
	private String tell;
	private String organisers;
	private String contact;
	private String typeId;
	private String willNum;
	private String actionImg;
	private String actionAddress;
	private String releaseDate;
	private String typeName;
	private String isSignUp;
	public ActionCenterItemBean() {
		// TODO Auto-generated constructor stub
	}
	
	public ActionCenterItemBean(String content, String state, String number, String endTime, String startTime, String aid, String actionName,
			String tell, String organisers, String contact, String typeId, String willNum, String actionImg, String actionAddress, String releaseDate,String typaName,String isSignUp) {
		super();
		this.content = content;
		this.state = state;
		this.number = number;
		this.endTime = endTime;
		this.startTime = startTime;
		this.aid = aid;
		this.actionName = actionName;
		this.tell = tell;
		this.organisers = organisers;
		this.contact = contact;
		this.typeId = typeId;
		this.willNum = willNum;
		this.actionImg = actionImg;
		this.actionAddress = actionAddress;
		this.releaseDate = releaseDate;
		this.typeName=typaName;
		this.isSignUp=isSignUp;
	}

	/**
	 * @Title: getContent <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getContent() {
		return content;
	}

	/**
	 * @Title: setContent <BR>
	 * @Description: please write your description <BR>
	 * @Param:content
	 */

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @Title: getState <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getState() {
		return state;
	}

	/**
	 * @Title: setState <BR>
	 * @Description: please write your description <BR>
	 * @Param:state
	 */

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @Title: getNumber <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getNumber() {
		return number;
	}

	/**
	 * @Title: setNumber <BR>
	 * @Description: please write your description <BR>
	 * @Param:number
	 */

	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @Title: getEndTime <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getEndTime() {
		return endTime;
	}

	/**
	 * @Title: setEndTime <BR>
	 * @Description: please write your description <BR>
	 * @Param:endTime
	 */

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @Title: getStartTime <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getStartTime() {
		return startTime;
	}

	/**
	 * @Title: setStartTime <BR>
	 * @Description: please write your description <BR>
	 * @Param:startTime
	 */

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @Title: getAid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getAid() {
		return aid;
	}

	/**
	 * @Title: setAid <BR>
	 * @Description: please write your description <BR>
	 * @Param:aid
	 */

	public void setAid(String aid) {
		this.aid = aid;
	}

	/**
	 * @Title: getActionName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getActionName() {
		return actionName;
	}

	/**
	 * @Title: setActionName <BR>
	 * @Description: please write your description <BR>
	 * @Param:actionName
	 */

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @Title: getTell <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTell() {
		return tell;
	}

	/**
	 * @Title: setTell <BR>
	 * @Description: please write your description <BR>
	 * @Param:tell
	 */

	public void setTell(String tell) {
		this.tell = tell;
	}

	/**
	 * @Title: getOrganisers <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getOrganisers() {
		return organisers;
	}

	/**
	 * @Title: setOrganisers <BR>
	 * @Description: please write your description <BR>
	 * @Param:organisers
	 */

	public void setOrganisers(String organisers) {
		this.organisers = organisers;
	}

	/**
	 * @Title: getContact <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getContact() {
		return contact;
	}

	/**
	 * @Title: setContact <BR>
	 * @Description: please write your description <BR>
	 * @Param:contact
	 */

	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @Title: getTypeId <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTypeId() {
		return typeId;
	}

	/**
	 * @Title: setTypeId <BR>
	 * @Description: please write your description <BR>
	 * @Param:typeId
	 */

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * @Title: getWillNum <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getWillNum() {
		return willNum;
	}

	/**
	 * @Title: setWillNum <BR>
	 * @Description: please write your description <BR>
	 * @Param:willNum
	 */

	public void setWillNum(String willNum) {
		this.willNum = willNum;
	}

	/**
	 * @Title: getActionImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getActionImg() {
		return actionImg;
	}

	/**
	 * @Title: setActionImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:actionImg
	 */

	public void setActionImg(String actionImg) {
		this.actionImg = actionImg;
	}

	/**
	 * @Title: getActionAddress <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getActionAddress() {
		return actionAddress;
	}

	/**
	 * @Title: setActionAddress <BR>
	 * @Description: please write your description <BR>
	 * @Param:actionAddress
	 */

	public void setActionAddress(String actionAddress) {
		this.actionAddress = actionAddress;
	}

	/**
	 * @Title: getReleaseDate <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @Title: setReleaseDate <BR>
	 * @Description: please write your description <BR>
	 * @Param:releaseDate
	 */

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
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
	 * @Title:  getIsSignUp <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getIsSignUp() {
		return isSignUp;
	}

	/**  
	 * @Title:  setIsSignUp <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:isSignUp
	 */
	
	public void setIsSignUp(String isSignUp) {
		this.isSignUp = isSignUp;
	}

	
}
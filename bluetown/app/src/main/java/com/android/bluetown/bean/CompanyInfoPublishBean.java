package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: CompanyGrowHelpBean
 * @Description:TODO(CompanyGrowHelpActivity 模块的CompanyGrowHelpBean)
 * @author: shenyz
 * @date: 2015年7月21日 下午4:50:34
 * 
 */
public class CompanyInfoPublishBean implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */

	private static final long serialVersionUID = 1L;
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	public static final int DEMAND_COLLECT = 1;
	public static final int CANCEL_DEMAND_COLLECT = 2;
	private List<String> picturesList;
	private List<String> orgImgList;
	private String companyName;
	private String nickName;
	private String tellphone;
	private String content;
	private String state;
	private String endTime;
	private String userId;
	private String nid;
	private String needType;
	private String title;
	private String createTime;
	private String pictures;
	private String isCollect;
	private String bid;

	/**
	 * @Title: getPicturesList <BR>
	 * @Description: please write your description <BR>
	 * @return: List<String> <BR>
	 */

	public List<String> getPicturesList() {
		return picturesList;
	}

	/**
	 * @Title: setPicturesList <BR>
	 * @Description: please write your description <BR>
	 * @Param:picturesList
	 */

	public void setPicturesList(List<String> picturesList) {
		this.picturesList = picturesList;
	}

	public List<String> getOrgImgList() {
		return orgImgList;
	}

	public void setOrgImgList(List<String> orgImgList) {
		this.orgImgList = orgImgList;
	}

	/**
	 * @Title: getCompanyName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @Title: setCompanyName <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyName
	 */

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @Title: getNickName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getNickName() {
		return nickName;
	}

	/**
	 * @Title: setNickName <BR>
	 * @Description: please write your description <BR>
	 * @Param:nickName
	 */

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTellphone() {
		return tellphone;
	}

	public void setTellphone(String tellphone) {
		this.tellphone = tellphone;
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
	 * @Title: getUserId <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getUserId() {
		return userId;
	}

	/**
	 * @Title: setUserId <BR>
	 * @Description: please write your description <BR>
	 * @Param:userId
	 */

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @Title: getNid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getNid() {
		return nid;
	}

	/**
	 * @Title: setNid <BR>
	 * @Description: please write your description <BR>
	 * @Param:nid
	 */

	public void setNid(String nid) {
		this.nid = nid;
	}

	/**
	 * @Title: getNeedType <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getNeedType() {
		return needType;
	}

	/**
	 * @Title: setNeedType <BR>
	 * @Description: please write your description <BR>
	 * @Param:needType
	 */

	public void setNeedType(String needType) {
		this.needType = needType;
	}

	/**
	 * @Title: getTitle <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * @Title: setTitle <BR>
	 * @Description: please write your description <BR>
	 * @Param:title
	 */

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @Title: getCreateTime <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @Title: setCreateTime <BR>
	 * @Description: please write your description <BR>
	 * @Param:createTime
	 */

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @Title: getPictures <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getPictures() {
		return pictures;
	}

	/**
	 * @Title: setPictures <BR>
	 * @Description: please write your description <BR>
	 * @Param:pictures
	 */

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	/**
	 * @Title: getIsCollect <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getIsCollect() {
		return isCollect;
	}

	/**
	 * @Title: setIsCollect <BR>
	 * @Description: please write your description <BR>
	 * @Param:isCollect
	 */

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

}

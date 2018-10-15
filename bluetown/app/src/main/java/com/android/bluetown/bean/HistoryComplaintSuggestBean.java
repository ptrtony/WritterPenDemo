package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: HistoryComplaintSuggestBean
 * @Description:TODO(历史投诉建议的javabean)
 * @author: shenyz
 * @date: 2015年8月6日 下午4:18:22
 * 
 */
public class HistoryComplaintSuggestBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 【注】data的数据结构：
	// typeName:报修类型
	// picturesList:图片集;
	// content：反馈内容；
	// createTime:反馈日期；
	// manageDate：处理日期；
	// types：0:投诉，1：建议
	// userId：用户id；
	// dispose：0:未处理1:已处理；
	// sid：建议id
	// 报修类型
	private String name;
	// 图片集;
	private List<String> picturesList;
	// 反馈内容
	private String content;
	private String address;
	// 反馈日期
	private String createTime;
	private String pictures;
	// 处理日期
	private String manageDate;
	// 反馈内容
	private String types;
	// 用户id
	private String userId;
	// 0:未处理1:已处理；
	private String dispose;
	// 建议id
	private String sid;

	public HistoryComplaintSuggestBean() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPicturesList() {
		return picturesList;
	}

	public void setPicturesList(List<String> picturesList) {
		this.picturesList = picturesList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getManageDate() {
		return manageDate;
	}

	public void setManageDate(String manageDate) {
		this.manageDate = manageDate;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDispose() {
		return dispose;
	}

	public void setDispose(String dispose) {
		this.dispose = dispose;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}

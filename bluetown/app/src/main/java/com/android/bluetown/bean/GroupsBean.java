package com.android.bluetown.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

//必须要有这个注释，否则afinal不识别该实体类
@SuppressWarnings("serial")
@Table(name = "GroupsBean")
public class GroupsBean implements Serializable {
	// 群信息
	private int id;
	private String headImg;
	private String type;
	private String flockName;
	private String scale;
	private String mid;
	private String flockImg;
	private String createDate;
	private String userId;
	private String flockNumber;

	public GroupsBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupsBean(String headImg, String type, String flockName,
			String scale, String mid, String flockImg, String createDate,
			String userId, String flockNumber) {
		super();
		this.headImg = headImg;
		this.type = type;
		this.flockName = flockName;
		this.scale = scale;
		this.mid = mid;
		this.flockImg = flockImg;
		this.createDate = createDate;
		this.userId = userId;
		this.flockNumber = flockNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlockName() {
		return flockName;
	}

	public void setFlockName(String flockName) {
		this.flockName = flockName;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getFlockImg() {
		return flockImg;
	}

	public void setFlockImg(String flockImg) {
		this.flockImg = flockImg;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFlockNumber() {
		return flockNumber;
	}

	public void setFlockNumber(String flockNumber) {
		this.flockNumber = flockNumber;
	}

}

package com.android.bluetown.bean;

import java.io.Serializable;

public class TokenBean implements Serializable{
	/** */
	private static final long serialVersionUID = 7625652051136690073L;
	// 主键
	private String ctid;
	// 随机生成
	private String token;
	// 登录用户的ID
	private String userId;
	// 过期时间
	private String expirationTime;
	// 创建时间
	private String createTime;
	// 修改时间
	private String updateTime;
	// 逻辑删除标志
	private Integer isDeleted;

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
}

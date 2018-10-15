package com.android.bluetown.bean;

public class QueryBusinessTypeListBean {
	/*
	 * "typeName": "教育", "sid": "109810000001"
	 */
	private String typeName;
	private String sid;
	public QueryBusinessTypeListBean() {
		// TODO Auto-generated constructor stub
	}
	public QueryBusinessTypeListBean(String typeName, String sid) {
		super();
		this.typeName = typeName;
		this.sid = sid;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}

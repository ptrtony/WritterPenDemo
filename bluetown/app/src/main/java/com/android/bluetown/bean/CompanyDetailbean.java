package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

import com.android.bluetown.result.Result;

public class CompanyDetailbean extends Result implements Serializable {
	private String address;
	private String typeName;
	private String content;
	private String isCollect;
	private String bid;
	private List<String> pictruesList;
	private String longitude;
	private String latitude;
	private String companyDistance;
	private String businessTell;
	private String companyName;
	private String businessImg;

	public CompanyDetailbean() {
	}

	public CompanyDetailbean(String address, String typeName, String content,
			String isCollect, String bid, List<String> pictruesList,
			String longitude, String latitude, String companyDistance,
			String businessTell, String companyName, String businessImg) {
		super();
		this.address = address;
		this.typeName = typeName;
		this.content = content;
		this.isCollect = isCollect;
		this.bid = bid;
		this.pictruesList = pictruesList;
		this.longitude = longitude;
		this.latitude = latitude;
		this.companyDistance = companyDistance;
		this.businessTell = businessTell;
		this.companyName = companyName;
		this.businessImg = businessImg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @Title: getPictruesList <BR>
	 * @Description: please write your description <BR>
	 * @return: List<String> <BR>
	 */

	public List<String> getPictruesList() {
		return pictruesList;
	}

	/**
	 * @Title: setPictruesList <BR>
	 * @Description: please write your description <BR>
	 * @Param:pictruesList
	 */

	public void setPictruesList(List<String> pictruesList) {
		this.pictruesList = pictruesList;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCompanyDistance() {
		return companyDistance;
	}

	public void setCompanyDistance(String companyDistance) {
		this.companyDistance = companyDistance;
	}

	public String getBusinessTell() {
		return businessTell;
	}

	public void setBusinessTell(String businessTell) {
		this.businessTell = businessTell;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusinessImg() {
		return businessImg;
	}

	public void setBusinessImg(String businessImg) {
		this.businessImg = businessImg;
	}

}

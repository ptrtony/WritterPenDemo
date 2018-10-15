package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

public class OtherMerchant implements Serializable {
	private String isCollect;
	private List<String> merchantImgList;
	private List<String> orgImgList;
	private String state;
	private String region;
	private String content;
	private String endTime;
	private String startTime;
	private String city;
	private String province;
	private String stell;
	private String isHot;
	private String sname;
	private String typeId;
	private String longitude;
	private String latitude;
	private String userId;
	private String meid;
	private String headImg;
	private String merchantName;
	private String merchantImg;
	private String consumption;
	private String merchantTell;
	private String merchantAddress;
	private String isClosed;

	public OtherMerchant() {
		// TODO Auto-generated constructor stub
	}

	public OtherMerchant(String isCollect, List<String> merchantImgList,
			List<String> orgImgList, String state, String region,
			String content, String endTime, String startTime, String city,
			String province, String stell, String isHot, String sname,
			String typeId, String longitude, String latitude, String userId,
			String meid, String headImg, String merchantName,
			String merchantImg, String consumption, String merchantTell,
			String merchantAddress) {
		super();
		this.isCollect = isCollect;
		this.merchantImgList = merchantImgList;
		this.orgImgList = orgImgList;
		this.state = state;
		this.region = region;
		this.content = content;
		this.endTime = endTime;
		this.startTime = startTime;
		this.city = city;
		this.province = province;
		this.stell = stell;
		this.isHot = isHot;
		this.sname = sname;
		this.typeId = typeId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.userId = userId;
		this.meid = meid;
		this.headImg = headImg;
		this.merchantName = merchantName;
		this.merchantImg = merchantImg;
		this.consumption = consumption;
		this.merchantTell = merchantTell;
		this.merchantAddress = merchantAddress;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public List<String> getMerchantImgList() {
		return merchantImgList;
	}

	public void setMerchantImgList(List<String> merchantImgList) {
		this.merchantImgList = merchantImgList;
	}

	public List<String> getOrgImgList() {
		return orgImgList;
	}

	public void setOrgImgList(List<String> orgImgList) {
		this.orgImgList = orgImgList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getStell() {
		return stell;
	}

	public void setStell(String stell) {
		this.stell = stell;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantImg() {
		return merchantImg;
	}

	public void setMerchantImg(String merchantImg) {
		this.merchantImg = merchantImg;
	}

	public String getConsumption() {
		return consumption;
	}

	public void setConsumption(String consumption) {
		this.consumption = consumption;
	}

	public String getMerchantTell() {
		return merchantTell;
	}

	public void setMerchantTell(String merchantTell) {
		this.merchantTell = merchantTell;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}
	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}

}

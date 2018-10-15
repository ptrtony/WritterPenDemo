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
public class CompanyShowItemBean implements Serializable {
	public static final int COLLECT_COMPANY = 1;
	public static final int CANCEL_COLLECT_COMPANY = 2;
	private String address;
	private String typeName;
	private String content;
	private String isCollect;
	private String bid;
	private String latitude;
	private String longitude;
	private String businessTell;
	private String companyName;
	private String businessImg;
	private String logoImg;

	public CompanyShowItemBean() {
		// TODO Auto-generated constructor stub
	}

	public CompanyShowItemBean(String address, String typeName, String content,
			String isCollect, String bid, String latitude, String longitude,
			String businessTell, String companyName, String businessImg,
			String companyDistance, String logoImg) {
		super();
		this.address = address;
		this.typeName = typeName;
		this.content = content;
		this.isCollect = isCollect;
		this.bid = bid;
		this.latitude = latitude;
		this.longitude = longitude;
		this.businessTell = businessTell;
		this.companyName = companyName;
		this.businessImg = businessImg;
		this.companyDistance = companyDistance;
		this.logoImg = logoImg;
	}

	private String companyDistance;

	/**
	 * @Title: getAddress <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getAddress() {
		return address;
	}

	/**
	 * @Title: setAddress <BR>
	 * @Description: please write your description <BR>
	 * @Param:address
	 */

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @Title: getTypeName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTypeName() {
		return typeName;
	}

	/**
	 * @Title: setTypeName <BR>
	 * @Description: please write your description <BR>
	 * @Param:typeName
	 */

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	/**
	 * @Title: getBid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBid() {
		return bid;
	}

	/**
	 * @Title: setBid <BR>
	 * @Description: please write your description <BR>
	 * @Param:bid
	 */

	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @Title: getLatitude <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getLatitude() {
		return latitude;
	}

	/**
	 * @Title: setLatitude <BR>
	 * @Description: please write your description <BR>
	 * @Param:latitude
	 */

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @Title: getLongitude <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getLongitude() {
		return longitude;
	}

	/**
	 * @Title: setLongitude <BR>
	 * @Description: please write your description <BR>
	 * @Param:longitude
	 */

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @Title: getBusinessTell <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBusinessTell() {
		return businessTell;
	}

	/**
	 * @Title: setBusinessTell <BR>
	 * @Description: please write your description <BR>
	 * @Param:businessTell
	 */

	public void setBusinessTell(String businessTell) {
		this.businessTell = businessTell;
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
	 * @Title: getBusinessImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBusinessImg() {
		return businessImg;
	}

	/**
	 * @Title: setBusinessImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:businessImg
	 */

	public void setBusinessImg(String businessImg) {
		this.businessImg = businessImg;
	}

	/**
	 * @Title: getCompanyDistance <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCompanyDistance() {
		return companyDistance;
	}

	/**
	 * @Title: setCompanyDistance <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyDistance
	 */

	public void setCompanyDistance(String companyDistance) {
		this.companyDistance = companyDistance;
	}

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

}

package com.android.bluetown.bean;

/**
 * 
 * @ClassName: CompanyShowItemBean
 * @Description:TODO(企业展示列表Item的javabean)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:14:32
 * 
 */
public class AroundMerchantsItemBean {
	/** 企业名称 */
	private String companyName;
	/** 时间 */
	private String companyTime;
	/** 企业距离 */
	private String companyDistance;
/** 企业类型 */
	private String type;
	private String distance;
	/** 企业图片 */
	private String companyImg;
	/** 是否进行收藏 *//*
	private boolean isCollect;*/

	public AroundMerchantsItemBean() {
		// TODO Auto-generated constructor stub
	}

	public AroundMerchantsItemBean(String companyName, String companyTime, String type,String distance,String companyDistance, String companyImg
		) {
		super();
		this.companyName = companyName;
		this.companyTime = companyTime;
		this.companyDistance = companyDistance;
		this.distance = distance;
		this.type = type;
		this.companyImg = companyImg;
	/*	this.isCollect = isCollect;*/
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
	 * @Title: getCompanyAddress <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCompanyTime() {
		return companyTime;
	}

	/**
	 * @Title: setCompanyAddress <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyAddress
	 */

	public void setCompanyTime(String companyTime) {
		this.companyTime = companyTime;
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

	/**
	 * @Title: getCompanyType <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getType() {
		return type;
	}

	/**
	 * @Title: setCompanyType <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyType
	 */

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @Title: getCompanyImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCompanyImg() {
		return companyImg;
	}

	/**
	 * @Title: setCompanyImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyImg
	 */

	public void setCompanyImg(String companyImg) {
		this.companyImg = companyImg;
	}
	public String getDistance() {
		return distance;
	}

	/**
	 * @Title: setCompanyImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyImg
	 */

	public void setDistance(String distance) {
		this.distance = distance;
	}
    
	/**
	 * @Title: isCollect <BR>
	 * @Description: please write your description <BR>
	 * @return: boolean <BR>
	 */

/*	public boolean isCollect() {
		return isCollect;
	}

	*//**
	 * @Title: setCollect <BR>
	 * @Description: please write your description <BR>
	 * @Param:isCollect
	 *//*

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}
*/
}

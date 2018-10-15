package com.android.bluetown.bean;

/**
 * 
 * @ClassName: CompanyShowItemBean
 * @Description:TODO(企业展示列表Item的javabean)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:14:32
 * 
 */
public class RealTimeZoneItemBean {
	/** 方向/门口 */
	private String direction;
	/** 背景图片 */
	private String companyImg;


	public RealTimeZoneItemBean() {
		// TODO Auto-generated constructor stub
	}

	public RealTimeZoneItemBean(String direction, String companyImg) {
		super();
		this.direction = direction;

		this.companyImg = companyImg;

	}

	/**
	 * @Title: getCompanyName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */




	/**
	 * @Title: getCompanyAddress <BR>
	 * @Description: please write your description <BR>

	/**
	 * @Title: setCompanyAddress <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyAddress

	/**
	 * @Title: getCompanyDistance <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getDirection() {
		return direction;
	}

	/**
	 * @Title: setCompanyDistance <BR>
	 * @Description: please write your description <BR>
	 * @Param:companyDistance
	 */

	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @Title: getCompanyType <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */



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




}

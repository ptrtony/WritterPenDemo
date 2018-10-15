package com.android.bluetown.bean;

import java.io.Serializable;

public class BannerInfoBean implements Serializable {
	private String bannerType;
	private String bannerTitle;
	private String bannerImg;

	public BannerInfoBean() {
		// TODO Auto-generated constructor stub
	}

	public BannerInfoBean(String bannerType, String bannerTitle, String bannerImg) {
		super();
		this.bannerType = bannerType;
		this.bannerTitle = bannerTitle;
		this.bannerImg = bannerImg;
	}

	/**
	 * @Title: getBannerType <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBannerType() {
		return bannerType;
	}

	/**
	 * @Title: setBannerType <BR>
	 * @Description: please write your description <BR>
	 * @Param:bannerType
	 */

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	/**
	 * @Title: getBannerTitle <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBannerTitle() {
		return bannerTitle;
	}

	/**
	 * @Title: setBannerTitle <BR>
	 * @Description: please write your description <BR>
	 * @Param:bannerTitle
	 */

	public void setBannerTitle(String bannerTitle) {
		this.bannerTitle = bannerTitle;
	}

	/**
	 * @Title: getBannerImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBannerImg() {
		return bannerImg;
	}

	/**
	 * @Title: setBannerImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:bannerImg
	 */

	public void setBannerImg(String bannerImg) {
		this.bannerImg = bannerImg;
	}

}

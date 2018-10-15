package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

public class ProductDetails implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */

	private static final long serialVersionUID = 1L;
	private String typeName;
	private String headImg;
	private String favourNumber;
	private List<String> pictruesList;
	private List<String> orgImgList;
	private String isFavour;
	private String isCollect;
	private String nickName;
	private String content;
	private String pictrues;
	private String cid;
	private String userId;
	private String commodityName;
	private String tell;
	private String typeId;
	private String createDate;
	private String logoImg;
	private String price;

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
	
	

	public List<String> getOrgImgList() {
		return orgImgList;
	}

	public void setOrgImgList(List<String> orgImgList) {
		this.orgImgList = orgImgList;
	}

	/**
	 * @Title: getNickName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getNickName() {
		return nickName;
	}

	/**
	 * @Title: setNickName <BR>
	 * @Description: please write your description <BR>
	 * @Param:nickName
	 */

	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	 * @Title: getUserId <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getUserId() {
		return userId;
	}

	/**
	 * @Title: setUserId <BR>
	 * @Description: please write your description <BR>
	 * @Param:userId
	 */

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @Title: getCid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCid() {
		return cid;
	}

	/**
	 * @Title: setCid <BR>
	 * @Description: please write your description <BR>
	 * @Param:cid
	 */

	public void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * @Title: getPictrues <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getPictrues() {
		return pictrues;
	}

	/**
	 * @Title: setPictrues <BR>
	 * @Description: please write your description <BR>
	 * @Param:pictrues
	 */

	public void setPictrues(String pictrues) {
		this.pictrues = pictrues;
	}

	/**
	 * @Title: getCommodityName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCommodityName() {
		return commodityName;
	}

	/**
	 * @Title: setCommodityName <BR>
	 * @Description: please write your description <BR>
	 * @Param:commodityName
	 */

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	/**
	 * @Title: getTypeId <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTypeId() {
		return typeId;
	}

	/**
	 * @Title: setTypeId <BR>
	 * @Description: please write your description <BR>
	 * @Param:typeId
	 */

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * @Title: getTell <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTell() {
		return tell;
	}

	/**
	 * @Title: setTell <BR>
	 * @Description: please write your description <BR>
	 * @Param:tell
	 */

	public void setTell(String tell) {
		this.tell = tell;
	}

	/**
	 * @Title: getCreateDate <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @Title: setCreateDate <BR>
	 * @Description: please write your description <BR>
	 * @Param:createDate
	 */

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @Title: getPrice <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getPrice() {
		return price;
	}

	/**
	 * @Title: setPrice <BR>
	 * @Description: please write your description <BR>
	 * @Param:price
	 */

	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @Title: getLogoImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getLogoImg() {
		return logoImg;
	}

	/**
	 * @Title: setLogoImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:logoImg
	 */

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	/**
	 * @Title: getIsFavour <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getIsFavour() {
		return isFavour;
	}

	/**
	 * @Title: setIsFavour <BR>
	 * @Description: please write your description <BR>
	 * @Param:isFavour
	 */

	public void setIsFavour(String isFavour) {
		this.isFavour = isFavour;
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
	 * @Title: getFavourNumber <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getFavourNumber() {
		return favourNumber;
	}

	/**
	 * @Title: setFavourNumber <BR>
	 * @Description: please write your description <BR>
	 * @Param:favourNumber
	 */

	public void setFavourNumber(String favourNumber) {
		this.favourNumber = favourNumber;
	}

	/**
	 * @Title: getHeadImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getHeadImg() {
		return headImg;
	}

	/**
	 * @Title: setHeadImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:headImg
	 */

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

}

package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

public class RecommendDish implements Serializable {
	private long id;
	private String typeName;
	private String type;
	private String typeId;
	private String merchantId;
	private String price;
	private String isSeason;
	private String dishesName;
	private String homeImg;
	private String disImg;
	private String discount;
	private String putTime;
	private String dishesId;
	private String recommend;
	private String isPut;
	private String createTime;
	private String favorablePrice;
	private String rcreateTime;
	private String menuContent;
	private String seasonContent;
	private List<String> disImgList;

	// （本地缓存）菜的数量
	private int dishesCount;

	public RecommendDish() {
		// TODO Auto-generated constructor stub
	}

	public RecommendDish(String typeName, String type, String typeId,
			String merchantId, String price, String isSeason,
			String dishesName, String homeImg, String disImg, String discount,
			String putTime, String dishesId, String recommend, String isPut,
			String createTime, String favorablePrice, String rcreateTime,
			String menuContent, String seasonContent, List<String> disImgList) {
		super();
		this.typeName = typeName;
		this.type = type;
		this.typeId = typeId;
		this.merchantId = merchantId;
		this.price = price;
		this.isSeason = isSeason;
		this.dishesName = dishesName;
		this.homeImg = homeImg;
		this.disImg = disImg;
		this.discount = discount;
		this.putTime = putTime;
		this.dishesId = dishesId;
		this.recommend = recommend;
		this.isPut = isPut;
		this.createTime = createTime;
		this.favorablePrice = favorablePrice;
		this.rcreateTime = rcreateTime;
		this.menuContent = menuContent;
		this.seasonContent = seasonContent;
		this.disImgList = disImgList;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIsSeason() {
		return isSeason;
	}

	public void setIsSeason(String isSeason) {
		this.isSeason = isSeason;
	}

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public String getHomeImg() {
		return homeImg;
	}

	public void setHomeImg(String homeImg) {
		this.homeImg = homeImg;
	}

	public String getDisImg() {
		return disImg;
	}

	public void setDisImg(String disImg) {
		this.disImg = disImg;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getPutTime() {
		return putTime;
	}

	public void setPutTime(String putTime) {
		this.putTime = putTime;
	}

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getIsPut() {
		return isPut;
	}

	public void setIsPut(String isPut) {
		this.isPut = isPut;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFavorablePrice() {
		return favorablePrice;
	}

	public void setFavorablePrice(String favorablePrice) {
		this.favorablePrice = favorablePrice;
	}

	public String getRcreateTime() {
		return rcreateTime;
	}

	public void setRcreateTime(String rcreateTime) {
		this.rcreateTime = rcreateTime;
	}

	public String getMenuContent() {
		return menuContent;
	}

	public void setMenuContent(String menuContent) {
		this.menuContent = menuContent;
	}

	public String getSeasonContent() {
		return seasonContent;
	}

	public void setSeasonContent(String seasonContent) {
		this.seasonContent = seasonContent;
	}

	public int getDishesCount() {
		return dishesCount;
	}

	public void setDishesCount(int dishesCount) {
		this.dishesCount = dishesCount;
	}

	public List<String> getDisImgList() {
		return disImgList;
	}

	public void setDisImgList(List<String> disImgList) {
		this.disImgList = disImgList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

}

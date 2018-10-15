package com.android.bluetown.result;

public class RecommedDishDetailsResult extends Result {
	private RecommendDishDetails data;

	public RecommendDishDetails getData() {
		return data;
	}

	public void setData(RecommendDishDetails data) {
		this.data = data;
	}

	/**
	 * 参数说明： dishesId:菜品id price：原价 home_img:菜品首图 dishes_name：菜品名称
	 * favorable_price：菜品优惠价格 recommend：是否推荐（0不推荐1推荐） disImgList:菜品图片集
	 * menuContent：菜品的描述
	 * 
	 * @author shenyz
	 * 
	 */
	public class RecommendDishDetails {
		private String typeName;
		private String disImgList;
		private String type;
		private String dishesId;
		private String isPut;
		private String recommend;
		private String createTime;
		private String favorablePrice;
		private String rcreateTime;
		private String seasonContent;
		private String menuContent;
		private String dishesName;
		private String discount;
		private String homeImg;
		private String disImg;
		private String isSeason;
		private String putTime;
		private String typeId;
		private String merchantId;
		private String price;

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getDisImgList() {
			return disImgList;
		}

		public void setDisImgList(String disImgList) {
			this.disImgList = disImgList;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDishesId() {
			return dishesId;
		}

		public void setDishesId(String dishesId) {
			this.dishesId = dishesId;
		}

		public String getIsPut() {
			return isPut;
		}

		public void setIsPut(String isPut) {
			this.isPut = isPut;
		}

		public String getRecommend() {
			return recommend;
		}

		public void setRecommend(String recommend) {
			this.recommend = recommend;
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

		public String getSeasonContent() {
			return seasonContent;
		}

		public void setSeasonContent(String seasonContent) {
			this.seasonContent = seasonContent;
		}

		public String getMenuContent() {
			return menuContent;
		}

		public void setMenuContent(String menuContent) {
			this.menuContent = menuContent;
		}

		public String getDishesName() {
			return dishesName;
		}

		public void setDishesName(String dishesName) {
			this.dishesName = dishesName;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
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

		public String getIsSeason() {
			return isSeason;
		}

		public void setIsSeason(String isSeason) {
			this.isSeason = isSeason;
		}

		public String getPutTime() {
			return putTime;
		}

		public void setPutTime(String putTime) {
			this.putTime = putTime;
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

	}
}

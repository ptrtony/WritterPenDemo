package com.android.bluetown.result;

import java.io.Serializable;
import java.util.List;

import com.android.bluetown.bean.CommentBean;
import com.android.bluetown.bean.RecommendBean;

/**
 * 食堂详情、美食商家详情
 * 
 * @author shenyz
 * 
 */
public class CanteenFoodDetailsResult extends Result {
	private CanteenDetails data;

	/**
	 * @Title: getData <BR>
	 * @Description: please write your description <BR>
	 * @return: CanteenDetails <BR>
	 */

	public CanteenDetails getData() {
		return data;
	}

	/**
	 * @Title: setData <BR>
	 * @Description: please write your description <BR>
	 * @Param:data
	 */

	public void setData(CanteenDetails data) {
		this.data = data;
	}

	public class CanteenDetails {
		private String commentNumber;
		private CommentBean merchantComment;
		private String isCollect;
		private CanteenMerchant merchant;
		private RecommendBean recommend;

		public String getCommentNumber() {
			return commentNumber;
		}

		public void setCommentNumber(String commentNumber) {
			this.commentNumber = commentNumber;
		}

		public CommentBean getMerchantComment() {
			return merchantComment;
		}

		public void setMerchantComment(CommentBean merchantComment) {
			this.merchantComment = merchantComment;
		}

		public String getIsCollect() {
			return isCollect;
		}

		public void setIsCollect(String isCollect) {
			this.isCollect = isCollect;
		}

		public CanteenMerchant getMerchant() {
			return merchant;
		}

		public void setMerchant(CanteenMerchant merchant) {
			this.merchant = merchant;
		}

		public RecommendBean getRecommend() {
			return recommend;
		}

		public void setRecommend(RecommendBean recommend) {
			this.recommend = recommend;
		}

	}

	public class CanteenMerchant implements Serializable{
		private List<String> merchantImgList;
		private List<String> orgImgList;
		private String userId;
		private String meid;
		private String headImg;
		private String createTime;
		private String bookPrice;
		private String province;
		private String city;
		private String consumption;
		private String merchantImg;
		private String merchantName;
		private String merchantTell;
		private String merchantAddress;
		private String sname;
		private String stell;
		private String endTime;
		private String startTime;
		private String state;
		private String content;
		private String region;
		private String isClosed;
		// 食堂商家自有参数
		private String garden;
		private String logoImg;
		// 美食商家自有参数
		private String isHot;
		private String typeId;
		private String hotBusiness;
		private String longitude;
		private String latitude;
		private String twoTypeId;

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
		public String getIsClosed() {
			return isClosed;
		}

		public void setIsClosed(String isClosed) {
			this.isClosed = isClosed;
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

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getBookPrice() {
			return bookPrice;
		}

		public void setBookPrice(String bookPrice) {
			this.bookPrice = bookPrice;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getConsumption() {
			return consumption;
		}

		public void setConsumption(String consumption) {
			this.consumption = consumption;
		}

		public String getMerchantImg() {
			return merchantImg;
		}

		public void setMerchantImg(String merchantImg) {
			this.merchantImg = merchantImg;
		}

		public String getMerchantName() {
			return merchantName;
		}

		public void setMerchantName(String merchantName) {
			this.merchantName = merchantName;
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

		public String getSname() {
			return sname;
		}

		public void setSname(String sname) {
			this.sname = sname;
		}

		public String getStell() {
			return stell;
		}

		public void setStell(String stell) {
			this.stell = stell;
		}

		public String getGarden() {
			return garden;
		}

		public void setGarden(String garden) {
			this.garden = garden;
		}

		public String getLogoImg() {
			return logoImg;
		}

		public void setLogoImg(String logoImg) {
			this.logoImg = logoImg;
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

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getIsHot() {
			return isHot;
		}

		public void setIsHot(String isHot) {
			this.isHot = isHot;
		}

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

		public String getHotBusiness() {
			return hotBusiness;
		}

		public void setHotBusiness(String hotBusiness) {
			this.hotBusiness = hotBusiness;
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

		public String getTwoTypeId() {
			return twoTypeId;
		}

		public void setTwoTypeId(String twoTypeId) {
			this.twoTypeId = twoTypeId;
		}

	}
}

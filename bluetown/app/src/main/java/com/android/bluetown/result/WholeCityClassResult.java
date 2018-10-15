package com.android.bluetown.result;

import java.util.List;

public class WholeCityClassResult extends Result {
	private List<WholeCityClass> data;

	public List<WholeCityClass> getData() {
		return data;
	}

	public void setData(List<WholeCityClass> data) {
		this.data = data;
	}

	/**
	 * region: 城市区域 province：省 city：市 region:区域 createTime：创建日期
	 * hotBusinessList:热门商圈 province：省 city：市 region:区域 createTime：创建日期
	 * hotRegion:热门区域
	 * 
	 * @author shenyz
	 * 
	 */
	public class WholeCityClass {
		private String region;
		private String type;
		private String hid;
		private String createTime;
		private String city;
		private String province;
		private String hotRegion;
		private List<WholeCitySubClass> hotBusinessList;

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getHid() {
			return hid;
		}

		public void setHid(String hid) {
			this.hid = hid;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
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

		public String getHotRegion() {
			return hotRegion;
		}

		public void setHotRegion(String hotRegion) {
			this.hotRegion = hotRegion;
		}

		public List<WholeCitySubClass> getHotBusinessList() {
			return hotBusinessList;
		}

		public void setHotBusinessList(List<WholeCitySubClass> hotBusinessList) {
			this.hotBusinessList = hotBusinessList;
		}

	}

	public class WholeCitySubClass {
		private String region;
		private String type;
		private String hid;
		private String createTime;
		private String city;
		private String province;
		private String hotRegion;

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getHid() {
			return hid;
		}

		public void setHid(String hid) {
			this.hid = hid;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
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

		public String getHotRegion() {
			return hotRegion;
		}

		public void setHotRegion(String hotRegion) {
			this.hotRegion = hotRegion;
		}

	}

}

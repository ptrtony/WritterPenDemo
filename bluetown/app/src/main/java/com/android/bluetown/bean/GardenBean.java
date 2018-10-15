package com.android.bluetown.bean;

import java.io.Serializable;

/**
 * province：省 city：市 region:区域 createTime：创建日期 hotRegion:所属园区 createTime:创建日期
 * hid：主键Id
 * 
 * @author shenyz
 * 
 */
public class GardenBean implements Serializable {
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

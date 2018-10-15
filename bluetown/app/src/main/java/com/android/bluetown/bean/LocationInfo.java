package com.android.bluetown.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

@SuppressWarnings("serial")
// 必须要有这个注释，否则afinal不识别该实体类
@Table(name = "LocationInfo")
public class LocationInfo implements Serializable {
	public int id;
	/**
	 * 纬度
	 */
	public double latitude;
	/**
	 * 经度
	 */
	public double longitude;
	/**
	 * 城市
	 */
	public String city = "";
	/**
	 * 省
	 */
	public String province = "";
	/**
	 * 地址
	 */
	public String addr = "";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
}

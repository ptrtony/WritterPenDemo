package com.android.bluetown.bean;

import java.io.Serializable;

public class ParkingSpaceBean implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	// 是否可租用 0：可租用 1不可租用
	public String isRentable;
	// 车位类型 1:固定车位 2：预约车位 3：VIP车位
	public String parkingSpaceType;
	// 车位号
	public String parkingSpaceNo;

}

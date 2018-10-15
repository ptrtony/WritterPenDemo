package com.android.bluetown.bean;

import java.io.Serializable;

public class RemindCarport implements Serializable {
	// ͣ�������
	private String parkingLotNo;
	// ʣ�೵λ
	// 停车场名称e
	private String parkingName;
	private String remainingSpaces;
	private String remainingReservedSpaces;
	// ͣ��������
	private String region;
	private String mid;
	private String vcount;
	private String vdate;
	
	public String getParkingName() {
		return parkingName;
	}

	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}
	public String getParkingLotNo() {
		return parkingLotNo;
	}

	public void setParkingLotNo(String parkingLotNo) {
		this.parkingLotNo = parkingLotNo;
	}

	public String getRemainingSpaces() {
		return remainingSpaces;
	}

	public void setRemainingSpaces(String remainingSpaces) {
		this.remainingSpaces = remainingSpaces;
	}


	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getVcount() {
		return vcount;
	}

	public void setVcount(String vcount) {
		this.vcount = vcount;
	}

	public String getVdate() {
		return vdate;
	}

	public void setVdate(String vdate) {
		this.vdate = vdate;
	}

	public String getRemainingReservedSpaces() {
		return remainingReservedSpaces;
	}

	public void setRemainingReservedSpaces(String remainingReservedSpaces) {
		this.remainingReservedSpaces = remainingReservedSpaces;
	}
}

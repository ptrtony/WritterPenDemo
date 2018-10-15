package com.android.bluetown.bean;

import java.io.Serializable;

public class CartBean implements Serializable {
	public static final String CTOTALPRICE = "currentTotalPrice";
	public static final String OTOTALPRICE = "currentOriginalTotalPrice";
	public static final String TOTALCOUNT = "totalDishCount";
	private long id;
	private String currentTotalPrice;
	private String currentOriginalTotalPrice;
	/**
	 * 已点菜单的总数量
	 */
	private int totalDishCount;

	public String getCurrentTotalPrice() {
		return currentTotalPrice;
	}

	public void setCurrentTotalPrice(String currentTotalPrice) {
		this.currentTotalPrice = currentTotalPrice;
	}

	public String getCurrentOriginalTotalPrice() {
		return currentOriginalTotalPrice;
	}

	public void setCurrentOriginalTotalPrice(String currentOriginalTotalPrice) {
		this.currentOriginalTotalPrice = currentOriginalTotalPrice;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public static String getCtotalprice() {
		return CTOTALPRICE;
	}

	public static String getOtotalprice() {
		return OTOTALPRICE;
	}

	public static String getTotalcount() {
		return TOTALCOUNT;
	}

	public int getTotalDishCount() {
		return totalDishCount;
	}

	public void setTotalDishCount(int totalDishCount) {
		this.totalDishCount = totalDishCount;
	}

}

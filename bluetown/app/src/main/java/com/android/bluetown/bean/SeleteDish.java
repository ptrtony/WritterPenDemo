package com.android.bluetown.bean;

import java.io.Serializable;

public class SeleteDish implements Serializable {

	private String dishesCount;
	private String dishesPrice;
	private String dishesName;
	private String dishesId;
	private String homeImg;
	private String price;

	public String getDishesCount() {
		return dishesCount;
	}

	public void setDishesCount(String dishesCount) {
		this.dishesCount = dishesCount;
	}

	public String getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(String dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getHomeImg() {
		return homeImg;
	}

	public void setHomeImg(String homeImg) {
		this.homeImg = homeImg;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}

package com.android.bluetown.sortlistview;

public class SortModel {
	private String userId;
	private String name;// 显示的数据
	private String signaturele;// 个性签名
	private String friendImg;// 用户图像
	private boolean isSelected;
	private String sortLetters;// 显示数据拼音的首字母
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSignaturele() {
		return signaturele;
	}

	public void setSignaturele(String signaturele) {
		this.signaturele = signaturele;
	}

	public String getFriendImg() {
		return friendImg;
	}

	public void setFriendImg(String friendImg) {
		this.friendImg = friendImg;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}

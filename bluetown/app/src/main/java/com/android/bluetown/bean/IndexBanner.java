package com.android.bluetown.bean;

import java.io.Serializable;

public class IndexBanner implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pimg;
	private String hid;
	private String aid;
	private String createDate;
	private String title;
	private String isSlide;
	private String publish;
	private String isTop;
	
	private String topTime;
	private String pastDate;
	private String content;
	public String getPimg() {
		return pimg;
	}
	public void setPimg(String pimg) {
		this.pimg = pimg;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getTitle() {
		return title;
	
	}
	public String getIsSlide() {
		return isSlide;
	}

	public void setIsSlide(String isSlide) {
		this.isSlide = isSlide;
	}
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
		
	public String getIsTop() {
		return isTop;
	}

	
	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}
	public String getTopTime() {
		return topTime;
	}
	public void setTopTime(String topTime) {
		this.topTime = topTime;
	}
	public String getPastDate() {
		return pastDate;
	}

	public void setPastDate(String pastDate) {
		this.pastDate = pastDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}

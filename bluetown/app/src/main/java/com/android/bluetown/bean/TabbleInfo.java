package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TabbleInfo implements Serializable {
	private String bookPrice;
	private List<String> noDateList;
	private String type;
	private String tid;
	private String merchantId;
	private String tableName;
	//本地做效果的参数
	private int group;
	private int selectIndex = -1;

	public TabbleInfo() {
		// TODO Auto-generated constructor stub
	}

	public TabbleInfo(String bookPrice, List<String> noDateList, String type,
			String tid, String merchantId, String tableName) {
		super();
		this.bookPrice = bookPrice;
		this.noDateList = noDateList;
		this.type = type;
		this.tid = tid;
		this.merchantId = merchantId;
		this.tableName = tableName;
	}

	public String getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(String bookPrice) {
		this.bookPrice = bookPrice;
	}

	public List<String> getNoDateList() {
		return noDateList;
	}

	public void setNoDateList(List<String> noDateList) {
		this.noDateList = noDateList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

	
}

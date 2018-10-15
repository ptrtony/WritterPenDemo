package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.TabbleInfo;

/**
 * 选材桌子列表的列表
 * 
 * @author shenyz
 * 
 */
public class TableListResult extends Result {
	public List<TabbleInfo> data;

	public List<TabbleInfo> getData() {
		return data;
	}

	public void setData(List<TabbleInfo> data) {
		this.data = data;
	}

	


}

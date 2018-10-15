package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.GardenBean;

public class GardenListResult extends Result {
	private List<GardenBean> data;

	public List<GardenBean> getData() {
		return data;
	}

	public void setData(List<GardenBean> data) {
		this.data = data;
	}
	

}

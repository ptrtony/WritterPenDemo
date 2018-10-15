package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.CanteenBean;

public class CanteenListResult extends Result{
	private List<CanteenBean> data;

	public List<CanteenBean> getData() {
		return data;
	}

	public void setData(List<CanteenBean> data) {
		this.data = data;
	}
	

}

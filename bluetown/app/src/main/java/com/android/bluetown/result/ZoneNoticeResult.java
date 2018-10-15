package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.ZoneNotice;

public class ZoneNoticeResult extends Result {
	private List<ZoneNotice> data;

	public List<ZoneNotice> getData() {
		return data;
	}

	public void setData(List<ZoneNotice> data) {
		this.data = data;
	}



}

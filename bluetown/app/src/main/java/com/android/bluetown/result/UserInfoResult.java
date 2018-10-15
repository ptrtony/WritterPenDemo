package com.android.bluetown.result;

import com.android.bluetown.bean.UserInfo;

public class UserInfoResult extends Result {
	private UserInfo data;

	public UserInfo getData() {
		return data;
	}

	public void setData(UserInfo data) {
		this.data = data;
	}

}

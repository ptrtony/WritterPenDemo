package com.android.bluetown.result;

import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.TokenBean;

public class LoginData {
	private MemberUser memberUser;
	private Rong rong;
	

	public class Rong {
		public String token;
		public String code;
		public String userId;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}


	public MemberUser getMemberUser() {
		return memberUser;
	}

	public void setMemberUser(MemberUser memberUser) {
		this.memberUser = memberUser;
	}

	public Rong getRong() {
		return rong;
	}

	public void setRong(Rong rong) {
		this.rong = rong;
	}

}

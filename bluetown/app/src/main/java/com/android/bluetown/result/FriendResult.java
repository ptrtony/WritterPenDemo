package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.UserInfo;

public class FriendResult extends Result {
	private FriendData data;

	/**
	 * @Title: getData <BR>
	 * @Description: please write your description <BR>
	 * @return: CircleData <BR>
	 */

	public FriendData getData() {
		return data;
	}

	/**
	 * @Title: setData <BR>
	 * @Description: please write your description <BR>
	 * @Param:data
	 */

	public void setData(FriendData data) {
		this.data = data;
	}

	public class FriendData {
		private UserInfo userInfo;
		private Friend friend;
		public UserInfo getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(UserInfo userInfo) {
			this.userInfo = userInfo;
		}

		public Friend getFriend() {
			return friend;
		}

		public void setFriend(Friend friend) {
			this.friend = friend;
		}

	}

	public class Friend extends Data {
		private List<FriendsBean> rows;

		public List<FriendsBean> getRows() {
			return rows;
		}

		public void setRows(List<FriendsBean> rows) {
			this.rows = rows;
		}
	}

}

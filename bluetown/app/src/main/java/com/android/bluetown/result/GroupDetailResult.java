package com.android.bluetown.result;

public class GroupDetailResult extends Result {
	private GroupDetail data;

	public GroupDetail getData() {
		return data;
	}

	public void setData(GroupDetail data) {
		this.data = data;
	}

	public class GroupDetail {
		private String nickName;
		private String headImg;
		private String type;
		private String userId;
		private String scale;
		private String flockImg;
		private String mid;
		private String createDate;
		private String flockNumber;
		private String flockName;
		// 0已加入  1未加入
		private String state;

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getHeadImg() {
			return headImg;
		}

		public void setHeadImg(String headImg) {
			this.headImg = headImg;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getScale() {
			return scale;
		}

		public void setScale(String scale) {
			this.scale = scale;
		}

		public String getFlockImg() {
			return flockImg;
		}

		public void setFlockImg(String flockImg) {
			this.flockImg = flockImg;
		}

		public String getMid() {
			return mid;
		}

		public void setMid(String mid) {
			this.mid = mid;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getFlockNumber() {
			return flockNumber;
		}

		public void setFlockNumber(String flockNumber) {
			this.flockNumber = flockNumber;
		}

		public String getFlockName() {
			return flockName;
		}

		public void setFlockName(String flockName) {
			this.flockName = flockName;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
		
		

	}

}

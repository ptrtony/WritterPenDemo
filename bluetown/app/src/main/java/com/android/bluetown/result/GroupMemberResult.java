package com.android.bluetown.result;

import java.util.List;

public class GroupMemberResult extends Result {

	private GroupMemberData data;

	public GroupMemberData getData() {
		return data;
	}

	public void setData(GroupMemberData data) {
		this.data = data;
	}

	public class GroupMemberData extends Data {
		private List<GroupMember> rows;

		public List<GroupMember> getRows() {
			return rows;
		}

		public void setRows(List<GroupMember> rows) {
			this.rows = rows;
		}
		

	}

	public class GroupMember {
		private String id;
		private String userId;
		private String statu;
		private String headImg;
		private String nickName;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getStatu() {
			return statu;
		}
		public void setStatu(String statu) {
			this.statu = statu;
		}
		public String getHeadImg() {
			return headImg;
		}
		public void setHeadImg(String headImg) {
			this.headImg = headImg;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		
	}
}

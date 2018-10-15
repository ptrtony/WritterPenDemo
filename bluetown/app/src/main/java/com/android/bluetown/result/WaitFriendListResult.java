package com.android.bluetown.result;

import java.io.Serializable;
import java.util.List;

/**
 * 参数说明: type:(0:好友信息,1群信息) userId：被添加人或邀请人id status：是否同意状态（0：审核中1：拒绝2：通过）
 * headImg：用户头像 nickName：用户昵称 flockName：群名称 flockId：群id
 * friendId:type为1时userId和登陆人id相同时就说明他是申请进群，申请人id就是friendId createDate:邀请时间
 * 
 * @author shenyz
 * 
 */
public class WaitFriendListResult extends Result {
	public WaitFriendListData data;

	public WaitFriendListData getData() {
		return data;
	}

	public void setData(WaitFriendListData data) {
		this.data = data;
	}

	public class WaitFriendListData extends Data {
		public List<WaitFriend> rows;

		public List<WaitFriend> getRows() {
			return rows;
		}

		public void setRows(List<WaitFriend> rows) {
			this.rows = rows;
		}

	}

	public class WaitFriend implements Serializable {
		private String id;
		private String type;
		private String userId;
		private String statu;
		private String headImg;
		private String createDate;
		private String memberId;
		private String friendId;
		private String nickName;
		private String status;
		private String km;
		private String flockName;
		private String flockId;
		// quid:群主id;
		// state：进去标示(0申请进群1邀请进群)
		private String state;
		private String qzid;

		public WaitFriend() {
			// TODO Auto-generated constructor stub
		}

		public WaitFriend(String id, String type, String userId, String statu,
				String headImg, String createDate, String memberId,
				String friendId, String nickName, String status, String km,
				String flockName, String flockId, String state, String qzid) {
			super();
			this.id = id;
			this.type = type;
			this.userId = userId;
			this.statu = statu;
			this.headImg = headImg;
			this.createDate = createDate;
			this.memberId = memberId;
			this.friendId = friendId;
			this.nickName = nickName;
			this.status = status;
			this.km = km;
			this.flockName = flockName;
			this.flockId = flockId;
			this.state = state;
			this.qzid = qzid;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
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

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getMemberId() {
			return memberId;
		}

		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}

		public String getFriendId() {
			return friendId;
		}

		public void setFriendId(String friendId) {
			this.friendId = friendId;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getKm() {
			return km;
		}

		public void setKm(String km) {
			this.km = km;
		}

		public String getFlockName() {
			return flockName;
		}

		public void setFlockName(String flockName) {
			this.flockName = flockName;
		}

		public String getFlockId() {
			return flockId;
		}

		public void setFlockId(String flockId) {
			this.flockId = flockId;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getQzid() {
			return qzid;
		}

		public void setQzid(String qzid) {
			this.qzid = qzid;
		}


	}
}

package com.android.bluetown.result;

import java.io.Serializable;
import java.util.List;

public class NearLyPersonResult extends Result {
	private NearLyData data;

	public NearLyData getData() {
		return data;
	}

	public void setData(NearLyData data) {
		this.data = data;
	}

	public class NearLyData extends Data {
		private List<NearlyPerson> rows;
		public List<NearlyPerson> getRows() {
			return rows;
		}

		public void setRows(List<NearlyPerson> rows) {
			this.rows = rows;
		}

	}

	@SuppressWarnings("serial")
	public class NearlyPerson implements Serializable {
		private String id;
		private String userId;
		private String km;
		private String nickName;
		private String statu;
		private String headImg;

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

		public String getKm() {
			return km;
		}

		public void setKm(String km) {
			this.km = km;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
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

	}

}

package com.android.bluetown.result;

public class VideoAccountResult extends Result {
	private VideoData data;

	public VideoData getData() {
		return data;
	}

	public void setData(VideoData data) {
		this.data = data;
	}

	public class VideoData {
		private String password;
		private String gid;
		private String createDate;
		private String url;
		private String garden;
		private String account;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getGid() {
			return gid;
		}

		public void setGid(String gid) {
			this.gid = gid;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getGarden() {
			return garden;
		}

		public void setGarden(String garden) {
			this.garden = garden;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

	}

}

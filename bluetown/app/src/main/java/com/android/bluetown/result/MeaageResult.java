package com.android.bluetown.result;

import java.util.List;

public class MeaageResult extends Result {
	public MessageData data;
	
	public MessageData getData() {
		return data;
	}

	public void setData(MessageData data) {
		this.data = data;
	}

	public class MessageData {
		private List<MessageBean> rows;

		public List<MessageBean> getRows() {
			return rows;
		}

		public void setRows(List<MessageBean> rows) {
			this.rows = rows;
		}

	}

	public class MessageBean {
		private String state;
		private String pid;
		private String userId;
		private String pushContent;
		private String pushDate;

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getPushContent() {
			return pushContent;
		}

		public void setPushContent(String pushContent) {
			this.pushContent = pushContent;
		}

		public String getPushDate() {
			return pushDate;
		}

		public void setPushDate(String pushDate) {
			this.pushDate = pushDate;
		}
	}
}

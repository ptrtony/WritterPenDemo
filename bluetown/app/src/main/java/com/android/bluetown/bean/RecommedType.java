package com.android.bluetown.bean;

import java.io.Serializable;

public class RecommedType implements Serializable{
		
		public RecommedType() {
			// TODO Auto-generated constructor stub
		}
		
		public RecommedType(String type, String typeName, String tid,
				String merchantId) {
			super();
			this.type = type;
			this.typeName = typeName;
			this.tid = tid;
			this.merchantId = merchantId;
		}

		private String type;
		private String typeName;
		private String tid;
		private String merchantId;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getTid() {
			return tid;
		}

		public void setTid(String tid) {
			this.tid = tid;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

}

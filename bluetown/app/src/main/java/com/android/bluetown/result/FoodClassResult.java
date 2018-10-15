package com.android.bluetown.result;
import java.util.List;

public class FoodClassResult extends Result {
	private List<FoodClass> data;
	public List<FoodClass> getData() {
		return data;
	}

	public void setData(List<FoodClass> data) {
		this.data = data;
	}

	public class FoodClass {
//		tid:类型id
//		typeName:类型名称
//		merchantTypeList:二级分类
		private String typeName;
		private String tid;
		private List<FoodSubClass> merchantTypeList;
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
		public List<FoodSubClass> getMerchantTypeList() {
			return merchantTypeList;
		}
		public void setMerchantTypeList(List<FoodSubClass> merchantTypeList) {
			this.merchantTypeList = merchantTypeList;
		}

	}
	
	public class FoodSubClass {
//		typeId:一级分类id
//		tid:类型id
//		typeName:类型名称
		private String typeName;
		private String createTime;
		private String tid;
		private String typeId;

		public String getTid() {
			return tid;
		}

		public void setTid(String tid) {
			this.tid = tid;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

	}

}


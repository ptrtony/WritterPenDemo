package com.android.bluetown.bean;

import java.util.ArrayList;

import com.android.bluetown.result.Result;

public class ProcedureApprovalBean extends Result{
	private ArrayList<ProcedurTypeBean> data;
	
	
	public ArrayList<ProcedurTypeBean> getData() {
		return data;
	}

	public void setData(ArrayList<ProcedurTypeBean> data) {
		this.data = data;
	}


	public class ProcedurTypeBean{
		private String typeName;
		private String tid;
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
	}
	
}

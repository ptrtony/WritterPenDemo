package com.android.bluetown.bean;

import java.util.ArrayList;

import com.android.bluetown.result.Result;

public class ProcedureHistoryBean extends Result{
	public DataBean data;
	
	public class DataBean {
		 public	String page;
		 public String total;
		 public ArrayList<DetailBean> rows;
		
	}
	public class DetailBean {
		 public	String name;
		 public String typeName; 
		 public String type;
		 public	String content;
		 public String status; 
		 public	String userId;
		 public String gid;
		 public	String title;
		 public String createTime; 
		 public String repulse;
	}
}

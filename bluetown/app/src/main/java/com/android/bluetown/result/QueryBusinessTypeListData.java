package com.android.bluetown.result;

import java.util.ArrayList;

import com.android.bluetown.bean.QueryBusinessTypeListBean;

public class QueryBusinessTypeListData extends Result{
	private  ArrayList<QueryBusinessTypeListBean> data;
	public ArrayList<QueryBusinessTypeListBean> getData() {
		return data;
	} 

	/**
	 * @Title: setData <BR>
	 * @Description: please write your description <BR>
	 * @Param:data
	 */

	public void setData(ArrayList<QueryBusinessTypeListBean> data) {
		this.data = data;
	}

}

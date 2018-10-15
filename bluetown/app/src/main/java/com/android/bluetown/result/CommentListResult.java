package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.CommentBean;

public class CommentListResult extends Result{
	private CommentListData data;
	
	public CommentListData getData() {
		return data;
	}
public class CommentListData extends Data{
	private List<CommentBean> rows;

	public List<CommentBean> getRows() {
		return rows;
	}

	public void setRows(List<CommentBean> rows) {
		this.rows = rows;
	}
}
	public void setData(CommentListData data) {
		this.data = data;
	}


	

}

package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.CircleBean;

public class CircleData extends Data {
	private List<CircleBean> rows;

	/**
	 * @Title: getRows <BR>
	 * @Description: please write your description <BR>
	 * @return: List<CircleBean> <BR>
	 */

	public List<CircleBean> getRows() {
		return rows;
	}

	/**
	 * @Title: setRows <BR>
	 * @Description: please write your description <BR>
	 * @Param:rows
	 */

	public void setRows(List<CircleBean> rows) {
		this.rows = rows;
	}

}

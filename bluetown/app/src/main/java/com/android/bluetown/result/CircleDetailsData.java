package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.CircleBean;
import com.android.bluetown.bean.PostBean;

public class CircleDetailsData extends Data {
	private CircleBean circleInfo;
	private List<PostBean> rows;
	private List<PostBean> top;

	/**
	 * @Title: getCircleInfo <BR>
	 * @Description: please write your description <BR>
	 * @return: CircleBean <BR>
	 */

	public CircleBean getCircleInfo() {
		return circleInfo;
	}

	/**
	 * @Title: setCircleInfo <BR>
	 * @Description: please write your description <BR>
	 * @Param:circleInfo
	 */

	public void setCircleInfo(CircleBean circleInfo) {
		this.circleInfo = circleInfo;
	}

	/**
	 * @Title: getRows <BR>
	 * @Description: please write your description <BR>
	 * @return: List<PostBean> <BR>
	 */

	public List<PostBean> getRows() {
		return rows;
	}

	/**
	 * @Title: setRows <BR>
	 * @Description: please write your description <BR>
	 * @Param:rows
	 */

	public void setRows(List<PostBean> rows) {
		this.rows = rows;
	}

	public List<PostBean> getTop() {
		return top;
	}

	public void setTop(List<PostBean> top) {
		this.top = top;
	}
}

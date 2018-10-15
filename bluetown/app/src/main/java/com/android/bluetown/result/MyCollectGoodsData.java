package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.ProductBean;

public class MyCollectGoodsData extends Data {
	private List<ProductBean> rows;

	/**
	 * @Title: getRows <BR>
	 * @Description: please write your description <BR>
	 * @return: List<ProductBean> <BR>
	 */

	public List<ProductBean> getRows() {
		return rows;
	}

	/**
	 * @Title: setRows <BR>
	 * @Description: please write your description <BR>
	 * @Param:rows
	 */

	public void setRows(List<ProductBean> rows) {
		this.rows = rows;
	}

}

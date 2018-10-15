package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.ProductCommentBean;


public class ProductDetailsList extends Data{
	private List<ProductCommentBean> rows;

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: List<ProductCommentBean> <BR>  
	 */
	
	public List<ProductCommentBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(List<ProductCommentBean> rows) {
		this.rows = rows;
	}
	
	

}

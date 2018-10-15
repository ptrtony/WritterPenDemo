package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.ActionCenterItemBean;

public class ActionCenterList extends Data {
	private List<ActionCenterItemBean> rows;

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: List<ActionCenterItemBean> <BR>  
	 */
	
	public List<ActionCenterItemBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(List<ActionCenterItemBean> rows) {
		this.rows = rows;
	}
	

}

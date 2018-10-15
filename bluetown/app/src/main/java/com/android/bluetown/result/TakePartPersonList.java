package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.TakePartPerson;

public class TakePartPersonList extends Data {
	private List<TakePartPerson> rows;

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: List<TakePartPerson> <BR>  
	 */
	
	public List<TakePartPerson> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(List<TakePartPerson> rows) {
		this.rows = rows;
	}




}

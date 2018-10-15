package com.android.bluetown.result;

import java.util.ArrayList;

import com.android.bluetown.bean.CompanyShowItemBean;

public class CompanyShowData extends Data{
	private ArrayList<CompanyShowItemBean> rows;

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ArrayList<CompanyShowItemBean> <BR>  
	 */
	
	public ArrayList<CompanyShowItemBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(ArrayList<CompanyShowItemBean> rows) {
		this.rows = rows;
	}
	
	

}

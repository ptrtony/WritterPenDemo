package com.android.bluetown.result;

import java.util.ArrayList;

import com.android.bluetown.bean.CompanyInfoPublishBean;

public class CompanyInfoPublishData extends Data{
	private ArrayList<CompanyInfoPublishBean> rows;

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ArrayList<CompanyShowItemBean> <BR>  
	 */
	
	public ArrayList<CompanyInfoPublishBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(ArrayList<CompanyInfoPublishBean> rows) {
		this.rows = rows;
	}
	
	

}

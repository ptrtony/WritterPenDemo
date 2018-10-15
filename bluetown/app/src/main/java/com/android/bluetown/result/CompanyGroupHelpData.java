package com.android.bluetown.result;

import java.util.ArrayList;

import com.android.bluetown.bean.CompanyGrowHelpBean;

public class CompanyGroupHelpData extends Data{
	private ArrayList<CompanyGrowHelpBean> rows;

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ArrayList<CompanyShowItemBean> <BR>  
	 */
	
	public ArrayList<CompanyGrowHelpBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(ArrayList<CompanyGrowHelpBean> rows) {
		this.rows = rows;
	}
	
	

}

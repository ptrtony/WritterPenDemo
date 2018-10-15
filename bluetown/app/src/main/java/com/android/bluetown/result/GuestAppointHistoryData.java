package com.android.bluetown.result;

import java.util.ArrayList;

import com.android.bluetown.bean.GuestAppointmentHistoryItemBean;

public class GuestAppointHistoryData extends Data{
	private ArrayList<GuestAppointmentHistoryItemBean> rows;

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ArrayList<CompanyShowItemBean> <BR>  
	 */
	
	public ArrayList<GuestAppointmentHistoryItemBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(ArrayList<GuestAppointmentHistoryItemBean> rows) {
		this.rows = rows;
	}
	
	

}

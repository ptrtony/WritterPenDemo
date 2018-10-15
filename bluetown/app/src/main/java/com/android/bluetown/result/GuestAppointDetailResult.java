package com.android.bluetown.result;

import com.android.bluetown.bean.GuestAppointDetails;


public class GuestAppointDetailResult extends Result{
	private GuestAppointDetails data;

	/**  
	 * @Title:  getData <BR>  
	 * @Description: please write your description <BR>  
	 * @return: GuestAppointHistoryData <BR>  
	 */
	
	public GuestAppointDetails getData() {
		return data;
	}

	/**  
	 * @Title:  setData <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:data
	 */
	
	public void setData(GuestAppointDetails data) {
		this.data = data;
	}

	
	
	

}

package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.SelfServiceType;


public class SelfServiceTypeResult extends Result{
	private List<SelfServiceType> data;

	/**  
	 * @Title:  getData <BR>  
	 * @Description: please write your description <BR>  
	 * @return: List<E> <BR>  
	 */
	
	public List<SelfServiceType> getData() {
		return data;
	}

	/**  
	 * @Title:  setData <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:data
	 */
	
	public void setData(List<SelfServiceType> data) {
		this.data = data;
	}

	

}

package com.android.bluetown.result;

import com.android.bluetown.result.OrderListResult.Order;


public class OrderDetailsResult extends Result{
	private Order data;

	/**  
	 * @Title:  getData <BR>  
	 * @Description: please write your description <BR>  
	 * @return: OrderDetailsData <BR>  
	 */
	
	public Order getData() {
		return data;
	}

	/**  
	 * @Title:  setData <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:data
	 */
	
	public void setData(Order data) {
		this.data = data;
	}


	
}

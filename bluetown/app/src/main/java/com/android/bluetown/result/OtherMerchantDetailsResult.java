package com.android.bluetown.result;

import com.android.bluetown.bean.OtherMerchant;

/**
 * 其他商家详情
 * 
 * @author shenyz
 * 
 */
public class OtherMerchantDetailsResult extends Result {
	private OtherMerchant data;

	/**
	 * @Title: getData <BR>
	 * @Description: please write your description <BR>
	 * @return: OtherMerchantDetailsData <BR>
	 */

	public OtherMerchant getData() {
		return data;
	}

	/**
	 * @Title: setData <BR>
	 * @Description: please write your description <BR>
	 * @Param:data
	 */

	public void setData(OtherMerchant data) {
		this.data = data;
	}

}

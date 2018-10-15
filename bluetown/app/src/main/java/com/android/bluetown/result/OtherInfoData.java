package com.android.bluetown.result;

import com.android.bluetown.bean.UserInfo;

public class OtherInfoData{
	private ProductListData commoditys;
	private UserInfo userInfo;
	
	/**  
	 * @Title:  getCommoditys <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ProductListData <BR>  
	 */
	
	public ProductListData getCommoditys() {
		return commoditys;
	}
	/**  
	 * @Title:  setCommoditys <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:commoditys
	 */
	
	public void setCommoditys(ProductListData commoditys) {
		this.commoditys = commoditys;
	}
	/**  
	 * @Title:  getUserInfo <BR>  
	 * @Description: please write your description <BR>  
	 * @return: UserInfo <BR>  
	 */
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	/**  
	 * @Title:  setUserInfo <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:userInfo
	 */
	
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	
}

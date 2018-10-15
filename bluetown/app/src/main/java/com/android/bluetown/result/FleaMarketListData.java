package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.TypeBean;

public class FleaMarketListData {
	private ProductListData commodity;
	private List<TypeBean> typeList;
	/**  
	 * @Title:  getCommodity <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ProductListData <BR>  
	 */
	
	public ProductListData getCommodity() {
		return commodity;
	}
	/**  
	 * @Title:  setCommodity <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:commodity
	 */
	
	public void setCommodity(ProductListData commodity) {
		this.commodity = commodity;
	}
	/**  
	 * @Title:  getTypeList <BR>  
	 * @Description: please write your description <BR>  
	 * @return: List<TypeBean> <BR>  
	 */
	
	public List<TypeBean> getTypeList() {
		return typeList;
	}
	/**  
	 * @Title:  setTypeList <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:typeList
	 */
	
	public void setTypeList(List<TypeBean> typeList) {
		this.typeList = typeList;
	}
	
	
	
	
	
	

}

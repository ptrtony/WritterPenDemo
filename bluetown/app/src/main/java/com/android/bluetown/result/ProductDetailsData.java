package com.android.bluetown.result;

import com.android.bluetown.bean.ProductDetails;


public class ProductDetailsData{
	private ProductDetailsList comment;
	private ProductDetails commodity;
	
	/**  
	 * @Title:  getComment <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ProductDetailsList <BR>  
	 */
	
	public ProductDetailsList getComment() {
		return comment;
	}
	/**  
	 * @Title:  setComment <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:comment
	 */
	
	public void setComment(ProductDetailsList comment) {
		this.comment = comment;
	}
	/**  
	 * @Title:  getCommodity <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ProductDetails <BR>  
	 */
	
	public ProductDetails getCommodity() {
		return commodity;
	}
	/**  
	 * @Title:  setCommodity <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:commodity
	 */
	
	public void setCommodity(ProductDetails commodity) {
		this.commodity = commodity;
	}

	
	
	
	

}

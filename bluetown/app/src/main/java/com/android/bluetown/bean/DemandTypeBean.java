package com.android.bluetown.bean;

public class DemandTypeBean {
	/** 版块的图标的资源Id */
	private int imgResId;
	/** 版块的名称 */
	private int modelNameResId;
	private boolean isCheck;
	
	public DemandTypeBean() {
		// TODO Auto-generated constructor stub
	}
	public DemandTypeBean(int imgResId, int modelNameResId, boolean isCheck) {
		super();
		this.imgResId = imgResId;
		this.modelNameResId = modelNameResId;
		this.isCheck = isCheck;
	}
	/**  
	 * @Title:  getImgResId <BR>  
	 * @Description: please write your description <BR>  
	 * @return: int <BR>  
	 */
	
	public int getImgResId() {
		return imgResId;
	}
	/**  
	 * @Title:  setImgResId <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:imgResId
	 */
	
	public void setImgResId(int imgResId) {
		this.imgResId = imgResId;
	}
	/**  
	 * @Title:  getModelNameResId <BR>  
	 * @Description: please write your description <BR>  
	 * @return: int <BR>  
	 */
	
	public int getModelNameResId() {
		return modelNameResId;
	}
	/**  
	 * @Title:  setModelNameResId <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:modelNameResId
	 */
	
	public void setModelNameResId(int modelNameResId) {
		this.modelNameResId = modelNameResId;
	}
	/**  
	 * @Title:  isCheck <BR>  
	 * @Description: please write your description <BR>  
	 * @return: boolean <BR>  
	 */
	
	public boolean isCheck() {
		return isCheck;
	}
	/**  
	 * @Title:  setCheck <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:isCheck
	 */
	
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	
	

}

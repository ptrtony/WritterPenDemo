	package com.android.bluetown.bean;

	/**
	 * 
	 * @ClassName: HomeModelBean
	 * @Description:TODO(HomeActivity 模块的javabean)
	 * @author: shenyz
	 * @date: 2015年7月21日 下午4:50:34
	 * 
	 */
	public class HomeModelBean {
		/** 版块的图标的资源Id */
		private int imgResId;
		/** 版块的名称 */
		private int modelNameResId;
		
		public HomeModelBean() {
			// TODO Auto-generated constructor stub
		}
		
		public HomeModelBean(int modelNameResId) {
			super();
			this.modelNameResId = modelNameResId;
		}

		public HomeModelBean(int imgResId, int modelNameResId) {
			super();
			this.imgResId = imgResId;
			this.modelNameResId = modelNameResId;
		}



		/**
		 * @Title: getImgResId <BR>
		 * @Description: please write your description <BR>
		 * @return: int <BR>
		 */

		public int getImgResId() {
			return imgResId;
		}

		/**
		 * @Title: setImgResId <BR>
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

	}


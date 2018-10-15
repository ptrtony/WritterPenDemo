package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.ParkYellowPageItemBean;
import com.android.bluetown.bean.TypeBean;

public class YellowPageData extends Data{
	private List<TypeBean> typeList;
	
	private List<ParkYellowPageItemBean> rows;

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

	/**  
	 * @Title:  getRows <BR>  
	 * @Description: please write your description <BR>  
	 * @return: List<ParkYellowPageItemBean> <BR>  
	 */
	
	public List<ParkYellowPageItemBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(List<ParkYellowPageItemBean> rows) {
		this.rows = rows;
	}
	
}

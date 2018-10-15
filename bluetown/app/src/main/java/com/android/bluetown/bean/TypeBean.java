package com.android.bluetown.bean;

import java.io.Serializable;

public class TypeBean implements Serializable {
	private String typeName;
	private String tid;
	private String type;

	/**
	 * @Title: getTypeName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTypeName() {
		return typeName;
	}

	/**
	 * @Title: setTypeName <BR>
	 * @Description: please write your description <BR>
	 * @Param:typeName
	 */

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @Title: getTid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTid() {
		return tid;
	}

	/**
	 * @Title: setTid <BR>
	 * @Description: please write your description <BR>
	 * @Param:tid
	 */

	public void setTid(String tid) {
		this.tid = tid;
	}

	/**  
	 * @Title:  getType <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getType() {
		return type;
	}

	/**  
	 * @Title:  setType <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:type
	 */
	
	public void setType(String type) {
		this.type = type;
	}
	
	
}

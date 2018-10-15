package com.android.bluetown.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SelfServiceType implements Serializable {
	private String typeName;
	private String tid;

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

}

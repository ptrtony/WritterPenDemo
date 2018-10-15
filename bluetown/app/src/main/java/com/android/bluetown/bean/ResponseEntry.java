/*
 *******************************************
 * File: ResponseEntry.java
 * Author: Lijc
 * Date: 2015-3-23
 * Company: BlueMobi
 ********************************************/
package com.android.bluetown.bean;

import java.io.Serializable;
/**
 * 
 * @ClassName: ResponseEntry
 * @Description: TODO(描述: 响应)
 * @author chenss
 * @Company: BlueMobi
 * @date 2015-5-20
 * @version V1.0
 */
public class ResponseEntry implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO(描述: )
	 */
	private static final long serialVersionUID = -1542850339620215964L;
	public static final String OK="000000";
	public static final String NO="999999";
	/**
	 * 状态码
	 */
	private String repCode;
	/**
	 * 错误提示信息
	 */
	private String repMsg;
	/**
	 * 数据
	 */
	private String data;
	public String getRepCode() {
		return repCode;
	}
	public void setRepCode(String repCode) {
		this.repCode = repCode;
	}
	public String getRepMsg() {
		return repMsg;
	}
	public void setRepMsg(String repMsg) {
		this.repMsg = repMsg;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}

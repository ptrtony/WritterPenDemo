package com.android.bluetown.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: CompanyGrowHelpBean
 * @Description:TODO(CompanyGrowHelpActivity 模块的CompanyGrowHelpBean)
 * @author: shenyz
 * @date: 2015年7月21日 下午4:50:34
 * 
 */
@SuppressWarnings("serial")
public class CompanyGrowHelpBean implements Serializable {
	/** 发布内容 */
	private String content;
	/** 发布时间 */
	private String createTime;
	private String bid;
	private String title;

	/**
	 * @Title: getContent <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getContent() {
		return content;
	}

	/**
	 * @Title: setContent <BR>
	 * @Description: please write your description <BR>
	 * @Param:content
	 */

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @Title: getCreateTime <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @Title: setCreateTime <BR>
	 * @Description: please write your description <BR>
	 * @Param:createTime
	 */

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @Title: getBid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBid() {
		return bid;
	}

	/**
	 * @Title: setBid <BR>
	 * @Description: please write your description <BR>
	 * @Param:bid
	 */

	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @Title: getTitle <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * @Title: setTitle <BR>
	 * @Description: please write your description <BR>
	 * @Param:title
	 */

	public void setTitle(String title) {
		this.title = title;
	}

}

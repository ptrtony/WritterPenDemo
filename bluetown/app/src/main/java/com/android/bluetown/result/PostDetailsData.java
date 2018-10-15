package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.PostBean;
import com.android.bluetown.bean.ReplyPostBean;

public class PostDetailsData extends Data {
	private PostBean managementInfo;
	private List<ReplyPostBean> rows;

	/**
	 * @Title: getManagementInfo <BR>
	 * @Description: please write your description <BR>
	 * @return: PostBean <BR>
	 */

	public PostBean getManagementInfo() {
		return managementInfo;
	}

	/**
	 * @Title: setManagementInfo <BR>
	 * @Description: please write your description <BR>
	 * @Param:managementInfo
	 */

	public void setManagementInfo(PostBean managementInfo) {
		this.managementInfo = managementInfo;
	}

	/**
	 * @Title: getRows <BR>
	 * @Description: please write your description <BR>
	 * @return: List<ReplyPostBean> <BR>
	 */

	public List<ReplyPostBean> getRows() {
		return rows;
	}

	/**
	 * @Title: setRows <BR>
	 * @Description: please write your description <BR>
	 * @Param:rows
	 */

	public void setRows(List<ReplyPostBean> rows) {
		this.rows = rows;
	}

}

package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.TypeBean;

public class ActionCenterData extends Data{
	private List<TypeBean> typeList;
	private ActionCenterList action;

	/**
	 * @Title: getTypeList <BR>
	 * @Description: please write your description <BR>
	 * @return: List<TypeBean> <BR>
	 */

	public List<TypeBean> getTypeList() {
		return typeList;
	}

	/**
	 * @Title: setTypeList <BR>
	 * @Description: please write your description <BR>
	 * @Param:typeList
	 */

	public void setTypeList(List<TypeBean> typeList) {
		this.typeList = typeList;
	}

	/**
	 * @Title: getAction <BR>
	 * @Description: please write your description <BR>
	 * @return: ActionCenterList <BR>
	 */

	public ActionCenterList getAction() {
		return action;
	}

	/**
	 * @Title: setAction <BR>
	 * @Description: please write your description <BR>
	 * @Param:action
	 */

	public void setAction(ActionCenterList action) {
		this.action = action;
	}

}

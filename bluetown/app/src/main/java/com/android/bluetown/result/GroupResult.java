package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.GroupsBean;

public class GroupResult extends Result {
	private GroupData data;

	/**
	 * @Title: getData <BR>
	 * @Description: please write your description <BR>
	 * @return: CircleData <BR>
	 */

	public GroupData getData() {
		return data;
	}

	/**
	 * @Title: setData <BR>
	 * @Description: please write your description <BR>
	 * @Param:data
	 */

	public void setData(GroupData data) {
		this.data = data;
	}

	public class GroupData extends Data{
		private List<GroupsBean> rows;

		public List<GroupsBean> getRows() {
			return rows;
		}

		public void setRows(List<GroupsBean> rows) {
			this.rows = rows;
		}
	}

}

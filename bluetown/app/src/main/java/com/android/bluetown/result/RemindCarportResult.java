package com.android.bluetown.result;

import java.util.List;
import com.android.bluetown.bean.RemindCarport;

public class RemindCarportResult extends Result {
	private RemindCarportData data;

	/**
	 * @Title: getData <BR>
	 * @Description: please write your description <BR>
	 * @return: RemindCarportData <BR>
	 */

	public RemindCarportData getData() {
		return data;
	}

	/**
	 * @Title: setData <BR>
	 * @Description: please write your description <BR>
	 * @Param:data
	 */

	public void setData(RemindCarportData data) {
		this.data = data;
	}
public class RemindCarportData {
		private List<RemindCarport> stallList;

	

	public List<RemindCarport> getStallList() {
			return stallList;
		}

		public void setStallList(List<RemindCarport> stallList) {
			this.stallList = stallList;
		}
	}
}

package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.MerchantBean;

public class HotMerchantResult extends Result {
	private HotMerchantData data;

	/**
	 * @Title: getData <BR>
	 * @Description: please write your description <BR>
	 * @return: CircleData <BR>
	 */

	public HotMerchantData getData() {
		return data;
	}

	/**
	 * @Title: setData <BR>
	 * @Description: please write your description <BR>
	 * @Param:data
	 */

	public void setData(HotMerchantData data) {
		this.data = data;
	}

	public class HotMerchantData extends Data {
		private List<MerchantBean> rows;

		public List<MerchantBean> getRows() {
			return rows;
		}

		public void setRows(List<MerchantBean> rows) {
			this.rows = rows;
		}

	}

}

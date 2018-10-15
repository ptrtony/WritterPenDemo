package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.HistoryComplaintSuggestBean;

public class HistoryComplaintResult extends Result {
	private HistoryComplaintData data;

	public HistoryComplaintData getData() {
		return data;
	}

	public void setData(HistoryComplaintData data) {
		this.data = data;
	}

	public class HistoryComplaintData extends Data {
		private List<HistoryComplaintSuggestBean> rows;

		public List<HistoryComplaintSuggestBean> getRows() {
			return rows;
		}

		public void setRows(List<HistoryComplaintSuggestBean> rows) {
			this.rows = rows;
		}

	}

}

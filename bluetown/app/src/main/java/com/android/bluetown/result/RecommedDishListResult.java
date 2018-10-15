package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.RecommendDish;

/**
 * 推荐菜的列表
 * 
 * @author shenyz
 * 
 */
public class RecommedDishListResult extends Result {
	public RecommendDishListData data;

	public RecommendDishListData getData() {
		return data;
	}

	public void setData(RecommendDishListData data) {
		this.data = data;
	}

	public class RecommendDishListData extends Data {
		private List<RecommendDish> rows;

		public List<RecommendDish> getRows() {
			return rows;
		}

		public void setRows(List<RecommendDish> rows) {
			this.rows = rows;
		}

	}


}

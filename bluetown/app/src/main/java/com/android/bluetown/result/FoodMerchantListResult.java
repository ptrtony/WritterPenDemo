package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.Merchant;

public class FoodMerchantListResult extends Result{
	private FoodMerchantListData data;
	
	public FoodMerchantListData getData() {
		return data;
	}

	public void setData(FoodMerchantListData data) {
		this.data = data;
	}

	public  class FoodMerchantListData extends Data{
		private List<Merchant> rows;

		public List<Merchant> getRows() {
			return rows;
		}

		public void setRows(List<Merchant> rows) {
			this.rows = rows;
		}
		
	}

}

package com.android.bluetown.result;

import java.util.ArrayList;

import com.android.bluetown.bean.HistoryServiceIteamBean;

public class HistoryServiceIteamData extends Data{
	private ArrayList<HistoryServiceIteamBean> rows;
	public ArrayList<HistoryServiceIteamBean> getRows() {
		return rows;
	}

	/**  
	 * @Title:  setRows <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rows
	 */
	
	public void setRows(ArrayList<HistoryServiceIteamBean> rows) {
		this.rows = rows;
	}
}

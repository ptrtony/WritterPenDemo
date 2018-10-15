package com.android.bluetown.result;

import java.util.List;

import com.android.bluetown.bean.IndexBanner;

public class MessageListResult extends Result {
	private MessageListData data;

	/**  
	 * @Title:  getData <BR>  
	 * @Description: please write your description <BR>  
	 * @return: MessageDetailsData <BR>  
	 */
	
	public MessageListData getData() {
		return data;
	}

	/**  
	 * @Title:  setData <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:data
	 */
	
	public void setData(MessageListData data) {
		this.data = data;
	}
	
	public class MessageListData extends Data{
		public List<IndexBanner> rows;

		public List<IndexBanner> getRows() {
			return rows;
		}

		public void setRows(List<IndexBanner> rows) {
			this.rows = rows;
		}
		
		
		
	} 

}

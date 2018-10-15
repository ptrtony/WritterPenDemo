package com.android.bluetown.result;

import com.android.bluetown.bean.ActionCenterItemBean;

public class ActionCenterDetailsData extends Data{
	private TakePartPersonList users;
	private ActionCenterItemBean actionCenterInfo;
	/**  
	 * @Title:  getUsers <BR>  
	 * @Description: please write your description <BR>  
	 * @return: TakePartPersonList <BR>  
	 */
	
	public TakePartPersonList getUsers() {
		return users;
	}
	/**  
	 * @Title:  setUsers <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:users
	 */
	
	public void setUsers(TakePartPersonList users) {
		this.users = users;
	}
	/**  
	 * @Title:  getActionCenterInfo <BR>  
	 * @Description: please write your description <BR>  
	 * @return: ActionCenterItemBean <BR>  
	 */
	
	public ActionCenterItemBean getActionCenterInfo() {
		return actionCenterInfo;
	}
	/**  
	 * @Title:  setActionCenterInfo <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:actionCenterInfo
	 */
	
	public void setActionCenterInfo(ActionCenterItemBean actionCenterInfo) {
		this.actionCenterInfo = actionCenterInfo;
	}

	
}

package com.android.bluetown.bean;
/**
 * 
 * @ClassName:  HistoryServiceIteamBean   
 * @Description:TODO(历史报修列表的javabean)   
 * @author: shenyz   
 * @date:   2015年8月6日 下午4:18:22   
 *
 */
public class ProcedureApprovalHistoryIteamBean {
	/**园区名称*/
	private String zoneName;
	/**审批时间*/
	private String approveDate;
	/**审批状态*/
	private String approveStatus;
	/**审批内容*/
	private String approveContent;
	/**审批拒绝内容*/
	private String approveRefuseContent;
	
	public ProcedureApprovalHistoryIteamBean() {
		// TODO Auto-generated constructor stub
	}

	public ProcedureApprovalHistoryIteamBean(String zoneName, String approveDate, String approveStatus, String approveContent,
			String approveRefuseContent) {
		super();
		this.zoneName = zoneName;
		this.approveDate = approveDate;
		this.approveStatus = approveStatus;
		this.approveContent = approveContent;
		this.approveRefuseContent = approveRefuseContent;
	}



	/**  
	 * @Title:  getZoneName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getZoneName() {
		return zoneName;
	}

	/**  
	 * @Title:  setZoneName <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:zoneName
	 */
	
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	/**  
	 * @Title:  getApproveDate <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getApproveDate() {
		return approveDate;
	}

	/**  
	 * @Title:  setApproveDate <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:approveDate
	 */
	
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	/**  
	 * @Title:  getApproveStatus <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getApproveStatus() {
		return approveStatus;
	}

	/**  
	 * @Title:  setApproveStatus <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:approveStatus
	 */
	
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	/**  
	 * @Title:  getApproveContent <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getApproveContent() {
		return approveContent;
	}

	/**  
	 * @Title:  setApproveContent <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:approveContent
	 */
	
	public void setApproveContent(String approveContent) {
		this.approveContent = approveContent;
	}

	/**  
	 * @Title:  getApproveRefuseContent <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getApproveRefuseContent() {
		return approveRefuseContent;
	}

	/**  
	 * @Title:  setApproveRefuseContent <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:approveRefuseContent
	 */
	
	public void setApproveRefuseContent(String approveRefuseContent) {
		this.approveRefuseContent = approveRefuseContent;
	}

	
}

package com.android.bluetown.bean;
/**
 * 
 * @ClassName:  HistoryServiceIteamBean   
 * @Description:TODO(历史报修列表的javabean)   
 * @author: shenyz   
 * @date:   2015年8月6日 下午4:18:22   
 *
 */
public class HistoryServiceIteamBean {
	/* "name": null,
      "typeName": "水龙头",
      "businessName": null,
      "dateTime": null,
      "address": "西安",
      "state": "0",
      "type": "1",
      "time": "2015\/2\/5",
      "userId": "1",
      "selection": "1",
      "tell": "18309186014",
      "remark": "备注",
      "createTime": "2015\/8\/7",
      "pid": "1",
      "image1": null,
      "image3": null,
      "image4": null,
      "image5": null,
      "image2": null,
      "rid": "111610000013",
      "grade": null,
      "seatNumber": "bx_111610000014",
      "businessId": "111310000001"*/
	/**报修时间*/
	private String time;
	/**报修故障类型*/
	private String typeName;
	/**报修状态*/
	private String state;
	/**报修状态*/
	private String rid;


	public HistoryServiceIteamBean() {
		// TODO Auto-generated constructor stub
	}

	public HistoryServiceIteamBean(String rid,String time, String typeName, String state) {
		super();
		this.rid = rid;
		this.time = time;
		this.typeName = typeName;
		this.state = state;
	}

	/**  
	 * @Title:  getServiceDate <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getTime() {
		return time;
	}

	/**  
	 * @Title:  setServiceDate <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:serviceDate
	 */
	
	public void getTime(String time) {
		this.time = time;
	}

	/**  
	 * @Title:  getFaultType <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getTypeName() {
		return typeName;
	}

	/**  
	 * @Title:  setFaultType <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:faultType
	 */
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**  
	 * @Title:  getServiceStatus <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getState() {
		return state;
	}

	/**  
	 * @Title:  setServiceStatus <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:serviceStatus
	 */
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

}

package com.android.bluetown.bean;

/**
 * 
 * @ClassName: CompanyGrowHelpBean
 * @Description:TODO(CompanyGrowHelpActivity 模块的CompanyGrowHelpBean)
 * @author: shenyz
 * @date: 2015年7月21日 下午4:50:34
 * 
 */
public class CircleBean {
	private String count;
	private String aid;
	private String state;
	private String gid;
	private String groupImg;
	private String groupName;

	public CircleBean() {
		// TODO Auto-generated constructor stub
	}

	public CircleBean(String count, String aid, String state, String gid,
			String groupImg, String groupName) {
		super();
		this.count = count;
		this.aid = aid;
		this.state = state;
		this.gid = gid;
		this.groupImg = groupImg;
		this.groupName = groupName;
	}

	/**
	 * @Title: getCount <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCount() {
		return count;
	}

	/**
	 * @Title: setCount <BR>
	 * @Description: please write your description <BR>
	 * @Param:count
	 */

	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * @Title: getAid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getAid() {
		return aid;
	}

	/**
	 * @Title: setAid <BR>
	 * @Description: please write your description <BR>
	 * @Param:aid
	 */

	public void setAid(String aid) {
		this.aid = aid;
	}

	/**
	 * @Title: getState <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getState() {
		return state;
	}

	/**
	 * @Title: setState <BR>
	 * @Description: please write your description <BR>
	 * @Param:state
	 */

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @Title: getGid <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getGid() {
		return gid;
	}

	/**
	 * @Title: setGid <BR>
	 * @Description: please write your description <BR>
	 * @Param:gid
	 */

	public void setGid(String gid) {
		this.gid = gid;
	}

	/**
	 * @Title: getGroupImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getGroupImg() {
		return groupImg;
	}

	/**
	 * @Title: setGroupImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:groupImg
	 */

	public void setGroupImg(String groupImg) {
		this.groupImg = groupImg;
	}

	/**
	 * @Title: getGroupName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getGroupName() {
		return groupName;
	}

	/**
	 * @Title: setGroupName <BR>
	 * @Description: please write your description <BR>
	 * @Param:groupName
	 */

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	

}

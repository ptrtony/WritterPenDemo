package com.android.bluetown.bean;

/**
 * 
 * @ClassName: SettingMessageItemBean
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月24日 上午10:26:26
 * 
 */
public class SettingMessageItemBean {
	private String msgUserImg;
	private String userNick;
	/**消息类型*/
	private String msgType;
	private String msgTime;
	private String msgContent;

	public SettingMessageItemBean() {
		// TODO Auto-generated constructor stub
	}

	public SettingMessageItemBean(String msgUserImg, String userNick, String msgType, String msgTime, String msgContent) {
		super();
		this.msgUserImg = msgUserImg;
		this.userNick = userNick;
		this.msgType = msgType;
		this.msgTime = msgTime;
		this.msgContent = msgContent;
	}

	/**
	 * @Title: getMsgUserImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getMsgUserImg() {
		return msgUserImg;
	}

	/**
	 * @Title: setMsgUserImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:msgUserImg
	 */

	public void setMsgUserImg(String msgUserImg) {
		this.msgUserImg = msgUserImg;
	}

	/**
	 * @Title: getUserNick <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getUserNick() {
		return userNick;
	}

	/**
	 * @Title: setUserNick <BR>
	 * @Description: please write your description <BR>
	 * @Param:userNick
	 */

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	/**
	 * @Title: getMsgType <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getMsgType() {
		return msgType;
	}

	/**
	 * @Title: setMsgType <BR>
	 * @Description: please write your description <BR>
	 * @Param:msgType
	 */

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * @Title: getMsgTime <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getMsgTime() {
		return msgTime;
	}

	/**
	 * @Title: setMsgTime <BR>
	 * @Description: please write your description <BR>
	 * @Param:msgTime
	 */

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}

	/**
	 * @Title: getMsgContent <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getMsgContent() {
		return msgContent;
	}

	/**
	 * @Title: setMsgContent <BR>
	 * @Description: please write your description <BR>
	 * @Param:msgContent
	 */

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

}

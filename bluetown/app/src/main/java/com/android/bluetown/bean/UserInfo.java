package com.android.bluetown.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
	
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */   
	
	private static final long serialVersionUID = 1L;
	private String name;
	//state：状态；(0:不是好友;1:是好友)
	private String state;
	private String type;
	private String password;
	private String userId;
	private String createDate;
	private String nickName;
	private String birthday;
	private String tellphone;
	private String sex;
	private String telphone;
	private String email;
	private String umoney;
	private String cfmPwd;
	private String headImg;
	private String integral;


	/**
	 * @Title: getName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getName() {
		return name;
	}

	/**
	 * @Title: setName <BR>
	 * @Description: please write your description <BR>
	 * @Param:name
	 */

	public void setName(String name) {
		this.name = name;
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
	 * @Title: getType <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getType() {
		return type;
	}

	/**
	 * @Title: setType <BR>
	 * @Description: please write your description <BR>
	 * @Param:type
	 */

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @Title: getPassword <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getPassword() {
		return password;
	}

	/**
	 * @Title: setPassword <BR>
	 * @Description: please write your description <BR>
	 * @Param:password
	 */

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @Title: getUserId <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getUserId() {
		return userId;
	}

	/**
	 * @Title: setUserId <BR>
	 * @Description: please write your description <BR>
	 * @Param:userId
	 */

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @Title: getCreateDate <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @Title: setCreateDate <BR>
	 * @Description: please write your description <BR>
	 * @Param:createDate
	 */

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @Title: getNickName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getNickName() {
		return nickName;
	}

	/**
	 * @Title: setNickName <BR>
	 * @Description: please write your description <BR>
	 * @Param:nickName
	 */

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @Title: getBirthday <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getBirthday() {
		return birthday;
	}

	/**
	 * @Title: setBirthday <BR>
	 * @Description: please write your description <BR>
	 * @Param:birthday
	 */

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @Title: getTellphone <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTellphone() {
		return tellphone;
	}

	/**
	 * @Title: setTellphone <BR>
	 * @Description: please write your description <BR>
	 * @Param:tellphone
	 */

	public void setTellphone(String tellphone) {
		this.tellphone = tellphone;
	}

	/**
	 * @Title: getSex <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getSex() {
		return sex;
	}

	/**
	 * @Title: setSex <BR>
	 * @Description: please write your description <BR>
	 * @Param:sex
	 */

	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @Title: getTelphone <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getTelphone() {
		return telphone;
	}

	/**
	 * @Title: setTelphone <BR>
	 * @Description: please write your description <BR>
	 * @Param:telphone
	 */

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	/**
	 * @Title: getEmail <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getEmail() {
		return email;
	}

	/**
	 * @Title: setEmail <BR>
	 * @Description: please write your description <BR>
	 * @Param:email
	 */

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @Title: getUmoney <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getUmoney() {
		return umoney;
	}

	/**
	 * @Title: setUmoney <BR>
	 * @Description: please write your description <BR>
	 * @Param:umoney
	 */

	public void setUmoney(String umoney) {
		this.umoney = umoney;
	}

	/**
	 * @Title: getCfmPwd <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getCfmPwd() {
		return cfmPwd;
	}

	/**
	 * @Title: setCfmPwd <BR>
	 * @Description: please write your description <BR>
	 * @Param:cfmPwd
	 */

	public void setCfmPwd(String cfmPwd) {
		this.cfmPwd = cfmPwd;
	}

	/**
	 * @Title: getHeadImg <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getHeadImg() {
		return headImg;
	}

	/**
	 * @Title: setHeadImg <BR>
	 * @Description: please write your description <BR>
	 * @Param:headImg
	 */

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	/**
	 * @Title: getIntegral <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getIntegral() {
		return integral;
	}

	/**
	 * @Title: setIntegral <BR>
	 * @Description: please write your description <BR>
	 * @Param:integral
	 */

	public void setIntegral(String integral) {
		this.integral = integral;
	}

}

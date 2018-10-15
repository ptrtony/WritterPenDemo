package com.android.bluetown.result;

public class RegisterResult extends Result {
	//注册成功 返回的用户Id
	private String memberId;
	//注册获取验证码
	private String checkCode;
	//登录返回的信息
	private LoginData  data;

	/**
	 * @Title: getMemberId <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */

	public String getMemberId() {
		return memberId;
	}

	/**
	 * @Title: setMemberId <BR>
	 * @Description: please write your description <BR>
	 * @Param:memberId
	 */

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**  
	 * @Title:  getCheckCode <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getCheckCode() {
		return checkCode;
	}

	/**  
	 * @Title:  setCheckCode <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:checkCode
	 */
	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	/**  
	 * @Title:  getData <BR>  
	 * @Description: please write your description <BR>  
	 * @return: RegisterData <BR>  
	 */
	
	public LoginData getData() {
		return data;
	}

	/**  
	 * @Title:  setData <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:data
	 */
	
	public void setData(LoginData data) {
		this.data = data;
	}
	
	
	
	
	
	
	

}

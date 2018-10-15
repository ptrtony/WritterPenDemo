package com.android.bluetown.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

@SuppressWarnings("serial")
// 必须要有这个注释，否则afinal不识别该实体类
@Table(name = "MemberUser")
public class MemberUser implements Serializable {
	private int id;
	public String memberId;
	public String address;
	public String statu;
	public String nickName;
	public String headImg;
	public String name;
	public String sex;
	public String companyName;
	/*** 上面为服务器返回数据，下面为保存登录信息追加的属性 **/
	// 保存的登录信息
	public int userType;
	public String username;
	public String password;
	public String rongtoken;
	public TokenBean token;
	public String ctoken;
	// 激光推送的别名
	public String alias;
	// 多园区参数
	public String gardenId;
	public String hotRegion;

	public MemberUser() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public TokenBean getTokenBean() {
		return token;

	}

	public void setTokenBean(TokenBean token) {
		this.token = token;

	}
	public String getRongToken() {
		return rongtoken;

	}
	
	public void setRongToken(String rongtoken) {
		this.rongtoken = rongtoken;

	}

	public void setCToken(String ctoken) {
		this.ctoken = ctoken;

	}
	public String getCToken() {
		return ctoken;

	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getGardenId() {
		return gardenId;
	}

	public void setGardenId(String gardenId) {
		this.gardenId = gardenId;
	}

	public String getHotRegion() {
		return hotRegion;
	}

	public void setHotRegion(String hotRegion) {
		this.hotRegion = hotRegion;
	}

}

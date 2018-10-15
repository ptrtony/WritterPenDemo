package com.android.bluetown.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;
@SuppressWarnings("serial")
//必须要有这个注释，否则afinal不识别该实体类
@Table(name = "FriendsBean")
public class FriendsBean implements Serializable {
	//好友信息()
	private int id;
	private String fid;	
	private String km;
	private String userId;
	private String headImg;
	private String statu;
	private String nickName;
	
	public FriendsBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	public FriendsBean(String fid,String km, String userId, String headImg,
			String statu, String nickName) {
		super();
		this.fid = fid;
		this.km = km;
		this.userId = userId;
		this.headImg = headImg;
		this.statu = statu;
		this.nickName = nickName;
	}



	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getKm() {
		return km;
	}
	public void setKm(String km) {
		this.km = km;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getStatu() {
		return statu;
	}
	public void setStatu(String statu) {
		this.statu = statu;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
	
	
}

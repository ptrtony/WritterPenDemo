package com.android.bluetown.bean;

//import com.android.bluetown.circie_base.circie_base_ui.adapter.MultiType;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: PostBean
 * @Description:TODO(帖子PostBean)
 * @author: shenyz
 * @date: 2015年8月19日 上午11:32:30
 * 
 */
public class PostBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String headImg;
	private String isCollect;
	private String nickName;
	private List<String> picturesList;
	private List<String> orgImgList;
	private String content;
	private String managementName;
	private String managementNumber;
	private String userId;
	private String shield;
	private String istop;
	private String pictures;
	private String istopTime;
	private String mid;
	private String groupId;
	private String createTime;
	private String isFriend;
	//是否点赞（0未点赞，1已点赞）
	private String isPraise;
	//点赞人数
	private String applyNumber;
	
	public PostBean() {
		// TODO Auto-generated constructor stub
	}

	public PostBean(String headImg, String isCollect, String nickName,
			List<String> picturesList, List<String> orgImgList,String content, String managementName,
			String managementNumber, String applyNumber, String userId,
			String shield, String istop, String pictures, String istopTime,
			String mid, String groupId, String createTime,String isFriend,String isPraise) {
		super();
		this.headImg = headImg;
		this.isCollect = isCollect;
		this.nickName = nickName;
		this.picturesList = picturesList;
		this.orgImgList=orgImgList;
		this.content = content;
		this.managementName = managementName;
		this.managementNumber = managementNumber;
		this.applyNumber = applyNumber;
		this.userId = userId;
		this.shield = shield;
		this.istop = istop;
		this.pictures = pictures;
		this.istopTime = istopTime;
		this.mid = mid;
		this.groupId = groupId;
		this.createTime = createTime;
		this.isFriend=isFriend;
		this.isPraise=isPraise;
	}
	

	public String getIsPraise() {
		return isPraise;
	}

	public void setIsPraise(String isPraise) {
		this.isPraise = isPraise;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public List<String> getPicturesList() {
		return picturesList;
	}

	public void setPicturesList(List<String> picturesList) {
		this.picturesList = picturesList;
	}
	
	

	public List<String> getOrgImgList() {
		return orgImgList;
	}

	public void setOrgImgList(List<String> orgImgList) {
		this.orgImgList = orgImgList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getManagementName() {
		return managementName;
	}

	public void setManagementName(String managementName) {
		this.managementName = managementName;
	}

	public String getManagementNumber() {
		return managementNumber;
	}

	public void setManagementNumber(String managementNumber) {
		this.managementNumber = managementNumber;
	}

	public String getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShield() {
		return shield;
	}

	public void setShield(String shield) {
		this.shield = shield;
	}

	public String getIstop() {
		return istop;
	}

	public void setIstop(String istop) {
		this.istop = istop;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getIstopTime() {
		return istopTime;
	}

	public void setIstopTime(String istopTime) {
		this.istopTime = istopTime;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}


//	@Override
//	public int getItemType() {
//		int type = 0;
//		if (content==null){
//			type = 0;
//		}
//		type =2;
//		return type;
//	}
}

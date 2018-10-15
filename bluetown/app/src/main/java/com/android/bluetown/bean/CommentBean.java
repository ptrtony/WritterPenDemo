package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 商家评论
 * @author shenyz
 *
 */
public class CommentBean implements Serializable {
	private String headImg;
	private String nickName;
	private List<String> commentImgList;
	private String userId;
	private String commentImg;
	private String commentTime;
	private String star;
	private String commentId;
	private String merchantId;
	private String type;
	private String content;

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public List<String> getCommentImgList() {
		return commentImgList;
	}

	public void setCommentImgList(List<String> commentImgList) {
		this.commentImgList = commentImgList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCommentImg() {
		return commentImg;
	}

	public void setCommentImg(String commentImg) {
		this.commentImg = commentImg;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

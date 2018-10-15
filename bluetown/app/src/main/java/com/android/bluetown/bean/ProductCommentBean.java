package com.android.bluetown.bean;

/**
 * 
 * @ClassName: ProductCommentBean
 * @Description:TODO(跳蚤市场-买家详情--评论)
 * @author: shenyz
 * @date: 2015年8月11日 下午5:25:37
 * 
 */
public class ProductCommentBean {
	private String content;
	private String cid;
	private String headImg;
	private String userId;
	private String commentDate;
	private String commodityId;
	private String nickName;
	private String replyDate;
	private String rcontent;

	public ProductCommentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public ProductCommentBean(String content, String cid, String headImg, String userId, String commentDate, String commodityId, String nickName,
			String replyDate, String rcontent) {
		super();
		this.content = content;
		this.cid = cid;
		this.headImg = headImg;
		this.userId = userId;
		this.commentDate = commentDate;
		this.commodityId = commodityId;
		this.nickName = nickName;
		this.replyDate = replyDate;
		this.rcontent = rcontent;
	}


	/**  
	 * @Title:  getContent <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getContent() {
		return content;
	}

	/**  
	 * @Title:  setContent <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:content
	 */
	
	public void setContent(String content) {
		this.content = content;
	}

	/**  
	 * @Title:  getCid <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getCid() {
		return cid;
	}

	/**  
	 * @Title:  setCid <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:cid
	 */
	
	public void setCid(String cid) {
		this.cid = cid;
	}

	/**  
	 * @Title:  getHeadImg <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getHeadImg() {
		return headImg;
	}

	/**  
	 * @Title:  setHeadImg <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:headImg
	 */
	
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	/**  
	 * @Title:  getUserId <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getUserId() {
		return userId;
	}

	/**  
	 * @Title:  setUserId <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:userId
	 */
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**  
	 * @Title:  getCommentDate <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getCommentDate() {
		return commentDate;
	}

	/**  
	 * @Title:  setCommentDate <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:commentDate
	 */
	
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	/**  
	 * @Title:  getCommodityId <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getCommodityId() {
		return commodityId;
	}

	/**  
	 * @Title:  setCommodityId <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:commodityId
	 */
	
	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}

	/**  
	 * @Title:  getNickName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getNickName() {
		return nickName;
	}

	/**  
	 * @Title:  setNickName <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:nickName
	 */
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**  
	 * @Title:  getReplyDate <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getReplyDate() {
		return replyDate;
	}

	/**  
	 * @Title:  setReplyDate <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:replyDate
	 */
	
	public void setReplyDate(String replyDate) {
		this.replyDate = replyDate;
	}

	/**  
	 * @Title:  getRcontent <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	
	public String getRcontent() {
		return rcontent;
	}

	/**  
	 * @Title:  setRcontent <BR>  
	 * @Description: please write your description <BR>  
	 * @Param:rcontent
	 */
	
	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}

	
}

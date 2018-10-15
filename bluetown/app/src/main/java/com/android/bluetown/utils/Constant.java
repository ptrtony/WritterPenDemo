package com.android.bluetown.utils;

/**
 * 
 * @ClassName: Contant
 * @Description:TODO(常量类)
 * @author: shenyz
 * @date: 2015年7月24日 上午11:04:30
 * 
 */
public class Constant {
	// public static String HOST_URL = "http://10.58.178.122:8080/BLUECITY";
	// public static String HOST_URL = "http://10.58.178.115:8080/BLUECITY";
	// public static String HOST_URL = "http://10.58.178.83:8080/BLUECITY";
	// public static String HOST_URL = "http://112.64.173.178/BLUECITY";
//	 public static String HOST_URL = "http://180.153.69.26:8282/BLUECITY";
	// public static String HOST_URL = "http://192.168.25.117:8085/BLUECITY";
//	 public static String HOST_URL = "http://192.168.25.62:8080/BLUECITY";
	// public static String HOST_URL = "http://192.168.25.58:8089/BLUECITY";
//	 public static final String HOST_URL = "http://test1.xydata.cn:88/BLUECITY";
	/**
	 * 新接口和老接口是同时使用  新接口只对于首页和折扣信息
	 */
	public static final String HOST_URL = "https://xapi.smartparks.cn:9010/BLUECITY";//正式老接口
	public static final String HOST_URL1 = "https://xapi.smartparks.cn:8901";//正式新接口
//	public static final String HOST_URL1 = "http://192.168.0.139:8001";//测试新接口
	public static final String HOST_URL2 = "https://easy-mock.com/mock/5b027b6671048314a81f6418/bluetown";
//	public static final String HOST_URL1 = "http://smartparkapi.dolphintek.cn:8901";//新接口测试
//	public static final String HOST_URL = "http://smartparkapi.dolphintek.cn:8083/BLUECITY";//测试老接口
	public static final String WEB_BASE_PATH = "file:///android_asset/dist/index.html";//服务器
//	public static final String WEB_BASE_URL = "http://bluetown-h5.era3.net";
	public static final String WEB_BASE_URL = "http://192.168.0.121:8080";//本地

	/** 企业用户 */
	public static int COMPANY_USER = 1;
	/** 普通用户 */
	public static int NORMAL_USER = 2;
	/** 商户 */
	public static int MERCHANT = 3;
	/** 遊客 */
	public static int VISITOR = 4;
	/**
	 * 刷新首页数据
	 */
	public static int HOME_REFRESH = -1;
	/** 5：关注，6：收藏，7：点赞 8:加入圈子 ,9:报名,10：收藏商家 */
	public static int ATTENTION = 5;
	public static int COLLOCT = 6;
	public static int PRIASE = 7;
	public static int ADD_CIRCLE = 8;
	public static int TAKE_PART_IN = 9;
	public static int COLLECT_MERCHANT = 10;
	public static final String[] TIME = new String[] { "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
			"13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00",
			"19:30", "20:00", "20:30", "21:00", "21:30" };
	/**
	 * 日历中显示的天数
	 */
	public static final int NUM_OF_DAYS = 6;
	/** 添加好友 */
	public static int ADD_FRIENDS = 0;
	/** 刪除好友 */
	public static int DELETE_FRIENDS = 1;
	public static final int GET_PRODUCT_TAG = 1;// 选择产品分类筛选标签
	public static final int GET_DATE = 0;
	public static final int GET_NORMAL_TAG = 2;// 选择默认排序筛选标签
	/** 请求成功 */
	public static String HTTP_SUCCESS = "000000";
	/** 请求错误 */
	public static String HTTP_ERROR = "999999";
	/** 请求查询无数据 */
	public static String HTTP_NO_DATA = "111111";
	public static String LOAD_URL="load_url";
	public static String PAGE_TITILE = "title";
	/**
	 * 请求超时
	 */
	public static int TIME_OUT = 30000;

	// 额外类
	public static class Extra {
		public static final String IMAGES = "com.android.bluetown.IMAGES";
		public static final String IMAGE_POSITION = "com.android.bluetown.IMAGE_POSITION";
	}

	public static class Interface {
		public static String WEATHER_FORECAST = "http://www.weather.com.cn/data/cityinfo/101010100.html";
		// 注册
		public static String REGISTER = "/mobi/member/MobiMemberAction/RegisterInfo.mobi";
		// 获取手机验证码
		public static String REGISTER_SEND_MSG = "/mobi/member/MobiMemberAction/sendMessage.mobi";
		// 登录
		public static String LOGIN = "/mobi/member/MobiMemberAction/LoginInfo.mobi";
		// 忘记密码找回密码--验证码
		public static String FORGOT_PWD_AUTH_CODE = "/mobi/member/MobiMemberAction/sendCheckMsm.mobi";
		// 忘记密码找回密码
		public static String FORGOT_PWD = "/mobi/member/MobiMemberAction/updatePwd.mobi";
		// 首页轮播
		public static String INDEX_BANNER = "/mobi/garden/MobiAdviertisementAction/getTopList.mobi";
		// 企业展示
		public static String COMPANY_SHOW = "/mobi/enterprise/MobiBusinessAction/queryBusinessList.mobi";
		// 企业类型
		public static String QUERYBUSINESS_TYPELIST = "/mobi/enterprise/MobiBusinessAction/queryBusinessTypeList.mobi";
		// 企业详情
		public static String COMPANY_DETAILS = "/mobi/enterprise/MobiBusinessAction/detail.mobi";
		// 访客预约
		public static String ADD_GUEST_APPOINT = "/mobi/garden/MobiMakingAppointAction/addAppoint.mobi";
		// 访客预约历史
		public static String GUEST_APPOINT_LIST = "/mobi/garden/MobiMakingAppointAction/queryAppointList.mobi";
		// 访客预约状态是否成功
		public static String GUEST_APPOINT_DETAILS = "/mobi/garden/MobiMakingAppointAction/getAppointById.mobi";
		// 自助报修信息
		public static String ADD_SELF_SERVICE = "/mobi/garden/MobiRepairsAction/addRepair.mobi";
		// 自助报修列表
		public static String SELF_SERVICE_TYPE = "/mobi/garden/MobiRepaisTypeAction/queryRepairsTypeList.mobi";
		// 自助报修历史列表
		public static String GETREPAIR_LIST = "/mobi/garden/MobiRepairsAction/getRepairList.mobi";
		// 报修评分
		public static String REPAIRGRADE = "/mobi/garden/MobiRepairsAction/repairGrade.mobi";
		// 自助报修历史列表
		public static String GETREPAIRS_DETAIL = "/mobi/garden/MobiRepairsAction/getRepairsDetail.mobi";
		// 剩余车位
		public static String REMIND_CARPORT = "/mobi/garden/MobiMakingAppointAction/getRulerInfo.mobi";
		//商家详情
		public static String BUSINESS_DETAILS = "/#/storeDetail/type/0/";
		//商家列表
		public static String MERCHANT_LISTS = "#/merchantList";
		//选择车位区域
		public static final String ParkingLots = "/api/Parking/GetParkingLots";

		public static final String PARKIN_ALLOW_RENT = "/api/parking/AllowRent";
		// 跳蚤市场商品列表
		public static String FLEA_MARKERT_LIST = "/mobi/deal/MobiCommodityAction/getCommodityList.mobi";
		// 跳蚤市场商品列表--商品详情
		public static String PRODUCT_DETAILS = "/mobi/deal/MobiCommodityAction/getCommodityDetail.mobi";
		// 商品评论
		public static String PRODUCT_COMMENT = "/mobi/deal/MobiCommodityAction/comment.mobi";
		// 商品回复
		public static String PRODUCT_REPLY = "/mobi/deal/MobiCommodityAction/reply.mobi";
		// 他人资料
		public static String OTHERS = "/mobi/deal/MobiCommodityAction/getUserDetail.mobi";
		// 发布商品
		public static String PUBLISH_PRODUCT = "/mobi/deal/MobiCommodityAction/createCommodity.mobi";
		// 企业成长帮助
		public static String COMPANY_GROUP_INFO_LIST = "/mobi/enterprise/MobiBusinessGrowthAction/queryBusinessGrowthList.mobi";
		// 企业需求发布列表信息
		public static String COMPANY_DEMAND_PUBLISH_LIST = "/mobi/enterprise/MobiBusinessNeedAction/queryBusinessNeedList.mobi";
		// 企业需求发布
		public static String COMPANY_DEMAND_PUBLISH = "/mobi/enterprise/MobiBusinessNeedAction/addBusinessNeed.mobi";

		// 投诉建议-投诉建议信息录入
		public static String SUGGESTION = "/mobi/garden/MobiSuggestionAction/addSuggestion.mobi";
		// 投诉建议-投诉建议历史列表
		public static String SUGGESTION_HISTORY_LIST = "/mobi/garden/MobiSuggestionAction/addSuggestion.mobi";
		// 投诉建议-投诉建议详情
		public static String SUGGESTION_DETAILS = "/mobi/garden/MobiSuggestionAction/addSuggestion.mobi";

		// 园区黄页
		public static String YELLOW_PAGE_LIST = "/mobi/garden/MobiYellowAction/queryYellowList.mobi";
		// 活动中心信息列表(附加查询)
		public static String ACTION_CENTER_LIST = "/mobi/action/MobiActionCenterAction/getActionCenterList.mobi";
		// 活动详情
		public static String ACTION_CENTER_DETAILS = "/mobi/action/MobiActionCenterAction/getActionCenterById.mobi";
		// 圈子列表
		public static String CIRCLE_LIST = "/mobi/circle/MobiGroupManagementAction/getGroupList.mobi";
		// 圈子详情
		public static String CIRCLE_DETAILS = "/mobi/circle/MobiGroupManagementAction/getGroupInfo.mobi";
		// 收藏该信息
		public static String CIRCLE_TYPEW_INFO = "/mobi/common/MobiCollectAction/addOrDeleteCollect.mobi";
		// 园区公告
		public static String ZONE_NOTICE = "/mobi/garden/MobiGardenNoticeAction/queryGardenNoticeList.mobi";
		// 发帖
		public static String PUBLISH_POST = "/mobi/circle/MobiGroupManagementAction/addManagement.mobi";
		// 跟帖
		public static String FOLLOW_POST = "/mobi/circle/MobiGroupManagementAction/addManagementComment.mobi";
		// 帖子详情
		public static String POST_DETAILS = "/mobi/circle/MobiGroupManagementAction/getManagementInfo.mobi";
		// 个人资料--个人资料详情
		public static String USER_INFO = "/mobi/member/MobiMemberUserAction/getMemberUserDetail.mobi";
		// 个人资料--基础资料录入
		public static String MODIFY_USER_INFO = "/mobi/member/MobiMemberUserAction/addMemberUser.mobi";
		// 手续报批
		public static String PROCEDURE_APPROVAL = "/mobi/garden/MobiRepairsAction/addGardenApproval.mobi";
		public static String PROCEDURE_APPROVAL_TYPE = "/mobi/garden/MobiRepairsAction/getGardenApprovalTypeList.mobi";
		public static String PROCEDURE_HISTORY_APPROVAL = "/mobi/garden/MobiRepairsAction/getGardenApprovalList.mobi";
		// 投诉建议
		public static String ADD_COMPLAINT = "/mobi/garden/MobiSuggestionAction/addSuggestion.mobi";
		// 历史投诉建议和投诉建议详情
		public static String COMPLAINT_HISTORY_LIST = "/mobi/garden/MobiSuggestionAction/getSuggestionList.mobi";
		// 天气查询
		public static String WEATHER = "/mobi/weather/MobiGetWeatherAction/getWeather.mobi";
		// 交友--用户群创建/修改群资料
		public static String CREATE_GROUP = "/mobi/member/MobiMemberUserAction/saveOrUpdate.mobi";
		// 交友--邀请好友加入群
		public static String INVITATION_IN_GROUP = "/mobi/member/MobiMemberUserAction/invitationInGroup.mobi";
		// 交友--添加好友
		public static String ADD_FRIEND = "/mobi/rongyun/MobiUserFriendAction/addFriend.mobi";
		// 交友--查看好友列表
		public static String FRIEND_LIST = "/mobi/rongyun/MobiUserFriendAction/findFriend.mobi";
		// 交友--查看群列表
		public static String GROUP_LIST = "/mobi/member/MobiMemberUserAction/findGroup.mobi";
		// 交友--群成员
		public static String GROUP_MEMBER = "/mobi/member/MobiMemberUserAction/findGroupMember.mobi";
		// 交友--群详情
		public static String GROUP_DETAIL = "/mobi/member/MobiMemberUserAction/findGroupDetail.mobi";
		// 交友--附近的人
		public static String NEARBY_PEOPLE = "/mobi/rongyun/MobiUserFriendAction/nearbyPeople.mobi";
		// 交友--坐标更新
		public static String UPDATE_COORD = "/mobi/rongyun/MobiUserFriendAction/updateCoord.mobi";
		// 商品下架
		public static String PRODUCT_SALE_OUT = "/mobi/deal/MobiCommodityAction/commodityUp.mobi";
		// 踢出成员用户/用户自己退群
		public static String DELETE_MEMBER = "/mobi/member/MobiMemberUserAction/deleteMember.mobi";
		// 交友--申请加入群
		public static String APPLY_FOR_IN_GROUP = "/mobi/member/MobiMemberUserAction/applyForInGroup.mobi";
		// 交友--同意 / 拒绝
		public static String ACCEPT_REQUEST = "/mobi/rongyun/MobiUserFriendAction/acceptRequest.mobi";
		// 交友--同意 / 拒绝进群
		public static String ACCEPT_OR_NOT_GROUP = "/mobi/member/MobiMemberUserAction/acceptOrNotGroup.mobi";
		// 交友--添加好友时消息列表
		public static String WAIT_FRIEND_LIST = "/mobi/rongyun/MobiUserFriendAction/getWaitFriend.mobi";
		// 交友--清空验证消息列表
		public static String CLEAR_ADD_MESSAGE = "/mobi/rongyun/MobiUserFriendAction/clearAddMessage.mobi";
		// 推送消息列表
		public static String PUSH_MESSAGE_LIST = "/mobi/rongyun/MobiUserFriendAction/getPushMessageList.mobi";
		// 解散群
		public static String GROUP_MASTER_DISMISS = "/mobi/member/MobiMemberUserAction/dismissGroup.mobi";
		// 附件餐饮
		// 商家列表
		public static String MERCHAT_LIST = "/mobi/member/MobiMemberUserAction/dismissGroup.mobi";
		// 享食光接口
		// 分类的城市列表
		public static String URL_CITY_CODE = "/mobi/MerchantsService/queryCityCodeByCityName.mobi";
		// 获取美食商家列表
		public final static String URL_MERCHANT_LIST = "mobi/MerchantsService/queryMerchantsList.mobi";
		public final static String URL_DISH_LIST = "mobi/MerchantsService/queryAllTypes.mobi";
		public final static String URL_AREA_LIST = "mobi/MerchantsService/queryAllBusinessCircles.mobi";
		// 周边餐饮-美食类一级分类
		public final static String FOOD_CLASS_TYPE = "/mobi/merchant/MobiMerchantAction/getRestaurantTypeList.mobi";
		// 周边餐饮-美食类-全城分类（商家热门商圈查询）
		public final static String FOOD_CITY_TYPE = "/mobi/merchant/MobiMerchantAction/getRegionList.mobi";
		// 其他商家列表
		public final static String OTHER_MERCHANT_LIST = "/mobi/merchant/MobiMerchantOtherAction/getMerchantOtherList.mobi";
		// 主页热门商家列表
		public final static String HOT_MERCHANT_LIST = "/mobi/merchant/MobiMerchantAction/getHotMerchantList.mobi";
		// 周边餐饮-美食类-商家列表
		public final static String FOOD_MERCHANT_LIST = "/mobi/merchant/MobiMerchantAction/getRestaurantList.mobi";
		// 周边餐饮-其他商家详情
		public final static String OTHER_MERCHANT_DETAILS = "/mobi/merchant/MobiMerchantOtherAction/getMerchantOtherById.mobi";
		// 周边商家-美食-商家详情
		public final static String MERCHANT_DETAILS = "/mobi/merchant/MobiMerchantAction/getRestaurantById.mobi";
		// 周边商家-食堂-商家详情
		public final static String CANTEEN_DETAILS = "/mobi/merchant/MobiRestaurantAction/getRestaurantById.mobi";
		// 园区列表
		public final static String GARDEN_LIST = "/mobi/merchant/MobiMerchantAction/getCityGardenList.mobi";
		// 食堂列表
		public final static String CANTEEN_LIST = "/mobi/merchant/MobiRestaurantAction/getRestaurantList.mobi";
		// 点评列表
		public final static String COMMENT_LIST = "/mobi/merchant/MobiMerchantAction/getCommentList.mobi";
		// 食堂推荐菜
		public final static String CANTEEN_RECOMMEMD_DISH = "/mobi/merchant/MobiRestaurantAction/getMenuList.mobi";
		// 食堂推荐菜类型
		public final static String CANTEEN_RECOMMEMD_DISH_TYPE = "/mobi/merchant/MobiRestaurantAction/getMenuTypeList.mobi";
		// 食堂推荐菜详情(没用)
		public final static String CANTEEN_RECOMMEMD_DISH_DETAILS = "/mobi/merchant/MobiRestaurantAction/getMenuById.mobi";
		// 美食推荐菜列表
		public final static String FOOD_RECOMMEMD_DISH = "/mobi/merchant/MobiMerchantAction/getMenuList.mobi";
		// 美食推荐菜类型
		public final static String FOOD_RECOMMEMD_DISH_TYPE = "/mobi/merchant/MobiMerchantAction/getMenuTypeList.mobi";
		// 美食推荐菜详情(没用)
		public final static String FOOD_RECOMMEMD_DISH_DETAILS = "/mobi/merchant/MobiMerchantAction/getMenuById.mobi";
		// 订餐选桌子
		public final static String ORDER_DISH_SELETE_TABLE = "/mobi/order/MobiCustOrderAction/getCustByTable.mobi";
		// 下订单
		public final static String PLACE_AN_ORDER = "/mobi/order/MobiCustOrderAction/addCustOrder.mobi";
		//美食商家折扣
		public final static String MERCHANT_DISCOUNT = "/api/Merchant/DiscountSetting";
		//充值 - 微信下单
		public final static String BILLACTION_ADD_WEIXIN = "/mobi/settlement/purse/BillAction/addWeixinBill.mobi";
		// 我的订单
		public final static String ORDER_LIST = "/mobi/order/MobiCustOrderAction/getCustOrderList.mobi";
		// 订单详情
		public final static String ORDER_DETAILS = "/mobi/order/MobiCustOrderAction/getCustOrderById.mobi";
		// 取消订单
		public final static String CANCEL_ORDER = "/mobi/order/MobiCustOrderAction/offOrder.mobi";
		// 退款
		public final static String REFUND = "/mobi/order/MobiCustOrderAction/refund.mobi";
		// 收藏的商家
		public final static String COLLECT_MERCHANT = "/mobi/order/MobiCustOrderAction/getCollectList.mobi";
		// 首页-消息中心
		public final static String MESSAGE_LIST = "/mobi/garden/MobiHomePageAction/getMessageList.mobi";
		// 首页-消息中心详情
		public final static String MESSAGE_DETAILS = "/mobi/garden/MobiHomePageAction/getMessageDetail.mobi";
		// 首页-实时监控列表
		public final static String GARDEN_VIDEO_LIST = "/mobi/garden/MobiGardenVideoAction/getGardenVideo.mobi";
		// 充值-下订单
		public final static String BillAction_add = "/mobi/settlement/purse/BillAction/add.mobi";
		//充值-关闭订单
		public final static String CLOSE_BILL= "/mobi/settlement/purse/BillAction/closeBill.mobi";
		//充值-验证微信支付成功
		public final static String WX_CHECK_ORDER = "/mobi/settlement/purse/BillAction/weixinOrderQuery.mobi";
		// 充值-成功返回
		public final static String BillAction_successPayment = "/mobi/settlement/purse/BillAction/successPayment.mobi";
		// 生成支付码
		public static final String PaymentCodeAction_createCode = "/mobi/settlement/PaymentCodeAction/createCode.mobi";
		// 扫码获取用户信息
		public static final String PaymentCodeAction_selectByCode = "/mobi/settlement/PaymentCodeAction/selectByCode.mobi";
		// 用户输入金额，密码给钱，商家输入金额直接收钱
		public static final String PaymentCodeAction_confirmPayment = "/mobi/settlement/PaymentCodeAction/confirmPayment.mobi";
		// 设置支付密码
		public static final String MobiMemberAction_payPassword = "/mobi/member/MobiMemberAction/payPassword.mobi";
		// 设置手势密码
		public static final String MobiMemberAction_handPassword = "/mobi/member/MobiMemberAction/handPassword.mobi";
		// 停车包月—生成订单
		public static final String ParkingOrderAction_add = "/mobi/settlement/purse/ParkingOrderAction/add.mobi";
		// 停车包月—历史记录
		public static final String ParkingOrderAction_queryOrdrList = "/mobi/settlement/purse/ParkingOrderAction/queryOrdrList.mobi";
		// 获取支付token
		public static final String BillAction_generateToken = "/mobi/settlement/purse/BillAction/generateToken.mobi";
		// 支付接口
		public static final String BillAction_confirmPayment = "/mobi/settlement/purse/BillAction/confirmPayment.mobi";
		// 查询用户
		public static final String MobiMemberAction_getMemberUserByAccount = "/mobi/member/MobiMemberAction/getMemberUserByAccount.mobi";
		// 转账
		public static final String PaymentCodeAction_transferAccounts = "/mobi/settlement/PaymentCodeAction/transferAccounts.mobi";
		// 用户认证
		public static final String MobiMemberAction_checkMember = "/mobi/member/MobiMemberAction/checkMember.mobi";
		// 查询账单列表
		public static final String BillAction_queryBillList = "/mobi/settlement/purse/BillAction/queryBillListByCustomer.mobi";
		// 小额免密支付
		public static final String MobiMemberAction_changeStatus = "/mobi/member/MobiMemberAction/changeStatus.mobi";
		// 在线订单确认支付
		public static final String BillAction_confirmPaymentOfOrder = "/mobi/settlement/purse/BillAction/confirmPaymentOfOrder.mobi";
		// 月账单
		public static final String BillAction_queryBillListByCustomerOfMonth = "/mobi/settlement/purse/BillAction/queryBillListByCustomerOfMonth.mobi";
		// 用户信息修改
		public static final String MobiMemberAction_updateSelfMessage = "/mobi/member/MobiMemberAction/updateSelfMessage.mobi";
		// 用户二维码 （不变）
		public static final String PaymentCodeAction_selectByDistinguishCode = "/mobi/settlement/PaymentCodeAction/selectByDistinguishCode.mobi";
		// 查询用户余额和昨日账单收入
		public static final String BillAction_queryMoneyInformation = "/mobi/settlement/purse/BillAction/queryMoneyInformation.mobi";
		// 转账记录
		public static final String BillAction_queryBillListOfZ = "/mobi/settlement/purse/BillAction/queryBillListOfZ.mobi";
		// 下载xml
		public static final String MobiCollectAction_downloadXml = "/mobi/common/MobiCollectAction/downloadXml.do";
		// 更换密码
		public static final String MobiMemberAction_updatePassWord = "/mobi/member/MobiMemberAction/updatePassWord.mobi";
		// 圈子（查看所有）
		public static final String MobiGroupManagementAction_getAllManagement = "/mobi/circle/MobiGroupManagementAction/getAllManagement.mobi";
		// 固定车办理接口调用成功
		public static final String ParkingOrderAction_successAddOrder = "/mobi/settlement/purse/ParkingOrderAction/successAddOrder.mobi";
		// 下载xml（json）
		public static final String MobiCollectAction_getVersionXml = "/mobi/common/MobiCollectAction/getVersionXml.mobi";
		// 锁定账号
		public final static String BillAction_lockingUser = "/mobi/settlement/purse/BillAction/lockingUser.mobi";
		// 获取所有道闸
		public final static String MobiMakingIncreaseAction_queryMakingIncreaseList = "/mobi/garden/MobiMakingIncreaseAction/queryMakingIncreaseList.mobi";
		// 根据mid查询车位信息
		public final static String ParkingOrderAction_getRulerInfo = "/mobi/settlement/purse/ParkingOrderAction/getRulerInfo.mobi";
		// 判断是否可以临停预约
		public final static String MobiMakingAppointAction_isAdd = "/mobi/garden/MobiMakingAppointAction/isAdd.mobi";
		// 更多公告
		public final static String MobiHomePageAction_queryAllHomePagePictureList = "/mobi/garden/MobiHomePageAction/queryAllHomePagePictureList.mobi";
		// 更新设备识别码
		public static final String MobiMemberAction_updateEquipment = "/mobi/member/MobiMemberAction/updateEquipment.mobi";
		// 获取识别码验证码
		public static final String MobiUserAction_repeat = "/mobi/settlement/MobiUserAction/repeat2.mobi";
		// 查询用户是否屏蔽
		public static final String MobiMemberAction_checkStateByUserId = "/mobi/member/MobiMemberAction/checkStateByUserId.mobi";
		// 广告跳转
		public static final String MobiAdviertisementAction_getAdvierisement = "/mobi/garden/MobiAdviertisementAction/getAdvierisement.do";
		// 获取LLid
		public static final String MenJinAction_getSdkKeyAndLLid = "/mobi/menjin/MenJinAction/getSdkKeyAndLLid.mobi";

		//上传图片
		public static final String FILE_UPLOAD_PICTURE= "/api/File/ImgUpload";
		//办事指南
		public static final String POLICY_NEW_NOTIFY = "/#/policyList";
		//深蓝公寓验证
		public static final String BLUE_APP_AUTHRIENTY = "/api/Authen/IsBlueUser";
		//身份验证
		public static final String IDENTITY_AUTHENTICATION = "/api/Authen/SubmitAutenInfo";
		//
		/**
		 * 银联支付
		 */
		public static final String URL_UMS = "https://mpos.quanminfu.com:8018/umsFrontWebQmjf/umspay";
		public static final String URL_UMS_CALLBACK = "http://my.html";
		public static final String URL_UMS_QUERY = "UmspayService/queryUmspayOrder.mobi";
		public static final String URL_UMS_SUCCESS = "respCode=0000";
		public static final String URL_UMS_ORDER = "UmspayService/createUmspayOrder.mobi";
		/**
		 * 首页
		 */
		public static final String URL_HOME = "/api/home/index";

		/**
		 * 微信支付
		 */
		// appid
		// 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data
		// android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
		public static final String APP_ID = "wx5140001f6ab0074c";

		// 商户号
		public static final String MCH_ID = "1244318402";

		// API密钥，在商户平台设置
		public static final String API_KEY = "xsg8482f47c249a6b2e84a402bf86xsg";
		/**
		 * alipay
		 */
		// 支付
		public static final String ALI_PAY = "/mobi/order/MobiCustOrderAction/addPayWay.mobi";
		// 支付宝成功的回调
		public static final String CALL_BACK_SERVER = "/common/AlipayService/receiveAlipayNoticeOfRecharge.mobi";
		//支付宝成功的回调 测试库
//		public static final String CALL_BACK_SERVER = "https://apitest.smartpark.cn/api/pay/notify";
 	}

	/**
	 * 
	 * @ClassName: ListStatus
	 * @Description:TODO(数据加载的状态)
	 * @author: shenyz
	 * @date: 2015年8月5日 上午11:37:11
	 * 
	 */
	public static class ListStatus {
		public static final int REFRESH = 1;
		public static final int LOAD = 2;
		public static final int INIT = 3;
	}

}

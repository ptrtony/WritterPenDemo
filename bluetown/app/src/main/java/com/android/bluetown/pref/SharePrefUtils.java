package com.android.bluetown.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @ClassName: SharePrefUtils
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月4日 上午10:48:14
 * 
 */
public class SharePrefUtils {
	/** 保存文件的名称 */
	public static final String FILE_NAME = "userInfo";
	/** SharedPreferences对象 */
	public static SharedPreferences mPreferences;
	/** 是否第一次使用该app */
	public static final String FIRST_USER = "isFirst";
	
	/** 单次最多发送图片数 */
	public static final int MAX_IMAGE_SIZE = 6;
	/** 发布时图片 首选项:临时图片 */
	public static final String PREF_TEMP_IMAGES = "pre_tem_imges";
	/** 当前选择的园区 */
	public static final String GARDEN = "garden";
	/** 当前选择的园区Id */
	public static final String GARDEN_ID = "gardenId";
	/** 用户退出状态的设置 */
	public static final String USER_STATE = "userState";
	/** token */
	public static final String RONG_TOKEN = "rong_token";
	/** token */
	public static final String TOKEN = "token";
	/** 密码 */
	public static final String PASSWORD = "password";
	/** 手势密码 */
	public static final String HANDPASSWORD = "handpassword";
	/** 支付密码 */
	public static final String PAYPASSWORD = "paypassword";
	/** 身份证正面 */
	public static final String PPID = "ppId";
	/** 身份证反面 */
	public static final String OOID = "ooId";
	/** 所属公司敲章证明 */
	public static final String STAMPIMG = "stampImg";
	/** 认证状态 */
	public static final String CHECKSTATE = "checkState";
	/** 身份证号*/
	public static final String IDCARD = "idcard";
	/** 真实姓名*/
	public static final String REALNAME = "realname";
	/** 公司名字*/
	public static final String COMPANYNAME = "companyname";
	/** 小额免密*/
	public static final String NOPASSWORDPAY = "nopasswordpay";
	/** 小额免密金额*/
	public static final String NOPASSWORDPAY_COUNT = "nopasswordpay_count";
	/** 头像*/
	public static final String HEAD_IMG = "head_img";
	/** 昵称*/
	public static final String NICK_NAME = "nick_name";
	/** 是否推送*/
	public static final String IS_PUSH = "is_push";
	/** 首页大图*/
	public static final String HEADIMG = "headImg";
	/** 用户识别码*/
	public static final String CODE = "code";
	/** 支付密码次数*/
	public static final String PAY_NUM = "payNum";
	/** LLId*/
	public static final String LL_ID = "llId";
	/** keyList*/
	public static final String KEY_LIST = "key_list";
	/** keyList过期时间*/
	public static final String KEY_LIST_TIME = "key_list_time";
	/**  选择用户类型*/
	public static final String SELECT_USER_TYPE = "select_user_type";
	/** xml*/
	public static final String APPOINT_CARNUM = "appointcarnum";
	public static final String APPOINT_INFO = "appointinfo";
	public static final String PARKING_MONTH_INFO = "parkingmonthinfo";
	public static final String CHECK_MEMBER = "checkmember";
	
	/**
	 * 定位信息
	 * 
	 * @param context
	 */
	public static final String CITY = "city";
	public static final String PROVINCE = "province";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String ADDRESS = "addr";

	public SharePrefUtils(Context context) {
		// TODO Auto-generated constructor stub
		mPreferences = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setBoolean(String key, boolean value) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key, boolean value) {
		return mPreferences.getBoolean(key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setString(String key, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key, String value) {
		return mPreferences.getString(key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setInt(String key, int value) {
		Editor editor = mPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key, int value) {
		return mPreferences.getInt(key, value);
	}

	public void remove(String key) {
		mPreferences.edit().remove(key).commit();
	}
}

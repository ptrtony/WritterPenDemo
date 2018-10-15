/**
 * 
 */
package com.android.bluetown.monitor.callbacks;

/**
 * 消息id常量类
 * 
 * @author zhoudaihui
 * 
 */
public interface MsgIds {
	/**
	 * 获取控制中心列表成功
	 */
	int GET_C_F_NONE_SUC = 7;
	/**
	 * 获取控制中心列表失败
	 */
	int GET_C_F_NONE_FAIL = 8;
	/**
	 * 调用getControlUnitList失败
	 */
	int GET_CU_F_CU_FAIL = 9;
	/**
	 * 调用getRegionListFromCtrlUnit失败
	 */
	int GET_R_F_C_FAIL = 10;
	/**
	 * 调用getCameraListFromCtrlUnit失败
	 */
	int GET_C_F_C_FAIL = 11;
	/**
	 * 从控制中心获取下级资源列表成功
	 */
	int GET_SUB_F_C_SUC = 12;
	/**
	 * 从控制中心获取下级资源列表成失败
	 */
	int GET_SUB_F_C_FAIL = 13;
	/**
	 * 调用getRegionListFromRegion失败
	 */
	int GET_R_F_R_FAIL = 14;
	/**
	 * 调用getCameraListFromRegion失败
	 */
	int GET_C_F_R_FAIL = 15;
	/**
	 * 从区域获取下级列表成功
	 */
	int GET_SUB_F_R_SUC = 16;
	/**
	 * 从区域获取下级列表失败
	 */
	int GET_SUB_F_R_FAILED = 17;

}

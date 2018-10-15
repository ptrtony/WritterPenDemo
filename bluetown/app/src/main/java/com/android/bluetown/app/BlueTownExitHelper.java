package com.android.bluetown.app;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

/**
 * 
 * @ClassName:  BlueTownExitHelper   
 * @Description:TODO(完全退出WisdomCityApplication的帮助类)   
 * @author: shenyz   
 * @date:   2015年7月30日 下午3:51:49   
 *
 */
public class BlueTownExitHelper {
	/**WisdomCityExitHelper对象*/
	private static BlueTownExitHelper instance;
	/** 存放Activity的listview */
	private static List<Activity> activityList = new LinkedList<Activity>();
	private static List<Activity> activityLists = new LinkedList<>();
	public BlueTownExitHelper() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @Title: getInstance   
	 * @Description: TODO(获取单一实例)   
	 * @return     
	 * @throws
	 */
	public static BlueTownExitHelper getInstance() {
		if (instance==null) {
			instance=new BlueTownExitHelper();
		}
		return instance;
	}
	/**
	 * @param activity
	 * 
	 * @throws
	 * @Title: addActivity 添加Activity 到容器中
	 * @Description: TODO(add Activity 添加Activity到集合中)
	 */
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public static void addActivity2(Activity activity){
		activityLists.add(activity);
	}
	/**
	 * @throws
	 * @Title: finishAllActivity
	 * @Description: TODO(结束所有Activity)
	 */
	public static void finishAllActivity() {
		for (Activity activity : activityList) {
			activity.finish();
		}

		System.exit(0);
	}

	public static void exitActivity() {
		for (Activity activity : activityLists) {
			activity.finish();
		}
	}
}

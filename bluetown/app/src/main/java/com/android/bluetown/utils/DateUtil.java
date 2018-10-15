package com.android.bluetown.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.bluetown.R;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	private static final String TAG = DateUtil.class.getSimpleName();
	private static final SimpleDateFormat STRING_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat DATE_STRING_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat DATE_POINT_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static final long DAY_IN_MILLISECOND = 24 * 60 * 60 * 1000;
	private static final long HOUR_IN_MILLISECOND = 60 * 60 * 1000;
	private static final long MINUTE_IN_MILLISECOND = 60 * 1000;
	private static final long TEN_SECOND_IN_MILLISECOND = 10 * 1000;
	private static final long SECOND_IN_MILLISECOND = 1000;

	/**
	 * 当前日期
	 */
	public static String getCurrentDateStr() {
		Date date = new Date();
		String dateStr = DATE_STRING_FORMAT.format(date);
		return dateStr;
	}

	/**
	 * 把str 2015-2-2转 2015-02-02
	 * 
	 * @param str
	 * @return
	 */
	public static String getFormatStr(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		Date date = null;
		;
		try {
			date = DATE_STRING_FORMAT.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dateStr = DATE_STRING_FORMAT.format(date);
		return dateStr;
	}

	/**
	 * 判断当前时间是否能被预约
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isSelect(String year) {
		boolean flag = true;
		try {
			SimpleDateFormat aa = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = aa.parse(year);
			long selectTime = date.getTime();
			long currentTime = new Date().getTime();
			long cha = selectTime - currentTime;
			// 提前一小时预约
			if (cha < 60 * MINUTE_IN_MILLISECOND) {
				flag = false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 获取当前num天后的日期
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String getWeekdayAfterDate(Date date, int num) {
		java.text.Format formatter = new SimpleDateFormat("MM.dd");
		long beforeTime = (date.getTime() / 1000) + 24 * 60 * 60 * num;
		date.setTime(beforeTime * 1000);
		return formatter.format(date);
	}

	/**
	 * 判断当前时间是否为有效的时间段
	 * 
	 * @param context
	 * @param timeStr
	 * @return
	 */
	public static boolean getCurrentCanSeleteTime(Context context,
			String timeStr) {
		if (!TextUtils.isEmpty(timeStr)) {
			Date date = null;
			try {
				date = DATE_POINT_FORMAT.parse(timeStr);
			} catch (ParseException e) {
				Log.w(TAG, e.getMessage(), e);
			}

			if (date != null) {
				long diff = System.currentTimeMillis() - date.getTime();
				// 无效时间段（即不可预约时间段）
				if (diff > DAY_IN_MILLISECOND) {
					return false;
				} else {
					// 有效时间段（即可预约时间段）
					return true;
				}
			}
		}
		return false;
	}

	public static String getPublishTime(Context context, String timeStr) {
		if (!TextUtils.isEmpty(timeStr)) {
			Date date = null;
			try {
				date = STRING_DATE_FORMAT.parse(timeStr);
			} catch (ParseException e) {
				Log.w(TAG, e.getMessage(), e);
			}

			if (date != null) {
				long diff = System.currentTimeMillis() - date.getTime();
				if (diff > DAY_IN_MILLISECOND) {
					return DATE_STRING_FORMAT.format(date);
				} else {
					return getTimeString(context, diff);
				}
			}
		}
		return "";
	}

	private static String getTimeString(Context context, long millis) {
		if (millis > HOUR_IN_MILLISECOND) {
			return context.getResources().getString(
					R.string.how_many_hours_ago, millis / HOUR_IN_MILLISECOND);
		} else if (millis > MINUTE_IN_MILLISECOND) {
			return context.getResources().getString(
					R.string.how_many_minutes_ago,
					millis / MINUTE_IN_MILLISECOND);
		} else if (millis > TEN_SECOND_IN_MILLISECOND) {
			return context.getResources().getString(
					R.string.how_many_seconds_ago,
					millis / SECOND_IN_MILLISECOND);
		} else {
			return context.getResources().getString(R.string.just_now);
		}
	}

	public static String getWeekByStrDate(String strDate) {
		String week = null;
		try {
			Date date = DATE_STRING_FORMAT.parse(strDate);
			SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
			week = dateFm.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return week;

	}

	public static String getStrByStrDate(String strDate) {
		String week = null;
		try {
			Date date = DATE_POINT_FORMAT.parse(strDate);
			SimpleDateFormat dateFm = new SimpleDateFormat(
					"yyyy-MM-dd EEEE HH:mm");
			week = dateFm.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (week.contains("Monday")) {
			week = week.replace("Monday", "周一");
		}
		if (week.contains("Tuesday")) {
			week = week.replace("Tuesday", "周二");
		}
		if (week.contains("Wednesday")) {
			week = week.replace("Wednesday", "周三");
		}
		if (week.contains("Thursday")) {
			week = week.replace("Thursday", "周四");
		}
		if (week.contains("Friday")) {
			week = week.replace("Friday", "周五");
		}
		if (week.contains("Saturday")) {
			week = week.replace("Saturday", "周六");
		}
		if (week.contains("Sunday")) {
			week = week.replace("Sunday", "周天");
		}

		if (week.contains("星期一")) {
			week = week.replace("星期一", "周一");
		}
		if (week.contains("星期二")) {
			week = week.replace("星期二", "周二");
		}
		if (week.contains("星期三")) {
			week = week.replace("星期三", "周三");
		}
		if (week.contains("星期四")) {
			week = week.replace("星期四", "周四");
		}
		if (week.contains("星期五")) {
			week = week.replace("星期五", "周五");
		}
		if (week.contains("星期六")) {
			week = week.replace("星期六", "周六");
		}
		if (week.contains("星期天")) {
			week = week.replace("星期天", "周天");
		}
		return week;

	}
}

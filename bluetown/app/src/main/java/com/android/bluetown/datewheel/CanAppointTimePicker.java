package com.android.bluetown.datewheel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;

/**
 * 访客预约可预约时间的Picker
 * 
 * @author zd
 * 
 */
public class CanAppointTimePicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker datePicker, timePicker;
	private Context context;
	private ArrayList<String> dateList = new ArrayList<String>();
	private ArrayList<String> timeList = new ArrayList<String>();
	private String timeStr;
	private String vDate;
	private int canAppointDate;
	// Http请求
	protected AbHttpUtil httpInstance;
	// Http请求参数
	protected AbRequestParams params;

	public CanAppointTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	public CanAppointTimePicker(Context context,String vDate) {
		super(context);
		this.context = context;
		init(context);

	}

	private void initHttpRequest() {
		// TODO Auto-generated method stub
		if (httpInstance == null) {
			httpInstance = AbHttpUtil.getInstance(context);
		}

		if (params == null) {
			params = new AbRequestParams();
		}
	}

	/**
	 * @param context
	 */
	private void init(Context context) {
		initHttpRequest();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(
				R.layout.can_appoint_time_picker, this);
		// 获取控件引用
		datePicker = (ScrollerNumberPicker) findViewById(R.id.datePicker);
		timePicker = (ScrollerNumberPicker) findViewById(R.id.timePicker);
		setCanAppointDate();
	}
	
	/**
	 * 设置可预约时间段
	 */
	private void setCanAppointDate() {
		vDate = BlueTownApp.ins.CANAPPOINT;
		if (!TextUtils.isEmpty(vDate)) {			
			canAppointDate = Integer.parseInt(vDate);
			// 获取当前时间
			SimpleDateFormat df = new SimpleDateFormat("HH");// 设置日期格式
			String initTimeStr = df.format(new Date());
			int initTime = Integer.parseInt(initTimeStr) + canAppointDate;
			for (int i = 0; i < 2; i++) {
				int tempTime = initTime + i;
				if (tempTime <= 23) {
					if (!dateList.contains("今天")){
						dateList.add("今天");
					}
					timeList.add(tempTime+ ":" + "00");
					timeList.add(tempTime + ":" + "30");
				} else {
					if (!dateList.contains("明天")){
						dateList.add("明天");
					}
					timeList.add((tempTime - 24) + ":" + "00");
					timeList.add((tempTime - 24) + ":" + "30");
				}
			}
			datePicker.setData(dateList);
			if (dateList.contains("明天")){
				datePicker.setDefault(1);
			}else{
				datePicker.setDefault(0);
			}

			timePicker.setData(timeList);
			timePicker.setDefault(1);
		}
	}

	public String getdateCount() {
		String dateStr = datePicker.getSelectedText();
		timeStr = timePicker.getSelectedText();
		// 获取当前日期
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = sf.format(date);
		// 通过日历获取下一天日期
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sf.parse(nowDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.add(Calendar.DAY_OF_YEAR, +1);
		String nextDate_1 = sf.format(cal.getTime());
		if (!TextUtils.isEmpty(dateStr)) {
			if (dateStr.equals("明天")) {
				String makeTime = nextDate_1 + " " + timeStr;
				return makeTime + ":00";

			}
		}
		String makeTime = nowDate + " " + timeStr;
		return makeTime + ":00";

	}
}

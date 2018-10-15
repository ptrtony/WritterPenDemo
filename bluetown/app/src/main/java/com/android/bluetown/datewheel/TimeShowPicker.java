package com.android.bluetown.datewheel;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.bluetown.R;

/**
 * 城市Picker
 * 
 * @author zd
 * 
 */
public class TimeShowPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker datePicker;
	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	private static ArrayList<String> list_year = new ArrayList<String>();
	private static ArrayList<String> list_month = new ArrayList<String>();
	private static ArrayList<String> list_day = new ArrayList<String>();

	private String date_string;
	private int mYear, mMonth, mDay;

	public TimeShowPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 今天
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR); // 获取当前年份
		mMonth = c.get(Calendar.MONTH);// 获取当前月份
		mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
		YearMsg();
		MonthMsg();
		DayMsg();

	}

	public void YearMsg() {
		for (int i = 10; i < 100; i++) {
			list_year.add("19" + i);
		}
		for (int i = 10; i < 100; i++) {
			list_year.add("20" + i);
		}
	}

	public void MonthMsg() {
		for (int i = 1; i <= 12; i++) {
			list_month.add("" + i);
		}

	}

	public void DayMsg() {
		if (mMonth == 1 || mMonth == 3 || mMonth == 5 || mMonth == 7
				|| mMonth == 8 || mMonth == 10 || mMonth == 12) {
			for (int i = 1; i <= 31; i++) {
				list_day.add(i + "");
			}
		} else if (mMonth == 4 || mMonth == 6 || mMonth == 9 || mMonth == 11) {
			for (int i = 1; i < 31; i++) {
				list_day.add(i + "");
			}
		} else {
			if ((mYear % 400 == 0) || (mYear % 4 == 0) && (mYear % 100 != 0)) {
				for (int i = 1; i <= 28; i++) {
					list_day.add(i + "");
				}
			} else {
				for (int i = 1; i <= 29; i++) {
					list_day.add(i + "");
				}
			}
		}

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.time_picker, this);
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
		datePicker = (ScrollerNumberPicker) findViewById(R.id.city);
		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); // 获取当前年份
		int mMonth = c.get(Calendar.MONTH);// 获取当前月份
		int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
		int yearIndex = list_year.indexOf(mYear + "");
		int monthIndex = list_month.indexOf(mMonth + "");
		int dayIndex = list_day.indexOf(mDay + "");
		provincePicker.setData(list_year);
		provincePicker.setDefault(yearIndex);
		datePicker.setData(list_month);
		datePicker.setDefault(monthIndex + 1);
		counyPicker.setData(list_day);
		counyPicker.setDefault(dayIndex);
	}

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getDate_string() {
		date_string = provincePicker.getSelectedText() + "年"
				+ datePicker.getSelectedText() + "月"
				+ counyPicker.getSelectedText() + "日";

		if (date_string != null) {
			Intent intent = new Intent();
			intent.putExtra("provincePicker", provincePicker.getSelectedText());
			intent.putExtra("datePicker", datePicker.getSelectedText());
			intent.putExtra("counyPicker", counyPicker.getSelectedText());
			intent.putExtra("date_string", date_string);
			intent.setAction("date_choice");
			getContext().sendBroadcast(intent);
		}
		return date_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}

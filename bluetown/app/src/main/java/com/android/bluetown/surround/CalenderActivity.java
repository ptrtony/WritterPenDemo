package com.android.bluetown.surround;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.view.MyCalendar;
import com.android.bluetown.view.MyCalendar.GrideViewHolder;
import com.android.bluetown.view.MyCalendar.OnDaySelectListener;

/**
 * 
 * @ClassName: CalenderActivity
 * @Description: TODO 日历
 * @author zhangf
 * @date 2014年12月22日 上午 3:04:08
 * 
 */
public class CalenderActivity extends TitleBarActivity implements
		OnDaySelectListener, OnClickListener {

	public LinearLayout ll;
	public MyCalendar c1;
	public Date date;
	public String nowday;
	public long nd = 1000 * 24L * 60L * 60L;// 一天的毫秒数
	public SimpleDateFormat simpleDateFormat, sd1, sd2;
	public String inday;
	public TextView tv_title;
	public List<MyCalendar> canlendar = new ArrayList<MyCalendar>();
	private String currentDate;
	private String selectDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_calender);
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		nowday = simpleDateFormat.format(new Date());
		sd1 = new SimpleDateFormat("yyyy");
		sd2 = new SimpleDateFormat("dd");
		ll = (LinearLayout) findViewById(R.id.ll);
		currentDate = nowday;
		selectDate = getIntent().getStringExtra("selectDate");
		List<String> listDate = getDateList();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < listDate.size(); i++) {
			c1 = new MyCalendar(this);
			c1.setLayoutParams(params);
			c1.setSelectDateDay(selectDate);
			Date date = null;
			try {
				date = simpleDateFormat.parse(listDate.get(i));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			c1.setTheDay(date);
			c1.setOnDaySelectListener(this);
			canlendar.add(c1);
			ll.addView(c1);
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.selete_date_title);
		setTitleLayoutBg(R.color.title_bg_blue);
		setRightImageView(R.drawable.ic_check_mark_white);
		rightImageLayout.setOnClickListener(this);
	}

	@Override
	public void onDaySelectListener(View view, String date) {
		// 若日历日期小于当前日期，或日历日期-当前日期超过三个月，则不能点击
		try {
			for (MyCalendar canld : canlendar) {
				canld.getGridViewAdapte().setSelectDay(date);
				canld.getGridViewAdapte().notifyDataSetChanged();
			}
			if (simpleDateFormat.parse(date).getTime() < simpleDateFormat
					.parse(nowday).getTime()) {
				return;
			}
			long dayxc = (simpleDateFormat.parse(date).getTime() - simpleDateFormat
					.parse(nowday).getTime()) / nd;
			if (dayxc > 30) {
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateDay = date.split("-")[2];
		if (Integer.parseInt(dateDay) < 10) {
			dateDay = date.split("-")[2].replace("0", "");
		}
		GrideViewHolder holder = (GrideViewHolder) view.getTag();

		view.setBackgroundResource(R.drawable.ic_date_today);
		holder.tvDay.setTextColor(Color.WHITE);
		if (TextUtils.isEmpty(inday)) {
			holder.tvDay.setText(dateDay);
			holder.tv.setTextColor(Color.WHITE);
			holder.tv.setText("预约日");
			inday = date;
			currentDate = date;
		} else {
			try {
				if (simpleDateFormat.parse(date).getTime() < simpleDateFormat
						.parse(inday).getTime()) {
					view.setBackgroundColor(getResources().getColor(
							R.color.white));
					holder.tvDay.setTextColor(Color.BLACK);
					return;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			holder.tvDay.setText(dateDay);
			currentDate = date;
		}
	}

	// 根据当前日期，向后数三个月（若当前day不是1号，为满足至少90天，则需要向后数4个月）
	@SuppressLint("SimpleDateFormat")
	public List<String> getDateList() {
		List<String> list = new ArrayList<String>();
		Date date = new Date();
		int nowMon = date.getMonth() + 1;
		String yyyy = sd1.format(date);
		String dd = sd2.format(date);
		if (nowMon == 9) {
			list.add(simpleDateFormat.format(date));
			list.add(yyyy + "-10-" + dd);
			list.add(yyyy + "-11-" + dd);
			if (!dd.equals("01")) {
				list.add(yyyy + "-12-" + dd);
			}
		} else if (nowMon == 10) {
			list.add(yyyy + "-10-" + dd);
			list.add(yyyy + "-11-" + dd);
			list.add(yyyy + "-12-" + dd);
			if (!dd.equals("01")) {
				list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
			}
		} else if (nowMon == 11) {
			list.add(yyyy + "-11-" + dd);
			list.add(yyyy + "-12-" + dd);
			list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
			if (!dd.equals("01")) {
				list.add((Integer.parseInt(yyyy) + 1) + "-02-" + dd);
			}
		} else if (nowMon == 12) {
			list.add(yyyy + "-12-" + dd);
			list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
			list.add((Integer.parseInt(yyyy) + 1) + "-02-" + dd);
			if (!dd.equals("01")) {
				list.add((Integer.parseInt(yyyy) + 1) + "-03-" + dd);
			}
		} else {
			list.add(yyyy + "-" + getMon(nowMon) + "-" + dd);
			list.add(yyyy + "-" + getMon((nowMon + 1)) + "-" + dd);
			list.add(yyyy + "-" + getMon((nowMon + 2)) + "-" + dd);
			/*
			 * if(!dd.equals("01")){
			 * list.add(yyyy+"-"+getMon((nowMon+3))+"-"+dd); }
			 */
		}
		return list;
	}

	public String getMon(int mon) {
		String month = "";
		if (mon < 10) {
			month = "0" + mon;
		} else {
			month = "" + mon;
		}
		return month;
	}

	@Override
	public void onClick(View v) {
		Intent intent = getIntent();
		intent.putExtra("selectDate", currentDate);
		//成功返回码
		setResult(111, intent);
		finish();
	}
}

package com.android.bluetown.adapter;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;

public class ReserveCalendarAdapter extends BaseContentAdapter {
	private int[] bgResIds = { R.drawable.ic_date_tommrrow,
			R.drawable.ic_date_tommrrow, R.drawable.ic_date_after,
			R.drawable.ic_date_1, R.drawable.ic_date_2, R.drawable.ic_date_3 };
	private String[] days;
	private String[] weeks;

	private int current;

	public ReserveCalendarAdapter(Context context, List<Calendar> collection) {
		super(context, collection);
		days = context.getResources().getStringArray(R.array.day);
		weeks = context.getResources().getStringArray(R.array.week);
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_reserve_calender,
					null);
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Resources resources = context.getResources();
		Calendar calendar = (Calendar) getItem(position);
		if (position == current) {
			holder.text1.setBackgroundResource(R.drawable.ic_date_today);
		} else {
			holder.text1.setBackgroundResource(bgResIds[position
					% bgResIds.length]);
		}
		if (position < 3) {
			holder.text1.setText(days[position]);
		} else {
			int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			holder.text1.setText(weeks[index]);
		}
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String yue = "";
		if (month < 10) {
			yue = "0" + month;
		} else {
			yue = month + "";
		}
		holder.text2.setText(resources.getString(R.string.month_and_day, yue,
				day));
		return convertView;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public class ViewHolder {
		public TextView text1;
		public TextView text2;
	}
}

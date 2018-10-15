package com.android.bluetown.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.utils.Lunar;

/**
 * 
 * @author baiyuliang
 * 
 */
public class MyCalendar extends LinearLayout {

	private static Context context;

	private Date theInDay;
	private String inday = "", outday = "";
	public static View viewIn;
	public static View viewOut;
	public static String positionIn;
	public static String positionOut;

	static long nd = 1000 * 24L * 60L * 60L;// 一天的毫秒数

	private List<String> gvList;// 存放天

	private OnDaySelectListener callBack;// 回调函数
	private String selectDateDay;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");// 日期格式化

	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd");// 日期格式化
	public CalendarGridViewAdapter gridViewAdapte;

	public CalendarGridViewAdapter getGridViewAdapte() {
		return gridViewAdapte;
	}

	public void setGridViewAdapte(CalendarGridViewAdapter gridViewAdapte) {
		this.gridViewAdapte = gridViewAdapte;
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public MyCalendar(Context context) {
		super(context);
		MyCalendar.context = context;
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public MyCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		MyCalendar.context = context;
	}

	public void setInDay(String inday) {
		this.inday = inday;
	}

	public void setOutDay(String outday) {
		this.outday = outday;
	}

	public void setTheDay(Date dateIn) {
		this.theInDay = dateIn;
		init();
	}

	/**
	 * 初始化日期以及view等控件
	 */
	private void init() {
		gvList = new ArrayList<String>();// 存放天
		final Calendar cal = Calendar.getInstance();// 获取日历实例
		cal.setTime(theInDay);// cal设置为当天的
		cal.set(Calendar.DATE, 1);// cal设置当前day为当前月第一天
		int tempSum = countNeedHowMuchEmpety(cal);// 获取当前月第一天为星期几
		int dayNumInMonth = getDayNumInMonth(cal);// 获取当前月有多少天
		setGvListData(tempSum, dayNumInMonth, cal.get(Calendar.YEAR) + "-"
				+ getMonth((cal.get(Calendar.MONTH) + 1)));

		View view = LayoutInflater.from(context).inflate(
				R.layout.comm_calendar, this, true);// 获取布局，开始初始化
		TextView tv_year = (TextView) view.findViewById(R.id.tv_year);
		if (cal.get(Calendar.YEAR) > new Date().getYear()) {
			tv_year.setVisibility(View.VISIBLE);
			tv_year.setText(cal.get(Calendar.YEAR) + "年");
		}
		TextView tv_month = (TextView) view.findViewById(R.id.tv_month);
		tv_month.setText(String.valueOf(theInDay.getMonth() + 1));
		NoScrollGridView gv = (NoScrollGridView) view
				.findViewById(R.id.gv_calendar);
		gridViewAdapte = new CalendarGridViewAdapter(gvList, inday, outday,
				getSelectDateDay());
		gv.setAdapter(gridViewAdapte);
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1,
					int position, long arg3) {
				String choiceDay = (String) adapterView.getAdapter().getItem(
						position);
				String[] date = choiceDay.split(",");
				String day = date[1];
				if (!" ".equals(day)) {
					if (Integer.parseInt(day) < 10) {
						day = "0" + date[1];
					}
					choiceDay = date[0] + "-" + day;
					try {
						if (dateFormat2.parse(date[0] + "-" + day).getTime() < dateFormat2
								.parse(gridViewAdapte.getNowday()).getTime()) {
							return;
						}
						// 若日历日期-当前日期>90天，则不能选择
						long dayxc = (dateFormat2.parse(date[0] + "-" + day)
								.getTime() - dateFormat2.parse(
								gridViewAdapte.getNowday()).getTime())
								/ nd;
						if (dayxc > 30) {
							return;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (callBack != null) {// 调用回调函数回调数据
						callBack.onDaySelectListener(arg1, choiceDay);
					}
				}
			}
		});
	}

	/**
	 * 为gridview中添加需要展示的数据
	 * 
	 * @param tempSum
	 * @param dayNumInMonth
	 */
	private void setGvListData(int tempSum, int dayNumInMonth, String YM) {
		gvList.clear();
		for (int i = 0; i < tempSum; i++) {
			gvList.add(" , ");
		}
		for (int j = 1; j <= dayNumInMonth; j++) {
			gvList.add(YM + "," + String.valueOf(j));
		}
	}

	private String getMonth(int month) {
		String mon = "";
		if (month < 10) {
			mon = "0" + month;
		} else {
			mon = "" + month;
		}
		return mon;
	}

	/**
	 * 获取当前月的总共天数
	 * 
	 * @param cal
	 * @return
	 */
	private int getDayNumInMonth(Calendar cal) {
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 获取当前月第一天在第一个礼拜的第几天，得出第一天是星期几
	 * 
	 * @param cal
	 * @return
	 */
	private int countNeedHowMuchEmpety(Calendar cal) {
		int firstDayInWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return firstDayInWeek;
	}

	/**
	 * gridview中adapter的viewholder
	 * 
	 * @author Administrator
	 * 
	 */
	public static class GrideViewHolder {
		public TextView tvDay, tv;
	}

	/**
	 * gridview的adapter
	 * 
	 * @author Administrator
	 * 
	 */
	public class CalendarGridViewAdapter extends BaseAdapter {

		List<String> gvList;// 存放天
		String inday, outday;
		private String nowday;
		private String selectDay = "";

		public String getSelectDay() {
			return selectDay;
		}

		public void setSelectDay(String selectDay) {
			this.selectDay = selectDay;
		}

		public CalendarGridViewAdapter(List<String> gvList, String inday,
				String outday, String selectDate) {
			super();
			this.gvList = gvList;
			this.inday = inday;
			this.outday = outday;
			this.nowday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			if (TextUtils.isEmpty(selectDate)) {
				selectDay = nowday;
			} else {
				selectDay = selectDate;
			}
		}

		public String getNowday() {
			return nowday;
		}

		public void setNowday(String nowday) {
			this.nowday = nowday;
		}

		@Override
		public int getCount() {
			return gvList.size();
		}

		@Override
		public String getItem(int position) {
			return gvList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GrideViewHolder holder;
			if (convertView == null) {
				holder = new GrideViewHolder();
				convertView = inflate(context,
						R.layout.common_calendar_gridview_item, null);
				holder.tv = (TextView) convertView
						.findViewById(R.id.tv_calendar);
				holder.tvDay = (TextView) convertView
						.findViewById(R.id.tv_calendar_day);
				convertView.setTag(holder);
			} else {
				holder = (GrideViewHolder) convertView.getTag();
			}
			String[] date = getItem(position).split(",");
			holder.tvDay.setText(date[1]);
			if ((position + 1) % 7 == 0 || (position) % 7 == 0) {
				holder.tvDay.setTextColor(context.getResources().getColor(
						R.color.black));
				convertView.setBackgroundResource(R.color.transparent);
			}
			if (!date[1].equals(" ")) {
				String day = date[1];
				// 小于0的天数
				if (Integer.parseInt(date[1]) < 10) {
					day = "0" + date[1];
				}

				// 当前日期
				if ((date[0] + "-" + day).equals(nowday)
						&& nowday.equals(selectDay)) {

					holder.tvDay.setText("今天");
					holder.tvDay.setTextColor(Color.WHITE);
					holder.tv.setTextColor(Color.WHITE);
					holder.tv.setText("预约日");
					convertView.setBackgroundResource(R.drawable.ic_date_today);
				} else {
					if ((date[0] + "-" + day).equals(selectDay)) {
						holder.tvDay.setText(day);
						holder.tvDay.setTextColor(Color.WHITE);
						holder.tv.setTextColor(Color.WHITE);
						holder.tv.setText("预约日");
						convertView
								.setBackgroundResource(R.drawable.ic_date_today);
					} else {
						holder.tvDay.setText(day);
						convertView.setBackgroundResource(R.color.transparent);
						holder.tvDay.setTextColor(context.getResources()
								.getColor(R.color.font_black));
						holder.tv.setTextColor(context.getResources().getColor(
								R.color.font_black));
						holder.tv.setText(getLunarStr(date[0] + "-" + day));
					}
				}
				try {
					// 若日历日期<当前日期，则不能选择
					if (dateFormat2.parse(date[0] + "-" + day).getTime() < dateFormat2
							.parse(nowday).getTime()) {
						holder.tvDay.setTextColor(Color.parseColor("#999999"));
						holder.tv.setTextColor(Color.parseColor("#999999"));
					}
					// 若日历日期-当前日期>90天，则不能选择
					long dayxc = (dateFormat2.parse(date[0] + "-" + day)
							.getTime() - dateFormat2.parse(nowday).getTime())
							/ nd;
					if (dayxc > 30) {
						holder.tvDay.setTextColor(Color.parseColor("#999999"));
						holder.tv.setTextColor(Color.parseColor("#999999"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return convertView;
		}
	}

	/**
	 * 自定义监听接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnDaySelectListener {
		void onDaySelectListener(View Selectview, String date);
	}

	/**
	 * 自定义监听接口设置对象
	 * 
	 * @param o
	 */
	public void setOnDaySelectListener(OnDaySelectListener o) {
		callBack = o;
	}

	/**
	 * @return the selectDateDay
	 */
	public String getSelectDateDay() {
		return selectDateDay;
	}

	/**
	 * @param selectDateDay
	 *            the selectDateDay to set
	 */
	public void setSelectDateDay(String selectDateDay) {
		this.selectDateDay = selectDateDay;
	}

	public String getLunarStr(String dateStr) {
		Date date = null;
		try {
			date = dateFormat2.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Lunar lunar = new Lunar(calendar);
		return Lunar.getChinaDayString(lunar.day);
	}
}
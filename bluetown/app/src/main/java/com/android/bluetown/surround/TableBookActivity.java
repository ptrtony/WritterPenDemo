package com.android.bluetown.surround;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ReserveAdapter;
import com.android.bluetown.adapter.ReserveCalendarAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.TabbleInfo;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.DateCallBackListener;
import com.android.bluetown.result.TableListResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DateUtil;
import com.android.bluetown.view.NoScrollGridView;
import com.bm.xsg.wheelview.OnWheelChangedListener;
import com.bm.xsg.wheelview.WheelView;
import com.bm.xsg.wheelview.adapters.ArrayWheelAdapter;

public class TableBookActivity extends TitleBarActivity implements
		OnClickListener, DateCallBackListener, OnWheelChangedListener {
	private RelativeLayout moreDateLy, reserveTime, reservePersonmore;
	private NoScrollGridView gvCalendar;
	private String mid, merchantName, order_flag;
	private ReserveCalendarAdapter calendarAdapter;
	private List<Calendar> calendarList;
	private ListView tableInfoList;
	// 选择的就餐时间和人数
	private TextView selectTimeText, selectPersonText, selectDateText;
	// 底部window
	private RelativeLayout popLy;
	private WheelView wheView;
	private TextView popCancel;
	private TextView popFinish;
	private TextView popTitle;
	private ArrayWheelAdapter<String> popAdater;
	private String popTime;
	private String persionCount;
	// bookPrice:订座价格
	private String bookPrice;
	private static final int POPTIMEGTYPE = 1;
	private static final int POPPERSIONTYPE = 2;
	private int popType = 0;
	protected String[] mProvinceDatas = new String[] { "时间不限",
			"早餐(6:00-11:00)", "午餐(12:00-14:00)", "晚餐(16:00-20:00)",
			"夜宵(20:00-23:00)" };
	protected String[] mPersionCount = new String[] { "人数不限", "1人", "2人", "3人",
			"4人", "5人" };
	private ReserveAdapter adapter;
	private List<TabbleInfo> list;
	// 选择的日期值
	private String selectDateVlaue;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		righTextLayout.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_select_dinner_time);
		mid = getIntent().getStringExtra("meid");
		merchantName = getIntent().getStringExtra("merchantName");
		order_flag = BlueTownApp.BOOK_OR_ORDER;
		setTitleView(merchantName);
		// 商家详情-预订（//先订位（不一定点菜）（TableBookActivity-下一步））
		if (order_flag.equals("0")) {
			setRighTextView(R.string.next);
		} else {
			setRighTextView(R.string.finish);
		}
		initUIView();
		// 初始化当前显示的时间值的选择桌子信息
		initData("");

	}

	/**
	 * 初始化界面组件
	 */
	private void initUIView() {
		// 顶部日期选择
		gvCalendar = (NoScrollGridView) findViewById(R.id.gv_reserve_calendar);
		reserveTime = (RelativeLayout) findViewById(R.id.rl_reserve_time);
		reservePersonmore = (RelativeLayout) findViewById(R.id.rl_reserve_personmore);
		// 就餐时间段的展示
		tableInfoList = (ListView) findViewById(R.id.lv_reserve_time);
		// 展示日历
		moreDateLy = (RelativeLayout) findViewById(R.id.rl_reserve_cenlendermore);
		selectTimeText = (TextView) findViewById(R.id.tv_reservet_times);
		selectPersonText = (TextView) findViewById(R.id.tv_reservet_persons);
		selectDateText = (TextView) findViewById(R.id.tv_select_date);
		// 底部
		popLy = (RelativeLayout) findViewById(R.id.reserve_bottom);
		wheView = (WheelView) findViewById(R.id.menu_select_wheel);
		popCancel = (TextView) findViewById(R.id.tv_pop_cancel);
		popFinish = (TextView) findViewById(R.id.tv_pop_finish);
		popTitle = (TextView) findViewById(R.id.tv_pop_title);
		gvCalendar.setNumColumns(6);
		gvCalendar.setSelector(new ColorDrawable(Color.TRANSPARENT));
		calendarList = getCalendars();
		calendarAdapter = new ReserveCalendarAdapter(this, calendarList);
		gvCalendar.setAdapter(calendarAdapter);
		gvCalendar.setOnItemClickListener(onCalendarItemClickListener);
		moreDateLy.setOnClickListener(this);
		reserveTime.setOnClickListener(this);
		reservePersonmore.setOnClickListener(this);
		popCancel.setOnClickListener(this);
		popFinish.setOnClickListener(this);
		popLy.setOnClickListener(this);
	}

	private void initData(String dataStr) {
		if (TextUtils.isEmpty(dataStr)) {
			selectDateVlaue = DateUtil.getCurrentDateStr();
		} else {
			selectDateVlaue = dataStr;
		}
		getTableInfos(selectDateVlaue);
	}

	/**
	 * 获取今天和之后五天在一个月中的日期
	 * 
	 * @return
	 */
	private List<Calendar> getCalendars() {
		List<Calendar> list = new ArrayList<Calendar>();
		for (int i = 0; i < Constant.NUM_OF_DAYS; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, i);
			list.add(calendar);
		}
		return list;
	}

	private void initWheelView(String[] strArray) {
		popAdater = new ArrayWheelAdapter<String>(this, strArray);
		popAdater.setTextSize(18);
		wheView.setViewAdapter(popAdater);
		wheView.addChangingListener(this);

		// 设置可见条目数量
		wheView.setVisibleItems(8);
		wheView.setWheelBackground(R.drawable.two_line_white_bg);
		wheView.setWheelForeground(R.drawable.wheel_val);
		if (popType == POPTIMEGTYPE) {
			if (TextUtils.isEmpty(popTime)) {
				wheView.setCurrentItem(0);
				popTime = mProvinceDatas[0];
			} else {
				int index = 0;
				for (int i = 0; i < mProvinceDatas.length; i++) {
					if (mProvinceDatas[i].equals(popTime)) {
						index = i;
						break;
					}
				}
				wheView.setCurrentItem(index);
			}
		} else {
			if (TextUtils.isEmpty(popTime)) {
				wheView.setCurrentItem(0);
				persionCount = mPersionCount[0];
			} else {
				int index = 0;
				for (int i = 0; i < mPersionCount.length; i++) {
					if (mPersionCount[i].equals(persionCount)) {
						index = i;
						break;
					}
				}
				wheView.setCurrentItem(index);
			}
		}
	}

	private OnItemClickListener onCalendarItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			calendarAdapter.setCurrent(position);
			calendarAdapter.notifyDataSetChanged();
			Calendar calendar = calendarList.get(position);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int year = calendar.get(Calendar.YEAR);
			String str = year + "-" + month + "-" + day;
			str = DateUtil.getFormatStr(str);
			initData(str);

		}
	};

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_reserve_cenlendermore:
			Intent intent = new Intent();
			intent.putExtra("selectDate", selectDateVlaue);
			intent.setClass(TableBookActivity.this, CalenderActivity.class);
			startActivityForResult(intent, 1009);
			break;
		case R.id.rl_reserve_time:
			// 展示就餐时间的选择
			popTitle.setText("选择就餐时间");
			popType = POPTIMEGTYPE;
			initWheelView(mProvinceDatas);
			popLy.setVisibility(View.VISIBLE);
			break;
		case R.id.rl_reserve_personmore:
			// 展示就餐人数的选择
			popTitle.setText("请选择人数");
			popType = POPPERSIONTYPE;
			initWheelView(mProvinceDatas);
			popLy.setVisibility(View.VISIBLE);
			break;
		case R.id.reserve_bottom:
			// 点击自己的其他位置自身消失
			popLy.setVisibility(View.GONE);
			break;
		case R.id.tv_pop_cancel:
			popLy.setVisibility(View.GONE);
			break;
		case R.id.tv_pop_finish:
			// 设置当前选择的就餐时间和就餐人数
			if (popType == POPTIMEGTYPE) {
				selectTimeText.setText(popTime);
			} else {
				selectPersonText.setText(persionCount);
			}
			popLy.setVisibility(View.GONE);
			break;
		case R.id.rightTextLayout:
			// 下一步
			if (order_flag.equals("0")) {
				if (!TextUtils.isEmpty(persionCount)) {
					Intent nextIntent = new Intent();
					nextIntent.putExtra("meid", mid);
					nextIntent.putExtra("merchantName", merchantName);
					nextIntent.putExtra("tableName", persionCount);
					nextIntent.putExtra("dinnerTime", selectDateVlaue + " "
							+ popTime);
					BlueTownApp.dishesPrice = bookPrice;
					BlueTownApp.originalPrice = bookPrice;
					nextIntent.setClass(TableBookActivity.this,
							OnlineBookSeatActivity.class);
					startActivity(nextIntent);
				} else {
					TipDialog.showDialog(TableBookActivity.this, R.string.tip,
							R.string.confirm, R.string.selete_dinner_time_tip);
				}
			} else {
				// 完成
				if (!TextUtils.isEmpty(persionCount)) {
					Intent showIntent = new Intent();
					showIntent.putExtra("tableName", persionCount);
					showIntent.putExtra("dinnerTime", selectDateVlaue + " "
							+ popTime);
					// 成功返回码
					setResult(112, showIntent);
					finish();
				} else {
					TipDialog.showDialog(TableBookActivity.this, R.string.tip,
							R.string.confirm, R.string.selete_dinner_time_tip);
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 格式化指定时间格式（带星期）
	 * 
	 * @param weekDay
	 * @return
	 */
	private String formatDateWeek(String weekDay) {
		Calendar c1 = Calendar.getInstance();
		String[] dates = selectDateVlaue.split("-");
		System.out.println("<<<<<<<<<" + dates[0] + "<<<" + dates[1] + 1
				+ "<<<" + dates[2]);
		c1.set(Integer.parseInt(dates[0]), Integer.parseInt(dates[1] + 1),
				Integer.parseInt(dates[2]));
		int week = c1.get(c1.DAY_OF_WEEK_IN_MONTH);
		switch (week) {
		case 1:
			weekDay = "星期一";
			break;
		case 2:
			weekDay = "星期二";
			break;
		case 3:
			weekDay = "星期三";
			break;
		case 4:
			weekDay = "星期四";
			break;
		case 5:
			weekDay = "星期五";
			break;
		case 6:
			weekDay = "星期六";
			break;
		case 7:
			weekDay = "星期日";
			break;

		default:
			break;
		}
		return weekDay;
	};

	/**
	 * 获取订餐桌子的列表
	 */
	private void getTableInfos(final String selectDateVlaue) {
		// TODO Auto-generated method stub
		// meid:商家id（必填）
		// onlyDate：当前时间
		params.put("meid", mid);
		params.put("onlyDate", selectDateVlaue);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ORDER_DISH_SELETE_TABLE, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						TableListResult result = (TableListResult) AbJsonUtil
								.fromJson(s, TableListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							setTableList(selectDateVlaue, result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (adapter != null) {
								adapter.notifyDataSetChanged();
							}
							Toast.makeText(TableBookActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(TableBookActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

	}

	/**
	 * 桌子列表信息的数据
	 * 
	 * @param selectDateVlaue
	 * @param result
	 */
	private void setTableList(final String selectDateVlaue,
			TableListResult result) {
		list = result.getData();
		adapter = new ReserveAdapter(TableBookActivity.this, list, this);
		adapter.setCurrentDateStr(selectDateVlaue);
		tableInfoList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 就餐时间点点击回调
	 */
	@Override
	public void clickCallback(Object object, int group, String timeStr) {
		// TODO Auto-generated method stub
		adapter.setGroup(group);
		adapter.notifyDataSetChanged();
		popTime = timeStr;
		TabbleInfo tabbleInfo = ((TabbleInfo) object);
		persionCount = tabbleInfo.getTableName();
		bookPrice = tabbleInfo.getBookPrice();
	}

	@Override
	public void onChanged(WheelView arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		int index = arg0.getCurrentItem();
		// 选择就餐时间
		if (popType == POPTIMEGTYPE) {
			popTime = mProvinceDatas[index];
		} else {
			// 选择就餐人数
			persionCount = mPersionCount[index];
		}
	}

	/**
	 * 选择的就餐日期
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if (resultCode == 111) {
			if (arg2 != null) {
				selectDateVlaue = arg2.getStringExtra("selectDate");
				selectDateText.setText(selectDateVlaue);
				calendarAdapter.setCurrent(-1);
				calendarAdapter.notifyDataSetChanged();
				initData(selectDateVlaue);
			}

		}

	}

}

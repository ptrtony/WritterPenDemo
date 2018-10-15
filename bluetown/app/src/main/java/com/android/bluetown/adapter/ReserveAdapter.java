package com.android.bluetown.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.TabbleInfo;
import com.android.bluetown.listener.DateCallBackListener;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DateUtil;
import com.android.bluetown.view.NoScrollGridView;

@SuppressWarnings("unchecked")
public class ReserveAdapter extends BaseContentAdapter {

	public Context context;
	private String currentDateStr;
	private int group = -1;
	private List<TabbleInfo> list;
	private DateCallBackListener listener;

	public ReserveAdapter(Context context, List<TabbleInfo> list,
			DateCallBackListener listener) {
		super(context, list);
		this.context = context;
		this.list = list;
		this.listener = listener;

	}

	@Override
	public View getContentView(int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = LayoutInflater.from(context).inflate(
					R.layout.item_reserve, null);
			holder.tv_itemreserve_name = (TextView) contentView
					.findViewById(R.id.tv_itemreserve_name);
			holder.tv_itemreserve_price = (TextView) contentView
					.findViewById(R.id.tv_itemreserve_price);
			holder.gv_itemreserve_time = (NoScrollGridView) contentView
					.findViewById(R.id.gv_itemreserve_time);
			holder.tvSeat = (TextView) contentView
					.findViewById(R.id.tv_no_seat);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		TabbleInfo info = (TabbleInfo) getItem(position);
		holder.tv_itemreserve_name.setText(info.getTableName());
		String price = "";
		if (TextUtils.isEmpty(info.getBookPrice())) {
			price = "0.00";
		} else {
			price = info.getBookPrice();
		}
		holder.tv_itemreserve_price.setText("￥" + price + "起订");
		// 不可预约时间段(包括后台反馈的不可预约时间和过期的时间)
		if (info.getNoDateList() != null) {
			if (info.getNoDateList().size() == Constant.TIME.length) {
				holder.tvSeat.setText(R.string.order_finish);
			} else {
				List<String> dateList = Arrays.asList(Constant.TIME);
				for (int i = 0; i < dateList.size(); i++) {
					String date = getCurrentDateStr() + " " + dateList.get(i);
					// 初始化当前不可预约时间点（包括后台反馈的和与当前时间之前的时间段（并且当前时间之后的一个小时不能被预约））
					if (!DateUtil.isSelect(date)) {
						dateList.remove(date);
					}
				}
				try {
					dateList.removeAll(info.getNoDateList());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				holder.tvSeat.setText("还有" + dateList.size() + "时间段课预约");
			}
		} else {
			// 都可以预约
			holder.tvSeat.setVisibility(View.GONE);
		}
		holder.gv_itemreserve_time.setSelector(new ColorDrawable(
				Color.TRANSPARENT));
		List<String> timeList = Arrays.asList(Constant.TIME);
		// 初始化group index（时间段集合中时间点的position）
		list.get(position).setGroup(position);
		ReserveGridAdapter adapter = new ReserveGridAdapter(context, timeList,
				((List<TabbleInfo>) data), listener,
				holder.gv_itemreserve_time, position);
		// 设置当前订餐的日期
		adapter.setCurrentDateStr(getCurrentDateStr());
		adapter.setGroup(getGroup());
		holder.gv_itemreserve_time.setAdapter(adapter);
		return contentView;
	}

	class ViewHolder {
		TextView tv_itemreserve_name;
		TextView tv_itemreserve_price;
		NoScrollGridView gv_itemreserve_time;
		TextView tvSeat;
	}

	/**
	 * @return the currentDateStr
	 */
	public String getCurrentDateStr() {
		return currentDateStr;
	}

	/**
	 * @param currentDateStr
	 *            the currentDateStr to set
	 */
	public void setCurrentDateStr(String currentDateStr) {
		this.currentDateStr = currentDateStr;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

}

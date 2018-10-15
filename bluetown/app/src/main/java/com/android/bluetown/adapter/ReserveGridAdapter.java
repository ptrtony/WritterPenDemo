package com.android.bluetown.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.TabbleInfo;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.DateCallBackListener;
import com.android.bluetown.utils.DateUtil;
import com.android.bluetown.view.NoScrollGridView;

public class ReserveGridAdapter extends BaseContentAdapter implements
		OnItemClickListener {

	public Context context;
	private List<TabbleInfo> list;
	// 初始化桌子信息中时间段的position
	private int group = -1;
	private Map<String, Boolean> map;
	// 桌子信息列表的postion
	private int groupPosition;
	// 当前的日期《格式：MMMM-dd-mm》
	private String currentDateStr;
	private DateCallBackListener listener;

	public ReserveGridAdapter(Context context, List<String> timeList,
			List<TabbleInfo> list, DateCallBackListener listener,
			NoScrollGridView gd, int position) {
		super(context, timeList);
		this.context = context;
		this.list = list;
		this.listener = listener;
		gd.setOnItemClickListener(this);
		map = new HashMap<String, Boolean>();
		groupPosition = position;
		// 初始化不可预约时间段，初始化map数据标记是否选中
		initMap(list.get(position).getNoDateList());
	}

	private void initMap(List<String> times) {
		map.clear();
		if (times != null) {
			for (String time : times) {
				map.put(time, false);
			}
		}
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public View getContentView(int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = LayoutInflater.from(context).inflate(
					R.layout.item_times, null);
			holder.tv_itemtimes = (TextView) contentView
					.findViewById(R.id.tv_itemtimes);
			holder.rl_times_item = (RelativeLayout) contentView
					.findViewById(R.id.rl_times_item);
			holder.isSelect = (ImageView) contentView
					.findViewById(R.id.isSelect);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		String str = (String) getItem(position);
		holder.tv_itemtimes.setText(str);
		String date = getCurrentDateStr() + " " + str;
		// 初始化当前不可预约时间点（包括后台反馈的和与当前时间之前的时间段（并且当前时间之后的一个小时不能被预约））
		if (map.containsKey(str) || !DateUtil.isSelect(date)) {
			holder.rl_times_item.setBackgroundResource(R.drawable.time_bg_gray);
			holder.tv_itemtimes.setTextColor(Color.WHITE);
			holder.isSelect.setVisibility(View.GONE);
		} else {
			if (list != null && list.size() > 0
					&& list.get(groupPosition).getGroup() == group
					&& list.get(groupPosition).getSelectIndex() == position) {
				holder.rl_times_item
						.setBackgroundResource(R.drawable.time_bg_red);
				holder.tv_itemtimes.setTextColor(Color.WHITE);
				holder.isSelect.setVisibility(View.VISIBLE);
			} else {
				holder.rl_times_item
						.setBackgroundResource(R.drawable.time_stroke_bg);
				holder.tv_itemtimes.setTextColor(context.getResources()
						.getColor(R.color.compnay_show_type_tag_text_bg));
				holder.isSelect.setVisibility(View.GONE);
			}
		}
		return contentView;
	}

	public class ViewHolder {
		public TextView tv_itemtimes;
		public RelativeLayout rl_times_item;
		public ImageView isSelect;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (map.containsKey(parent.getItemAtPosition(position))) {
			TipDialog.showDialog(context, R.string.tip, R.string.confirm,
					"该时间段不可预订。");
			return;
		}
		String date = getCurrentDateStr() + " "
				+ parent.getItemAtPosition(position).toString();
		// 不能被预约的时间段
		if (!DateUtil.isSelect(date)) {
			TipDialog.showDialog(context, R.string.tip, R.string.confirm,
					"预约时间超时，请重新选择。");
			return;
		}
		if (list.get(groupPosition).getGroup() != group) {
			list.get(groupPosition).setSelectIndex(position);
			listener.clickCallback(list.get(groupPosition),list.get(groupPosition).getGroup(), parent
					.getItemAtPosition(position).toString());
		} else {
			group = list.get(groupPosition).getGroup();
			list.get(groupPosition).setSelectIndex(position);
			notifyDataSetChanged();
		}

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

}

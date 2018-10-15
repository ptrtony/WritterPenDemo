package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.MakingIncreaseBean;

public class ParkingMonthlyDetailAdapter extends BaseContentAdapter {

	public ParkingMonthlyDetailAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_parking_monthly_detail, null);
			mHolder = new ViewHolder();
			mHolder.textView1 = (TextView) convertView
					.findViewById(R.id.textView1);
//			mHolder.spare = (TextView) convertView
//					.findViewById(R.id.spare);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final MakingIncreaseBean item = (MakingIncreaseBean) getItem(position);
		mHolder.textView1.setText(item.getParkingName());
//		mHolder.spare.setText("空位"+item.getSpare());
	}
	
	static class ViewHolder {
		private TextView textView1;
//		private TextView spare;
	}

}

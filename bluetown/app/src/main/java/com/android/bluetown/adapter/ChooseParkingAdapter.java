package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.ParkingSpaceBean;

public class ChooseParkingAdapter extends BaseContentAdapter {
	private int clickTemp = -1;

	public ChooseParkingAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}
	public String getText(int position) {
		final ParkingSpaceBean item = (ParkingSpaceBean) getItem(position);
		return item.parkingSpaceNo;
	}
	public void setSeclection(int position) {
		ParkingSpaceBean item = (ParkingSpaceBean) getItem(position);
		if(item.isRentable.equals("0")){
			clickTemp = position;
		}
		
	}
	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_choose_parking, null);
			mHolder = new ViewHolder();
			mHolder.text = (TextView) convertView
					.findViewById(R.id.text);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);
		
		return convertView;
	}

	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final ParkingSpaceBean item = (ParkingSpaceBean) getItem(position);
		mHolder.text.setText(item.parkingSpaceNo);
		if(item.isRentable.equals("0")){
			if(clickTemp==position){			
				mHolder.text.setBackgroundResource(R.drawable.yuanjiao_choose);
				mHolder.text.setTextColor(0xffffffff);
			}else{
				mHolder.text.setBackgroundResource(R.drawable.yuanjiao_can_choose);
				mHolder.text.setTextColor(0xffbdbdbd);
			}
		}else{
			mHolder.text.setBackgroundResource(R.drawable.yuanjiao_cant_choose);
			mHolder.text.setTextColor(0xffbdbdbd);
		}
		
	}
	
	static class ViewHolder {
		private TextView text;
	}

}

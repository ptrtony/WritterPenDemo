package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.GuestAppointmentHistoryItemBean;

/**
 * 
 * @ClassName: GuestAppointmentHistoryListAdapter
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月20日 下午2:51:44
 * 
 */
public class GuestAppointmentHistoryListAdapter extends BaseContentAdapter {

	public GuestAppointmentHistoryListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.item_guestappointment_history, null);
			mHolder = new ViewHolder();
			mHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.car = (TextView) convertView.findViewById(R.id.tv_car);
			mHolder.mtime = (TextView) convertView.findViewById(R.id.tv_mtime);
//			mHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
//			mHolder.appointType = (ImageView) convertView
//					.findViewById(R.id.appointType);
			mHolder.appointLy = (RelativeLayout) convertView
					.findViewById(R.id.appointLy);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final GuestAppointmentHistoryItemBean item = (GuestAppointmentHistoryItemBean) getItem(position);
		String makeingTime = item.getMakingTime();
		if (!TextUtils.isEmpty(makeingTime)) {
			mHolder.time.setText(item.getMakingTime().replaceAll(" ", "\n"));
		}

		mHolder.car.setText(item.getBusNumber());
		// orderType 预约状态(0失败，1成功，2按时到达，3过期未到）
		if (!TextUtils.isEmpty(item.getOrderType())) {
			if (item.getOrderType().equals("1")
					|| item.getOrderType().equals("2")) {
				mHolder.appointLy.setBackgroundResource(R.drawable.guestappoint_history_bg);
//				mHolder.type.setText(R.string.appoint_success);
//				mHolder.appointType.setImageResource(R.drawable.sec);
			} else if (item.getOrderType().equals("3")) {
				mHolder.appointLy.setBackgroundResource(R.drawable.guestappoint_history_bg_gray);
//						.setBackgroundResource(R.drawable.bg_card_fail);
//				mHolder.type.setText(R.string.appoint_fail1);
//				mHolder.appointType.setImageResource(R.drawable.sec_fail);
			} else {
				mHolder.appointLy.setBackgroundResource(R.drawable.guestappoint_history_bg_gray);
//						.setBackgroundResource(R.drawable.bg_card_fail);
//				mHolder.type.setText(R.string.appoint_fail);
//				mHolder.appointType.setImageResource(R.drawable.sec_fail);
			}
		}
		mHolder.mtime.setText(item.getMakingTime());
	}

	static class ViewHolder {
		private TextView time;
		private TextView car;
		private TextView type;
		private TextView mtime;
//		private ImageView appointType;
		private RelativeLayout appointLy;
	}

}

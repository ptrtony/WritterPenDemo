package com.android.bluetown.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.ParkingBean;

/**
 * @author hedi
 * @data:  2016年5月27日 下午5:53:29 
 * @Description:  TODO<停车包月历史> 
 */
public class ParkingHistoryAdapter extends BaseContentAdapter {
	SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
	public ParkingHistoryAdapter(Context mContext, List<?> data) {
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
			mHolder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
			mHolder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
			mHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.car = (TextView) convertView.findViewById(R.id.tv_car);
			mHolder.mtime = (TextView) convertView.findViewById(R.id.tv_mtime);
			mHolder.refund = (ImageView) convertView.findViewById(R.id.refund);
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
		mHolder.textView2.setText("到期时间");
		final ParkingBean item = (ParkingBean) getItem(position);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(s1.parse(item.getEndTime()));
			String makeingTime = s2.format(c.getTime());
			if (!TextUtils.isEmpty(makeingTime)) {
				mHolder.time.setText(makeingTime);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if(item.getOrderStatus().equals("4")){
			mHolder.appointLy.setBackgroundResource(R.drawable.guestappoint_history_bg_gray);
			mHolder.refund.setVisibility(View.VISIBLE);
		}else if(item.getOrderStatus().equals("3")){
			mHolder.appointLy.setBackgroundResource(R.drawable.guestappoint_history_bg_gray);
			mHolder.refund.setVisibility(View.GONE);
		}else{
			mHolder.appointLy.setBackgroundResource(R.drawable.guestappoint_history_bg);
			mHolder.refund.setVisibility(View.GONE);
		}
		mHolder.car.setText(item.getCarNumber());
		mHolder.textView3.setText(item.getParkingName()+"/"+item.getParkingSpace());
		mHolder.mtime.setText(item.getCreateTime());
	}

	static class ViewHolder {
		private TextView time;
		private TextView car;
		private TextView textView2;
		private TextView textView3;
		private TextView mtime;
		private ImageView refund;
		private RelativeLayout appointLy;
	}

}


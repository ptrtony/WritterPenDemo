package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.ParkYellowPageItemBean;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: ParkYellowPageListAdapter
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月21日 下午3:21:47
 * 
 */
public class ParkYellowPageListAdapter extends BaseContentAdapter {
	public ParkYellowPageListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_parkyellowpage, null);
			mHolder = new ViewHolder();
			mHolder.mName = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.mPhone = (TextView) convertView.findViewById(R.id.tv_phone);
			mHolder.mAddress = (TextView) convertView.findViewById(R.id.tv_address);
			mHolder.mDistance = (TextView) convertView.findViewById(R.id.tv_distance);
			mHolder.mImg = (RoundedImageView) convertView.findViewById(R.id.truckImage);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final ParkYellowPageItemBean item = (ParkYellowPageItemBean) getItem(position);
		if (!TextUtils.isEmpty(item.getImg())) {
			imageLoader.displayImage(item.getImg(), mHolder.mImg,
					defaultOptions);
		}else {
				imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty,
					mHolder.mImg, defaultOptions);
		}
		
		mHolder.mName.setText(item.getBusinessName());
		mHolder.mAddress.setText(item.getBusinessAdress());
		String distance=item.getDistance();
		if (!TextUtils.isEmpty(distance)) {
			mHolder.mDistance.setText(distance + "km");
			if (Double.parseDouble(distance)<2) {
				mHolder.mDistance.setBackgroundResource(R.drawable.bg_m);
			}else {
				mHolder.mDistance.setBackgroundResource(R.drawable.bg_m_y);
			}
		}
		mHolder.mPhone.setText(item.getBusinessTell());
		mHolder.mPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+item.getBusinessTell()));
			    context.startActivity(intent);
			}
		});
	}

	static class ViewHolder {
		private TextView mName;
		private TextView mPhone;
		private TextView mAddress;
		private TextView mDistance;
		private RoundedImageView mImg;

	}

}

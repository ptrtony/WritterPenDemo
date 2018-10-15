package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CanteenBean;

public class CanteensAdapter2 extends BaseContentAdapter {

	public CanteensAdapter2(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_canteen2, null);
			mHolder = new ViewHolder();
			mHolder.canteenImg = (ImageView) convertView
					.findViewById(R.id.canteenImg);
			mHolder.canteenTitle = (TextView) convertView
					.findViewById(R.id.canteenTitle);
			mHolder.canteenadds = (TextView) convertView
					.findViewById(R.id.canteenadds);
			mHolder.price = (TextView) convertView
					.findViewById(R.id.price);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	/**
	 * content:食堂详情 meid：食堂id headImg:图片 merchantName：食堂名称
	 * 
	 * @param mHolder
	 * @param position
	 */
	private void setData(final ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		CanteenBean canteenBean = (CanteenBean) getItem(position);
		if (!TextUtils.isEmpty(canteenBean.getHeadImg())) {
			imageLoader.displayImage(canteenBean.getHeadImg(),
					mHolder.canteenImg,defaultOptions);
		} else {
			mHolder.canteenImg.setImageResource(R.drawable.ic_msg_empty);
		}
		mHolder.canteenTitle.setText(canteenBean.getMerchantName());
		mHolder.canteenadds.setText(canteenBean.getMerchantAddress());
		mHolder.price.setText("人均¥"+canteenBean.getConsumption());
	
	}

	static class ViewHolder {
		private ImageView canteenImg;
		private TextView canteenTitle;
		private TextView canteenadds;
		private TextView price;
	}

}

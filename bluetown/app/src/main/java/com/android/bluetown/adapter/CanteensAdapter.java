package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CanteenBean;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: CanteensAdapter
 * @Description:TODO(展示食堂的adapter)
 * @author: shenyz
 * @date: 2015年8月10日 下午4:33:03
 * 
 */
public class CanteensAdapter extends BaseContentAdapter {

	public CanteensAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_canteen, null);
			mHolder = new ViewHolder();
			mHolder.canteenImg = (RoundedImageView) convertView
					.findViewById(R.id.canteenImg);
			mHolder.canteenTitle = (TextView) convertView
					.findViewById(R.id.canteenTitle);
			
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
	
	}

	static class ViewHolder {
		private RoundedImageView canteenImg;
		private TextView canteenTitle;
	}

}

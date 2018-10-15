package com.android.bluetown.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.ActionCenterItemBean;
import com.bumptech.glide.Glide;

/**
 * 
 * @ClassName: ActionCenterListAdapter
 * @Description:TODO(活动中心列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class ActionCenterListAdapter extends BaseContentAdapter {
	private String flag;

	public ActionCenterListAdapter(Context mContext, String flag) {
		super(mContext);
		// TODO Auto-generated constructor stub
		this.flag = flag;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;

		if (convertView == null) {
			if (!TextUtils.isEmpty(flag)) {
				if (flag.equals("main")) {
					convertView = mInflater.inflate(R.layout.item_actioncenter,
							null);
				} else {
					convertView = mInflater.inflate(
							R.layout.item_actioncenter_fragment, null);
				}
			}
//
			mHolder = new ViewHolder();
			mHolder.companyImg = (ImageView) convertView
					.findViewById(R.id.iv_company);
			mHolder.mName = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.mTime = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.mAddress = (TextView) convertView
					.findViewById(R.id.tv_address);
			mHolder.mPeople = (TextView) convertView
					.findViewById(R.id.tv_people);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final ActionCenterItemBean item = (ActionCenterItemBean) getItem(position);
		Glide.with(context).load(item.getActionImg()).skipMemoryCache(true).centerCrop().error(R.drawable.ic_msg_empty).into(mHolder.companyImg);
//		imageLoader.displayImage(item.getActionImg(), mHolder.companyImg,defaultOptions);
		mHolder.mName.setText(item.getActionName());
		mHolder.mAddress.setText(item.getActionAddress());
		mHolder.mPeople.setText(item.getWillNum() + "/" + item.getNumber());
		if (item.getStartTime().contains(" ")) {
			mHolder.mTime.setText(item.getStartTime().subSequence(0,
					item.getStartTime().indexOf(" ")));
		} else {
			mHolder.mTime.setText(item.getStartTime());
		}

	}

	static class ViewHolder {
		private ImageView companyImg;
		private TextView mName;
		private TextView mTime;
		private TextView mAddress;
		private TextView mPeople;
	}

}

package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.SettingMessageItemBean;

/**
 * @author hedi
 * @data: 2016年4月28日 下午4:56:49
 * @Description: 月账单
 */
public class BillMonthAdapter extends BaseContentAdapter {
	private int clickTemp = -1;

	public BillMonthAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}
	public void setSeclection(int position) {
		clickTemp = position;
	}
	public String gettime(int position) {
		final SettingMessageItemBean item = (SettingMessageItemBean) getItem(position);
		return item.getUserNick();
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_bill_month, null);
			mHolder = new ViewHolder();
			mHolder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(clickTemp==position){
			mHolder.text.setBackgroundResource(R.drawable.circle_blue);
			mHolder.text.setTextColor(0xffffffff);
		}else{
			mHolder.text.setBackgroundResource(R.drawable.circle_gray);
			mHolder.text.setTextColor(0xffbdbdbd);
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final SettingMessageItemBean item = (SettingMessageItemBean) getItem(position);
		/*
		 * mHolder.nameType.setText(item.getNameType());
		 * mHolder.time.setText(item.getTime());
		 * mHolder.contents.setText(item.getContents());
		 */
		mHolder.text.setText(item.getMsgUserImg());
	}

	static class ViewHolder {
		private TextView text;

	}

}

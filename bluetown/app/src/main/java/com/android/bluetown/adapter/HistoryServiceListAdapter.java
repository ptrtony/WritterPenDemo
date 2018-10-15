package com.android.bluetown.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.HistoryServiceIteamBean;

/**
 * 
 * @ClassName: CompanyShowListAdapter
 * @Description:TODO(企业展示列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
@SuppressLint("ResourceAsColor") public class HistoryServiceListAdapter extends BaseContentAdapter {
	private List<?> data;
	private Context mContext;
	public HistoryServiceListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.mContext=mContext;
		this.data=data;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_history_service, null);
			mHolder = new ViewHolder();
			mHolder.historyServiceDate = (TextView) convertView.findViewById(R.id.faultServiceDate);
			mHolder.faultType = (TextView) convertView.findViewById(R.id.faultTypeContent);
			mHolder.faultStatus = (TextView) convertView.findViewById(R.id.faultStatus);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (position%2==0) {
			convertView.setBackgroundResource(R.color.white);
		}else {
			convertView.setBackgroundResource(R.color.app_bg_color);
		}
		setData(convertView,mHolder, position);

		return convertView;
	}

	private void setData(View convertView,ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final HistoryServiceIteamBean item = (HistoryServiceIteamBean) getItem(position);
		mHolder.historyServiceDate.setText(item.getTime());
		mHolder.faultType.setText(item.getTypeName());
		String serviceStatus=item.getState();
		if (serviceStatus.equals("1")) {
			mHolder.faultStatus.setText(R.string.dealed);	
		}else {
			mHolder.faultStatus.setText(R.string.deal_with);
		}
		
	}

	static class ViewHolder {
		private TextView historyServiceDate;
		private TextView faultType;
		private TextView faultStatus;
	}

}

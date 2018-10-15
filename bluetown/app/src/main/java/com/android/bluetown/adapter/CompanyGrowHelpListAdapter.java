package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CompanyGrowHelpBean;

/**
 * 
 * @ClassName:  CompanyGrowHelpListAdapter   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: shenyz   
 * @date:   2015年8月10日 下午4:33:03   
 *
 */
public class CompanyGrowHelpListAdapter extends BaseContentAdapter {

	public CompanyGrowHelpListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_company_grow_help, null);
			mHolder = new ViewHolder();
			mHolder.publishTime = (TextView) convertView.findViewById(R.id.publishTime);
			mHolder.publishContent = (TextView) convertView.findViewById(R.id.publishContent);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final CompanyGrowHelpBean item = (CompanyGrowHelpBean) getItem(position);
		mHolder.publishContent.setText(item.getTitle());
		mHolder.publishTime.setText(item.getCreateTime());
	}

	static class ViewHolder {
		private TextView publishContent;
		private TextView publishTime;
	}

}

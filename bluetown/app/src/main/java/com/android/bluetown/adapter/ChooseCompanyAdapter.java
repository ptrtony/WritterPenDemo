package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CompanyShowItemBean;
import com.android.bluetown.listener.OnCompanyChangeListener;

public class ChooseCompanyAdapter extends BaseContentAdapter {

	public ChooseCompanyAdapter(Context mContext, List<?> data,
			OnCompanyChangeListener listener) {
		super(mContext, data);
		// TODO Auto-generated constructor stub

	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_choose_company, null);
			mHolder = new ViewHolder();
			mHolder.companyName = (TextView) convertView
					.findViewById(R.id.companyName);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	

	private void setData(final ViewHolder mHolder, final int position) {
		// TODO Auto-generated method stub
		final CompanyShowItemBean item = (CompanyShowItemBean) getItem(position);
		mHolder.companyName.setText(item.getCompanyName());		
	}

	static class ViewHolder {
		private TextView companyName;
	}

}

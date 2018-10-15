package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.Merchant;

/**
 * 历史搜索
 * 
 * @author lig
 * 
 */
public class SearchAdapter extends BaseContentAdapter {
	public SearchAdapter(Context context, List<?> list) {
		super(context, list);
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_food_search, null);
			mHolder = new ViewHolder();
			mHolder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		Merchant merchant=(Merchant)getItem(position);
		mHolder.tv_content.setText(merchant.getMerchantName());
		return convertView;
	}

	static class ViewHolder {
		private TextView tv_content;
	}
}

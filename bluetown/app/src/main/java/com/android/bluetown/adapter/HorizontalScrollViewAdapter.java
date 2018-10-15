package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.TypeBean;

public class HorizontalScrollViewAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<TypeBean> mDatas;

	public HorizontalScrollViewAdapter(Context context, List<TypeBean> mDatas) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	public int getCount() {
		return mDatas.size();
	}

	public Object getItem(int position) {
		return mDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_action_type, parent,
					false);
			viewHolder.divider = (View) convertView.findViewById(R.id.divider);
			viewHolder.mText = (TextView) convertView
					.findViewById(R.id.actionType);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mText.setText(mDatas.get(position).getTypeName());
		return convertView;
	}

	private class ViewHolder {
		View divider;
		TextView mText;
	}

}

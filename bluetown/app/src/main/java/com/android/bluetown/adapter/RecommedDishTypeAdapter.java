package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.RecommedType;

/**
 * 推荐菜的分类
 * 
 * @author shenyz
 * 
 */
public class RecommedDishTypeAdapter extends BaseContentAdapter {
	// 设置默认显示的item
	private int defSelec;

	public RecommedDishTypeAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.context = mContext;
	}

	public int getDefSelec() {
		return defSelec;
	}


	public void setDefSelec(int defSelec) {
		this.defSelec = defSelec;
	}


	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_recommed_dish_type,
					null);
			mHolder = new ViewHolder();
			mHolder.dishType = (TextView) convertView
					.findViewById(R.id.dishType);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		RecommedType recommedType = (RecommedType) getItem(position);
		if (position==defSelec) {
			mHolder.dishType.setBackgroundColor(context.getResources().getColor(R.color.white));
		}else {
			mHolder.dishType.setBackgroundColor(context.getResources().getColor(R.color.invite_friend_title_color));
		}
		mHolder.dishType.setText(recommedType.getTypeName());

	}

	static class ViewHolder {
		private TextView dishType;
	}
}

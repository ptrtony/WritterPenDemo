package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.result.FoodClassResult.FoodClass;
import com.android.bluetown.result.FoodClassResult.FoodSubClass;
import com.android.bluetown.result.WholeCityClassResult.WholeCityClass;
import com.android.bluetown.result.WholeCityClassResult.WholeCitySubClass;

public class MerchantTypeAdapter extends BaseContentAdapter {
	// 是否显示左边的箭头
	private boolean isVision;
	// 分类的标志（美食分类，全程分类，综合分类）
	private String typeFlag;
	private Context context;
	// 设置默认显示的item
	private int defSelec;

	/**
	 * 
	 * @param mContext
	 * @param data
	 * @param isVision
	 *            是否显示左边的箭头
	 * @param typeFlag
	 *            美食分类，全程分类，综合分类
	 */
	public MerchantTypeAdapter(Context mContext, List<?> data,
			boolean isVision, String typeFlag) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.isVision = isVision;
		this.typeFlag = typeFlag;
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
			convertView = mInflater.inflate(R.layout.item_merchant_type, null);
			mHolder = new ViewHolder();
			mHolder.typeLayout = (LinearLayout) convertView
					.findViewById(R.id.typeLayout);
			mHolder.mName = (TextView) convertView
					.findViewById(R.id.merchant_left_name);
			mHolder.icn = (ImageView) convertView
					.findViewById(R.id.merchant_left_icn);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		if (isVision) {
			if (typeFlag.equals("food")) {
				// 美食总分类
				final FoodClass item = (FoodClass) getItem(position);
				mHolder.mName.setText(item.getTypeName());
			} else {
				// 全城总分类(totalCity)
				final WholeCityClass item = (WholeCityClass) getItem(position);
				mHolder.mName.setText(item.getRegion());
			}
			mHolder.icn.setVisibility(View.VISIBLE);
			if (position == defSelec) {
				mHolder.typeLayout
						.setBackgroundResource(R.drawable.food_type_bg);
				mHolder.mName.setTextColor(context.getResources().getColor(
						R.color.font_light_red));
				mHolder.icn.setImageResource(R.drawable.arrow_left_p);
			} else {
				mHolder.typeLayout.setBackgroundColor(context.getResources()
						.getColor(R.color.white));
				mHolder.mName.setTextColor(context.getResources().getColor(
						R.color.font_gray));
				mHolder.icn.setImageResource(R.drawable.arrow_left);

			}

		} else {
			mHolder.icn.setVisibility(View.GONE);
			if (typeFlag.equals("food") || typeFlag.equals("city")) {
				mHolder.typeLayout
						.setBackgroundResource(R.drawable.food_sub_type_bg);
				if (position == defSelec) {
					mHolder.mName.setTextColor(context.getResources().getColor(
							R.color.font_light_red));
				} else {
					mHolder.mName.setTextColor(context.getResources().getColor(
							R.color.font_gray));

				}
			} else if (typeFlag.equals("order")) {
				if (position == defSelec) {
					mHolder.typeLayout
							.setBackgroundResource(R.drawable.food_sub_type_bg);
					mHolder.mName.setTextColor(context.getResources().getColor(
							R.color.font_light_red));
				} else {
					mHolder.typeLayout.setBackgroundColor(context
							.getResources().getColor(R.color.white));
					mHolder.mName.setTextColor(context.getResources().getColor(
							R.color.font_gray));

				}
			}

			// 美食子分类
			if (typeFlag.equals("food")) {
				final FoodSubClass item = (FoodSubClass) getItem(position);
				mHolder.mName.setText(item.getTypeName());
			} else if (typeFlag.equals("city")) {
				// 全城子分类
				final WholeCitySubClass item = (WholeCitySubClass) getItem(position);
				// 区域名称
				mHolder.mName.setText(item.getHotRegion());
			} else if (typeFlag.equals("order")) {
				// 全城总分类
				final String item = (String) getItem(position);
				// 热门商圈名称
				mHolder.mName.setText(item);
			}
		}

	}

	static class ViewHolder {
		private ImageView icn;
		private TextView mName;
		private LinearLayout typeLayout;
	}
}

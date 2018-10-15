package com.android.bluetown.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.HomeModelBean;

/**
 * 
 * @ClassName: HomeGridViewAdapter
 * @Description:TODO(HomeActivity 版块的Adapter)
 * @author: shenyz
 * @date: 2015年7月21日 下午4:56:11
 * 
 */
public class HomeGridViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HomeModelBean> modelBeans;
	private boolean isShowMain;
	private boolean isChecked;
	private String model;

	public HomeGridViewAdapter() {
		// TODO Auto-generated constructor stub
	}

	public HomeGridViewAdapter(Context context,
			ArrayList<HomeModelBean> modelBeans, boolean isShowMain,
			boolean isChecked) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.modelBeans = modelBeans;
		this.isShowMain = isShowMain;
		this.isChecked = isChecked;
	}

	public HomeGridViewAdapter(Context context,
			ArrayList<HomeModelBean> modelBeans, String model) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.modelBeans = modelBeans;
		this.model = model;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modelBeans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return modelBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Holder holder = null;
		if (!TextUtils.isEmpty(model)) {
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(context, R.layout.item_food_type,
						null);
				holder.canteenImg = (ImageView) convertView
						.findViewById(R.id.canteenImg);
				holder.canteenTypeTitle = (TextView) convertView
						.findViewById(R.id.canteenTypeTitle);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
		} else {
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(context, R.layout.item_home_model,
						null);
				holder.modelCheck = (CheckBox) convertView
						.findViewById(R.id.typeCheck);
				holder.modelImg = (ImageView) convertView
						.findViewById(R.id.modelImg);
				holder.modelName = (TextView) convertView
						.findViewById(R.id.modelName);
				holder.hotModelImg = (ImageView) convertView
						.findViewById(R.id.hotModelImg);
				holder.hotModelName = (TextView) convertView
						.findViewById(R.id.hotModelName);
				holder.newFlag = (ImageView) convertView
						.findViewById(R.id.newFlag);
				holder.itemLy1 = (RelativeLayout) convertView
						.findViewById(R.id.itemLy1);
				holder.itemLy2 = (RelativeLayout) convertView
						.findViewById(R.id.itemLy2);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
		}
		setData(holder, position);
		return convertView;
	}

	/**
	 * 
	 * @Title: setData
	 * @Description: TODO(给Item设置数据)
	 * @param holder
	 * @param position
	 * @throws
	 */
	private void setData(Holder holder, int position) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(model)) {
			holder.canteenImg.setImageResource(modelBeans.get(position)
					.getImgResId());
			holder.canteenTypeTitle.setText(context.getString(modelBeans.get(
					position).getModelNameResId()));

		} else {
			if (isChecked) {
				// 我要开店--店铺类型
				holder.itemLy1.setVisibility(View.VISIBLE);
				holder.newFlag.setVisibility(View.GONE);
				holder.modelCheck.setVisibility(View.VISIBLE);
				holder.modelImg.setImageResource(modelBeans.get(position)
						.getImgResId());
				holder.modelName.setText(context.getString(modelBeans.get(
						position).getModelNameResId()));
				holder.modelName.setBackgroundColor(Color.WHITE);
			} else {
				if (isShowMain) {
					// HomeActivity主要版块
					holder.itemLy1.setVisibility(View.VISIBLE);
					holder.itemLy2.setVisibility(View.GONE);
					holder.modelCheck.setVisibility(View.GONE);
					holder.modelImg.setImageResource(modelBeans.get(position)
							.getImgResId());
					holder.modelName.setText(context.getString(modelBeans.get(
							position).getModelNameResId()));
					if (position == 7 || position == 8 || position == 9) {
						switch (position) {
						// 跳蚤市场
						case 8:
							isShowNewFlag(holder,
									BlueTownApp.fleaMarketMsgCount);
							break;
						// 活动中心
						case 7:
							isShowNewFlag(holder, BlueTownApp.actionMsgCount);
							break;
						// 交友
						case 9:
							isShowNewFlag(holder, BlueTownApp.unReadMsgCount);
							break;
						default:
							break;
						}
					} else {
						holder.newFlag.setVisibility(View.GONE);
					}

				} else {
					// FindActivity中的常用和更多模块
					holder.itemLy1.setVisibility(View.GONE);
					holder.itemLy2.setVisibility(View.VISIBLE);
					holder.hotModelImg.setImageResource(modelBeans
							.get(position).getImgResId());
					holder.hotModelName.setText(context.getString(modelBeans
							.get(position).getModelNameResId()));

				}

			}
		}

	}

	/**
	 * @param holder
	 */
	private void isShowNewFlag(Holder holder, int msgCount) {
		if (msgCount != 0) {
			holder.newFlag.setVisibility(View.VISIBLE);
		} else {
			holder.newFlag.setVisibility(View.GONE);
		}
	}

	static class Holder {
		private ImageView modelImg, newFlag;
		private TextView modelName;
		private CheckBox modelCheck;
		private ImageView hotModelImg;
		private TextView hotModelName;
		private RelativeLayout itemLy1, itemLy2;
		private ImageView canteenImg;
		private TextView canteenTypeTitle;
	}

}

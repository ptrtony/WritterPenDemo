package com.android.bluetown.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.CartItem;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.bean.SeleteDish;
import com.android.bluetown.listener.OnFoodCallBackListener;
import com.android.bluetown.surround.RecommendDishDetailsActivity;

/**
 * 
 * @ClassName: RecommendDishListAdapter
 * @Description:TODO(推荐菜列表的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class RecommendDishListAdapter extends BaseContentAdapter {
	private OnFoodCallBackListener listener;
	private Activity activity;

	public RecommendDishListAdapter(Context mContext, List<?> data,
			OnFoodCallBackListener listener) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.listener = listener;
		this.activity = (Activity) context;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_recommend_dish, null);
			mHolder = new ViewHolder();
			mHolder.recommendDishImg = (ImageView) convertView
					.findViewById(R.id.recommendDishImg);
			mHolder.miniDish = (ImageView) convertView
					.findViewById(R.id.miniDish);
			mHolder.addDish = (ImageView) convertView
					.findViewById(R.id.addDish);
			mHolder.dishName = (TextView) convertView
					.findViewById(R.id.dishName);
			mHolder.dishPrice = (TextView) convertView
					.findViewById(R.id.dishPrice);
			mHolder.originalPrice = (TextView) convertView
					.findViewById(R.id.originalPrice);
			mHolder.dishCount = (TextView) convertView
					.findViewById(R.id.dishCount);
			mHolder.againOne = (Button) convertView.findViewById(R.id.againOne);
			mHolder.addMinDishLy = (LinearLayout) convertView
					.findViewById(R.id.addMinDishLy);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(final ViewHolder mHolder, final int position) {
		// TODO Auto-generated method stub
		final RecommendDish item = (RecommendDish) getItem(position);
		if (!TextUtils.isEmpty(item.getHomeImg())) {
			imageLoader.displayImage(item.getHomeImg(),
					mHolder.recommendDishImg);
		} else {
		    imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty,
					
					mHolder.recommendDishImg, defaultOptions);
		}
		mHolder.dishName.setText(item.getDishesName());
		mHolder.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		mHolder.originalPrice.setText("￥" + item.getPrice());
		mHolder.dishPrice.setText("￥" + item.getFavorablePrice());
		int number = getItemCount(item);
		if (number == 0) {
			mHolder.dishCount.setText(number + "");
			mHolder.againOne.setVisibility(View.VISIBLE);
			mHolder.addMinDishLy.setVisibility(View.GONE);
		} else {
			mHolder.dishCount.setText(number + "");
			mHolder.againOne.setVisibility(View.GONE);
			mHolder.addMinDishLy.setVisibility(View.VISIBLE);
		}

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 选择的菜的数量
				String strCount = mHolder.dishCount.getText().toString();
				int dishCount = Integer.parseInt(strCount);
				switch (v.getId()) {
				case R.id.miniDish:
					if (dishCount <= 1) {
						if (dishCount == 0) {
							break;
						}
						dishCount = 0;
						mHolder.dishCount.setText(dishCount + "");
						mHolder.againOne.setVisibility(View.VISIBLE);
						mHolder.addMinDishLy.setVisibility(View.GONE);
						item.setDishesCount(dishCount);
						listener.minusFoodListener(item, CartItem.NORMAL_OPERARION);
					} else {
						dishCount = dishCount - 1;
						mHolder.dishCount.setText(dishCount + "");
						mHolder.againOne.setVisibility(View.GONE);
						mHolder.addMinDishLy.setVisibility(View.VISIBLE);
						item.setDishesCount(dishCount);
						listener.minusFoodListener(item, CartItem.NORMAL_OPERARION);
					}
					
					break;
				case R.id.addDish:
					dishCount = dishCount + 1;
					mHolder.dishCount.setText(dishCount + "");
					item.setDishesCount(dishCount);
					listener.addFoodListener(item, CartItem.NORMAL_OPERARION);
					break;
				case R.id.againOne:
					mHolder.againOne.setVisibility(View.GONE);
					mHolder.addMinDishLy.setVisibility(View.VISIBLE);
					dishCount = dishCount + 1;
					mHolder.dishCount.setText(dishCount + "");
					item.setDishesCount(1);
					listener.addFoodListener(item, CartItem.NORMAL_OPERARION);
					break;
				case R.id.recommendDishImg:
					Intent intent = new Intent();
					intent.putExtra("dishIndex", position);
					intent.putExtra("dishList", (Serializable) data);
					intent.setClass(activity,
							RecommendDishDetailsActivity.class);
					activity.startActivity(intent);
					break;
				default:
					break;
				}
			}
		};
		mHolder.againOne.setOnClickListener(clickListener);
		mHolder.miniDish.setOnClickListener(clickListener);
		mHolder.addDish.setOnClickListener(clickListener);
		mHolder.recommendDishImg.setOnClickListener(clickListener);
	}

	/**
	 * 获取当前菜的个数
	 * 
	 * @param item
	 * @return
	 */
	private int getItemCount(final RecommendDish item) {
		int number = 0;
		// 获取订单中该菜单的数量
		List<RecommendDish> dishList = BlueTownApp.getDishList();
		List<SeleteDish> orderSelectList = BlueTownApp.getOrderDishList();
		if (dishList == null) {
			dishList = new ArrayList<RecommendDish>();
		}
		if (dishList.size() > 0) {
			for (RecommendDish dish : dishList) {
				if (item.getDishesId().equals(dish.getDishesId())) {
					number = dish.getDishesCount();
					break;
				}
			}
		}
		if (orderSelectList != null && orderSelectList.size() > 0) {
			for (SeleteDish dish : orderSelectList) {
				if (dish.getDishesId().equals(item.getDishesId())) {
					number = Integer.parseInt(dish.getDishesCount());
					item.setDishesCount(number);
					for (RecommendDish dishItem : dishList) {
						if (dishItem.getDishesId().equals(item.getDishesId())) {
							dishList.remove(dishItem);
							break;
						}
					}
					dishList.add(item);
					BlueTownApp.setDishList(dishList);
					break;
				}
			}

		}
		return number;

	}

	static class ViewHolder {
		private ImageView recommendDishImg, miniDish, addDish;
		private TextView dishName;
		private TextView dishPrice;
		private TextView originalPrice;
		private TextView dishCount;
		private Button againOne;
		private LinearLayout addMinDishLy;
	}

}

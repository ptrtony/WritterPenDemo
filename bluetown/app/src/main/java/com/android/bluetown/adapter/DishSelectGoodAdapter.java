package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CartItem;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.listener.OnFoodCallBackListener;

/**
 * 点菜选好了
 * 
 * @author shenyz
 * 
 */
public class DishSelectGoodAdapter extends BaseContentAdapter {
	private Context context;
	private OnFoodCallBackListener listener;

	public DishSelectGoodAdapter(Context context, List<RecommendDish> list,
			OnFoodCallBackListener listener) {
		super(context, list);
		this.context = context;
		this.listener = listener;

	}

	@Override
	public View getContentView(int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = LayoutInflater.from(context).inflate(
					R.layout.item_dish_select_list, null);
			holder.iv_resaustgood_head = (ImageView) contentView
					.findViewById(R.id.iv_resaustgood_head);
			holder.tv_resaustgood_name = (TextView) contentView
					.findViewById(R.id.tv_resaustgood_name);
			holder.tv_resaustgood_price = (TextView) contentView
					.findViewById(R.id.tv_resaustgood_price);
			holder.tv_resaustgood_guoshi = (TextView) contentView
					.findViewById(R.id.tv_resaustgood_guoshi);
			holder.iv_resaustgood_jian = (ImageView) contentView
					.findViewById(R.id.iv_resaustgood_jian);
			holder.tv_resaustgood_num = (TextView) contentView
					.findViewById(R.id.tv_resaustgood_num);
			holder.rl_resaustgood_number = (RelativeLayout) contentView
					.findViewById(R.id.rl_resaustgood_number);
			holder.iv_resaustgood_jia = (ImageView) contentView
					.findViewById(R.id.iv_resaustgood_jia);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		final RecommendDish dish = (RecommendDish) getItem(position);
		holder.tv_resaustgood_name.setText(dish.getDishesName());
		holder.tv_resaustgood_price.setText("￥" + dish.getFavorablePrice());
		holder.tv_resaustgood_guoshi.setText("￥" + dish.getPrice());
		holder.tv_resaustgood_num.setText(dish.getDishesCount() + "");
		if (!TextUtils.isEmpty(dish.getHomeImg())) {
			imageLoader.displayImage(dish.getHomeImg(),
					holder.iv_resaustgood_head,defaultOptions);
		} else {
			holder.iv_resaustgood_head.setImageResource(R.drawable.ic_msg_empty);
		}
		holder.tv_resaustgood_guoshi.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG);
		OnClickListener click = new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strCount = holder.tv_resaustgood_num.getText()
						.toString();
				int dishCount = Integer.parseInt(strCount);
				switch (v.getId()) {
				case R.id.iv_resaustgood_jia:
					dishCount = dishCount + 1;
					holder.tv_resaustgood_num.setText(dishCount + "");
					listener.addFoodListener(dish, CartItem.ADD_OPERARION);
					break;
				case R.id.iv_resaustgood_jian:
					if (dishCount <= 1) {
						if (dishCount == 0) {
							break;
						}
						dishCount = 0;
						listener.dleFoodListener(dish);
					} else {
						dishCount = dishCount - 1;
						holder.tv_resaustgood_num.setText(dishCount + "");
						listener.minusFoodListener(dish,
								CartItem.MINUS_OPERARION);
					}
					break;

				default:
					break;
				}

			}
		};
		holder.iv_resaustgood_jian.setOnClickListener(click);
		holder.iv_resaustgood_jia.setOnClickListener(click);
		return contentView;
	}

	class ViewHolder {
		ImageView iv_resaustgood_head;
		TextView tv_resaustgood_name;
		TextView tv_resaustgood_price;
		TextView tv_resaustgood_guoshi;
		ImageView iv_resaustgood_jian;
		TextView tv_resaustgood_num;
		ImageView iv_resaustgood_jia;
		RelativeLayout rl_resaustgood_number;
	}

}

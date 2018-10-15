package com.android.bluetown.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.bean.SeleteDish;

@SuppressLint("InflateParams")
public class DishListAdapter extends BaseContentAdapter {

	public Context context;
	private List<?> list;
	private String flag;

	public DishListAdapter(Context context, List<?> list, String flag) {
		super(context, list);
		this.context = context;
		this.list = list;
		this.flag = flag;
	}

	@Override
	public View getContentView(int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (contentView == null) {
			holder = new ViewHolder();
			if (flag.equals("dish")||flag.equals("order-dish")) {
				contentView = LayoutInflater.from(context).inflate(
						R.layout.item_online_dish, null);
			} else {
				contentView = LayoutInflater.from(context).inflate(
						R.layout.item_order_dish, null);
			}

			holder.tv_name = (TextView) contentView
					.findViewById(R.id.tv_line_name);
			holder.tv_money = (TextView) contentView
					.findViewById(R.id.tv_line_price);
			holder.tv_count = (TextView) contentView
					.findViewById(R.id.tv_line_count);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		if (flag.equals("dish")) {
			final RecommendDish item = (RecommendDish) getItem(position);
			holder.tv_name.setText(item.getDishesName());
			String price = item.getFavorablePrice();
			if (!TextUtils.isEmpty(price)) {
				double unitPrice = Double.parseDouble(price);
				double totalPrice = item.getDishesCount() * unitPrice;
				String totalPriceStr = context.getString(R.string.fee,
						totalPrice);
				holder.tv_money.setText(totalPriceStr + "元");
			} else {
				holder.tv_money.setText("0.00元");
			}
			holder.tv_count.setText(item.getDishesCount() + "份");
		} else {
			final SeleteDish item = (SeleteDish) getItem(position);
			holder.tv_name.setText(item.getDishesName());
			String price = item.getDishesPrice();
			if (!TextUtils.isEmpty(price)) {
				double unitPrice = Double.parseDouble(price);
				int dishCount = Integer.parseInt(item.getDishesCount());
				double totalPrice = dishCount * unitPrice;
				String totalPriceStr = context.getString(R.string.fee,
						totalPrice);
				holder.tv_money.setText(totalPriceStr + "元");
			} else {
				holder.tv_money.setText("0.00元");
			}
			holder.tv_count.setText(item.getDishesCount() + "份");
		}

		return contentView;
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_count;
		TextView tv_money;

	}

}

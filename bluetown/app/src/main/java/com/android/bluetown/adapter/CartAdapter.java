package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.CartItem;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.listener.OnFoodCallBackListener;

public class CartAdapter extends BaseContentAdapter {
	private Context context;
	private OnFoodCallBackListener listener;

	public CartAdapter() {
		// TODO Auto-generated constructor stub
	}

	public CartAdapter(Context mContext, List<RecommendDish> data,
			OnFoodCallBackListener listener) {
		// TODO Auto-generated constructor stub
		super(mContext, data);
		this.context = mContext;
		this.listener = listener;

	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_shop_card, null);
			holder.tv_dialog_name = (TextView) convertView
					.findViewById(R.id.tv_dialog_name);
			holder.iv_diaog_jia = (ImageView) convertView
					.findViewById(R.id.iv_diaog_jia);
			holder.tv_dialog_num = (TextView) convertView
					.findViewById(R.id.tv_dialog_num);
			holder.iv_dialog_jian = (ImageView) convertView
					.findViewById(R.id.iv_dialog_jian);
			holder.tv_dialog_price = (TextView) convertView
					.findViewById(R.id.tv_dialog_price);
			holder.iv_dialog_delete = (ImageView) convertView
					.findViewById(R.id.iv_dialog_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RecommendDish item = (RecommendDish) getItem(position);
		holder.tv_dialog_name.setText(item.getDishesName());
		holder.tv_dialog_num.setText(item.getDishesCount() + "");
		float faPrice = Float.parseFloat(item.getFavorablePrice());
		float totalPrice = faPrice * item.getDishesCount();
		String totalOpriceStr = context.getString(R.string.fee, totalPrice);
		holder.tv_dialog_price.setText(totalOpriceStr + "元");
		OnClickListener click = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 选择的菜的数量
				String strCount = holder.tv_dialog_num.getText().toString();
				int dishCount = Integer.parseInt(strCount);
				try {
					switch (v.getId()) {
					case R.id.iv_diaog_jia:
						dishCount = dishCount + 1;
						holder.tv_dialog_num.setText(dishCount + "");
						listener.addFoodListener(item, CartItem.ADD_OPERARION);
						break;
					case R.id.iv_dialog_jian:
						if (dishCount <= 1) {
							if (dishCount == 0) {
								break;
							}

							dishCount = 0;
							holder.tv_dialog_num.setText(dishCount + "");
						} else {
							dishCount = dishCount - 1;
							holder.tv_dialog_num.setText(dishCount + "");
						}
						listener.minusFoodListener(item, CartItem.CART_MINUS);
						break;
					case R.id.iv_dialog_delete:
						// 清除某一菜
						listener.dleFoodListener(item);
						break;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		holder.iv_diaog_jia.setOnClickListener(click);
		holder.iv_dialog_jian.setOnClickListener(click);
		holder.iv_dialog_delete.setOnClickListener(click);
		return convertView;
	}

	class ViewHolder {
		TextView tv_dialog_name;
		ImageView iv_diaog_jia;
		TextView tv_dialog_num;
		ImageView iv_dialog_jian;
		TextView tv_dialog_price;
		ImageView iv_dialog_delete;
	}

}

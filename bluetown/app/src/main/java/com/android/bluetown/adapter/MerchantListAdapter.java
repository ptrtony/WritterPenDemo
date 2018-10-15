package com.android.bluetown.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.Merchant;
import com.android.bluetown.utils.ImageUtils;
import com.android.bluetown.view.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MerchantListAdapter extends BaseAdapter {

	public Context context;
	private List<Merchant> list;

	public MerchantListAdapter(Context context, List<Merchant> list) {
		this.context = context;
		this.list = list;
	}

	public List<Merchant> getList() {
		return list;
	}

	public void setList(List<Merchant> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder;
		// TODO Auto-generated method stub
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = LayoutInflater.from(context).inflate(
					R.layout.item_restaurant, null);
			holder.civ_restaurant_logo = (RoundedImageView) contentView
					.findViewById(R.id.civ_restaurant_logo);
			holder.iv_restaurant = (ImageView) contentView
					.findViewById(R.id.iv_restaurant);
			holder.tv_restaurant_name = (TextView) contentView
					.findViewById(R.id.tv_restaurant_name);
			holder.tv_meter_restaurant = (TextView) contentView
					.findViewById(R.id.tv_meter_restaurant);

			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		Merchant merchant = list.get(position);
		String merchantImg = merchant.getHeadImg();
		String merchantLogo = merchant.getLogoImg();
		String merchantName = merchant.getMerchantName();
		if (!TextUtils.isEmpty(merchantImg)) {
			DisplayImageOptions options = ImageUtils.getCirleMin();
			ImageLoader.getInstance().displayImage(merchantLogo,
					holder.civ_restaurant_logo, options);
		} else {
			holder.civ_restaurant_logo
					.setImageResource(R.drawable.ic_msg_empty);
		}
		if (!TextUtils.isEmpty(merchantImg)) {
			ImageLoader.getInstance().displayImage(merchantImg,
					holder.iv_restaurant, BlueTownApp.defaultOptions);
		} else {
			holder.iv_restaurant.setImageResource(R.drawable.ic_msg_empty);
		}
		if (!TextUtils.isEmpty(merchantName)) {
			if (merchantName.length() > 13) {
				holder.tv_restaurant_name.setText(merchantName.subSequence(0,
						13) + "...");
			} else {
				holder.tv_restaurant_name.setText(merchantName);
			}

		}
		BigDecimal bg = new BigDecimal(Double.parseDouble(merchant.getKm()));
		double ditans = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		String value = String.valueOf(ditans);
		holder.tv_meter_restaurant.setText(value + "km");
		return contentView;
	}

	class ViewHolder {
		RoundedImageView civ_restaurant_logo;
		ImageView iv_restaurant;
		TextView tv_restaurant_name;
		TextView tv_meter_restaurant;
	}

}

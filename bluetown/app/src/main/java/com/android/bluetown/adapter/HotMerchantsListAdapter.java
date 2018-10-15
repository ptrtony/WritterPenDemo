package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.MerchantBean;

/**
 * 
 * @ClassName: AroundMerchantsListAdapter
 * @Description:TODO(热门商家的Adapter)
 * @author: shenyz
 * @date: 2015年8月5日 上午11:16:37
 * 
 */
public class HotMerchantsListAdapter extends BaseContentAdapter {
	private List<?> data;
	private Context context;

	public HotMerchantsListAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.data = data;
		this.context = mContext;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_hot_merchant, null);
			mHolder = new ViewHolder();
			mHolder.merchantCoverImg = (ImageView) convertView
					.findViewById(R.id.hotMerchantImg);
			mHolder.merchantName = (TextView) convertView
					.findViewById(R.id.merchantName);
			mHolder.canteenDes = (TextView) convertView
					.findViewById(R.id.merchantCanteenDes);
			mHolder.canteenPrice = (TextView) convertView
					.findViewById(R.id.canteenPrice);
			mHolder.distance = (TextView) convertView
					.findViewById(R.id.merchantDistance);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		// 参数说明：
		// km:距离
		// merchantName：商家名称
		// headImg：商家图片首图
		// content：商家详情
		// consumption：人均消费
		final MerchantBean item = (MerchantBean) getItem(position);
		String merchartCoverPic = item.getHeadImg();
		if (!TextUtils.isEmpty(merchartCoverPic)) {
			imageLoader
					.displayImage(merchartCoverPic, mHolder.merchantCoverImg);
		} else {
			mHolder.merchantCoverImg.setImageResource(R.drawable.ic_msg_empty);
		}
		mHolder.merchantName.setText(item.getMerchantName());
		mHolder.canteenDes.setText(item.getContent());
		if(item.getKm()==null){
			mHolder.distance.setVisibility(View.GONE);
		}else{
			mHolder.distance.setText(item.getKm() + "km");
		}		
		String canteenPriceStr = "人均:" + item.getConsumption() + "元";
		int priceDivierIndex = canteenPriceStr.indexOf("元");
		SpannableString mSpannableString = new SpannableString(canteenPriceStr);
		mSpannableString.setSpan(new TypefaceSpan("monospace"), 3,
		
				priceDivierIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableString.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.font_red)), 3, priceDivierIndex,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mHolder.canteenPrice.setText(mSpannableString);
	}

	static class ViewHolder {
		private ImageView merchantCoverImg;
		private TextView merchantName;
		private TextView canteenDes;
		private TextView canteenPrice;
		private TextView distance;
	}

}

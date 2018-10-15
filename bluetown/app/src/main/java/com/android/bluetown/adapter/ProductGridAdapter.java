package com.android.bluetown.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.ProductBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;

public class ProductGridAdapter extends BaseContentAdapter {
	private boolean isSaleOut;

	public ProductGridAdapter(Context mContext, List<?> data, boolean isSaleOut) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.isSaleOut = isSaleOut;
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_product, null);
			mHolder = new ViewHolder();
			mHolder.productName = (TextView) convertView
					.findViewById(R.id.productName);
			mHolder.productPrice = (TextView) convertView
					.findViewById(R.id.productPrice);
			mHolder.soldOut = (TextView) convertView.findViewById(R.id.soldOut);
			mHolder.productImg = (ImageView) convertView
					.findViewById(R.id.productImg);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final ProductBean item = (ProductBean) getItem(position);
		mHolder.productName.setText(item.getCommodityName());
		mHolder.productPrice.setText("￥"+item.getPrice());
		if (item.getPictruesList() != null) {
			if (item.getPictruesList().size() > 0) {
				if (!TextUtils.isEmpty(item.getPictruesList().get(0))) {
					imageLoader.displayImage(item.getPictruesList().get(0),
							mHolder.productImg);
				} else {
					imageLoader.displayImage(
							"drawable://" + R.drawable.ic_msg_empty,
							
					mHolder.productImg);
				}
			}
		}else {
			imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty,
					
					mHolder.productImg);
		}
		
		if (isSaleOut) {
			mHolder.soldOut.setVisibility(View.VISIBLE);
		} else {
			mHolder.soldOut.setVisibility(View.GONE);
		}
		
		mHolder.soldOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSaleDialog(context, R.string.tip, R.string.confirm,
						"下架后将无法恢复，是否继续？", item);
			}

		});
	}

	static class ViewHolder {
		private TextView productName;
		private ImageView productImg;
		private TextView productPrice, soldOut;
	}

	public void saleOut(final ProductBean item) {
		// userId:(必填)
		// commodityId:商品id(必填)
		params.put("userId", item.getUserId());
		params.put("commodityId", item.getCid());
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.PRODUCT_SALE_OUT, params,
				new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							TipDialog
									.showDialog(context, R.string.tip,
											R.string.confirm,
											R.string.sale_out_success);
						} else {
							TipDialog.showDialog(context, R.string.tip,
									R.string.confirm, result.getRepMsg());
						}
					}
				});
	}

	public void showSaleDialog(final Context context, int titleId,
			int confirmTextId, String contentStr, final ProductBean item) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(contentStr);
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentText(contentStr);
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				saleOut(item);
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

}

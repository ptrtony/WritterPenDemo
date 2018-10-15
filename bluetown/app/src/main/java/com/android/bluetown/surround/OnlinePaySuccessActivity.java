package com.android.bluetown.surround;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.SeleteDish;

/**
 * 
 * @ClassName: OnlinePayMoneySureActivity
 * @Description: TODO 支付成功
 * @author zhangf
 * @date 2014年12月22日 上午 3:04:08
 * 
 */
public class OnlinePaySuccessActivity extends TitleBarActivity implements
		OnClickListener {
	private Button bt_onlinepay_rever;
	private String mid, orderType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_online_pay_success);
		BlueTownExitHelper.addActivity(this);
		bt_onlinepay_rever = (Button) this
				.findViewById(R.id.bt_onlinepay_rever);
		orderType = BlueTownApp.orderType;
		if (orderType.equals("0")) {
			bt_onlinepay_rever.setText(R.string.book_orderlist);
		} else {
			bt_onlinepay_rever.setText(R.string.book_order_dish);

		}
		bt_onlinepay_rever.setOnClickListener(this);
		mid = getIntent().getStringExtra("mid");
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.pay_success);
		backImageLayout.setOnClickListener(this);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.bt_onlinepay_rever:
			// 预订订单页面还是预定点菜页面
			intent.setClass(OnlinePaySuccessActivity.this,
					OrderListActivity.class);
			intent.putExtra("flag", orderType);
			startActivity(intent);
			break;
		case R.id.backLayout:
			List<SeleteDish> seleteDishs = BlueTownApp.getOrderDishList();
			if ((seleteDishs != null && seleteDishs.size() > 0)
					|| BlueTownApp.ACTIVITY_ACTION.equals("1")) {
				intent.putExtra("flag", orderType);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(OnlinePaySuccessActivity.this,
						OrderListActivity.class);
				startActivity(intent);
				finish();
			} else {
				intent.putExtra("meid", mid);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(OnlinePaySuccessActivity.this,
						MerchantDetailsActivity.class);
				startActivity(intent);
				finish();
			}
			resetCacheData();
			break;
		default:
			break;
		}

	}

	/**
	 * 重置缓存数据
	 */
	private void resetCacheData() {
		BlueTownApp.setDishList(null);
		BlueTownApp.setOrderDishList(null);
		BlueTownApp.setDishesCount(0);
		BlueTownApp.ACTIVITY_ACTION = "";
		BlueTownApp.setDishesPrice("0.00");
		BlueTownApp.setOriginalPrice("0.00");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startAction();
			resetCacheData();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 支付成功后，activity跳转
	 */
	private void startAction() {
		List<SeleteDish> seleteDishs = BlueTownApp.getOrderDishList();
		Intent intent = new Intent();
		if (seleteDishs != null && seleteDishs.size() > 0) {
			intent.putExtra("flag", orderType);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(OnlinePaySuccessActivity.this,
					OrderListActivity.class);
			startActivity(intent);
			finish();
		} else {
			intent.putExtra("meid", mid);
			intent.setClass(OnlinePaySuccessActivity.this,
					MerchantDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}
}

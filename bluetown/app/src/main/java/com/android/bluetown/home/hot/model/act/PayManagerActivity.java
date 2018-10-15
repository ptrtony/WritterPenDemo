package com.android.bluetown.home.hot.model.act;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.custom.dialog.TipDialog;

/**
 * 
 * @ClassName: PayManagerActivity
 * @Description:TODO(HomeActivity-缴费管理)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:39:09
 * 
 */
public class PayManagerActivity extends TitleBarActivity implements OnClickListener {
	private RelativeLayout propertyFeeLy, waterRateLy, electricChargeLy, coalGasLy, creditCardPaymentLy;
	private LinearLayout locationLy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_pay_manager);
		BlueTownExitHelper.addActivity(this);
		propertyFeeLy = (RelativeLayout) findViewById(R.id.propertyFeeLy);
		waterRateLy = (RelativeLayout) findViewById(R.id.waterRateLy);
		electricChargeLy = (RelativeLayout) findViewById(R.id.electricChargeLy);
		coalGasLy = (RelativeLayout) findViewById(R.id.coalGasLy);
		creditCardPaymentLy = (RelativeLayout) findViewById(R.id.creditCardPaymentLy);
		locationLy = (LinearLayout) findViewById(R.id.locationLy);
		propertyFeeLy.setOnClickListener(this);
		waterRateLy.setOnClickListener(this);
		electricChargeLy.setOnClickListener(this);
		coalGasLy.setOnClickListener(this);
		creditCardPaymentLy.setOnClickListener(this);
		locationLy.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.payment_management);
//		setBottomLine();
		rightImageLayout.setVisibility(View.INVISIBLE);
		setMainLayoutBg(R.drawable.payment_pic_bg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.propertyFeeLy:
			TipDialog.showDialog(PayManagerActivity.this, R.string.tip, R.string.confirm, R.string.more_info);
			break;
		case R.id.waterRateLy:
			TipDialog.showDialog(PayManagerActivity.this, R.string.tip, R.string.confirm, R.string.more_info);
			break;
		case R.id.electricChargeLy:
			TipDialog.showDialog(PayManagerActivity.this, R.string.tip, R.string.confirm, R.string.more_info);
			break;
		case R.id.coalGasLy:
			TipDialog.showDialog(PayManagerActivity.this, R.string.tip, R.string.confirm, R.string.more_info);
			break;
		case R.id.creditCardPaymentLy:
			TipDialog.showDialog(PayManagerActivity.this, R.string.tip, R.string.confirm, R.string.more_info);
			break;
		case R.id.locationLy:
			TipDialog.showDialog(PayManagerActivity.this, R.string.tip, R.string.confirm, R.string.more_info);
			break;
		default:
			break;
		}
	}

}

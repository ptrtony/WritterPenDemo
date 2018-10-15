package com.android.bluetown;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: RegisterActivity
 * @Description:TODO(用户注册界面)
 * @author: shenyz
 * @date: 2015年7月17日 上午11:59:42
 * 
 */
public class RegisterActivity extends TitleBarActivity implements OnClickListener {
	/** 企业用户、普通用户、商户 */
	private Button companyUserBtn, normalUserBtn, merchantBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_register);
		BlueTownExitHelper.addActivity(this);
		initViews();
	}

	/**
	 * 
	 * @Title: initViews
	 * @Description: TODO(初始化界面组件并设置onClick点击事件)
	 * @throws
	 */
	private void initViews() {
		companyUserBtn = (Button) findViewById(R.id.companyUser);
		normalUserBtn = (Button) findViewById(R.id.normalUser);
		merchantBtn = (Button) findViewById(R.id.merchant);
		companyUserBtn.setOnClickListener(this);
		normalUserBtn.setOnClickListener(this);
		merchantBtn.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.bm.wisdomcity.base.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.register);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setMainLayoutBg(R.drawable.app_pic_bg);
//		setBottomLine();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		switch (v.getId()) {
		case R.id.companyUser:
			//普通用户跳转注册手机验证界面
			intent.putExtra("flag", getString(R.string.register));
			intent.putExtra("userType", Constant.COMPANY_USER);
			intent.setClass(RegisterActivity.this,RegisterVerifyFindPwdActivty.class);
			startActivity(intent);
			break;
		case R.id.normalUser:
			//普通用户跳转注册手机验证界面
			intent.putExtra("flag", getString(R.string.register));
			intent.putExtra("userType", Constant.NORMAL_USER);
			intent.setClass(RegisterActivity.this,RegisterVerifyFindPwdActivty.class);
			startActivity(intent);
			break;
		case R.id.merchant:
			//商户跳转到我要开店页面
			intent.putExtra("userType", Constant.MERCHANT);
			intent.setClass(RegisterActivity.this,OpenShopActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}

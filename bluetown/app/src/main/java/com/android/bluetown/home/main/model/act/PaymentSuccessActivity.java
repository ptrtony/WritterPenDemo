package com.android.bluetown.home.main.model.act;

import android.os.Bundle;
import android.view.View;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * 
 * @ClassName: 钱包--收款成功
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月20日 下午4:49:51
 * 
 */
public class PaymentSuccessActivity extends TitleBarActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_payment_success);
		BlueTownExitHelper.addActivity(this);
	}

	public void initTitle() {
		setBackImageView();
		setTitleView(R.string.guest_appoint_sucess);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

}

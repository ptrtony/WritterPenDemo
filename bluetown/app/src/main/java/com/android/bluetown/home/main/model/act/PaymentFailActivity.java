package com.android.bluetown.home.main.model.act;

import android.os.Bundle;
import android.view.View;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * 
 * @ClassName: 钱包--付款失败
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月20日 下午4:49:12
 * 
 */
public class PaymentFailActivity extends TitleBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_payment_fail);
		BlueTownExitHelper.addActivity(this);
	}

	public void initTitle() {
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.guest_appoint_sucess);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}
}

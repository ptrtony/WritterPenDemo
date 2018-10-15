package com.android.bluetown.mywallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * @author hedi
 * @data:  2016年5月5日 上午9:37:58 
 * @Description:  发送验证码 支付密码
 */
public class SendVerifyCodeActivity extends TitleBarActivity implements
OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_send_verifycode);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.confirm:
			startActivity(new Intent(SendVerifyCodeActivity.this,
					MyWalletSetPSWActivity.class));
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("发送验证码");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		findViewById(R.id.confirm).setOnClickListener(this);
	}

}

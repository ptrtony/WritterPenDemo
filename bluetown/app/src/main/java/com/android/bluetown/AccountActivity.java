package com.android.bluetown;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.mywallet.activity.MyWalletHistoryActivity;

//我的账户
public class AccountActivity extends TitleBarActivity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_mywallet);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	private void initView() {
		findViewById(R.id.layoutButton1).setOnClickListener(this);
		findViewById(R.id.rightTextView).setOnClickListener(this);
		
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.my_wallet);
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView(R.string.history_check);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layoutButton1:
			TipDialog.showDialog(AccountActivity.this, R.string.tip, R.string.confirm, "目前无法充值，请连接服务器");
			break;
		case R.id.rightTextView:
			startActivity(new Intent(AccountActivity.this, MyWalletHistoryActivity.class));
			break;
		default:
			break;
		}
		
	}

}
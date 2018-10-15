package com.android.bluetown.mywallet.activity;

import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.CoreActivity;
import com.android.bluetown.PayCodeActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;

//我的钱包
public class MyWalletActivity extends TitleBarActivity implements
		OnClickListener {
	private FinalDb db;
	private String userId;
	private SharePrefUtils prefUtils;
	private TextView tv_balance;
	private String balance; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_mywallet);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	private void initView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		tv_balance=(TextView)findViewById(R.id.balance);
		findViewById(R.id.ll_balance).setOnClickListener(this);
		findViewById(R.id.rl_recharge).setOnClickListener(this);
//		findViewById(R.id.rl_transfer).setOnClickListener(this);
		findViewById(R.id.rl_saoyisao).setOnClickListener(this);
		findViewById(R.id.rl_QR).setOnClickListener(this);
		findViewById(R.id.paysetup).setOnClickListener(this);
		

	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.my_wallet);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.ll_balance:
			startActivity(new Intent(MyWalletActivity.this,
					BliiListActivity.class));
			break;
		case R.id.rl_recharge:
			startActivity(new Intent(MyWalletActivity.this,
					RechargeActivity.class));
			break;
//		case R.id.rl_transfer:
//			Intent intent = (new Intent(MyWalletActivity.this,
//					TransferActivity.class));
//			intent.putExtra("balance", balance);
//			startActivity(intent);
//			break;
		case R.id.rl_saoyisao:
			startActivity(new Intent(MyWalletActivity.this,
					CoreActivity.class));
			break;
		case R.id.rl_QR:
			startActivity(new Intent(MyWalletActivity.this,
					PayCodeActivity.class));
			break;
		case R.id.paysetup:
			startActivity(new Intent(MyWalletActivity.this,
					MyWalletSetupActivity.class));
			break;

		default:
			break;
		}

	}
	private void getdata() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken", prefUtils.getString(SharePrefUtils.TOKEN, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_queryMoneyInformation, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								balance=json.optString("data2");
								tv_balance.setText("¥"+json.optString("data2"));
							} else {
								Toast.makeText(MyWalletActivity.this,
										json.optString("repMsg"),
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getdata();
	}

}

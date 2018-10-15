package com.android.bluetown.mywallet.activity;

import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.alipay.AlipayUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.OnPasswordInputFinish;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;
import com.android.bluetown.view.NumberPayCodeView.BaseNumberCodeView;
import com.android.bluetown.view.NumberPayCodeView.NumberCodeView;
import com.android.bluetown.view.PasswordView;

public class MyWalletSetPSWActivity extends TitleBarActivity {
	private NumberCodeView pwdView;
	private String password = null;
	private TextView text;
	private FinalDb db;
	private String userId;
	private SharePrefUtils prefUtils;
	private int state = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_mywalletsetpsw);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("设置支付密码");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		pwdView = findViewById(R.id.pwd_view);
		text = (TextView) findViewById(R.id.text);
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(MyWalletSetPSWActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}

		pwdView.setIsPassword(true);
		pwdView.setOnFinishInput(new BaseNumberCodeView.OnPasswordInputFinish() {
			@Override
			public void inputFinish(String payPWd) {
				if (state == 1) {
					state = 2;
					password = payPWd;
					pwdView. restoreViews();
					text.setText("请再次输入以确认支付密码");
				} else if (state == 2) {
					if (password.equals(payPWd)) {
						setpassword(password);
					} else {
						text.setText("支付密码不一致，请重新输入");
						text.setTextColor(0xfff26666);
						pwdView.restoreViews();
					}
				}
			}
		});
	}

	private void setpassword(final String passWord) {
		// TODO Auto-generated method stub
		params.put("uid", userId);
		params.put("payPassword", MD5Util.encoder(passWord));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MobiMemberAction_payPassword, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								prefUtils.setString(SharePrefUtils.PAYPASSWORD, MD5Util.encoder(passWord));
								Toast.makeText(MyWalletSetPSWActivity.this,
										"设置成功", Toast.LENGTH_SHORT).show();
								prefUtils.setInt(SharePrefUtils.PAY_NUM, 5);
								MyWalletSetPSWActivity.this.finish();
							} else {
								Toast.makeText(MyWalletSetPSWActivity.this,
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

}

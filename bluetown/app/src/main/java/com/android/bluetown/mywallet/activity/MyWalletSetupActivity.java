package com.android.bluetown.mywallet.activity;

import java.util.List;

import net.tsz.afinal.FinalDb;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.datewheel.NoPSWPayPickerDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.popup.PasswordPop;
import com.android.bluetown.utils.AlphaUtil;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;
import com.android.bluetown.view.SwitchView;
import com.android.bluetown.view.SwitchView.OnStateChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Description 支付设置
 * @author hedi
 * @date 2016年4月18日14:31:50
 * @version 1.0.0
 */
public class MyWalletSetupActivity extends TitleBarActivity implements
		OnClickListener {
	private TextView tv_no_psw;
	private PersonPickerRecevier recevier;
	private SwitchView switchview;
	private RelativeLayout rl_no_psw;
	private SharePrefUtils prefUtils;
	private FinalDb db;
	private String userId;
	private String personCount;
	private String merchatId;
	private String code;

	/**
	 * 注册设置园区的广播
	 */
	private void registerPersonRecevier() {
		recevier = new PersonPickerRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.guestcount.choice.action");
		filter.addAction("android.setStatus.action");
		registerReceiver(recevier, filter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_mywalletsetup);
		BlueTownExitHelper.addActivity(this);
		registerPersonRecevier();
		initView();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("支付设置");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		tv_no_psw = (TextView) findViewById(R.id.tv_no_psw);
		switchview = (SwitchView) findViewById(R.id.view_switch);
		rl_no_psw = (RelativeLayout) findViewById(R.id.rl_no_psw);
		findViewById(R.id.change_psw).setOnClickListener(this);
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(MyWalletSetupActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		tv_no_psw.setOnClickListener(this);
		if (prefUtils.getString(SharePrefUtils.NOPASSWORDPAY, "").equals("1")) {
			switchview.setOpened(true);
			personCount = prefUtils.getString(SharePrefUtils.NOPASSWORDPAY_COUNT, "");
			tv_no_psw.setText(personCount);

		} else {
			switchview.setOpened(false);
			rl_no_psw.setVisibility(View.GONE);
		}

		switchview.setOnStateChangedListener(new OnStateChangedListener() {

			@Override
			public void toggleToOn(View view) {
				// TODO Auto-generated method stub
				switchview.setOpened(false);
				rl_no_psw.setVisibility(View.GONE);
				if (prefUtils.getString(SharePrefUtils.PAYPASSWORD, "").equals(
						"null")
						|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "")
								.equals("")
						|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "") == null) {
					new PromptDialog.Builder(MyWalletSetupActivity.this)
					.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
					.setMessage("请前往设置支付密码")
					.setButton1("取消",
							new PromptDialog.OnClickListener() {

								@Override
								public void onClick(Dialog dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();

								}
							})
					.setButton2("确认前往",
							new PromptDialog.OnClickListener() {

								@Override
								public void onClick(Dialog dialog,
										int which) {
									// TODO Auto-generated method stub
									startActivity(new Intent(MyWalletSetupActivity.this, MyWalletSetPSWActivity.class));
									dialog.cancel();
								}
							}).show();
					return;
				}
				NoPSWPayPickerDialog personDialog = new NoPSWPayPickerDialog(
						MyWalletSetupActivity.this);
				personDialog.show();
			}

			@Override
			public void toggleToOff(View view) {
				// TODO Auto-generated method stub
				switchview.setOpened(true);
				rl_no_psw.setVisibility(View.VISIBLE);
				PasswordPop pop = new PasswordPop(MyWalletSetupActivity.this,
						findViewById(R.id.hideView), userId, "0", "0", null,
						null, null, 0);
				pop.showPopupWindow(findViewById(R.id.hideView));
			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_no_psw:
			char[] b = personCount.toCharArray();
			String result = "";
			for (int i = 0; i < b.length; i++) {
				if (("0123456789.").indexOf(b[i] + "") != -1) {
					result += b[i];
				}
			}
			PasswordPop pop = new PasswordPop(MyWalletSetupActivity.this,
					findViewById(R.id.hideView), userId, "1", result, null,
					null, null, 4);
			pop.showPopupWindow(findViewById(R.id.hideView));

			break;
		case R.id.change_psw:
			if (prefUtils.getString(SharePrefUtils.PAYPASSWORD, "").equals(
					"null")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "")
							.equals("")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "") == null) {
				startActivity(new Intent(MyWalletSetupActivity.this,
						MyWalletSetPSWActivity.class));
			} else {
				startActivity(new Intent(MyWalletSetupActivity.this,
						PayPasswordCheckActivity.class));
			}

			break;
		}
	}

	private class PersonPickerRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals("android.guestcount.choice.action")) {
				personCount = intent.getStringExtra("personCount");
				tv_no_psw.setText(personCount);
				setStatus(userId, merchatId,
						MD5Util.encoder(code),
						personCount, MyWalletSetupActivity.this);
			}

			if (action.equals("android.setStatus.action")) {
				String money = intent.getStringExtra("money");
				code = intent.getStringExtra("code");
				merchatId = intent.getStringExtra("status");
				if ("1".equals(merchatId)) {
					switchview.setOpened(true);
					rl_no_psw.setVisibility(View.VISIBLE);
					prefUtils.setString(SharePrefUtils.NOPASSWORDPAY, "1");
					prefUtils.setString(SharePrefUtils.NOPASSWORDPAY_COUNT, money);
					NoPSWPayPickerDialog personDialog = new NoPSWPayPickerDialog(
							MyWalletSetupActivity.this);
					personDialog.show();
				} else if ("0".equals(merchatId)) {
					switchview.setOpened(false);
					rl_no_psw.setVisibility(View.GONE);
					prefUtils.setString(SharePrefUtils.NOPASSWORDPAY, "0");
					prefUtils.setString(SharePrefUtils.NOPASSWORDPAY_COUNT, money);
				}
			}
		}
	}


	private void setStatus(String userid, final String status,
						   final String ppwd, final String money, final Activity mcontext) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("uid", userid);
		params.put("status", status);
		params.put("pwd", ppwd);
		params.put("money", money);
		httpUtil.post(Constant.HOST_URL
						+ Constant.Interface.MobiMemberAction_changeStatus, params,
				new AbsHttpStringResponseListener(mcontext, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
//						JSONObject json;
//						try {
//							json = new JSONObject(arg1);
//							String repCode = json.optString("repCode");
							prefUtils.setString(SharePrefUtils.NOPASSWORDPAY_COUNT,money);
//							if (repCode.equals(Constant.HTTP_SUCCESS)) {
//								prefUtils.setString(SharePrefUtils.NOPASSWORDPAY_COUNT,money);
//							} else if (repCode.equals("666666")) {
//								new PromptDialog.Builder(mcontext)
//										.setViewStyle(
//												PromptDialog.VIEW_STYLE_NORMAL)
//										.setMessage(json.optString("repMsg"))
//										.setButton1(
//												"确认前往",
//												new PromptDialog.OnClickListener() {
//
//													@Override
//													public void onClick(
//															Dialog dialog,
//															int which) {
//														// TODO Auto-generated
//														// method stub
//														dialog.cancel();
//														Intent intent = new Intent(
//																mcontext,
//																RechargeActivity.class);
//														intent.putExtra("type",
//																0);
//														mcontext.startActivity(intent);
//													}
//												}).show();
//							} else {
//								new PromptDialog.Builder(mcontext)
//										.setViewStyle(
//												PromptDialog.VIEW_STYLE_NORMAL)
//										.setMessage(json.optString("repMsg"))
//										.setButton1(
//												"确定",
//												new PromptDialog.OnClickListener() {
//
//													@Override
//													public void onClick(
//															Dialog dialog,
//															int which) {
//														// TODO Auto-generated
//														// method stub
//														dialog.cancel();
//													}
//												}).show();
//							}

//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}

					}

				});
	}

}

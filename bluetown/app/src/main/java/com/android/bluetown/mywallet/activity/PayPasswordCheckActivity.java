package com.android.bluetown.mywallet.activity;

import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.OnPasswordInputFinish;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;
import com.android.bluetown.view.NumberPayCodeView.BaseNumberCodeView;
import com.android.bluetown.view.NumberPayCodeView.NumberCodeView;
import com.android.bluetown.view.PasswordView;

public class PayPasswordCheckActivity extends TitleBarActivity implements
		OnClickListener {
	private NumberCodeView pwdView;
	private TextView text;
	private int i = 5;
	private int j = 5;
	private SharePrefUtils prefUtils;
	private FinalDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_mywalletsetpsw);
		BlueTownExitHelper.addActivity(this);
		db = FinalDb.create(this);
		initView();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("修改支付密码");
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView("忘记密码");
		righTextLayout.setOnClickListener(this);
	}

	private void lock(String userid, final Activity context) {
		AbRequestParams params = new AbRequestParams();
		params.put("userId", userid);
		params.put("communicationToken",
				prefUtils.getString(prefUtils.TOKEN, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_lockingUser, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								new PromptDialog.Builder(context)
								.setViewStyle(
										PromptDialog.VIEW_STYLE_NORMAL)
								.setMessage(json.optString("repMsg"))
								.setCancelable(false)
								.setButton1(
										"确定",
										new PromptDialog.OnClickListener() {

											@Override
											public void onClick(
													Dialog dialog,
													int which) {
												// TODO Auto-generated
												// method stub
												Intent intent = new Intent();
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
														| Intent.FLAG_ACTIVITY_NEW_TASK);
												intent.setClass(context, MainActivity.class);
												context.startActivity(intent);
											}
										}).show();

							} else {
								new PromptDialog.Builder(context)
										.setViewStyle(
												PromptDialog.VIEW_STYLE_NORMAL)
										.setMessage(json.optString("repMsg"))
										.setButton1(
												"确定",
												new PromptDialog.OnClickListener() {

													@Override
													public void onClick(
															Dialog dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														pwdView.restoreViews();
														dialog.cancel();
													}
												}).show();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

				});
	}

	private void initView() {
		pwdView = findViewById(R.id.pwd_view);
		text = (TextView) findViewById(R.id.text);
		text.setText("请输入原支付密码，以验证身份");
		prefUtils = new SharePrefUtils(this);
		pwdView.setIsPassword(true);
		pwdView.setOnFinishInput(new BaseNumberCodeView.OnPasswordInputFinish() {
			@Override
			public void inputFinish(String payPWd) {
				if (MD5Util.encoder(payPWd).equals(
						prefUtils.getString(SharePrefUtils.PAYPASSWORD, ""))) {
					Intent intent = new Intent(PayPasswordCheckActivity.this,
							MyWalletSetPSWActivity.class);
					startActivity(intent);
					PayPasswordCheckActivity.this.finish();
				} else {
					i--;
					text.setText("支付密码错误，剩余" + i + "次机会");
					text.setTextColor(0xfff26666);
					pwdView.restoreViews();
					if (i == 0) {
						new PromptDialog.Builder(PayPasswordCheckActivity.this)
								.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
								.setMessage("原支付密码输入错误，请重新登录")
								.setButton1("确定",
										new PromptDialog.OnClickListener() {

											@Override
											public void onClick(Dialog dialog,
																int which) { // TODO
												String userId = "";
												List<MemberUser> users = db
														.findAll(MemberUser.class);
												if (users != null
														&& users.size() != 0) {
													MemberUser user = users
															.get(0);
													if (user != null) {
														userId = user
																.getMemberId();
													}
												}
												if (!TextUtils.isEmpty(userId)) {
													// 删除登录保存的登录信息

													lock(userId,
															PayPasswordCheckActivity.this);

												}
											}
										}).show();

					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = (View) inflater.inflate(R.layout.item_edittext,
					null);
			new PromptDialog.Builder(PayPasswordCheckActivity.this)
					.setView(layout)
					.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
					.setButton1("确定", new PromptDialog.OnClickListener() {

						@Override
						public void onClick(Dialog dialog, int which) {
							// TODO Auto-generated method stub
							EditText password = (EditText) layout
									.findViewById(R.id.password);
							if (MD5Util.encoder(password.getText().toString())
									.equals(prefUtils.getString(
											SharePrefUtils.PASSWORD, ""))) {
								Intent intent = new Intent(
										PayPasswordCheckActivity.this,
										MyWalletSetPSWActivity.class);
								startActivity(intent);
								PayPasswordCheckActivity.this.finish();
							} else {
								j--;
								if (j == 0) {
									String userId = "";
									List<MemberUser> users = db
											.findAll(MemberUser.class);
									if (users != null && users.size() != 0) {
										MemberUser user = users.get(0);
										if (user != null) {
											userId = user.getMemberId();
										}
									}
									if (!TextUtils.isEmpty(userId)) {
										// 删除登录保存的登录信息
										lock(userId,
												PayPasswordCheckActivity.this);
									}
								} else {
									password.setText("");
									Toast.makeText(
											PayPasswordCheckActivity.this,
											"密码错误(剩余" + j + "次机会)",
											Toast.LENGTH_SHORT).show();
								}
							}

						}
					}).setButton2("取消", new PromptDialog.OnClickListener() {

						@Override
						public void onClick(Dialog dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					}).show();

			break;

		default:
			break;
		}
	}
}

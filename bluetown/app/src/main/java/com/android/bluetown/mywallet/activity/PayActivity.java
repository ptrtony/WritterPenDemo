package com.android.bluetown.mywallet.activity;

import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.my.AuthenticationActivity;
import com.android.bluetown.my.AuthenticationIngActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.EditInputFilter;
import com.android.bluetown.popup.PasswordPop;

/**
 * @author hedi
 * @data: 2016年5月24日 上午11:35:07
 * @Description: TODO<支付页面>
 */
public class PayActivity extends TitleBarActivity implements OnClickListener {
	private EditText account_count;
	private TextView confirm;
	private TextView cant_confirm;
	private TextView name;
	private FinalDb db;
	private String userId;
	private String token;
	private String telephone;
	private String merchantId;
	private String ttoken;
	private SharePrefUtils prefUtils;
	private TextView amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_pay);
		BlueTownExitHelper.addActivity(this);
		initView();
		getdata();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm:
			if(!prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("1")){
				TipDialog.showDialogNoClose(PayActivity.this,
						R.string.tip, R.string.gotoAuthentication,
						R.string.Authenticationinfo, AuthenticationActivity.class);
				return;
			}
			if(prefUtils.getString(SharePrefUtils.CHECKSTATE, "").equals("3")){
				TipDialog.showDialogNoClose(this,
						R.string.tip, R.string.AuthenticationIng,
						R.string.Authenticationinfo,
						AuthenticationIngActivity.class);
				return;
			}
			if (prefUtils.getString(SharePrefUtils.PAYPASSWORD, "").equals(
					"null")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "")
							.equals("")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "") == null) {
				new PromptDialog.Builder(PayActivity.this)
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
								startActivity(new Intent(PayActivity.this, MyWalletSetPSWActivity.class));
								dialog.cancel();
							}
						}).show();
				return;
			}
			if (prefUtils.getString(SharePrefUtils.NOPASSWORDPAY, "")
					.equals("1")) {
				if (Double.parseDouble(account_count.getText().toString()) < Double
						.parseDouble(prefUtils.getString(
								SharePrefUtils.NOPASSWORDPAY_COUNT, ""))) {
					pay(userId, getIntent().getStringExtra("merchantId"),
							getIntent().getStringExtra("ttoken"), account_count
									.getText().toString(), prefUtils.getString(
									SharePrefUtils.PAYPASSWORD, ""),
							telephone,getIntent().getStringExtra("merchantName"),
							PayActivity.this);
					return;
				}
			}
			PasswordPop pop = new PasswordPop(PayActivity.this,
					findViewById(R.id.hideView), userId, getIntent()
							.getStringExtra("merchantId"), getIntent()
							.getStringExtra("ttoken"), account_count.getText()
							.toString(), telephone, getIntent().getStringExtra(
							"merchantName"), 1);
			pop.showPopupWindow(findViewById(R.id.hideView));

			break;

		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("付款");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	private void initView() {
		account_count = (EditText) findViewById(R.id.account_count);
		confirm = (TextView) findViewById(R.id.confirm);
		cant_confirm = (TextView) findViewById(R.id.cant_confirm);
		name=(TextView)findViewById(R.id.name);
		amount=(TextView)findViewById(R.id.amount);
		confirm.setOnClickListener(this);
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(PayActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				token = prefUtils.getString(SharePrefUtils.TOKEN, "");
				userId = user.getMemberId();
				telephone = user.getUsername();
			}
		}
		name.setText("向"+getIntent().getStringExtra("merchantName")+"付款");
		InputFilter[] filters = { new EditInputFilter(1000000000) };
		account_count.setFilters(filters);
		account_count.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						account_count.setText(s);
						account_count.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					account_count.setText(s);
					account_count.setSelection(2);
				}
				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						account_count.setText(s.subSequence(0, 1));
						account_count.setSelection(1);
						return;
					}
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					confirm.setVisibility(View.GONE);
					cant_confirm.setVisibility(View.VISIBLE);
				} else {
					confirm.setVisibility(View.VISIBLE);
					cant_confirm.setVisibility(View.GONE);
				}
			}
		});
	}
	private void getdata() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
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
								amount.setText(json.optString("data2"));
							} else {
								Toast.makeText(PayActivity.this,
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
	private void pay(String userid, String merchantId, String ttoken,
			final String money, String ppwd, String phoneNumber,
			String merchantName, final Activity mcontext) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("merchantId", merchantId);
		params.put("type", "用户");
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("tradeType", "1");
		params.put("tradeTypeStr", "支付");
		params.put("tradeTypeM", "3");
		params.put("tradeTypeStrM", "收款");
		params.put("payType", "3");
		params.put("payTypeStr", "平台支付");
		params.put("amonut", money);
		params.put("phoneNumber", phoneNumber);
		params.put("customerName", phoneNumber);
		params.put("merchantName", merchantName);
		params.put("billStatus", "6" );
		params.put("billStatusStr", "转账" );
		params.put("commodityInformation", merchantName );
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.PaymentCodeAction_confirmPayment, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								mcontext.finish();
								Intent intent = new Intent(mcontext,
										TransferSuccessActivity.class);
								intent.putExtra("title", "支付");
								intent.putExtra("money", "¥" + money);
								mcontext.startActivity(intent);
							} 
							else if (repCode.equals("666666")) {
								new PromptDialog.Builder(mcontext)
								.setViewStyle(
										PromptDialog.VIEW_STYLE_NORMAL)
								.setMessage(json.optString("repMsg"))
								.setButton1(
										"确认前往",
										new PromptDialog.OnClickListener() {

											@Override
											public void onClick(
													Dialog dialog,
													int which) {
												// TODO Auto-generated
												// method stub
												dialog.cancel();
												Intent intent = new Intent(mcontext,
														RechargeActivity.class);
												intent.putExtra("type", 0);
												mcontext.startActivity(intent);
											}
										}).show();
							} else {
								new PromptDialog.Builder(mcontext)
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
														dialog.cancel();
													}
												}).show();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}


				});
	}
}

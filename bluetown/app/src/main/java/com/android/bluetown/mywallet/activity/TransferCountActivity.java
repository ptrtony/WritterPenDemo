package com.android.bluetown.mywallet.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
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
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.EditInputFilter;
import com.android.bluetown.popup.PasswordPop;
import com.android.bluetown.view.RoundedImageView;

/**
 * @author hedi
 * @data: 2016年4月27日 下午1:03:39
 * @Description: 转账金额
 */
public class TransferCountActivity extends TitleBarActivity implements
		OnClickListener {
	private RoundedImageView userImg;
	private EditText account_count;
	private TextView confirm;
	private TextView cant_confirm;
	private TextView name;
	private TextView telephone;
	private TextView balance;
	private FinalDb db;
	private String userId;
	private String garden;
	private String phoneNumber;
	private SharePrefUtils prefUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_transfer_count);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm:
			gettoken();
			break;
		case R.id.rightTextLayout:
			startActivity(new Intent(TransferCountActivity.this,
					TransferHistoryActivity.class));
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("转账金额");
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView("转账记录");
		righTextLayout.setOnClickListener(this);
	}

	private void initView() {
		userImg=(RoundedImageView)findViewById(R.id.touxing);
		telephone = (TextView) findViewById(R.id.account_id);
		name = (TextView) findViewById(R.id.name);
		account_count = (EditText) findViewById(R.id.account_count);
		confirm = (TextView) findViewById(R.id.confirm);
		cant_confirm = (TextView) findViewById(R.id.cant_confirm);
		balance=(TextView)findViewById(R.id.balance);
		balance.setText(getIntent().getStringExtra("balance"));
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(TransferCountActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				garden = user.getHotRegion();
				phoneNumber = user.getUsername();
			}
		}
		if (getIntent().getStringExtra("headImg") != null
				&& !"".equals(getIntent().getStringExtra("headImg"))) {
			finalBitmap.display(userImg, getIntent().getStringExtra("headImg"));
		} else {
			userImg.setImageResource(R.drawable.ic_msg_empty);
		}
		telephone.setText(getIntent().getStringExtra("telphone"));
		name.setText(getIntent().getStringExtra("nickName"));
		confirm.setOnClickListener(this);
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

	private void gettoken() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_generateToken, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data=json.optJSONObject("data");
								if (prefUtils.getString(SharePrefUtils.PAYPASSWORD, "").equals(
										"null")
										|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "")
												.equals("")
										|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "") == null) {
									new PromptDialog.Builder(TransferCountActivity.this)
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
													startActivity(new Intent(TransferCountActivity.this, MyWalletSetPSWActivity.class));
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
										transferAccounts(userId, getIntent().getStringExtra("userId"), data.optString("token"),account_count.getText().toString(),
												prefUtils.getString(SharePrefUtils.PAYPASSWORD, ""), phoneNumber, getIntent().getStringExtra("telphone"), TransferCountActivity.this);
										return;
									}
								}
								PasswordPop pop = new PasswordPop(
										TransferCountActivity.this,
										findViewById(R.id.hideView), userId,
										getIntent().getStringExtra("userId"),
										data.optString("token"),account_count.getText()
										.toString(), phoneNumber, getIntent().getStringExtra("telphone"), 3);
								pop.showPopupWindow(findViewById(R.id.hideView));
							} else {
								Toast.makeText(TransferCountActivity.this,
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
	private void transferAccounts(String userid, String merchantId,
			String ttoken, final String money, String ppwd, String phoneNumber,
			String merchantName, final Activity mcontext) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("merchantId", merchantId);
		params.put("type", "用户");
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("tradeType", "4");
		params.put("tradeTypeStr", "转账");
		params.put("tradeTypeM", "4");
		params.put("tradeTypeStrM", "转账");
		params.put("payType", "3");
		params.put("payTypeStr", "平台支付");
		params.put("amonut", money);
		params.put("phoneNumber", phoneNumber);
		params.put("customerName", phoneNumber);
		params.put("merchantName", merchantName);
		params.put("billStatus", "6" );
		params.put("billStatusStr", "转账" );
		params.put("commodityInformation", "转账" );
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.PaymentCodeAction_transferAccounts,
				params, new AbsHttpStringResponseListener(this, null) {
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
								intent.putExtra("title", "转账");
								intent.putExtra("money", "¥" + money);
								mcontext.startActivity(intent);
							} else if (repCode.equals("666666")) {
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

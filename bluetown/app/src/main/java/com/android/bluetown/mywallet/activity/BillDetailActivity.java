package com.android.bluetown.mywallet.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.alipay.AlipayUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.AliPayListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.popup.PasswordPop;

/**
 * @author hedi
 * @data: 2016年4月27日 下午4:44:03
 * @Description: 账单详情
 */
public class BillDetailActivity extends TitleBarActivity implements
		OnClickListener,AliPayListener {
	private RelativeLayout rl_other_account;
	private TextView pay, amount, billTypeStr, billStatusStr, createTime,
			paymentNum, payTypeStr, paytype,other_account;
	private String userId,phoneNumber;
	private FinalDb db;
	private SharePrefUtils prefUtils;
	private List<MemberUser> users;
	private String ttoken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_bill_detail);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initView();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pay:
			if (prefUtils.getString(SharePrefUtils.PAYPASSWORD, "").equals(
					"null")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "")
							.equals("")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "") == null) {
				new PromptDialog.Builder(BillDetailActivity.this)
						.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
						.setMessage("请前往设置支付密码")
						.setButton1("取消", new PromptDialog.OnClickListener() {

							@Override
							public void onClick(Dialog dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();

							}
						})
						.setButton2("确认前往", new PromptDialog.OnClickListener() {

							@Override
							public void onClick(Dialog dialog, int which) {
								// TODO Auto-generated method stub
								startActivity(new Intent(
										BillDetailActivity.this,
										MyWalletSetPSWActivity.class));
								dialog.cancel();
							}
						}).show();
				return;
			}
			if (getIntent().getStringExtra("billStatusStr").equals("充值")) {
				AlipayUtil alipayUtil = new AlipayUtil(BillDetailActivity.this,
						getIntent().getStringExtra("transactionNumber"), 0 + "",getIntent().getStringExtra("bid"));
				alipayUtil.pay(getResources().getString(R.string.app_name),
						getIntent().getStringExtra("transactionNumber"), Double.parseDouble(getIntent().getStringExtra("amount")));
			} else {
				if (prefUtils.getString(SharePrefUtils.NOPASSWORDPAY, "")
						.equals("1")) {
					if (Double
							.parseDouble(getIntent().getStringExtra("amount")) < Double
							.parseDouble(prefUtils.getString(
									SharePrefUtils.NOPASSWORDPAY_COUNT, ""))) {
						payOrderParking(userId, ttoken, prefUtils.getString(
								SharePrefUtils.PAYPASSWORD, ""), getIntent()
								.getStringExtra("orderId"),
								BillDetailActivity.this);
						return;
					}
				}
				PasswordPop pop = new PasswordPop(BillDetailActivity.this,
						findViewById(R.id.hideView), userId, getIntent()
								.getStringExtra("orderId"), null, ttoken, null,
						null, 2);
				pop.showPopupWindow(findViewById(R.id.hideView));
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("账单详情 ");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				phoneNumber=user.getUsername();
			}
		}
		rl_other_account = (RelativeLayout) findViewById(R.id.rl_other_account);
		pay = (TextView) findViewById(R.id.pay);
		amount = (TextView) findViewById(R.id.amount);
		billTypeStr = (TextView) findViewById(R.id.billTypeStr);
		billStatusStr = (TextView) findViewById(R.id.billStatusStr);
		createTime = (TextView) findViewById(R.id.createTime);
		paymentNum = (TextView) findViewById(R.id.paymentNum);
		payTypeStr = (TextView) findViewById(R.id.payTypeStr);
		paytype = (TextView) findViewById(R.id.paytype);
		other_account = (TextView) findViewById(R.id.other_account);
		pay.setOnClickListener(this);
		if (getIntent().getStringExtra("billTypeStr").equals("已完成")
				|| getIntent().getStringExtra("billTypeStr").equals("已付款")
				|| getIntent().getStringExtra("billTypeStr").equals("已退款")) {
			billTypeStr.setTextColor(this.getResources().getColor(
					R.color.font_green));
		} else {
			billTypeStr.setTextColor(this.getResources().getColor(
					R.color.orange));
		}
		if (getIntent().getStringExtra("billTypeStr").equals("未付款")) {
			pay.setVisibility(View.VISIBLE);
			gettoken();
		} else {
			pay.setVisibility(View.GONE);
		}
		billTypeStr.setText(getIntent().getStringExtra("billTypeStr"));
		if (getIntent().getStringExtra("tradeTypeStr").equals("充值")
				|| getIntent().getStringExtra("tradeTypeStr").equals("收款")) {
			amount.setText("+" + getIntent().getStringExtra("amount"));
		} else if (getIntent().getStringExtra("tradeTypeStr").equals("支付")) {
			amount.setText("-" + getIntent().getStringExtra("amount"));
		} else if (getIntent().getStringExtra("tradeTypeStr").equals("转账")) {
			rl_other_account.setVisibility(View.VISIBLE);
			if (getIntent().getStringExtra("customerId").equals(userId)) {
				amount.setText("-" + getIntent().getStringExtra("amount"));
				other_account.setText(getIntent().getStringExtra("merchantName"));
			} else {
				amount.setText("+" + getIntent().getStringExtra("amount"));
				other_account.setText(getIntent().getStringExtra("customerName"));
			}
		}
		billStatusStr.setText(getIntent()
				.getStringExtra("commodityInformation"));
		createTime.setText(getIntent().getStringExtra("createTime"));
		if(getIntent().getStringExtra("paymentNum").equals("")||getIntent().getStringExtra("paymentNum")==null||getIntent().getStringExtra("paymentNum").equals("null")){
			paymentNum.setText("无");
		}else{
			paymentNum.setText(getIntent().getStringExtra("paymentNum"));	
		}	
		payTypeStr.setText(getIntent().getStringExtra("tradeTypeStr"));
		paytype.setText(getIntent().getStringExtra("payTypeStr"));

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
								JSONObject data = json.optJSONObject("data");
								ttoken = data.optString("token");
							} else {
								Toast.makeText(BillDetailActivity.this,
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

	private void payOrder(String userid, String ttoken, final String ppwd,
			String orderId, final Activity context) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("orderId", orderId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_confirmPaymentOfOrder, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								context.finish();
								Intent intent = new Intent(context,
										TransferSuccessActivity.class);
								intent.putExtra("title", "支付");
								context.startActivity(intent);
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

	private void payOrderParking(String userid, String ttoken,
			final String ppwd, String orderId, final Activity context) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("orderId", orderId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_confirmPayment, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								context.finish();
								Intent intent = new Intent(context,
										TransferSuccessActivity.class);
								intent.putExtra("title", "支付");
								context.startActivity(intent);
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
	@Override
	public void paySuccess() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(BillDetailActivity.this, TransferSuccessActivity.class);
		intent.putExtra("title", "充值");
		intent.putExtra("money", getIntent().getStringExtra("amount"));
		startActivity(intent);
		finish();
		Toast.makeText(this, "支付成功！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void payFaild() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "支付失败！", Toast.LENGTH_SHORT).show();
	}

}
package com.android.bluetown.home.main.model.act;

import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mywallet.activity.MyWalletSetPSWActivity;
import com.android.bluetown.mywallet.activity.RechargeActivity;
import com.android.bluetown.mywallet.activity.TransferSuccessActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.CustomDigitalClock;
import com.android.bluetown.popup.PasswordPop;

/**
 * @author hedi
 * @data: 2016年5月3日 下午6:06:35
 * @Description: 停车订单支付
 */
public class ParkingPayOrderActivity extends TitleBarActivity implements
		OnClickListener {
	private CustomDigitalClock remaintime;
	private Date now;
	private SharePrefUtils prefUtils;
	private FinalDb db;
	private String userId;
	private TextView tv_balance;
	private String balance;
	private TextView no_balance;
	private TextView amount;
	private TextView order_id;
	private TextView charge;
	private String bid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_parking_pay_order);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initView();
		setDate();
		getdata();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm:
			if (prefUtils.getString(SharePrefUtils.PAYPASSWORD, "").equals(
					"null")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "")
							.equals("")
					|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "") == null) {
				new PromptDialog.Builder(ParkingPayOrderActivity.this)
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
										ParkingPayOrderActivity.this,
										MyWalletSetPSWActivity.class));
								dialog.cancel();
							}
						}).show();
				return;
			}
			addbill();
			
			
			break;
		case R.id.charge:
			Intent intent=(new Intent(
					ParkingPayOrderActivity.this,
					RechargeActivity.class));
			intent.putExtra("type", 0);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView("订单支付");
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		remaintime = (CustomDigitalClock) findViewById(R.id.remaintime);
		amount=(TextView)findViewById(R.id.amount);
		tv_balance = (TextView) findViewById(R.id.balance);
		no_balance = (TextView) findViewById(R.id.no_balance);
		order_id=(TextView)findViewById(R.id.order_id);
		charge=(TextView)findViewById(R.id.charge);
		amount.setText("¥"+getIntent().getStringExtra("money"));
		order_id.setText("智慧园车位包月-订单编号："+getIntent().getStringExtra("orderId"));
		findViewById(R.id.confirm).setOnClickListener(this);
		charge.setOnClickListener(this);
		db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}

	private void setDate() {
		now = new Date(System.currentTimeMillis());
		remaintime.setEndTime(now.getTime() + 1000 * 60 * 15);
	}

	private void payOrder(String userid, String ttoken, final String ppwd,
			String orderId, final Activity mcontext) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("orderId", orderId);
		if(getIntent().getIntExtra("type", 0)==1){
			params.put("oldOrderId", getIntent().getStringExtra("oldOrderId"));
		}
		params.put("url", getIntent().getStringExtra("url"));
		params.put("merchantName", prefUtils.getString(SharePrefUtils.GARDEN, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_confirmPayment, params,
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
								intent.putExtra("money",  getIntent().getStringExtra("money"));
								mcontext.startActivity(intent);
							} 
							else if(repCode.equals("777777")){
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
												Intent intent = new Intent();
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
														| Intent.FLAG_ACTIVITY_NEW_TASK);
												intent.setClass(ParkingPayOrderActivity.this, MainActivity.class);
												startActivity(intent);
											}
										}).show();
							}else {
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
								balance = json.optString("data2");
								if (Double.parseDouble(getIntent()
										.getStringExtra("money")) > Double
										.parseDouble(balance)) {
									no_balance.setVisibility(View.VISIBLE);
									charge.setVisibility(View.VISIBLE);
									findViewById(R.id.confirm).setVisibility(View.GONE);

								}								
								tv_balance.setText("¥"
										+ json.optString("data2"));
							} else {
								Toast.makeText(ParkingPayOrderActivity.this,
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
	private void addbill() {
		// TODO Auto-generated method stub
		params.put("bid", bid);
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("tradeType", "1");
		params.put("tradeTypeStr", "支付");
		params.put("orderId", getIntent().getStringExtra("orderId"));
		params.put("orderNum", getIntent().getStringExtra("orderNum"));
		params.put("phoneNumber", getIntent().getStringExtra("phoneNumber"));
		params.put("customerName", prefUtils.getString(SharePrefUtils.REALNAME, ""));
		params.put("billStatus", "1");
		params.put("billStatusStr", "停车费");
		params.put("amonut", getIntent().getStringExtra("money"));
		params.put("paymentType", "3");
		params.put("paymentTypeStr", "平台支付");
		params.put("commodityInformation", "车位包月-"+getIntent().getStringExtra("mouthNumber")+"个月");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_add, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data = json.optJSONObject("data");
								bid=data.optString("bid");
								if (prefUtils.getString(SharePrefUtils.NOPASSWORDPAY, "").equals(
										"1")) {
									if (Double.parseDouble(getIntent().getStringExtra("money")) < Double
											.parseDouble(prefUtils.getString(
													SharePrefUtils.NOPASSWORDPAY_COUNT, ""))) {
										payOrder(getIntent().getStringExtra("userId"), getIntent()
												.getStringExtra("ttoken"), prefUtils.getString(
												SharePrefUtils.PAYPASSWORD, ""), getIntent()
												.getStringExtra("orderId"),
												ParkingPayOrderActivity.this);
										return;
									}
								}			
								if(getIntent().getIntExtra("type", 0)==1){
									PasswordPop pop = new PasswordPop(ParkingPayOrderActivity.this,
											findViewById(R.id.hideView), getIntent().getStringExtra(
													"userId"), getIntent().getStringExtra("orderId"),
													getIntent().getStringExtra("oldOrderId"), getIntent().getStringExtra("ttoken"), getIntent().getStringExtra("url"), getIntent().getStringExtra("money"), 2);
									pop.showPopupWindow(findViewById(R.id.hideView));
								}else{
									PasswordPop pop = new PasswordPop(ParkingPayOrderActivity.this,
											findViewById(R.id.hideView), getIntent().getStringExtra(
													"userId"), getIntent().getStringExtra("orderId"),
													null, getIntent().getStringExtra("ttoken"), getIntent().getStringExtra("url"), getIntent().getStringExtra("money"), 2);
									pop.showPopupWindow(findViewById(R.id.hideView));
								}
							} else {
								Toast.makeText(
										ParkingPayOrderActivity.this,
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

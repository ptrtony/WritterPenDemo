package com.android.bluetown.surround;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.DishListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.bean.SeleteDish;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.AliPayListener;
import com.android.bluetown.mywallet.activity.MyWalletSetPSWActivity;
import com.android.bluetown.mywallet.activity.RechargeActivity;
import com.android.bluetown.mywallet.activity.TransferSuccessActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.ListViewForScrollView;
import com.android.bluetown.popup.PasswordPop;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 支付订单跳转页面
 * 
 * @author shenyz
 * 
 */
public class OnlinePayActivity extends TitleBarActivity implements
		OnClickListener, AliPayListener {
	private RelativeLayout orderStatusLayout;
	private ImageView mCloseImage;
	private TextView merNameView;
	private TextView desType;
	private TextView merTime;
	private TextView recordFood;
	private ListViewForScrollView dishListView;
	private TextView totalMoney;
	private Button mConfirmPayBtn;
	private RadioGroup group;
	private LinearLayout payProgress;
	private LinearLayout orderDishLy;
	private SharePrefUtils prefUtils;
	// 订单 立即支付--菜的列表
	private List<SeleteDish> oderDishList;
	// 支付方式（默认用支付宝支付）
	private int payType = 1;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this,
			Constant.Interface.APP_ID);
	private FinalDb db;
	private String ttoken;
	private List<RecommendDish> dishList;

	private List<MemberUser> users;
	private String userId, orderNum, amount, mid, merchantName, tableName,
			dinnerTime;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.online_book_title);
		// setRighTextView(R.string.cancel_order);
		// righTextLayout.setOnClickListener(this);
		righTextLayout.setVisibility(View.INVISIBLE);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_online_book_pay);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initUIView();
		initUIData();
		gettoken();
	}

	/**
	 * 初始化界面组件及初始化相应对象
	 */
	private void initUIView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		orderNum = getIntent().getStringExtra("orderNum");
		amount = getIntent().getStringExtra("amount");
		mid = getIntent().getStringExtra("mid");
		merchantName = getIntent().getStringExtra("merchantName");
		dinnerTime = getIntent().getStringExtra("dinnerTime");
		tableName = getIntent().getStringExtra("tableName");
		try {
			oderDishList = BlueTownApp.getOrderDishList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		orderStatusLayout = (RelativeLayout) this
				.findViewById(R.id.rl_onlinepay_timess);
		mCloseImage = (ImageView) this
				.findViewById(R.id.iv_onlinerservepay_selete);
		merNameView = (TextView) findViewById(R.id.bt_onlineserver_locationss);
		desType = (TextView) findViewById(R.id.bt_onlineserver_Seatss);
		merTime = (TextView) findViewById(R.id.bt_onlineserver_timesss);
		orderDishLy = (LinearLayout) findViewById(R.id.ll_online_lv_parent);
		dishListView = (ListViewForScrollView) findViewById(R.id.lv_online_food);
		recordFood = (TextView) findViewById(R.id.tv_online_record);
		group = (RadioGroup) findViewById(R.id.rg_onlinerespay);
		totalMoney = (TextView) findViewById(R.id.tv_onlinerserpay_total);
		mConfirmPayBtn = (Button) this
				.findViewById(R.id.bt_onlinerserverpay_sures);
		payProgress = (LinearLayout) findViewById(R.id.pay_progress);
		group.setOnCheckedChangeListener(onCheckedListener);
		mCloseImage.setOnClickListener(this);
		mConfirmPayBtn.setOnClickListener(this);
		backImageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OnlinePayActivity.this,
						OrderDetailActivity.class);
				intent.putExtra("cid", orderNum);
				intent.putExtra("position", 0);
				startActivity(intent);
				OnlinePayActivity.this.finish();
			}
		});

	}

	/**
	 * 初始刷界面组件
	 */
	private void initUIData() {
		merNameView.setText(merchantName);
		desType.setText(tableName);
		merTime.setText(dinnerTime);
		totalMoney.setText("总计：" + amount + "元");
		List<RecommendDish> dishList = BlueTownApp.getDishList();
		if (dishList != null && dishList.size() > 0
				&& getIntent().getIntExtra("type", 0) != 1) {
			orderDishLy.setVisibility(View.VISIBLE);
			DishListAdapter adapter = new DishListAdapter(this, dishList,
					"dish");
			dishListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			recordFood.setText("共计" + dishList.size() + "份");
		} else if (oderDishList != null && oderDishList.size() > 0) {
			// 订单--立即支付
			orderDishLy.setVisibility(View.VISIBLE);
			DishListAdapter adapter = new DishListAdapter(this, oderDishList,
					"order-dish");
			dishListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			recordFood.setText("共计" + oderDishList.size() + "份");
		} else {
			orderDishLy.setVisibility(View.GONE);
			recordFood.setText("共计0份");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.rightTextLayout:
			// 取消订单
			showDialog(OnlinePayActivity.this, R.string.tip, R.string.confirm,
					R.string.cancel, R.string.cancel_order_tip, userId,
					orderNum);
			break;
		case R.id.iv_onlinerservepay_selete:
			// 点击提示30分钟订单状态的关闭按钮
			orderStatusLayout.setVisibility(View.GONE);
			break;
		case R.id.bt_onlinerserverpay_sures:
			switch (payType) {
			case 0:
				// 银联支付
				Intent intent = new Intent(OnlinePayActivity.this,
						UMSActivity.class);
				startActivity(intent);
				break;
			case 1:
				// 支付宝支付
				if (prefUtils.getString(SharePrefUtils.PAYPASSWORD, "").equals(
						"null")
						|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "")
								.equals("")
						|| prefUtils.getString(SharePrefUtils.PAYPASSWORD, "") == null) {
					new PromptDialog.Builder(OnlinePayActivity.this)
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
											startActivity(new Intent(
													OnlinePayActivity.this,
													MyWalletSetPSWActivity.class));
											dialog.cancel();
										}
									}).show();
					return;
				}
				if (prefUtils.getString(SharePrefUtils.NOPASSWORDPAY, "")
						.equals("1")) {
					if (Double.parseDouble(amount) < Double
							.parseDouble(prefUtils.getString(
									SharePrefUtils.NOPASSWORDPAY_COUNT, ""))) {
						payOrder(userId, ttoken, prefUtils.getString(
								SharePrefUtils.PAYPASSWORD, ""), orderNum,
								OnlinePayActivity.this, merchantName);
						return;
					}
				}
				PasswordPop pop = new PasswordPop(OnlinePayActivity.this,
						findViewById(R.id.hideView), userId, orderNum,
						merchantName, ttoken, amount, null, 5);
				pop.showPopupWindow(findViewById(R.id.hideView));
				break;

			// AlipayUtil alipayUtil = new AlipayUtil(OnlinePayActivity.this,
			// orderNum, payType + "","");
			// // alipayUtil.pay(getResources().getString(R.string.app_name),
			// // orderNum, Double.parseDouble(amount));
			// alipayUtil.pay(getResources().getString(R.string.app_name),
			// orderNum, Double.parseDouble(amount));
			// break;
			case 2:
				// 微信支付
				// 判断是否安装过微信
				if (!msgApi.isWXAppInstalled()) {
					Toast.makeText(OnlinePayActivity.this, "您还未安装微信,请安装微信在支付",
							Toast.LENGTH_SHORT).show();
					return;
				}
//				 createOrder();
				break;
			case -1:
				Toast.makeText(OnlinePayActivity.this, "请选择支付方式",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	}

	private OnCheckedChangeListener onCheckedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// swiTODO Auto-generated method stub

			switch (checkedId) {
			case R.id.cb_onlinepay_one:
				payType = 0;
				break;
			case R.id.cb_onlinepay_two:
				payType = 1;
				break;
			case R.id.cb_onlinepay_three:
				payType = 2;
				break;
			}

		}
	};

	/**
	 * 支付失败
	 */
	@Override
	public void payFaild() {
		// TODO Auto-generated method stub
		payProgress.setVisibility(View.GONE);
		Toast.makeText(this, "支付失败！", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 支付成功
	 */
	@Override
	public void paySuccess() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("mid", mid);
		intent.setClass(OnlinePayActivity.this, OnlinePaySuccessActivity.class);
		startActivity(intent);
		payProgress.setVisibility(View.GONE);
		finish();
		Toast.makeText(this, "支付成功！", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 取消订单
	 * 
	 * @param userId
	 * @param orderNum
	 */
	private void cancelOrder(String userId, String orderNum) {
		// TODO Auto-generated method stub
		params.put("cid", orderNum);
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.CANCEL_ORDER,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Intent intent = new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("meid", mid);
							intent.putExtra("isClosed","0");
							intent.setClass(OnlinePayActivity.this,
									RecommendDishActivity.class);
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(OnlinePayActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
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
								JSONObject data = json.optJSONObject("data");
								ttoken = data.optString("token");
							} else {
								Toast.makeText(OnlinePayActivity.this,
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
			String orderId, final Activity mcontext, String commodityInformation) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("orderId", orderId);
		params.put("billStatus", "2");
		params.put("billStatusStr", "商户消费");
		params.put("commodityInformation", commodityInformation);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_confirmPaymentOfOrder, params,
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
								intent.putExtra("money",amount+"");
								BlueTownApp.setOrderDishList(null);
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
														Intent intent = new Intent(
																mcontext,
																RechargeActivity.class);
														intent.putExtra("type",
																0);
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

	/**
	 * 取消订单提示对话框
	 * 
	 * @param context
	 * @param titleId
	 *            标题
	 * @param confirmTextId
	 *            确认
	 * @param cancelTextId
	 *            取消
	 * @param contentStr
	 *            内容
	 * @param userId
	 *            用户id
	 * @param orderNum
	 *            订单号
	 */
	public void showDialog(final Context context, int titleId,
			int confirmTextId, int cancelTextId, int contentStr,
			final String userId, final String orderNum) {
		SweetAlertDialog dialog = new SweetAlertDialog(context);
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				cancelOrder(userId, orderNum);
			}
		});
		dialog.setCancelClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(OnlinePayActivity.this, OrderDetailActivity.class);
			intent.putExtra("cid", orderNum);
			intent.putExtra("position", 0);
			startActivity(intent);
			OnlinePayActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}

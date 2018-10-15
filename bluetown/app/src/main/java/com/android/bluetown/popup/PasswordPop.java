package com.android.bluetown.popup;

import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.android.bluetown.MainActivity;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mywallet.activity.MyWalletSetPSWActivity;
import com.android.bluetown.mywallet.activity.RechargeActivity;
import com.android.bluetown.mywallet.activity.TransferSuccessActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.AlphaUtil;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;
import com.android.bluetown.view.NumberPayCodeView.BaseNumberCodeView;
import com.android.bluetown.view.NumberPayCodeView.BottomSheet.BottomSheetNumberCodeView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * @author hedi
 * @data: 2016年4月27日 下午2:49:08
 * @Description: TODO<请描述此文件是做什么的>
 */
public class PasswordPop extends PopupWindow {
	private View conentView;
	private SharePrefUtils prefUtils;
	private AbHttpUtil httpUtil = null;
	private BottomSheetNumberCodeView pwdView;
	private int i ;
	private int j = 5;
	private FinalDb db;
	private Activity activity;
	public PasswordPop(final Activity context, View body, final String userid,
			final String merchantId, final String money, final String ttoken,
			final String phoneNumber, final String merchantName, final int type) {
		this.activity = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.password_pop, null);
		prefUtils = new SharePrefUtils(context);
		i=prefUtils.getInt(SharePrefUtils.PAY_NUM, 5);
		db = FinalDb.create(context);
		httpUtil = AbHttpUtil.getInstance(context);
		httpUtil.setTimeout(10000);
		httpUtil.setEasySSLEnabled(true);
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);

		this.setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//		this.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		this.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示

		pwdView = conentView.findViewById(R.id.pwd_view);
		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				AlphaUtil.setBackgroundAlpha(context,1.0f);
			}
		});
		// 添加密码输入完成的响应
//		pwdView.getCancelImageView().setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				PasswordPop.this.dismiss();
//				AlphaUtil.setBackgroundAlpha(context,1.0f);
//			}
//
//		});

		pwdView.setIsPassword(true);
		pwdView.setOnHideBottomLayoutListener(new BottomSheetNumberCodeView.OnHideBottomLayoutListener() {
			@Override
			public void onHide() {
				PasswordPop.this.dismiss();
				AlphaUtil.setBackgroundAlpha(context,1.0f);
			}
		});


		pwdView.setNumberCodeCallback(new BaseNumberCodeView.OnInputNumberCodeCallback() {
			@Override
			public void onResult(String code) {
				// 输入完成后我们简单显示一下输入的密码
				// 也就是说——>实现你的交易逻辑什么的在这里写
				if (MD5Util.encoder(code).equals(
						prefUtils.getString(SharePrefUtils.PAYPASSWORD, ""))) {
					prefUtils.setInt(SharePrefUtils.PAY_NUM, 5);
					switch (type) {
						case 1:
							pay(userid, merchantId, money, ttoken,
									MD5Util.encoder(code),
									phoneNumber, merchantName, context);
							break;
						case 2:
							payOrderParking(userid, ttoken,
									MD5Util.encoder(code),
									merchantId, context, phoneNumber, merchantName,
									money);
							break;
						case 3:
							transferAccounts(userid, merchantId, money, ttoken,
									MD5Util.encoder(code),
									phoneNumber, merchantName, context);
							break;
						case 4:
//							setStatus(userid, merchantId,
//									MD5Util.encoder(code),
//									money, context,code);
							Intent intent = new Intent();
							intent.putExtra("status", merchantId);
							intent.putExtra("money", money);
							intent.putExtra("code",code);
//							intent.putExtra("merchantId",status);
							intent.putExtra("money",money);
							intent.setAction("android.setStatus.action");
							activity.sendBroadcast(intent);
							PasswordPop.this.dismiss();
							AlphaUtil.setBackgroundAlpha(activity,1.0f);
							break;
						case 5:
							payOrder(userid, ttoken,
									MD5Util.encoder(code),
									merchantId, context, money, phoneNumber);
							break;
						default:
							if (inputPasswordSucceed!=null){
								pwdView.restoreViews();
								inputPasswordSucceed.inputPasswordSucceed();
								AlphaUtil.setBackgroundAlpha(context,1.0f);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
									if (!context.isDestroyed()){
                                        dismiss();

                                    }
								}
							}
							break;
					}

				} else {
					i--;
					prefUtils.setInt(SharePrefUtils.PAY_NUM, i);
					pwdView.restoreViews();
					if (i == 0) {
						Toast.makeText(context, "密码错误",
								Toast.LENGTH_SHORT).show();
						String userId = "";
						List<MemberUser> users = db.findAll(MemberUser.class);
						if (users != null && users.size() != 0) {
							MemberUser user = users.get(0);
							if (user != null) {
								userId = user.getMemberId();
							}
						}
						if (!TextUtils.isEmpty(userId)) {
							// 删除登录保存的登录信息
							lock(userId, context);
						}

						return;

					}
					new PromptDialog.Builder(context)
							.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
							.setMessage("支付密码错误，剩余" + i + "次机会")
							.setButton1("重试",
									new PromptDialog.OnClickListener() {

										@Override
										public void onClick(Dialog dialog,
															int which) {
											// TODO Auto-generated method stub
											pwdView.restoreViews();
											dialog.cancel();

										}
									})
							.setButton2("忘记密码",
									new PromptDialog.OnClickListener() {

										@Override
										public void onClick(Dialog dialog,
															int which) {
											// TODO Auto-generated method stub
											dialog.cancel();
											LayoutInflater inflater = (LayoutInflater) context
													.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
											final View layout = (View) inflater
													.inflate(
															R.layout.item_edittext,
															null);
											new PromptDialog.Builder(context)
													.setView(layout)
													.setViewStyle(
															PromptDialog.VIEW_STYLE_NORMAL)
													.setButton1(
															"确定",
															new PromptDialog.OnClickListener() {

																@Override
																public void onClick(
																		Dialog dialog,
																		int which) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	dialog.cancel();
																	EditText password = (EditText) layout
																			.findViewById(R.id.password);
																	if (MD5Util
																			.encoder(
																					password.getText()
																							.toString())
																			.equals(prefUtils
																					.getString(
																							SharePrefUtils.PASSWORD,
																							""))) {
//																		switch (type) {
//																		case 1:
//																			pay(userid,
//																					merchantId,
//																					money,
//																					ttoken,
//																					MD5Util.encoder(pwdView
//																							.getStrPassword()),
//																					phoneNumber,
//																					merchantName,
//																					context);
//																			break;
//																		case 2:
//																			payOrderParking(
//																					userid,
//																					ttoken,
//																					MD5Util.encoder(pwdView
//																							.getStrPassword()),
//																					merchantId,
//																					context,
//																					phoneNumber,
//																					merchantName,
//																					money);
//																			break;
//																		case 3:
//																			transferAccounts(
//																					userid,
//																					merchantId,
//																					money,
//																					ttoken,
//																					MD5Util.encoder(pwdView
//																							.getStrPassword()),
//																					phoneNumber,
//																					merchantName,
//																					context);
//																			break;
//																		case 4:
//																			setStatus(
//																					userid,
//																					merchantId,
//																					MD5Util.encoder(pwdView
//																							.getStrPassword()),
//																					money,
//																					context);
//																			break;
//																		case 5:
//																			payOrder(
//																					userid,
//																					ttoken,
//																					MD5Util.encoder(pwdView
//																							.getStrPassword()),
//																					merchantId,
//																					context,
//																					money,
//																					phoneNumber);
//																			break;
//																		default:
//																			break;
//																		}
																		dialog.cancel();
																		PasswordPop.this.dismiss();
																		AlphaUtil.setBackgroundAlpha(context,1.0f);
																		Intent intent = new Intent(
																				context,
																				MyWalletSetPSWActivity.class);
																		context.startActivity(intent);
																	} else {
																		j--;
																		if (j == 0) {
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
																			if (!TextUtils
																					.isEmpty(userId)) {
																				// 删除登录保存的登录信息
																				lock(userId,
																						context);
																			}
																		} else {
																			password.setText("");
																			Toast.makeText(
																					context,
																					"密码错误(剩余"
																							+ j
																							+ "次机会)",
																					Toast.LENGTH_SHORT)
																					.show();
																		}
																	}

																}
															})
													.setButton2(
															"取消",
															new PromptDialog.OnClickListener() {

																@Override
																public void onClick(
																		Dialog dialog,
																		int which) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	dialog.cancel();
																}
															}).show();
										}
									}).show();
				}
			}
		});
//		pwdView.setOnFinishInput(new OnPasswordInputFinish() {
//			@Override
//			public void inputFinish() {
//				// 输入完成后我们简单显示一下输入的密码
//				// 也就是说——>实现你的交易逻辑什么的在这里写
//				if (MD5Util.encoder(pwdView.getStrPassword()).equals(
//						prefUtils.getString(SharePrefUtils.PAYPASSWORD, ""))) {
//					prefUtils.setInt(SharePrefUtils.PAY_NUM, 5);
//					switch (type) {
//					case 1:
//						pay(userid, merchantId, money, ttoken,
//								MD5Util.encoder(pwdView.getStrPassword()),
//								phoneNumber, merchantName, context);
//						break;
//					case 2:
//						payOrderParking(userid, ttoken,
//								MD5Util.encoder(pwdView.getStrPassword()),
//								merchantId, context, phoneNumber, merchantName,
//								money);
//						break;
//					case 3:
//						transferAccounts(userid, merchantId, money, ttoken,
//								MD5Util.encoder(pwdView.getStrPassword()),
//								phoneNumber, merchantName, context);
//						break;
//					case 4:
//						setStatus(userid, merchantId,
//								MD5Util.encoder(pwdView.getStrPassword()),
//								money, context);
//						break;
//					case 5:
//						payOrder(userid, ttoken,
//								MD5Util.encoder(pwdView.getStrPassword()),
//								merchantId, context, money, phoneNumber);
//						break;
//					default:
//						break;
//					}
//
//				} else {
//					i--;
//					prefUtils.setInt(SharePrefUtils.PAY_NUM, i);
//					pwdView.clearPassword();
//					if (i == 0) {
//						Toast.makeText(context, "密码错误",
//								Toast.LENGTH_SHORT).show();
//						String userId = "";
//						List<MemberUser> users = db.findAll(MemberUser.class);
//						if (users != null && users.size() != 0) {
//							MemberUser user = users.get(0);
//							if (user != null) {
//								userId = user.getMemberId();
//							}
//						}
//						if (!TextUtils.isEmpty(userId)) {
//							// 删除登录保存的登录信息
//							lock(userId, context);
//						}
//
//						return;
//
//					}
//					new PromptDialog.Builder(context)
//							.setViewStyle(PromptDialog.VIEW_STYLE_NORMAL)
//							.setMessage("支付密码错误，剩余" + i + "次机会")
//							.setButton1("重试",
//									new PromptDialog.OnClickListener() {
//
//										@Override
//										public void onClick(Dialog dialog,
//												int which) {
//											// TODO Auto-generated method stub
//											pwdView.clearPassword();
//											dialog.cancel();
//
//										}
//									})
//							.setButton2("忘记密码",
//									new PromptDialog.OnClickListener() {
//
//										@Override
//										public void onClick(Dialog dialog,
//												int which) {
//											// TODO Auto-generated method stub
//											dialog.cancel();
//											LayoutInflater inflater = (LayoutInflater) context
//													.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//											final View layout = (View) inflater
//													.inflate(
//															R.layout.item_edittext,
//															null);
//											new PromptDialog.Builder(context)
//													.setView(layout)
//													.setViewStyle(
//															PromptDialog.VIEW_STYLE_NORMAL)
//													.setButton1(
//															"确定",
//															new PromptDialog.OnClickListener() {
//
//																@Override
//																public void onClick(
//																		Dialog dialog,
//																		int which) {
//																	// TODO
//																	// Auto-generated
//																	// method
//																	// stub
//																	dialog.cancel();
//																	EditText password = (EditText) layout
//																			.findViewById(R.id.password);
//																	if (MD5Util
//																			.encoder(
//																					password.getText()
//																							.toString())
//																			.equals(prefUtils
//																					.getString(
//																							SharePrefUtils.PASSWORD,
//																							""))) {
////																		switch (type) {
////																		case 1:
////																			pay(userid,
////																					merchantId,
////																					money,
////																					ttoken,
////																					MD5Util.encoder(pwdView
////																							.getStrPassword()),
////																					phoneNumber,
////																					merchantName,
////																					context);
////																			break;
////																		case 2:
////																			payOrderParking(
////																					userid,
////																					ttoken,
////																					MD5Util.encoder(pwdView
////																							.getStrPassword()),
////																					merchantId,
////																					context,
////																					phoneNumber,
////																					merchantName,
////																					money);
////																			break;
////																		case 3:
////																			transferAccounts(
////																					userid,
////																					merchantId,
////																					money,
////																					ttoken,
////																					MD5Util.encoder(pwdView
////																							.getStrPassword()),
////																					phoneNumber,
////																					merchantName,
////																					context);
////																			break;
////																		case 4:
////																			setStatus(
////																					userid,
////																					merchantId,
////																					MD5Util.encoder(pwdView
////																							.getStrPassword()),
////																					money,
////																					context);
////																			break;
////																		case 5:
////																			payOrder(
////																					userid,
////																					ttoken,
////																					MD5Util.encoder(pwdView
////																							.getStrPassword()),
////																					merchantId,
////																					context,
////																					money,
////																					phoneNumber);
////																			break;
////																		default:
////																			break;
////																		}
//																		dialog.cancel();
//																		PasswordPop.this.dismiss();
//																		AlphaUtil.setBackgroundAlpha(context,1.0f);
//																		Intent intent = new Intent(
//																				context,
//																				MyWalletSetPSWActivity.class);
//																		context.startActivity(intent);
//																	} else {
//																		j--;
//																		if (j == 0) {
//																			String userId = "";
//																			List<MemberUser> users = db
//																					.findAll(MemberUser.class);
//																			if (users != null
//																					&& users.size() != 0) {
//																				MemberUser user = users
//																						.get(0);
//																				if (user != null) {
//																					userId = user
//																							.getMemberId();
//																				}
//																			}
//																			if (!TextUtils
//																					.isEmpty(userId)) {
//																				// 删除登录保存的登录信息
//																				lock(userId,
//																						context);
//																			}
//																		} else {
//																			password.setText("");
//																			Toast.makeText(
//																					context,
//																					"密码错误(剩余"
//																							+ j
//																							+ "次机会)",
//																					Toast.LENGTH_SHORT)
//																					.show();
//																		}
//																	}
//
//																}
//															})
//													.setButton2(
//															"取消",
//															new PromptDialog.OnClickListener() {
//
//																@Override
//																public void onClick(
//																		Dialog dialog,
//																		int which) {
//																	// TODO
//																	// Auto-generated
//																	// method
//																	// stub
//																	dialog.cancel();
//																}
//															}).show();
//										}
//									}).show();
//				}

//			}
//		});

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
		params.put("billStatus", "6");
		params.put("billStatusStr", "转账");
		params.put("commodityInformation", merchantName);
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.PaymentCodeAction_confirmPayment, params,
				new AbsHttpStringResponseListener(mcontext, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								PasswordPop.this.dismiss();
								AlphaUtil.setBackgroundAlpha(mcontext,1.0f);
								mcontext.finish();
								Intent intent = new Intent(mcontext,
										TransferSuccessActivity.class);
								intent.putExtra("title", "支付");
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
														pwdView.restoreViews();
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

				});
	}

	private void payOrderParking(String userid, String ttoken,
			final String ppwd, String orderId, final Activity mcontext,
			String url, final String money, String oldOrderId) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("orderId", orderId);
		params.put("oldOrderId", oldOrderId);
		params.put("url", url);
		params.put("merchantName", prefUtils.getString(SharePrefUtils.GARDEN, ""));
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_confirmPayment, params,
				new AbsHttpStringResponseListener(mcontext, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								PasswordPop.this.dismiss();
								AlphaUtil.setBackgroundAlpha(mcontext,1.0f);
								mcontext.finish();
								Intent intent = new Intent(mcontext,
										TransferSuccessActivity.class);
								intent.putExtra("title", "支付");
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
														pwdView.restoreViews();
														dialog.cancel();
														Intent intent = new Intent(
																mcontext,
																RechargeActivity.class);
														intent.putExtra("type",
																0);
														mcontext.startActivity(intent);
													}
												}).show();
							} else if (repCode.equals("777777")) {
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
														intent.setClass(
																mcontext,
																MainActivity.class);
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
		params.put("billStatus", "6");
		params.put("billStatusStr", "转账");
		params.put("commodityInformation", "转账");
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.PaymentCodeAction_transferAccounts,
				params, new AbsHttpStringResponseListener(mcontext, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								PasswordPop.this.dismiss();
								AlphaUtil.setBackgroundAlpha(mcontext,1.0f);
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
														pwdView.restoreViews();
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

				});
	}

//	private void setStatus(String userid, final String status,
//			final String ppwd, final String money, final Activity mcontext,String code) {
//		AbRequestParams params = new AbRequestParams();
//		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
//		params.put("uid", userid);
//		params.put("status", status);
//		params.put("pwd", ppwd);
//		params.put("money", money);
//		httpUtil.post(Constant.HOST_URL
//				+ Constant.Interface.MobiMemberAction_changeStatus, params,
//				new AbsHttpStringResponseListener(mcontext, null) {
//					@Override
//					public void onSuccess(int arg0, String arg1) {
//						// TODO Auto-generated method stub
//						JSONObject json;
//						try {
//							json = new JSONObject(arg1);
//							String repCode = json.optString("repCode");
//							if (repCode.equals(Constant.HTTP_SUCCESS)) {
//								Intent intent = new Intent();
//								intent.putExtra("status", status);
//								intent.putExtra("money", money);
//								intent.putExtra("code",code);
//								intent.putExtra("merchantId",status);
//								intent.putExtra("money",money);
//								intent.setAction("android.setStatus.action");
//								mcontext.sendBroadcast(intent);
//								PasswordPop.this.dismiss();
//								AlphaUtil.setBackgroundAlpha(mcontext,1.0f);
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
//														pwdView.restoreViews();
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
//														pwdView.restoreViews();
//														dialog.cancel();
//													}
//												}).show();
//							}
//
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//					}
//
//				});
//	}

	private void payOrder(String userid, String ttoken, final String ppwd,
			String orderId, final Activity mcontext,
			String commodityInformation, final String money) {
		AbRequestParams params = new AbRequestParams();
		// httpUtil.setToken(SharePUtil.get(SharePUtil.KEY_TOKEN, ""));
		params.put("userId", userid);
		params.put("transactionToken", ttoken);
		params.put("ppwd", ppwd);
		params.put("orderId", orderId);
		params.put("commodityInformation", commodityInformation);
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_confirmPaymentOfOrder, params,
				new AbsHttpStringResponseListener(mcontext, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								PasswordPop.this.dismiss();
								AlphaUtil.setBackgroundAlpha(mcontext,1.0f);
								mcontext.finish();
								Intent intent = new Intent(mcontext,
										TransferSuccessActivity.class);
								intent.putExtra("title", "支付");
								intent.putExtra("money", "¥" + money);
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
														pwdView.restoreViews();
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

				});
	}

	private void lock(String userid, final Activity mcontext) {
		AbRequestParams params = new AbRequestParams();
		params.put("userId", userid);
		params.put("communicationToken",
				prefUtils.getString(prefUtils.TOKEN, ""));
		httpUtil.post(Constant.HOST_URL
				+ Constant.Interface.BillAction_lockingUser, params,
				new AbsHttpStringResponseListener(mcontext, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						JSONObject json;
						try {
							json = new JSONObject(arg1);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								new PromptDialog.Builder(mcontext)
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
												prefUtils.setInt(SharePrefUtils.PAY_NUM, 5);
												Intent intent = new Intent();
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
														| Intent.FLAG_ACTIVITY_NEW_TASK);
												intent.setClass(mcontext, MainActivity.class);
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

				});
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			AlphaUtil.setBackgroundAlpha(activity,0.6f);
		} else {
			AlphaUtil.setBackgroundAlpha(activity,1.0f);
			this.dismiss();
		}
	}

	public interface InputPasswordSucceed{
		void inputPasswordSucceed();
	}

	private InputPasswordSucceed inputPasswordSucceed;

	public void setInputPassword(InputPasswordSucceed inputPasswordSucceed){
		this.inputPasswordSucceed = inputPasswordSucceed;
	}
}

/**
 * 
 */
package com.android.bluetown.surround;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.DishListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.OrderParams;
import com.android.bluetown.bean.SeleteDish;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.OrderDetailsResult;
import com.android.bluetown.result.OrderListResult.Order;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.ListViewForScrollView;

/**
 * @author chenxb
 * @date 2015-3-23 10:29
 * 
 */
public class OrderDetailActivity extends TitleBarActivity implements
		OnClickListener {
	private TextView tvMerchant;
	private TextView tvDeposit;
	private TextView tvAddress;
	private TextView tvTableType;
	private TextView tvMealTime;
	private TextView tvCreateTime;
	private TextView tvRealName;
	private TextView tvMobile;
	private TextView tvOrderNumber;
	private TextView tvOrderComment;
	private TextView tvVerifyCode;
	private Button btnAction;
	private String orderId;
	private int position;
	private Order order;
	private LinearLayout dishListLy;
	private TextView orderTotal;
	private ListViewForScrollView mListView;
	private String userId;
	private FinalDb db;
	private String code="0";
	private List<MemberUser> users;
	private Handler handler;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
		backImageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = OrderDetailActivity.this.getIntent();
				resultIntent.putExtra("code", code);
				setResult(Activity.RESULT_OK, resultIntent);
				OrderDetailActivity.this.finish();
			}
		});

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent resultIntent = OrderDetailActivity.this.getIntent();
			resultIntent.putExtra("code", code);
			setResult(Activity.RESULT_OK, resultIntent);
			OrderDetailActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_order_detail);
		setTitleView(R.string.order_detail);
		orderId = getIntent().getStringExtra("cid");
		position = getIntent().getIntExtra("position", 0);
		initUIView();
		getOrderDetails(orderId);
	}

	public void initUIView() {
	    db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		
		handler = BlueTownApp.getHanler();
	
		tvMerchant = (TextView) findViewById(R.id.tv_merchant);
		tvDeposit = (TextView) findViewById(R.id.tv_deposit);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		tvTableType = (TextView) findViewById(R.id.tv_table_type);
		tvMealTime = (TextView) findViewById(R.id.tv_meal_time);
		tvCreateTime = (TextView) findViewById(R.id.tv_create_time);
		tvRealName = (TextView) findViewById(R.id.tv_real_name);
		tvMobile = (TextView) findViewById(R.id.tv_mobile);
		tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
		tvOrderComment = (TextView) findViewById(R.id.tv_order_comment);
		tvVerifyCode = (TextView) findViewById(R.id.tv_verifyCode);
		dishListLy = (LinearLayout) findViewById(R.id.dishListLy);
		mListView = (ListViewForScrollView) findViewById(R.id.lv_onlineserver);
		orderTotal = (TextView) findViewById(R.id.tv_order_total);
		btnAction = (Button) findViewById(R.id.btn_action);
		tvMerchant.setOnClickListener(this);
		tvAddress.setOnClickListener(this);
	}

	/**
	 * 获取订单详情
	 * 
	 * @param mid
	 */
	private void getOrderDetails(String mid) {
		// TODO Auto-generated method stub
		params.put("cid", orderId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.ORDER_DETAILS,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						OrderDetailsResult result = (OrderDetailsResult) AbJsonUtil
								.fromJson(s, OrderDetailsResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(OrderDetailActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						} else {
							Toast.makeText(OrderDetailActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();

						}
					}

					private void dealResult(OrderDetailsResult result) {
						// TODO Auto-generated method stub
						order = result.getData();
						tvMerchant.setText(order.getMerchantName());
						String amount = order.getAmount();
						if (!TextUtils.isEmpty(amount)) {
							double temp = Double.parseDouble(amount);
							amount = getString(R.string.fee, temp);
							tvDeposit.setText("预订定金:" + amount + "元");
						} else {
							amount = "0.00";
							tvDeposit.setText("预订定金:" + amount + " 元");
						}

						tvAddress.setText(order.getMerchantTell());
						tvTableType.setText(order.getTableNum());
						tvMealTime.setText(order.getOrderTime());
						tvCreateTime.setText("创建时间:" + order.getCreateTime());
						tvRealName.setText("姓名:" + order.getContactName());
						tvMobile.setText("电话:" + order.getContactTel());
						if(TextUtils.isEmpty(order.getVerifyCode())){
							tvVerifyCode.setVisibility(View.GONE);
						}else{
							tvVerifyCode.setText("消费码："+order.getVerifyCode());
						}
						
						tvOrderNumber.setText("订单编号:" + order.getOrderNum());
						if (TextUtils.isEmpty(order.getDescription())) {
							tvOrderComment.setText("备注:无");
						} else {
							tvOrderComment.setText("备注:"
									+ order.getDescription());
						}
						List<SeleteDish> mDishList = order.getMenuList();
						if (mDishList != null) {
							dishListLy.setVisibility(View.VISIBLE);
							DishListAdapter adapter = new DishListAdapter(
									OrderDetailActivity.this, mDishList,
									"order");
							mListView.setAdapter(adapter);
							setDishTotal(amount);
						} else {
							dishListLy.setVisibility(View.GONE);
						}
						setCurrentButtun(order);
					}

					/**
					 * 设置点菜合计的金额
					 */
					private void setDishTotal(String amount) {
						String orderNumTotal = "点菜合计：￥" + amount;
						int priceDivierIndex = orderNumTotal.indexOf("：") + 1;
						SpannableString mSpannableString = new SpannableString(
								orderNumTotal);
						mSpannableString.setSpan(
								new ForegroundColorSpan(context.getResources()
										.getColor(R.color.font_red)),
								priceDivierIndex, orderNumTotal.length(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						orderTotal.setText(mSpannableString);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_merchant: {
			Intent intent = new Intent(this, MerchantDetailsActivity.class);
			intent.putExtra("meid", order.getMerchantId());
			startActivity(intent);
			break;
		}
		case R.id.tv_address: {
			String tel = order.getMerchantTell();
			if (!TextUtils.isEmpty(tel) && tel.trim().length() > 0) {
				showDialog(OrderDetailActivity.this, R.string.confirm,
						R.string.call_up_tip, R.string.cancel, tel);
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 是否要拨打商家电话
	 * 
	 * @param context
	 * @param titleId
	 *            对话框标题
	 * @param confirmTextId
	 *            确认按钮的内容
	 * @param contentStr
	 *            对话框内容
	 */
	public void showDialog(final Context context, int confirmTextId,
			int contentStr, int cancelTextId, final String phone) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("tel:" + phone);
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
				sweetAlertDialog.dismiss();
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

	/**
	 * 设置当前按钮的状态
	 * 
	 * @param vh
	 * @param result
	 */
	private void setCurrentButtun(final Order result) {
		// 订单状态、退款状态
		String orderStatus = result.getOrderStatus();
		String backTag = result.getBackTag();
		String status = result.getStatus();
		/**
		 * orderStatus:0未付款，1已付款，2已完成，3已关闭 (当orderStatus =1 时 backTag=0 显示 已付款，
		 * backTag=1 显示退款成功 backTag=2 显示申请退单 backTag=3 显示退单中 backTag=4 显示拒绝退单 )
		 */
		/**
		 * 未付款
		 */
		if (orderStatus.equals("0")) {
			// 判断是30分钟之内，还是30分钟之外
			// 交易中----30分钟内没有付款（另一种未付款状态，已关闭状态）
			btnAction.setBackgroundResource(R.drawable.blue_darker_btn_bg);
			btnAction.setText("立即付款");
			btnAction.setClickable(true);
			// 付款
			btnAction.setOnClickListener(new OnClick("pay", result, position));
			code="1";
		} else if (orderStatus.equals("1")) {
			/**
			 * 已付款（包括交易中的状态和已关闭和已完成的状态）
			 */
			// backTag=0 显示 已付款， backTag=1 显示退款成功 backTag=2 显示申请退单 backTag=3
			// 显示退单中 backTag=4 显示拒绝退单
			if (backTag.equals("0")) {
				if(status.equals("1")){
					btnAction.setText("申请退款");
				}else{
					btnAction.setText("取消订单");
				}	
				// 取消订单
				setUnClick(result, new OnClick("cancelOrder", result, position));
				code="1";
			} else if (backTag.equals("2") || backTag.equals("3")) {
				btnAction.setText("申请退款中");
				btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
				btnAction.setClickable(false);
			}

		} else if (orderStatus.equals("2")) {
			/**
			 * 已完成
			 */
			btnAction.setText("再来一份");
			btnAction.setBackgroundResource(R.drawable.orange_darker_btn_bg);
			btnAction.setClickable(true);
			btnAction.setOnClickListener(new OnClick("add", result, position));
		} else if (orderStatus.equals("3")) {
			/**
			 * 已关闭
			 */
			if (backTag.equals("1")) {
				if(status.equals("5")){
					btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
					btnAction.setText("已退款");
					btnAction.setClickable(false);
				}else if(status.equals("4")){
					btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
					btnAction.setText("已取消");
					btnAction.setClickable(false);
				}else if(status.equals("8")){
					btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
					btnAction.setText("逾期未接单");
					btnAction.setClickable(false);
				}
				
			} else if (backTag.equals("4")) {
				btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
				btnAction.setText("退款被拒绝");
				btnAction.setClickable(false);
			} else {
				btnAction.setBackgroundResource(R.drawable.red_darker_btn_bg);
				btnAction.setText("再订");
				btnAction.setClickable(true);
				btnAction.setOnClickListener(new OnClick("again", result,
						position));
				code="1";
			}

		}else if (orderStatus.equals("4")) {
			/**
			 * 已退款
			 */
			if (backTag.equals("1")) {
				if(status.equals("5")){
					btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
					btnAction.setText("已退款");
					btnAction.setClickable(false);
				}else if(status.equals("4")){
					btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
					btnAction.setText("已取消");
					btnAction.setClickable(false);
				}else if(status.equals("8")){
					btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
					btnAction.setText("逾期未接单");
					btnAction.setClickable(false);
				}
				
			} else if (backTag.equals("4")) {
				btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
				btnAction.setText("退款被拒绝");
				btnAction.setClickable(false);
			} else {
				btnAction.setBackgroundResource(R.drawable.red_darker_btn_bg);
				btnAction.setText("再订");
				btnAction.setClickable(true);
				btnAction.setOnClickListener(new OnClick("again", result,
						position));
				code="1";
			}

		}
	}

	/**
	 * 设置已付款订单一小时不允许操作
	 * 
	 * @param vh
	 * @param result
	 * @param clickListener
	 */
	private void setUnClick(Order result, OnClickListener clickListener) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String todayDateTime = format.format(new Date());
			Date nowTime = format.parse(todayDateTime);
			Date orderTime = format.parse(result.getOrderTime()+":00");
			// 相差的毫秒数
			long diff = orderTime.getTime() - nowTime.getTime();
			// 秒
			long timeInterval = diff / 1000;
			if (timeInterval < (60 * 60)) {
				btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
				btnAction.setClickable(false);
			} else {
				btnAction
						.setBackgroundResource(R.drawable.orange_darker_btn_bg);
				btnAction.setClickable(true);
				btnAction.setOnClickListener(clickListener);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 取消订单
	 * 
	 * @param userId
	 * @param orderNum
	 */
	private void cancelOrder(String userId, final Order changeOrder,
			final int poistion, final SweetAlertDialog sweetAlertDialog) {
		// TODO Auto-generated method stub
		params.put("cid", changeOrder.getOrderNum());
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.REFUND,
				params, new AbsHttpStringResponseListener(OrderDetailActivity.this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(OrderDetailActivity.this, "订单取消成功！",
									Toast.LENGTH_SHORT).show();
							getOrderDetails(orderId);
							btnAction.setBackgroundResource(R.drawable.light_gray_btn_bg);
							btnAction.setText("已退款");
							btnAction.setClickable(false);
//							Message message = handler.obtainMessage();
//							message.what = OrderParams.CANCEL_ORDER;
//							message.arg1 = poistion;
//							message.obj = order;
//							handler.sendMessage(message);
							sweetAlertDialog.dismiss();
						} else {
							Toast.makeText(OrderDetailActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
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
			final String userId, final Order order, final int position) {
		SweetAlertDialog dialog = new SweetAlertDialog(context);
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				cancelOrder(userId, order, position, sweetAlertDialog);
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

	public class OnClick implements OnClickListener {
		private String operationFlag;
		private Order result;
		private int position;

		public OnClick() {

		}

		public OnClick(String operationFlag, Order result, int position) {
			this.operationFlag = operationFlag;
			this.result = result;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			// 立即支付
			if (operationFlag.equals("pay")) {
				// (orderType:订单类型（0只订座，1订座订菜)
				BlueTownApp.orderType = result.getOrderType();
				BlueTownApp.ACTIVITY_ACTION = "1";
				intent.setClass(OrderDetailActivity.this,
						OnlinePayActivity.class);
				intent.putExtra("orderNum", result.getOrderNum());
				intent.putExtra("amount", result.getAmount());
				intent.putExtra("mid", result.getMerchantId());
				intent.putExtra("merchantName", result.getMerchantName());
				intent.putExtra("dinnerTime", result.getOrderTime());
				intent.putExtra("tableName", result.getTableNum());
				intent.putExtra("type", 1);
				BlueTownApp.setOrderDishList(result.getMenuList());
				startActivity(intent);
			} else if (operationFlag.equals("cancelOrder")) {
				// type 0 餐厅订单 1食堂订单
				String type = result.getType();
				if (type.equals("0")) {
					BlueTownApp.DISH_TYPE = "food";
				} else {
					BlueTownApp.DISH_TYPE = "canteen";
				}
			
				// 取消订单
				showDialog(OrderDetailActivity.this, R.string.tip,
						R.string.confirm, R.string.cancel,
						R.string.cancel_order_tip, userId, result, position);
			} else if (operationFlag.equals("add")) {
				// 追加（继续点菜）
				BlueTownApp.dishesPrice = result.getAmount();
				// (餐厅还是食堂点菜)
				String type = result.getType();
				if (type.equals("0")) {
					BlueTownApp.DISH_TYPE = "food";
				} else {
					BlueTownApp.DISH_TYPE = "canteen";
				}
				BlueTownApp.ACTIVITY_ACTION = "1";
				if (BlueTownApp.getDishList() != null) {
					BlueTownApp.getDishList().clear();
				}
				BlueTownApp.setDishesCount(0);
				BlueTownApp.setDishesPrice("0.00");
				BlueTownApp.setOriginalPrice("0.00");
				intent.setClass(OrderDetailActivity.this,
						MerchantDetailsActivity.class);
				intent.putExtra("meid", result.getMerchantId());
				startActivity(intent);
			} else if (operationFlag.equals("again")) {
				// type ： 0 餐厅订单 1食堂订单）
				String type = result.getType();
				if (type.equals("0")) {
					BlueTownApp.DISH_TYPE = "food";
				} else {
					BlueTownApp.DISH_TYPE = "canteen";
				}
				// (orderType:订单类型（0只订座，1订座订菜)
				BlueTownApp.orderType = result.getOrderType();
				BlueTownApp.dishesPrice = result.getAmount();
				if (result.getOrderType().equals("0")) {
					// 预订-tableactivity--=下一步
					BlueTownApp.BOOK_OR_ORDER = "0";
					// 点菜顺序
					BlueTownApp.ORDER_BY = "0";
					intent.setClass(OrderDetailActivity.this,
							TableBookActivity.class);
					intent.putExtra("meid", result.getMerchantId());
					intent.putExtra("merchantName", result.getMerchantName());
					startActivity(intent);
				} else {
					intent.setClass(OrderDetailActivity.this,
							OnlineBookActivity.class);
					intent.putExtra("meid", result.getMerchantId());
					intent.putExtra("isClosed",1);
					intent.putExtra("merchantName", result.getMerchantName());
					setBookParams();
					startActivity(intent);
				}

			}
		}

		/**
		 * 设置参数
		 */
		private void setBookParams() {
			// 继续点菜
			BlueTownApp.ORDER_TYPE = "continue";
			// 预订-tableactivity--=下一步
			BlueTownApp.BOOK_OR_ORDER = "1";
			BlueTownApp.ORDER_BY = "1";
			BlueTownApp.ACTIVITY_ACTION = "1";
			BlueTownApp.setDishList(null);
			BlueTownApp.setDishesCount(0);
			BlueTownApp.setDishesPrice("0.00");
			BlueTownApp.setOriginalPrice("0.00");
			BlueTownApp.setOrderDishList(result.getMenuList());
			int dishCount = 0;
			for (SeleteDish dish : result.getMenuList()) {
				dishCount = dishCount + Integer.parseInt(dish.getDishesCount());
			}
			BlueTownApp.setDishesCount(dishCount);
			BlueTownApp.setDishesPrice(result.getAmount());
			BlueTownApp.setOriginalPrice(result.getAmount());
		}
	}

}

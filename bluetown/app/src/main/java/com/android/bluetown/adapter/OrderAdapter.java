package com.android.bluetown.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.SeleteDish;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.listener.OrderCallBackListener;
import com.android.bluetown.result.OrderListResult.Order;
import com.android.bluetown.result.Result;
import com.android.bluetown.surround.MerchantDetailsActivity;
import com.android.bluetown.surround.OnlineBookActivity;
import com.android.bluetown.surround.OnlinePayActivity;
import com.android.bluetown.surround.TableBookActivity;
import com.android.bluetown.utils.Constant;

/**
 * 显示全部订单的适配器
 * 
 * @author wangxuan
 * 
 */
@SuppressLint("NewApi")
public class OrderAdapter extends BaseContentAdapter {
	private Context mContext;
	private List<Order> list_result;
	private Resources resources;
	private FinalDb db;
	private String userId = "";

	private List<MemberUser> users;
	private OrderCallBackListener listener;

	public OrderAdapter(Context mContext, List<Order> list_result,
			OrderCallBackListener listener) {
		super(mContext, list_result);
		this.mContext = mContext;
		this.list_result = list_result;
		this.listener = listener;
		resources = mContext.getResources();
		db = FinalDb.create(mContext);
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_order, null);
			vh = new ViewHolder();
			vh.order_creat_time = (TextView) convertView
					.findViewById(R.id.order_creat_time);
			vh.order_shop_name = (TextView) convertView
					.findViewById(R.id.order_shop_name);
			vh.order_number = (TextView) convertView
					.findViewById(R.id.order_number);
			vh.order_time = (TextView) convertView
					.findViewById(R.id.order_time);
			vh.order_msg = (TextView) convertView.findViewById(R.id.order_msg);
			vh.order_money = (TextView) convertView
					.findViewById(R.id.order_money);
			vh.order_state = (TextView) convertView
					.findViewById(R.id.order_state);
			vh.order_click = (Button) convertView
					.findViewById(R.id.order_click);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final Order result = list_result.get(position);
		vh.order_creat_time.setText("创建时间 " + result.getCreateTime());
		vh.order_shop_name.setText(result.getMerchantName());
		vh.order_number.setText(result.getTableNum());
		vh.order_time.setText(result.getOrderTime());
		String amount = result.getAmount();
		if (!TextUtils.isEmpty(amount)) {
			double temp = Double.parseDouble(amount);
			amount = context.getString(R.string.fee, temp);
		} else {
			amount = "0.00";
		}
		vh.order_money.setText(amount);
		setCurrentButtun(vh, position, result);
		return convertView;
	}

	/**
	 * 设置当前按钮的状态
	 * 
	 * @param vh
	 * @param result
	 */
	private void setCurrentButtun(ViewHolder vh, int position,
			final Order result) {
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
			vh.order_state.setText("未付款");
			vh.order_state
					.setTextColor(resources.getColor(R.color.font_orange));
			vh.order_click.setBackgroundResource(R.drawable.blue_darker_btn_bg);
			vh.order_click.setText("立即付款");
			vh.order_click.setClickable(true);
			// 付款
			vh.order_click.setOnClickListener(new OnClick("pay", result,
					position));
		} else if (orderStatus.equals("1")) {
			/**
			 * 已付款（包括交易中的状态和已关闭和已完成的状态）
			 */
			// backTag=0 显示 已付款， backTag=1 显示退款成功 backTag=2 显示申请退单 backTag=3
			// 显示退单中 backTag=4 显示拒绝退单
			if (backTag.equals("0")) {
				vh.order_state.setTextColor(resources
						.getColor(R.color.title_bg_blue));				
				if(status.equals("1")){
					vh.order_state.setText("商家已接单");
					vh.order_click.setText("申请退款");
				}else{
					vh.order_state.setText("已下单");
					vh.order_click.setText("取消订单");
				}				
				// 取消订单
				setUnClick(vh, result, new OnClick("cancelOrder", result,
						position));
			} else if (backTag.equals("2") || backTag.equals("3")) {
				vh.order_state.setTextColor(resources
						.getColor(R.color.font_gray));
				vh.order_state.setText("申请退款中");
				vh.order_click.setText("申请退款中");
				vh.order_click
						.setBackgroundResource(R.drawable.light_gray_btn_bg);
				vh.order_click.setClickable(false);
			}

		} else if (orderStatus.equals("2")) {
			/**
			 * 已完成
			 */
			if(status.equals("6")){
				vh.order_state.setTextColor(resources
						.getColor(R.color.font_gray));
				vh.order_state.setText("逾期未到");
				vh.order_click.setText("再来一份");
				vh.order_click
						.setBackgroundResource(R.drawable.orange_darker_btn_bg);
				vh.order_click.setClickable(true);
				vh.order_click.setOnClickListener(new OnClick("add", result,
						position));
			}else{
				vh.order_state.setTextColor(resources.getColor(R.color.font_gray));
				vh.order_state.setText("已完成");
				vh.order_click.setText("再来一份");
				vh.order_click
						.setBackgroundResource(R.drawable.orange_darker_btn_bg);
				vh.order_click.setClickable(true);
				vh.order_click.setOnClickListener(new OnClick("add", result,
						position));
			}
			
		} else if (orderStatus.equals("3")) {
			/**
			 * 已关闭
			 */
			if (backTag.equals("1")) {
				if(status.equals("5")){
					vh.order_state.setTextColor(resources
							.getColor(R.color.font_gray));
					vh.order_state.setText("已退款");
					vh.order_click
							.setBackgroundResource(R.drawable.light_gray_btn_bg);
					vh.order_click.setText("已退款");
					vh.order_click.setClickable(false);
				}else if(status.equals("4")){
					vh.order_state.setTextColor(resources
							.getColor(R.color.font_gray));
					vh.order_state.setText("已取消");
					vh.order_click
							.setBackgroundResource(R.drawable.light_gray_btn_bg);
					vh.order_click.setText("已取消");
					vh.order_click.setClickable(false);
				}else if(status.equals("8")){
					vh.order_state.setTextColor(resources
							.getColor(R.color.font_gray));
					vh.order_state.setText("逾期未接单");
					vh.order_click
							.setBackgroundResource(R.drawable.light_gray_btn_bg);
					vh.order_click.setText("逾期未接单");
					vh.order_click.setClickable(false);
				}
				
			} else if (backTag.equals("4")) {
				vh.order_state.setTextColor(resources
						.getColor(R.color.font_gray));
				vh.order_state.setText("退款被拒绝");
				vh.order_click
						.setBackgroundResource(R.drawable.light_gray_btn_bg);
				vh.order_click.setText("退款被拒绝");
				vh.order_click.setClickable(false);
			} else {
				vh.order_state.setTextColor(resources
						.getColor(R.color.font_gray));
				vh.order_state.setText("已关闭");
				vh.order_click
						.setBackgroundResource(R.drawable.red_darker_btn_bg);
				vh.order_click.setText("再订");
				vh.order_click.setClickable(true);
				vh.order_click.setOnClickListener(new OnClick("again", result,
						position));
			}

		}else if (orderStatus.equals("4")) {
			/**
			 * 已退款
			 */
			if (backTag.equals("1")) {
				if(status.equals("5")){
					vh.order_state.setTextColor(resources
							.getColor(R.color.font_gray));
					vh.order_state.setText("已退款");
					vh.order_click
							.setBackgroundResource(R.drawable.light_gray_btn_bg);
					vh.order_click.setText("已退款");
					vh.order_click.setClickable(false);
				}else if(status.equals("4")){
					vh.order_state.setTextColor(resources
							.getColor(R.color.font_gray));
					vh.order_state.setText("已取消");
					vh.order_click
							.setBackgroundResource(R.drawable.light_gray_btn_bg);
					vh.order_click.setText("已取消");
					vh.order_click.setClickable(false);
				}else if(status.equals("8")){
					vh.order_state.setTextColor(resources
							.getColor(R.color.font_gray));
					vh.order_state.setText("逾期未接单");
					vh.order_click
							.setBackgroundResource(R.drawable.light_gray_btn_bg);
					vh.order_click.setText("逾期未接单");
					vh.order_click.setClickable(false);
				}
			} else if (backTag.equals("4")) {
				vh.order_state.setTextColor(resources
						.getColor(R.color.font_gray));
				vh.order_state.setText("退款被拒绝");
				vh.order_click
						.setBackgroundResource(R.drawable.light_gray_btn_bg);
				vh.order_click.setText("退款被拒绝");
				vh.order_click.setClickable(false);
			} else {
				vh.order_state.setTextColor(resources
						.getColor(R.color.font_gray));
				vh.order_state.setText("已关闭");
				vh.order_click
						.setBackgroundResource(R.drawable.red_darker_btn_bg);
				vh.order_click.setText("再订");
				vh.order_click.setClickable(true);
				vh.order_click.setOnClickListener(new OnClick("again", result,
						position));
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
	private void setUnClick(ViewHolder vh, Order result,
			OnClickListener clickListener) {
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
				vh.order_click
						.setBackgroundResource(R.drawable.light_gray_btn_bg);
				vh.order_click.setClickable(false);
			} else {
				vh.order_click
						.setBackgroundResource(R.drawable.orange_darker_btn_bg);
				vh.order_click.setClickable(true);
				vh.order_click.setOnClickListener(clickListener);
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
	 *            用户id
	 * @param order
	 *            订单
	 * @param sweetAlertDialog
	 */
	private void cancelOrder(String userId, final Order order,
			final int position, final SweetAlertDialog sweetAlertDialog) {
		// TODO Auto-generated method stub
		params.put("cid", order.getOrderNum());
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.REFUND,
				params, new AbsHttpStringResponseListener(context, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(mContext, "订单取消成功！",
									Toast.LENGTH_SHORT).show();
							order.setOrderStatus("1");
							order.setBackTag("1");
							listener.onOrderStatusChange(position, order);
							sweetAlertDialog.dismiss();
						} else {
							Toast.makeText(mContext, result.getRepMsg(),
									Toast.LENGTH_SHORT).show();
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
	 *            确定按钮
	 * @param cancelTextId
	 *            取消按钮
	 * @param contentStr
	 *            对话框内容
	 * @param userId
	 *            用户id
	 * @param result
	 *            订单
	 */
	public void showDialog(final Context context, int titleId,
			int confirmTextId, int cancelTextId, int contentStr,
			final String userId, final Order result, final int position) {
		SweetAlertDialog dialog = new SweetAlertDialog(context);
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				cancelOrder(userId, result, position, sweetAlertDialog);
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
		private int position;
		private Order result;

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
				intent.setClass(mContext, OnlinePayActivity.class);
				intent.putExtra("orderNum", result.getOrderNum());
				intent.putExtra("amount", result.getAmount());
				intent.putExtra("mid", result.getMerchantId());
				intent.putExtra("merchantName", result.getMerchantName());
				intent.putExtra("dinnerTime", result.getOrderTime());
				intent.putExtra("tableName", result.getTableNum());
				BlueTownApp.setOrderDishList(result.getMenuList());
				mContext.startActivity(intent);
			} else if (operationFlag.equals("cancelOrder")) {
				// type 0 餐厅订单 1食堂订单
				String type = result.getType();
				if (type.equals("0")) {
					BlueTownApp.DISH_TYPE = "food";
				} else {
					BlueTownApp.DISH_TYPE = "canteen";
				}
			users = db.findAll(MemberUser.class);
				if (users != null && users.size() != 0) {
					MemberUser user = users.get(0);
					if (user != null) {
						userId = user.getMemberId();
					}

				}
				// 取消订单
				showDialog(mContext, R.string.tip, R.string.confirm,
						R.string.cancel, R.string.cancel_order_tip, userId,
						result, position);
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
				intent.setClass(mContext, MerchantDetailsActivity.class);
				intent.putExtra("meid", result.getMerchantId());
				mContext.startActivity(intent);
			} else if (operationFlag.equals("again")) {
				// (餐厅还是食堂点菜)
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
					intent.setClass(mContext, TableBookActivity.class);
					intent.putExtra("meid", result.getMerchantId());
					intent.putExtra("merchantName", result.getMerchantName());
					mContext.startActivity(intent);
				} else {
					intent.setClass(mContext, OnlineBookActivity.class);
					intent.putExtra("meid", result.getMerchantId());
					intent.putExtra("merchantName", result.getMerchantName());
					setBookParams();
					mContext.startActivity(intent);
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
			if (result.getMenuList() != null) {
				for (SeleteDish dish : result.getMenuList()) {
					dishCount = dishCount
							+ Integer.parseInt(dish.getDishesCount());
				}
				BlueTownApp.setDishesCount(dishCount);
				BlueTownApp.setDishesPrice(result.getAmount());
				BlueTownApp.setOriginalPrice(result.getAmount());
			}

		}
	}

	class ViewHolder {

		TextView order_creat_time;// 创建时间
		TextView order_shop_name;// 商家名称
		TextView order_number;// 订单人数
		TextView order_time;// 订单时间
		TextView order_msg;// 订单内容
		TextView order_money;// 订单金额
		TextView order_state;// 订单状态
		Button order_click;// 对当前的订单进行操作

	}
}
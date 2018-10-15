package com.android.bluetown.surround;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.DishListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;

import com.android.bluetown.result.PlaceOrderResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.MyRadioGroup;

/**
 * 在线预定-只订座(或在线预定--提前点菜)
 * 
 * @author shenyz
 * 
 */
public class OnlineBookSeatActivity extends TitleBarActivity implements
		OnClickListener {
	private TextView bt_onlineserver_locations;
	private TextView bt_onlineserver_Seats;
	private TextView bt_onlineserver_timess;
	private TextView totalPrice;
	private TextView continueOrder;
	private EditText btOnlineserverName;
	private EditText tvOnlinePhone;
	private EditText etNote;
	private Button bt_onlinerserver_sures;
	private MyRadioGroup seXRadioGroup;
	private MyRadioGroup radioGroup;
	private RadioButton selectRBSit;
	private RadioButton selectRBFood;
	private ListView foodView;
	
	private String userId, username, tel, sex, mid, merchantName, tableName,
			dinnerTime, flag;
	private String orderType = "";
	private String orderNum = "";
	private String bookPrice = "";
	// 提前点菜
	private List<RecommendDish> dishList;
	private FinalDb db;
	private List<MemberUser> users;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.online_book_title);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.activity_online_book_seat);
		BlueTownExitHelper.addActivity(this);
		initUIView();
	}

	private void initUIView() {
		// TODO Auto-generated method stub
	
		bt_onlineserver_locations = (TextView) this
				.findViewById(R.id.bt_onlineserver_locations);
		bt_onlineserver_Seats = (TextView) this
				.findViewById(R.id.bt_onlineserver_Seats);
		bt_onlineserver_timess = (TextView) this
				.findViewById(R.id.bt_onlineserver_timess);
		bt_onlinerserver_sures = (Button) this
				.findViewById(R.id.bt_onlinerserver_sures);
		continueOrder = (TextView) findViewById(R.id.continue_food);
		radioGroup = (MyRadioGroup) findViewById(R.id.rg_onlineres);
		selectRBSit = (RadioButton) findViewById(R.id.cb_online_one);
		selectRBFood = (RadioButton) findViewById(R.id.cb_online_two);
		foodView = (ListView) findViewById(R.id.lv_cart_food);
		totalPrice = (TextView) findViewById(R.id.tv_online_total);
		btOnlineserverName = (EditText) this.findViewById(R.id.et_online_name);
		tvOnlinePhone = (EditText) this.findViewById(R.id.et_seat_phone);
		etNote = (EditText) this.findViewById(R.id.et_online_teshumust);
		seXRadioGroup = (MyRadioGroup) this.findViewById(R.id.mrg_onliner_sex);
		seXRadioGroup.setOnCheckedChangeListener(onCheckedListener);
		bt_onlinerserver_sures.setOnClickListener(this);
		selectRBSit.setOnClickListener(this);
		selectRBFood.setOnClickListener(this);
		continueOrder.setOnClickListener(this);
		initUIData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/**
	 * 初始化界面数据
	 */
	private void initUIData() {
	db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				sex = user.getSex();
				username = user.getName();
				tel = user.getUsername();
			}
		}
		mid = getIntent().getStringExtra("meid");
		merchantName = getIntent().getStringExtra("merchantName");
		tableName = getIntent().getStringExtra("tableName");
		dinnerTime = getIntent().getStringExtra("dinnerTime");
		flag = BlueTownApp.DISH_TYPE;
		orderType = BlueTownApp.orderType;
		
		bt_onlineserver_locations.setText(merchantName);
		bt_onlineserver_Seats.setText(tableName);
		bt_onlineserver_timess.setText(dinnerTime);
		btOnlineserverName.setText(username);
		tvOnlinePhone.setText(tel);
		if (!orderType.isEmpty() && orderType.equals("1")) {
			selectRBFood.setChecked(true);
			selectRBSit.setChecked(false);
		} else {
			selectRBFood.setChecked(false);
			selectRBSit.setChecked(true);
		}
		initOrderListInfo();
		totalPrice.setText("总计：" + bookPrice + "元");
	}

	/**
	 * 初始化订单信息
	 */
	private void initOrderListInfo() {
		bookPrice = BlueTownApp.dishesPrice + "";
		totalPrice.setText(bookPrice);
		dishList = BlueTownApp.getDishList();
		if (dishList != null && dishList.size() > 0) {
			DishListAdapter adapter = new DishListAdapter(this, dishList,
					"dish");
			foodView.setAdapter(adapter);
			// 先订座后订菜
			continueOrder.setVisibility(View.VISIBLE);
			foodView.setVisibility(View.VISIBLE);
		} else {
			// 默认只订座
			continueOrder.setVisibility(View.GONE);
			foodView.setVisibility(View.GONE);
		}
	}

	private MyRadioGroup.OnCheckedChangeListener onCheckedListener = new MyRadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(MyRadioGroup group, int checkedId) {
			// swiTODO Auto-generated method stub
			switch (checkedId) {
			case R.id.rb_online_men:
				sex = "0";
				break;
			case R.id.rb_online_women:
				sex = "1";
				break;
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 选择订单类型(orderType ：订单类型（0只订座，1订座订菜）)
		switch (v.getId()) {
		case R.id.bt_onlinerserver_sures:// 下订单
			String amount = "";
			String type = "";
			String memo = etNote.getText().toString();
			String telPhone = tvOnlinePhone.getText().toString();
			String username = btOnlineserverName.getText().toString();
			if (flag.equals("canteen")) {
				type = "1";
			} else {
				type = "0";
			}
			if (TextUtils.isEmpty(userId)) {
				TipDialog.showDialogNoClose(OnlineBookSeatActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
				return;
			}

			if (TextUtils.isEmpty(username)) {
				TipDialog.showDialog(OnlineBookSeatActivity.this, R.string.tip,
						R.string.confirm, R.string.user_name_empty);
				return;
			}

			if (TextUtils.isEmpty(telPhone)) {
				TipDialog.showDialog(OnlineBookSeatActivity.this, R.string.tip,
						R.string.confirm, R.string.telp_empty);
				return;
			}

			if (telPhone.length() < 11 || !isTelPhoto(telPhone)) {
				TipDialog.showDialog(OnlineBookSeatActivity.this, R.string.tip,
						R.string.confirm, R.string.error_phone);
				return;
			}

			if (orderType.isEmpty()) {
				orderType = "0";
			} else if (orderType.equals("0") && dishList != null
					&& dishList.size() > 0) {
				orderType = "1";
			}

			// 只订座
			if (orderType.equals("0")) {
				amount = bookPrice;
				if (amount.isEmpty() || amount.equals("0.00")) {
					TipDialog.showDialog(OnlineBookSeatActivity.this,
							R.string.tip, R.string.confirm,
							R.string.price_empty);
					return;
				}
				placeOrder(userId, mid, dinnerTime, username, telPhone, sex,
						memo, tableName, orderType, amount, null, type);
			} else {
				dishList = BlueTownApp.getDishList();
				amount = BlueTownApp.getDishesPrice();
				if (amount.isEmpty() || amount.equals("0.00")) {
					TipDialog.showDialog(OnlineBookSeatActivity.this,
							R.string.tip, R.string.confirm,
							R.string.price_empty);
					return;
				}
				placeOrder(userId, mid, dinnerTime, username, telPhone, sex,
						memo, tableName, orderType, amount, dishList, type);
			}

			break;

		case R.id.continue_food: // 继续点菜
			Intent orderIntent = new Intent();
			orderIntent.putExtra("meid", mid);
			orderIntent.putExtra("merchantName", merchantName);
			orderIntent.putExtra("tableName", tableName);
			orderIntent.putExtra("dinnerTime", dinnerTime);
			orderIntent.putExtra("isClosed","0");
			orderIntent.setClass(OnlineBookSeatActivity.this,
					RecommendDishActivity.class);
			BlueTownApp.ORDER_TYPE = "continue";
			startActivity(orderIntent);
			break;
		case R.id.cb_online_one: // 只订座
			selectRBFood.setChecked(false);
			selectRBSit.setChecked(true);
			// 只订座不点菜
			BlueTownApp.orderType = "0";
			BlueTownApp.ORDER_BY = "0";
			foodView.setVisibility(View.GONE);
			continueOrder.setVisibility(View.GONE);
			// 定金（订座定金）
			totalPrice.setText("总计：" + bookPrice);
			break;
		case R.id.cb_online_two: // 先订菜到了等吃
			selectRBFood.setChecked(true);
			selectRBSit.setChecked(false);
			// 提前点菜
			BlueTownApp.orderType = "1";
			foodView.setVisibility(View.GONE);
			continueOrder.setVisibility(View.GONE);
			Intent intent = new Intent();
			intent.setClass(OnlineBookSeatActivity.this,
					RecommendDishActivity.class);
			intent.putExtra("meid", mid);
			intent.putExtra("isClosed","0");
			intent.putExtra("merchantName", merchantName);
			intent.putExtra("dinnerTime", dinnerTime);
			intent.putExtra("tableName", tableName);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: isTelPhoto
	 * @Description: TODO(判断电话号码是否有效)
	 * @param mobiles
	 *            电话号码
	 * @return
	 * @throws
	 */
	private boolean isTelPhoto(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 下订单
	 * 
	 * @param userId
	 *            ：订餐人id
	 * @param meid
	 *            ：商家id
	 * @param orderTime
	 *            ：预约时间
	 * @param contactName
	 *            ：联系名称
	 * @param contactTel
	 *            ：联系电话
	 * @param contactSex
	 *            ：性别
	 * @param description
	 *            ：备注(非必填)
	 * @param tableNum
	 *            ：餐桌的类型(传实值)
	 * @param orderType
	 *            ：订单类型（0只订座，1订座订菜）
	 * @param amount
	 *            ：支付金额
	 * @param menus
	 *            ：顶菜的信息
	 * @param type
	 *            ： 0 餐厅订单 1食堂订单
	 */
	private void placeOrder(String userId, String meid, final String orderTime,
			String contactName, String contactTel, String contactSex,
			String description, final String tableNum, final String orderType,
			final String amount, List<RecommendDish> menus, String type) {
		params.put("userId", userId);
		params.put("meid", meid);
		params.put("orderTime", orderTime);
		params.put("contactName", contactName);
		params.put("contactTel", contactTel);
		params.put("contactSex", contactSex);
		params.put("description", description);
		params.put("tableNum", tableNum);
		params.put("orderType", orderType);
		params.put("amount", amount);
		if (menus != null) {
			params.put("menus", AbJsonUtil.toJson(menus));
		} else {
			params.put("menus", "[]");
		}
		params.put("type", type);
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.PLACE_AN_ORDER, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						PlaceOrderResult result = (PlaceOrderResult) AbJsonUtil
								.fromJson(s, PlaceOrderResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							orderNum = result.getData().getOrderNum();
							Intent intent = new Intent();
							intent.putExtra("orderNum", orderNum);
							intent.putExtra("amount", amount);
							intent.putExtra("mid", mid);
							intent.putExtra("flag", flag);
							intent.putExtra("merchantName", merchantName);
							intent.putExtra("dinnerTime", orderTime);
							intent.putExtra("tableName", tableNum);
							intent.setClass(OnlineBookSeatActivity.this,
									OnlinePayActivity.class);
							OnlineBookSeatActivity.this.finish();
							startActivity(intent);
						} else {
							Toast.makeText(OnlineBookSeatActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

	}

}

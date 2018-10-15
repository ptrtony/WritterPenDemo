package com.android.bluetown.surround;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.DishListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.bean.SeleteDish;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.PlaceOrderResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.ListViewForScrollView;

/**
 * 在线预订
 * 
 * @author shenyz
 * 
 */
public class OnlineBookActivity extends TitleBarActivity implements
		OnClickListener {
	// 餐厅名称、桌子、就餐时间,选择就餐时间、选择的食物的份数、继续点菜、总钱数,总原价钱数
	private TextView merchantName, tableName, dinnerTime, dinnerTimeTitle,
			dishCount, continueOrder, totalPriceCountText,totalOriginalPrice;
	// 用户姓名、联系方式、特殊要求
	private EditText mNameText, mPhoneText, mSpecialRequestText;
	// 女士还是先生
	private RadioButton mWomenBtn, mManBtn;
	// 显示菜单的ListView
	private ListViewForScrollView mListView;
	// 支付定金
	private Button mPayBtn;
	// flag食堂订单还是美食订单
	private String merName, totalPriceCount, mid, flag, sex, userId, orderNum,
			dinnerT, tableNum;
	private int dishesCount;
	private List<RecommendDish> dishList;
	// 订单-再订 的点菜列表
	private List<SeleteDish> orderDishList;
	private DishListAdapter adapter;
	private FinalDb db;

	private List<MemberUser> users;
	private Drawable womenDrawable, drawable;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.online_book_title);
		setTitleLayoutBg(R.color.title_bg_blue);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_online_book);
		BlueTownExitHelper.addActivity(this);
		initUIData();

	}

	/**
	 * 初始化界面显示的数据
	 */
	private void initUIData() {
		// 默认软键盘不弹出
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initUIView();
		totalOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		merName = getIntent().getStringExtra("merchantName");
		mid = getIntent().getStringExtra("meid");

		try {
			// 继续点菜和先订座后点菜
			dinnerT = getIntent().getStringExtra("dinnerTime");
			tableNum = getIntent().getStringExtra("tableName");
			// 再订的点菜列表
			orderDishList = BlueTownApp.getOrderDishList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 食堂订单还是美食订单
		flag = BlueTownApp.DISH_TYPE;
		merchantName.setText(merName);
		totalOriginalPrice.setText(BlueTownApp.getOriginalPrice());
		initUserInfo();
		initSearInfo();

	}

	/**
	 * 初始化用户信息
	 */
	private void initUserInfo() {
		String realname = "";
		String tel = "";
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				sex = user.getSex();
				realname = user.getName();
				tel = user.getUsername();
			}

		}
		if (null==sex)return;
		// 0代表男，1代表女
		if (sex.equals("1")) {
			setSexMan();
		} else {
			setSexWomen();

		}
		mNameText.setText(realname);
		mPhoneText.setText(tel);
	}

	/**
	 * 初始化订单信息
	 */
	private void initOrderListInfo() {
		double totalPrice = Double.parseDouble(BlueTownApp.getDishesPrice());
		totalPriceCount = getString(R.string.fee, totalPrice);
		totalPriceCount = "总计：" + totalPriceCount;
		totalPriceCountText.setText(totalPriceCount);
		dishList = BlueTownApp.getDishList();
		if (dishList != null && dishList.size() > 0) {
			dishesCount = dishList.size();
			dishCount.setText("预订点菜(" + dishesCount + ")");
			adapter = new DishListAdapter(this, dishList, "dish");
			mListView.setAdapter(adapter);
		} else {
			// 再订
			if (orderDishList != null && orderDishList.size() > 0) {
				dishesCount = orderDishList.size();
				dishCount.setText("预订点菜(" + dishesCount + ")");
				adapter = new DishListAdapter(this, orderDishList, "order-dish");
				mListView.setAdapter(adapter);
			}else{
				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 初始化座位信息
	 */
	private void initSearInfo() {
		if (tableNum != null && !tableNum.isEmpty()) {
			tableName.setVisibility(View.VISIBLE);
			dinnerTime.setVisibility(View.VISIBLE);
//			dinnerTimeTitle.setVisibility(View.GONE);
			tableName.setText(tableNum);
			dinnerTime.setText(dinnerT);
		} else {
			tableName.setVisibility(View.GONE);
			dinnerTime.setVisibility(View.GONE);
//			dinnerTimeTitle.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化界面组件
	 */
	private void initUIView() {
		db = FinalDb.create(this);
		merchantName = (TextView) findViewById(R.id.bt_onlineserver_location);
		tableName = (TextView) findViewById(R.id.tv_line_desk);
		dinnerTime = (TextView) findViewById(R.id.tv_line_time);
		dinnerTimeTitle = (TextView) findViewById(R.id.tv_line_time_title);
		dishCount = (TextView) findViewById(R.id.bt_onlineserver_fenshu);
		continueOrder = (TextView) findViewById(R.id.tv_continue_order);
		totalPriceCountText = (TextView) findViewById(R.id.tv_online_total);
		mNameText = (EditText) findViewById(R.id.et_onlineserve_name);
		mPhoneText = (EditText) findViewById(R.id.et_onlineserve_phones);
		mSpecialRequestText = (EditText) findViewById(R.id.et_onlineserve_teshumust);
		mWomenBtn = (RadioButton) findViewById(R.id.rb_onlineserve_women);
		mManBtn = (RadioButton) findViewById(R.id.rb_onlineserve_men);
		mListView = (ListViewForScrollView) findViewById(R.id.lv_onlineserver);
		mPayBtn = (Button) findViewById(R.id.bt_onlinerserver_sure);
		totalOriginalPrice = findViewById(R.id.tv_online_original_total);
		dinnerTime.setOnClickListener(this);
		dinnerTimeTitle.setOnClickListener(this);
		mPayBtn.setOnClickListener(this);
		mWomenBtn.setOnClickListener(this);
		mManBtn.setOnClickListener(this);
		continueOrder.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initOrderListInfo();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_onlinerserver_sure:
			String username = mNameText.getText().toString();
			String linkphone = mPhoneText.getText().toString();
			String tName = tableName.getText().toString();
//			String dTime = dinnerTime.getText().toString();
			String dishesPrice = totalPriceCountText.getText().toString();
			String description = mSpecialRequestText.getText().toString();
//			if (TextUtils.isEmpty(tName)) {
//				TipDialog.showDialog(OnlineBookActivity.this, R.string.tip,
//						R.string.confirm, R.string.selete_dinner_time_tip);
//				return;
//			}
			if (TextUtils.isEmpty(merName)) {
				TipDialog.showDialog(OnlineBookActivity.this, R.string.tip,
						R.string.confirm, R.string.merchant_name_empty_tip);
				return;
			}
			if (TextUtils.isEmpty(username)) {
				TipDialog.showDialog(OnlineBookActivity.this, R.string.tip,
						R.string.confirm, R.string.user_name_empty_tip);
				return;
			}
			if (TextUtils.isEmpty(linkphone)) {
				TipDialog.showDialog(OnlineBookActivity.this, R.string.tip,
						R.string.confirm, R.string.link_method_empty);
				return;
			}

			if (dishesPrice.isEmpty() || dishesPrice.equals("0.00")) {
				TipDialog.showDialog(OnlineBookActivity.this, R.string.tip,
						R.string.confirm, R.string.price_empty);
				return;
			}
			if (!TextUtils.isEmpty(dishesPrice)) {
				dishesPrice = dishesPrice.substring(
						dishesPrice.indexOf("：") + 1);
			}
			// 食堂订单还是餐厅订单
			String type = getOrderPlace();
			// orderType订单类型（0只订座，1订座订菜）

			String orderType = BlueTownApp.orderType;
			if (orderDishList != null && orderDishList.size() > 0) {
				placeOrder(userId, mid, username, linkphone, sex,
						description, tName, orderType, dishesPrice,
						orderDishList, type);
			} else {
				placeOrder(userId, mid, username, linkphone, sex,
						description, tName, orderType, dishesPrice, dishList,
						type);
			}

			break;
		case R.id.rb_onlineserve_women:
			setSexWomen();
			sex = "0";

			break;
		case R.id.rb_onlineserve_men:
			setSexMan();
			sex = "1";

			break;
		case R.id.tv_continue_order:
			// 继续点菜
			Intent orderIntent = new Intent();
			orderIntent.putExtra("meid", mid);
			orderIntent.putExtra("merchantName", merName);
			orderIntent.putExtra("tableName", tableName.getText().toString());
			orderIntent.putExtra("dinnerTime", dinnerTime.getText().toString());
			orderIntent.putExtra("isClosed","0");
			orderIntent.setClass(OnlineBookActivity.this,
					RecommendDishActivity.class);
			BlueTownApp.ORDER_TYPE = "continue";
			startActivity(orderIntent);
			break;
		case R.id.tv_line_time_title:
			Intent intent = new Intent();
			intent.putExtra("merchantName", merName);
			intent.putExtra("meid", mid);
			intent.setClass(OnlineBookActivity.this, TableBookActivity.class);
			startActivityForResult(intent, 110);
			break;
		case R.id.tv_line_time:
			Intent intent1 = new Intent();
			intent1.putExtra("merchantName", merName);
			intent1.putExtra("meid", mid);
			intent1.setClass(OnlineBookActivity.this, TableBookActivity.class);
			startActivityForResult(intent1, 110);
			break;

		default:
			break;
		}
	}

	/**
	 * 返回订单类型（食堂订单还是餐厅订单）
	 * 
	 * @return
	 */
	private String getOrderPlace() {
		String type = "";
		// type ： 0 餐厅订单 1食堂订单）
		if (flag.equals("food")) {
			type = "0";
		} else {
			// canteen
			type = "1";
		}
		return type;
	}

	/**
	 * 设置女士
	 */
	private void setSexWomen() {
		mWomenBtn.setChecked(true);
		womenDrawable = getResources().getDrawable(R.drawable.gender_check_on);
		womenDrawable.setBounds(0, 0, womenDrawable.getMinimumWidth(),
				womenDrawable.getMinimumHeight()); // 设置边界
		mWomenBtn.setCompoundDrawables(womenDrawable, null, null, null);
		mManBtn.setChecked(false);
		drawable = getResources().getDrawable(R.drawable.gender_check_normal);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		mManBtn.setCompoundDrawables(drawable, null, null, null);
	}

	/**
	 * 设置先生
	 */
	private void setSexMan() {
		mWomenBtn.setChecked(false);
		womenDrawable = getResources().getDrawable(
				R.drawable.gender_check_normal);
		womenDrawable.setBounds(0, 0, womenDrawable.getMinimumWidth(),
				womenDrawable.getMinimumHeight()); // 设置边界
		mWomenBtn.setCompoundDrawables(womenDrawable, null, null, null);
		mManBtn.setChecked(true);
		drawable = getResources().getDrawable(R.drawable.gender_check_on);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		mManBtn.setCompoundDrawables(drawable, null, null, null);
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
	private void placeOrder(String userId, String meid,
			String contactName, String contactTel, String contactSex,
			String description, final String tableNum, String orderType,
			final String amount, List<?> menus, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
		String orderTime = sdf.format(new java.util.Date());
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
		if (menus != null && menus.size() > 0) {
			params.put("menus", AbJsonUtil.toJson(menus));
		} else {
			params.put("menus", "[]");
		}
		params.put("type", type);
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.PLACE_AN_ORDER, params,
				new AbsHttpStringResponseListener(this, null) {
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
							intent.putExtra("merchantName", merName);
							intent.putExtra("dinnerTime", orderTime);
							intent.putExtra("tableName", tableNum);
							intent.setClass(OnlineBookActivity.this,
									OnlinePayActivity.class);
							OnlineBookActivity.this.finish();
							startActivity(intent);
						} else {
							Toast.makeText(OnlineBookActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

	}

	/**
	 * 展示就餐的时间和人数
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if (resultCode == 112) {
			if (arg2 != null) {
				tableName.setVisibility(View.VISIBLE);
				dinnerTime.setVisibility(View.VISIBLE);
				dinnerTimeTitle.setVisibility(View.GONE);
				tableName.setText(arg2.getStringExtra("tableName"));
				dinnerTime.setText(arg2.getStringExtra("dinnerTime"));
			}

		}
	}

}

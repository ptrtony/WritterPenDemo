package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.FinalDb;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ParkingMonthlyDetailAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MakingIncreaseBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ParkingAllowRentBean;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.datewheel.PersonPickerDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.CheckUtil;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ParseJSONTools;
import com.android.bluetown.utils.Util;

/**
 * @author hedi
 * @data: 2016年5月3日 下午1:16:14
 * @Description: 包月车位
 */
public class ParkingMonthlyDetailActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener {
	private GridView girdview;
	private List<MakingIncreaseBean> list;
	private ParkingMonthlyDetailAdapter adapter;
	private PersonPickerRecevier recevier;
	private boolean wheelScrolled = false;
	private DayArrayAdapter dayAdapter;
	private AbstractWheel wheel;
	private String[] str;
	private String amount;
	private TextView parking_type, amount_money;
	private TextView tv_company2, tv_company3;
	private EditText tv_carnum;
	private SharePrefUtils prefUtils;
	private FinalDb db;
	private String phoneNumber;
	private String parkingSpaceType;
	private TextView mTvMind;
	/**
	 * 注册设置园区的广播
	 */
	private void registerPersonRecevier() {
		recevier = new PersonPickerRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.guestcount.choice.action");
		registerReceiver(recevier, filter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_parking_monthly_detail);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initView();
		setData();
		getCarport();
		registerPersonRecevier();
		getOpenModelApi();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightImageLayout:
			Intent intent = (new Intent(ParkingMonthlyDetailActivity.this,
					ParkingHistoryActivity.class));
			startActivity(intent);
			break;
		case R.id.rl_parking_type:
			PersonPickerDialog appointDialog = new PersonPickerDialog(
					ParkingMonthlyDetailActivity.this);
			appointDialog.show();
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("车位包月");
		setTitleLayoutBg(R.color.title_bg_blue);
		setRightImageView(R.drawable.ic_history);
		rightImageLayout.setOnClickListener(this);
	}

	private void initView() {
		parking_type = (TextView) findViewById(R.id.parking_type);
		tv_company2 = (TextView) findViewById(R.id.tv_company2);
		tv_company3 = (TextView) findViewById(R.id.tv_company3);
		amount_money = (TextView) findViewById(R.id.amount_money);
		tv_carnum = (EditText) findViewById(R.id.tv_carnum);
		girdview = (GridView) findViewById(R.id.gridview);
		 mTvMind = findViewById(R.id.tv_remind);
		findViewById(R.id.rl_parking_type).setOnClickListener(this);
		db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				phoneNumber = user.getUsername();
			}
		}
		tv_company2.setText(prefUtils.getString(SharePrefUtils.REALNAME, ""));
		tv_company3.setText(phoneNumber);
		girdview.setOnItemClickListener(this);
		girdview.setSelector(new BitmapDrawable());
		wheel = (AbstractWheel) findViewById(R.id.people);
		str = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12", "15", "18", "24" };
		dayAdapter = new DayArrayAdapter(this, str);
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setViewAdapter(dayAdapter);
		if (!parking_type.getText().toString().isEmpty()) {
			amount_money.setText("合计￥"
					+ Integer.parseInt(str[wheel.getCurrentItem()])
					* Double.parseDouble((Util.String2num(parking_type
							.getText().toString()))));
			amount = String
					.valueOf(Integer.parseInt(str[wheel.getCurrentItem()])
							* Double.parseDouble((Util.String2num(parking_type
									.getText().toString()))));
		}
		tv_carnum.setTransformationMethod(new InputLowerToUpper());

	}

	public class InputLowerToUpper extends ReplacementTransformationMethod {
		@Override
		protected char[] getOriginal() {
			char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
					'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
					'w', 'x', 'y', 'z' };
			return lower;
		}

		@Override
		protected char[] getReplacement() {
			char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
					'W', 'X', 'Y', 'Z' };
			return upper;
		}

	}

	// Wheel scrolled flag

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(AbstractWheel wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(AbstractWheel wheel) {
			wheelScrolled = false;
			wheel.setViewAdapter(dayAdapter);
		}
	};

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
				wheel.setViewAdapter(dayAdapter);
			}
		}
	};

	private AbstractWheel getWheel(int id) {
		return (AbstractWheel) findViewById(id);
	}

	private class DayArrayAdapter extends AbstractWheelTextAdapter {
		private String[] str;

		/**
		 * Constructor
		 */
		protected DayArrayAdapter(Context context, String[] data) {
			super(context, R.layout.wheel_text_centered, NO_RESOURCE);
			str = data;
			setItemTextResource(R.id.text);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView text = (TextView) view.findViewById(R.id.text);
			text.setText(str[index]);
			if (str.length == 15) {
				if (index == getWheel(R.id.people).getCurrentItem()) {
					text.setTextColor(0xff4a90fb);
					if (!parking_type.getText().toString().isEmpty()) {
						amount_money.setText("合计￥"
								+ Integer.parseInt(str[index])
								* Double.parseDouble((Util
										.String2num(parking_type.getText()
												.toString()))));
						amount = String.valueOf(Integer.parseInt(str[index])
								* Double.parseDouble((Util
										.String2num(parking_type.getText()
												.toString()))));
					}

				}
			}

			return view;
		}

		@Override
		public int getItemsCount() {
			return str.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}
	}

	private void setData() {
		list = new ArrayList<MakingIncreaseBean>();
		adapter = new ParkingMonthlyDetailAdapter(this, list);
		girdview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(tv_carnum.getText())
				|| TextUtils.isEmpty(parking_type.getText())) {
			TipDialog.showDialog(ParkingMonthlyDetailActivity.this,
					R.string.tip, R.string.confirm, "请输入完整信息");
			return;
		}
		if (!CheckUtil.isCarNum(tv_carnum.getText().toString())) {
			TipDialog.showDialog(ParkingMonthlyDetailActivity.this,
					R.string.tip, R.string.confirm, R.string.car_num_tip);
			return;
		}
		TipDialog.showDialog(ParkingMonthlyDetailActivity.this,R.string.tip, R.string.confirm,
				mTvMind.getText().toString());
//		MakingIncreaseBean mb = new MakingIncreaseBean();
//		mb = list.get(position);
//		Intent intent = (new Intent(ParkingMonthlyDetailActivity.this,
//				ChooseParkingActivity.class));
//		intent.putExtra("userName", tv_company2.getText().toString());
//		intent.putExtra("phoneNumber", tv_company3.getText().toString());
//		intent.putExtra("carNumber",
//				CheckUtil.exChange(tv_carnum.getText().toString()));
//		intent.putExtra("parkingType", parking_type.getText().toString());
//		intent.putExtra("parkingSpaceType", parkingSpaceType);
//		intent.putExtra("mouthNumber", str[wheel.getCurrentItem()]);
//		intent.putExtra("amount", amount);
//		intent.putExtra("region", mb.getRegion());
//		intent.putExtra("url", mb.getUrl());
//		intent.putExtra("mid", mb.getMid());
//		intent.putExtra("parkingLotNo", mb.parkingLotNo);
//		intent.putExtra("parkingName", mb.parkingName);
//		startActivity(intent);

	}

	public void getOpenModelApi(){
		httpUtil.post(Constant.HOST_URL1 + Constant.Interface.PARKIN_ALLOW_RENT, new AbsHttpStringResponseListener(ParkingMonthlyDetailActivity.this) {
			@Override
			public void onSuccess(int i, String s) {
				ParkingAllowRentBean rentBean = (ParkingAllowRentBean) AbJsonUtil.fromJson(s,ParkingAllowRentBean.class);
				if (rentBean.rep_code.equals(Constant.HTTP_SUCCESS)){
						mTvMind.setText(rentBean.body.tips);
				}
			}
		});
	}
	private void getCarport() {
		// TODO Auto-generated method stub
		httpInstance
				.post(Constant.HOST_URL
						+ Constant.Interface.MobiMakingIncreaseAction_queryMakingIncreaseList,
						params, new AbsHttpResponseListener() {
							@Override
							public void onSuccess(int arg0, String arg1) {
								JSONObject json;
								try {
									json = new JSONObject(arg1);
									String repCode = json.optString("repCode");
									if (repCode.equals(Constant.HTTP_SUCCESS)) {
										JSONArray data = json
												.optJSONArray("data");
										for (int j = 0; j < data.length(); j++) {
											MakingIncreaseBean bb = new MakingIncreaseBean();
											JSONObject itemObj = data
													.optJSONObject(j);
											bb = (MakingIncreaseBean) ParseJSONTools
													.getInstance()
													.fromJsonToJava(
															itemObj,
															MakingIncreaseBean.class);
											list.add(bb);
										}
										adapter.notifyDataSetChanged();
									} else if (repCode.equals("777777")) {
										new PromptDialog.Builder(
												ParkingMonthlyDetailActivity.this)
												.setViewStyle(
														PromptDialog.VIEW_STYLE_NORMAL)
												.setMessage(
														json.optString("repMsg"))
												.setCancelable(false)
												.setButton1(
														"确定",
														new PromptDialog.OnClickListener() {

															@Override
															public void onClick(
																	Dialog dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub
																ParkingMonthlyDetailActivity.this
																		.finish();
																dialog.cancel();
															}
														}).show();

									} else {
										new PromptDialog.Builder(
												ParkingMonthlyDetailActivity.this)
												.setViewStyle(
														PromptDialog.VIEW_STYLE_NORMAL)
												.setMessage(
														json.optString("repMsg"))
												.setButton1(
														"确定",
														new PromptDialog.OnClickListener() {

															@Override
															public void onClick(
																	Dialog dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub
																dialog.cancel();
															}
														}).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

						});
	}

	private class PersonPickerRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals("android.guestcount.choice.action")) {
				String appointTime = intent.getStringExtra("personCount");
				parkingSpaceType = intent.getStringExtra("type");
				parking_type.setText(appointTime);
				amount_money.setText("合计￥"
						+ Integer.parseInt(str[wheel.getCurrentItem()])
						* Double.parseDouble((Util.String2num(parking_type
								.getText().toString()))));
				amount = String.valueOf(Integer.parseInt(str[wheel
						.getCurrentItem()])
						* Double.parseDouble((Util.String2num(parking_type
								.getText().toString()))));
			}
		}
	}
}

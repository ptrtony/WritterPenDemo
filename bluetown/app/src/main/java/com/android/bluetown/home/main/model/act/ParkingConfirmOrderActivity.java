package com.android.bluetown.home.main.model.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mywallet.activity.GesturePSWActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.CustomDigitalClock;

/**
 * @author hedi
 * @data: 2016年5月3日 下午5:32:58
 * @Description: 确认订单
 */
public class ParkingConfirmOrderActivity extends TitleBarActivity implements
		OnClickListener {
	private FinalDb db;
	private String poid;
	private String userId;
	private String garden;
	private String phoneNumber;
	private String orderNum;
	private SharePrefUtils prefUtils;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private TextView carNumber, parkingName ,parkingSpace, date, parkingType, amount,monthnum,parking_month_info;
	private Date time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_parking_confirm_order);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm:
			order();
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
		setTitleView("确认订单");
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		if(getIntent().getIntExtra("type", 0)==1){
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sf.parse(getIntent().getStringExtra("endTime")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.add(Calendar.MONTH,
					+Integer.parseInt((getIntent().getStringExtra("mouthNumber"))));
			c.add(Calendar.DATE,-1);
			time = c.getTime();
		}else{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH,
					+Integer.parseInt((getIntent().getStringExtra("mouthNumber"))));
			c.add(Calendar.DATE,-1);
			time = c.getTime();
		}		
		carNumber = (TextView) findViewById(R.id.carNumber);
		parkingSpace = (TextView) findViewById(R.id.parkingSpace);
		parkingName = (TextView) findViewById(R.id.parkingName);
		date = (TextView) findViewById(R.id.date);
		parkingType = (TextView) findViewById(R.id.parkingType);
		amount = (TextView) findViewById(R.id.amount);
		monthnum = (TextView) findViewById(R.id.monthnum);
		parking_month_info = (TextView) findViewById(R.id.parking_month_info);
		carNumber.setText(getIntent().getStringExtra("carNumber"));
		parkingSpace.setText(getIntent().getStringExtra("parkingSpace"));
		parkingName.setText(getIntent().getStringExtra("parkingName"));
		parkingType.setText(getIntent().getStringExtra("parkingType"));
		amount.setText("¥" + getIntent().getStringExtra("amount"));
		date.setText(sf.format(time));
		monthnum.setText(getIntent().getStringExtra("mouthNumber")+"个月");
		parking_month_info.setText(prefUtils.getString(SharePrefUtils.PARKING_MONTH_INFO, ""));
		findViewById(R.id.confirm).setOnClickListener(this);
		db = FinalDb.create(ParkingConfirmOrderActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				garden = user.getHotRegion();
				phoneNumber = user.getUsername();
			}
		}
	}

	private void order() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("garden", garden);
		params.put("parkingName", getIntent().getStringExtra("parkingName"));
		params.put("parkingNo", getIntent().getStringExtra("parkingLotNo"));
		params.put("region", getIntent().getStringExtra("region"));
		params.put("parkingSpace", getIntent().getStringExtra("parkingSpace"));
		params.put("parkingType", getIntent().getStringExtra("parkingType"));
		if(getIntent().getIntExtra("type", 0)==1){
			params.put("startTime", getIntent().getStringExtra("endTime"));
		}else{
			params.put("startTime", sf.format(new Date()));
		}		
		params.put("mouthNumber", getIntent().getStringExtra("mouthNumber"));
		params.put("carNumber", getIntent().getStringExtra("carNumber"));
		params.put("amonut", getIntent().getStringExtra("amount"));
		params.put("parkingId", getIntent().getStringExtra("mid"));
		params.put("url", getIntent().getStringExtra("url"));
		params.put("paymentType", "3");
		params.put("paymentTypeStr", "平台支付");
		params.put("userName", getIntent().getStringExtra("userName"));
		params.put("phoneNumber", phoneNumber);
		params.put("poid", poid);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ParkingOrderAction_add, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								JSONObject data = json.optJSONObject("data");
								poid=data.optString("poid");
								orderNum=data.optString("orderNum");
								gettoken(data.optString("poid"));
							} else {
								Toast.makeText(
										ParkingConfirmOrderActivity.this,
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

	private void gettoken(final String orderId) {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("communicationToken",
				prefUtils.getString(SharePrefUtils.TOKEN, ""));
		params.put("timeNum", "15");
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
								Intent intent = (new Intent(
										ParkingConfirmOrderActivity.this,
										ParkingPayOrderActivity.class));
								intent.putExtra("type", 1);
								intent.putExtra("userId", userId);
								intent.putExtra("ttoken",
										data.optString("token"));
								intent.putExtra("orderId", orderId);
								intent.putExtra("money", getIntent()
										.getStringExtra("amount"));
								intent.putExtra("url", getIntent()
										.getStringExtra("url"));
								intent.putExtra("mouthNumber", getIntent()
										.getStringExtra("mouthNumber"));
								intent.putExtra("phoneNumber", phoneNumber);
								intent.putExtra("orderNum", orderNum);
								if(getIntent().getIntExtra("type", 0)==1){
									intent.putExtra("oldOrderId", getIntent().getStringExtra("oldOrderId"));
								}								
								startActivity(intent);
							} else {
								Toast.makeText(
										ParkingConfirmOrderActivity.this,
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

}

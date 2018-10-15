package com.android.bluetown.home.main.model.act;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.GuestAppointDetails;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.result.GuestAppointDetailResult;
import com.android.bluetown.utils.CheckUtil;
import com.android.bluetown.utils.Constant;

/**
 * @author hedi
 * @data: 2016年6月25日 上午10:07:59
 * @Description: TODO<车位包月历史详情>
 */
public class ParkingMonthlySuccessActivity extends TitleBarActivity implements
		OnClickListener {
	private String mid;
	private TextView carNumber, parkingName, parkingSpace, date, monthnum,
			parkingType, amount, complete,orderNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_parking_monthly_success);
		BlueTownExitHelper.addActivity(this);
		initViews();
	}

	public void initTitle() {
		setBackImageView();
		setTitleView("停车包月详情");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	private void initViews() {
		carNumber = (TextView) findViewById(R.id.carNumber);
		parkingName = (TextView) findViewById(R.id.parkingName);
		parkingSpace = (TextView) findViewById(R.id.parkingSpace);
		date = (TextView) findViewById(R.id.date);
		monthnum = (TextView) findViewById(R.id.monthnum);
		parkingType = (TextView) findViewById(R.id.parkingType);
		amount = (TextView) findViewById(R.id.amount);
		complete = (TextView) findViewById(R.id.complete);
		orderNumber = (TextView)findViewById(R.id.orderNumber);
		complete.setOnClickListener(this);
		carNumber.setText(getIntent().getStringExtra("carNumber"));
		parkingName.setText(getIntent().getStringExtra("parkingName"));
		parkingSpace.setText(getIntent().getStringExtra("parkingSpace"));
		date.setText(getIntent().getStringExtra("date"));
		monthnum.setText(getIntent().getStringExtra("monthnum"));
		parkingType.setText(getIntent().getStringExtra("parkingType"));
		amount.setText("¥" + getIntent().getStringExtra("amount"));
		orderNumber.setText(getIntent().getStringExtra("orderNum"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ("2".equals(getIntent().getStringExtra("orderStatus"))) {
			try {
				Date d1 = df.parse(getIntent().getStringExtra("date"));
				Date d2 = new Date(System.currentTimeMillis());
				long diff = d1.getTime() - d2.getTime();
				if (diff >= 0) {
					complete.setVisibility(View.VISIBLE);
				}
				if(getIntent().getStringExtra("isXubao")!=null){
					if(getIntent().getStringExtra("isXubao").equals("0")){
						complete.setVisibility(View.VISIBLE);
					}else{
						complete.setVisibility(View.GONE);
					}
				}else{
					complete.setVisibility(View.GONE);
				}

			} catch (Exception e) {
			}

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	// private void getData() {
	// // TODO Auto-generated method stub
	// params.put("mid", mid);
	// httpInstance.post(Constant.HOST_URL
	// + Constant.Interface.GUEST_APPOINT_DETAILS, params,
	// new AbsHttpResponseListener() {
	// @Override
	// public void onSuccess(int i, String s) {
	// GuestAppointDetailResult result = (GuestAppointDetailResult) AbJsonUtil
	// .fromJson(s, GuestAppointDetailResult.class);
	// if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
	// dealResult(result);
	// }
	//
	// }
	//
	// private void dealResult(GuestAppointDetailResult result) {
	// // TODO Auto-generated method stub
	// GuestAppointDetails appointDetails = result.getData();
	//
	// }
	// });
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.complete:
			Intent intent = (new Intent(ParkingMonthlySuccessActivity.this,
					ContinueParkingActivity.class));
			intent.putExtra("userName", getIntent().getStringExtra("userName"));
			intent.putExtra("phoneNumber",
					getIntent().getStringExtra("phoneNumber"));
			intent.putExtra("carNumber", getIntent()
					.getStringExtra("carNumber"));
			intent.putExtra("parkingType",
					getIntent().getStringExtra("parkingType"));
			intent.putExtra("parkingSpace",
					getIntent().getStringExtra("parkingSpace"));
			intent.putExtra("mouthNumber",
					getIntent().getStringExtra("mouthNumber"));
			intent.putExtra("amount", getIntent().getStringExtra("amount"));
			intent.putExtra("region", getIntent().getStringExtra("region"));
			intent.putExtra("mid", getIntent().getStringExtra("mid"));
			intent.putExtra("parkingLotNo",
					getIntent().getStringExtra("parkingLotNo"));
			intent.putExtra("parkingName",
					getIntent().getStringExtra("parkingName"));
			intent.putExtra("endTime", getIntent().getStringExtra("date"));
			intent.putExtra("oldOrderId",
					getIntent().getStringExtra("oldOrderId"));
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}

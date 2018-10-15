package com.android.bluetown.home.main.model.act;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: GuestAppointmentSuccessActivity
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月20日 下午4:49:51
 * 
 */
public class GuestAppointmentSuccessActivity extends TitleBarActivity implements
		OnClickListener {
	private String mid;
	private TextView busNumber, visitorCount, mname, tell, welcomeTime, remark,
			complete, commitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_guestappointment_success);
		BlueTownExitHelper.addActivity(this);
		initViews();
		getData();
	}

	public void initTitle() {
		setBackImageView();
		setTitleView(R.string.guest_appoint_sucess);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

	private void initViews() {
		mid = getIntent().getStringExtra("mid");
		busNumber = (TextView) findViewById(R.id.tv_company1);
		visitorCount = (TextView) findViewById(R.id.tv_company2);
		mname = (TextView) findViewById(R.id.tv_company3);
		tell = (TextView) findViewById(R.id.tv_company4);
		welcomeTime = (TextView) findViewById(R.id.tv_company6);
		remark = (TextView) findViewById(R.id.tv_company5);
		commitTime = (TextView) findViewById(R.id.tv_company7);
		complete = (TextView) findViewById(R.id.complete);
		complete.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getData() {
		// TODO Auto-generated method stub
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.GUEST_APPOINT_DETAILS, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						GuestAppointDetailResult result = (GuestAppointDetailResult) AbJsonUtil
								.fromJson(s, GuestAppointDetailResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						}

					}

					private void dealResult(GuestAppointDetailResult result) {
						// TODO Auto-generated method stub
						GuestAppointDetails appointDetails = result.getData();
						busNumber.setText(appointDetails.getBusNumber());

						visitorCount.setText(appointDetails.getVisitorCount());
						mname.setText(appointDetails.getMname());
						tell.setText(appointDetails.getTell());
						welcomeTime.setText(appointDetails.getMakingTime());
						commitTime.setText(appointDetails.getCreateTime());
						String memo = appointDetails.getRemark();
						if (!TextUtils.isEmpty(memo)) {
							remark.setText(appointDetails.getRemark());
						} else {
							remark.setText("无");
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.complete:
			GuestAppointmentSuccessActivity.this.finish();
			break;

		default:
			break;
		}
	}
}

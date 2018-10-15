package com.android.bluetown.home.main.model.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
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
 * @ClassName: GuestAppointmentFailActivity
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月20日 下午4:49:12
 * 
 */
public class GuestAppointmentFailActivity extends TitleBarActivity {
	private String mid;
	private TextView busNumber, visitorCount, mname, tell, welcomeTime, remark,
			commitTime;
	private TextView failTip, appointFailReason;
	private LinearLayout apponitfailReasonLy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_guestappointment_fail);
		BlueTownExitHelper.addActivity(this);
		initViews();
		getData();
	}

	public void initTitle() {
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.guest_appoint_fail);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}
	private void initViews() {
		mid = getIntent().getStringExtra("mid");
		apponitfailReasonLy = (LinearLayout) findViewById(R.id.apponitfailReason);
		busNumber = (TextView) findViewById(R.id.tv_company1);
		visitorCount = (TextView) findViewById(R.id.tv_company2);
		mname = (TextView) findViewById(R.id.tv_company3);
		tell = (TextView) findViewById(R.id.tv_company4);
		welcomeTime = (TextView) findViewById(R.id.tv_company6);
		remark = (TextView) findViewById(R.id.tv_company5);
		failTip = (TextView) findViewById(R.id.failTip);
		commitTime = (TextView) findViewById(R.id.tv_company7);
		appointFailReason = (TextView) findViewById(R.id.appointFailReason);
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
						String orderType = appointDetails.getOrderType();
						// orderType 预约状态(0失败，1成功，2按时到达，3过期未到）
						if (orderType != null && !orderType.isEmpty()) {
							if (orderType.equals("0")) {
								apponitfailReasonLy.setVisibility(View.VISIBLE);
								failTip.setText(R.string.appoint_fail_title);
								appointFailReason
										.setText(R.string.appoint_fail_reason_content);
							} else {
								// 3过期未到
								failTip.setText(R.string.appoint_fail1);
								apponitfailReasonLy.setVisibility(View.GONE);
							}
						}
					}
				});
	}
}

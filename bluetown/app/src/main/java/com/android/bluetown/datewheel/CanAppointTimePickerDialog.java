package com.android.bluetown.datewheel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.bluetown.R;

/**
 * 访客预约可预约时间的Picker的对话框
 * 
 * @author admin
 * 
 */
public class CanAppointTimePickerDialog {
	private Context context;
	private View loadingView;
	private Dialog loading;
	private Button ok, cancel;
	private CanAppointTimePicker apponitPicker;
	private String appointTime;
	private String vDate;

	public CanAppointTimePickerDialog(Context context,String vDate) {
		super();
		this.context = context;
		loadingView = LayoutInflater.from(context).inflate(
				R.layout.can_appoint_choice_show, null);
		this.vDate = vDate;
		initData();
	}

	private void initData() {
		loading = new Dialog(context, R.style.loadingDialog);
		loading.setContentView(loadingView);
		loading.getWindow().setGravity(Gravity.FILL);
		loading.setCanceledOnTouchOutside(true);
		ok = (Button) loadingView.findViewById(R.id.ok);
		cancel = (Button) loadingView.findViewById(R.id.cancel);	
		apponitPicker = (CanAppointTimePicker) loadingView
				.findViewById(R.id.apponitPicker);	
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appointTime = apponitPicker.getdateCount();
				Intent intent = new Intent();
				intent.putExtra("appointTime", appointTime);
				intent.setAction("android.guesttime.choice.action");
				context.sendBroadcast(intent);
				loading.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();
			}
		});
	}

	public void show() {
		if (loading != null) {
			loading.show();
		}
	}

	public void close() {

		if (loading != null) {
			loading.dismiss();
		}
	}
}

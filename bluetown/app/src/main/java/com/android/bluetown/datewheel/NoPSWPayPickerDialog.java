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

public class NoPSWPayPickerDialog {
	private Context context;
	private View loadingView;
	private Dialog loading;
	private Button ok, cancel;
	private NoPSWPayPicker personPicker;
	private String personNum;

	public NoPSWPayPickerDialog(Context context) {
		super();
		this.context = context;
		loadingView = LayoutInflater.from(context).inflate(
				R.layout.no_psw_pay_show, null);
		initData();
	}

	private void initData() {
		loading = new Dialog(context, R.style.loadingDialog);
		loading.setContentView(loadingView);
		loading.getWindow().setGravity(Gravity.FILL);
		loading.setCanceledOnTouchOutside(true);
		ok = (Button) loadingView.findViewById(R.id.ok);
		cancel = (Button) loadingView.findViewById(R.id.cancel);
		personPicker = (NoPSWPayPicker) loadingView.findViewById(R.id.personpicker);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				personNum = personPicker.getPersonCount();
				Intent intent = new Intent();
				intent.putExtra("personCount", personNum);
				intent.setAction("android.guestcount.choice.action");
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

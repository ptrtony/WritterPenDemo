package com.android.bluetown.datewheel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.bluetown.R;

public class TimeChoiceDialog {

	private Context context;
	private View loadingView;
	private Dialog loading;
	private Button city_ok;
	private TimeShowPicker timeShowPicker;
	public String date;

	public TimeChoiceDialog(Context context) {
		super();
		this.context = context;
		loadingView = LayoutInflater.from(context).inflate(
				R.layout.time_choice_show, null);
		InitData();
	}

	private void InitData() {
		loading = new Dialog(context, R.style.loadingDialog);
		loading.setContentView(loadingView);
		loading.getWindow().setGravity(Gravity.FILL);
		loading.setCanceledOnTouchOutside(true);
		city_ok = (Button) loadingView.findViewById(R.id.city_ok);
		timeShowPicker = (TimeShowPicker) loadingView
				.findViewById(R.id.citypicker);
		city_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				date = timeShowPicker.getDate_string();
				loading.dismiss();
			}
		});
	}

	public void Show() {

		if (loading != null) {
			loading.show();
		}
	}

	public void Close() {

		if (loading != null) {
			loading.dismiss();
		}
	}
}

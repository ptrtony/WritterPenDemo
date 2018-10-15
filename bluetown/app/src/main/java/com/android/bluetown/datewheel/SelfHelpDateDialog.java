package com.android.bluetown.datewheel;

import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.android.bluetown.R;

public class SelfHelpDateDialog {

	private Context context;
	private View loadingView;
	private Dialog loading;
	private Button city_ok;
	private DatePicker timeShowPicker;
	public String date;

	public SelfHelpDateDialog(Context context) {
		super();
		this.context = context;
		loadingView = LayoutInflater.from(context).inflate(
				R.layout.self_help_service_date_show, null);
		InitData();
	}

	private void InitData() {
		loading = new Dialog(context, R.style.loadingDialog);
		loading.setContentView(loadingView);
		loading.getWindow().setGravity(Gravity.FILL);
		loading.setCanceledOnTouchOutside(true);
		city_ok = (Button) loadingView.findViewById(R.id.city_ok);
		timeShowPicker = (DatePicker) loadingView.findViewById(R.id.citypicker);
		setSevenMinDay();
		city_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				date = timeShowPicker.getYear() + "年"
						+ (timeShowPicker.getMonth()+1) + "月"
						+ timeShowPicker.getDayOfMonth() + "日";
				Intent intent = new Intent();
				intent.putExtra("date_string", date);
				intent.setAction("self_date_choice");
				context.sendBroadcast(intent);
				loading.dismiss();
			}
		});
	}

	/**
	 * 设置时间一个星期
	 */
	private void setSevenMinDay() {
		Calendar calendar1 = Calendar.getInstance(Locale.CHINA);
		long minMillis = calendar1.getTimeInMillis();
		// 设置最小时间不能大于等于当前时间
		timeShowPicker.setMinDate(minMillis - 1000);
		calendar1.add(Calendar.DATE, 7);
		long maxMillis = calendar1.getTimeInMillis();
		timeShowPicker.setMaxDate(maxMillis);
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

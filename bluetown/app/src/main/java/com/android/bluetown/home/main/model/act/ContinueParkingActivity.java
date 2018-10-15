package com.android.bluetown.home.main.model.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.utils.Utils;

/**
 * @author hedi
 * @data:  2016年6月25日 下午4:35:40 
 * @Description:  TODO<续包车位> 
 */
public class ContinueParkingActivity extends TitleBarActivity implements
		OnClickListener{
	private boolean wheelScrolled = false;
	private DayArrayAdapter dayAdapter;
	private AbstractWheel wheel;
	private String[] str;
	private String amount,mouthNumber;
	private TextView next,amount_money;
	private String endTime;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_continue_parking);
		BlueTownExitHelper.addActivity(this);
		initView();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.next:
			Intent intent = (new Intent(ContinueParkingActivity.this,
					ParkingConfirmOrderActivity.class));
			intent.putExtra("userName", getIntent().getStringExtra("userName"));
			intent.putExtra("amount", amount);
			intent.putExtra("carNumber", getIntent()
					.getStringExtra("carNumber"));
			intent.putExtra("parkingType",
					getIntent().getStringExtra("parkingType"));
			intent.putExtra("mouthNumber",mouthNumber);
			intent.putExtra("region", getIntent().getStringExtra("region"));
			intent.putExtra("parkingSpace",getIntent().getStringExtra("parkingSpace"));
			intent.putExtra("url", "");
			intent.putExtra("parkingLotNo", getIntent().getStringExtra("parkingLotNo"));
			intent.putExtra("parkingName", getIntent().getStringExtra("parkingName"));
			intent.putExtra("endTime", endTime);
			intent.putExtra("mid", getIntent().getStringExtra("mid"));
			intent.putExtra("oldOrderId", getIntent().getStringExtra("oldOrderId"));
			intent.putExtra("type", 1);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("车位续租");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initView() {
		wheel = (AbstractWheel) findViewById(R.id.people);
		str = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12", "15", "18", "24" };
		dayAdapter = new DayArrayAdapter(this, str);
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setViewAdapter(dayAdapter);
		next=(TextView)findViewById(R.id.next);
		amount_money=(TextView)findViewById(R.id.amount_money);		
		next.setOnClickListener(this);
		mouthNumber="1";
		amount=1*Double.parseDouble(Utils.String2num(getIntent().getStringExtra("parkingType")))+"";
		amount_money.setText("合计  ¥"+amount);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sf.parse(getIntent().getStringExtra("endTime")));
			c.add(Calendar.DATE,+1);
			Date time = c.getTime();
			endTime=sf.format(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					mouthNumber=str[index];
					amount=Double.parseDouble(str[index])*Double.parseDouble(Utils.String2num(getIntent().getStringExtra("parkingType")))+"";
					amount_money.setText("合计  ¥"+amount);
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

	


	
}

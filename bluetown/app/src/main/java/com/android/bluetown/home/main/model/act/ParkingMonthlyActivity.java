package com.android.bluetown.home.main.model.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * @author hedi
 * @data:  2016年5月3日 上午10:35:12 
 * @Description:  车位包月 
 */
public class ParkingMonthlyActivity extends TitleBarActivity implements
OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_parking_monthly);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.rightImageLayout:
			break;
		case R.id.buy_one:
			intent = new Intent(ParkingMonthlyActivity.this, ParkingMonthlyDetailActivity.class);
			intent.putExtra("type", 21);
			startActivityForResult(intent, 0);
			break;
		case R.id.buy_three:
			intent = new Intent(ParkingMonthlyActivity.this, ParkingMonthlyDetailActivity.class);
			intent.putExtra("type", 21);
			startActivityForResult(intent, 0);
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
	private void initView(){
		findViewById(R.id.buy_one).setOnClickListener(this);
		findViewById(R.id.buy_three).setOnClickListener(this);
	}

}

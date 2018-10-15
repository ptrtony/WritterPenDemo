package com.android.bluetown;

import android.os.Bundle;
import android.view.View;

import com.android.bluetown.app.BlueTownExitHelper;

/**
 * 
 * @ClassName: HomeActivity
 * @Description:TODO(主页中的我的Tab )
 * @author: shenyz
 * @date: 2015年7月21日 上午10:16:23
 * 
 */
public class RegisterProActivity extends TitleBarActivity implements View.OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_register_pro);
		findViewById(R.id.backlayout).setOnClickListener(this);
		BlueTownExitHelper.addActivity(this);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化界面
	 * @see com.bm.wisdomcity.base.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setTitleState();
		setTitleView(R.string.register_pro);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setTitleLayoutBg(R.color.title_bg_blue);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.backlayout:
				finish();
				break;
		}
	}
}

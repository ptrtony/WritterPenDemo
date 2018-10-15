package com.android.bluetown.home.hot.model.act;

import android.os.Bundle;
import android.view.View;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;

/**
 * 企业需求发布成功页面
 * 
 * @author shenyz
 * 
 */
public class DemanPublishSuccessActivity extends TitleBarActivity {
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.demand_publish);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setTitleLayoutBg(R.color.title_bg_blue);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_demand_publish_success);
	}

}

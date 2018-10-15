package com.android.bluetown.home.hot.model.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.WebViewActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * 
 * @ClassName: DiarySearchActivity
 * @Description:TODO(HomeActivity-日常查询)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:36:12
 * 
 */
public class DiarySearchActivity extends TitleBarActivity implements OnClickListener {
	private TextView mailCheck, trafficCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_diary_search);
		BlueTownExitHelper.addActivity(this);
		mailCheck = (TextView) findViewById(R.id.mailCheck);
		trafficCheck = (TextView) findViewById(R.id.trafficCheck);
		mailCheck.setOnClickListener(this);
		trafficCheck.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.diary_search);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setMainLayoutBg(R.drawable.app_pic_bg_1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.mailCheck:
			intent.putExtra("URL", "http://www.kuaidi100.com/");
			intent.putExtra("title", getString(R.string.mail_check));
			intent.setClass(DiarySearchActivity.this, WebViewActivity.class);
			startActivity(intent);
			break;
		case R.id.trafficCheck:
			intent.putExtra("URL", "http://wf.nbjj.gov.cn/");
			intent.putExtra("title", getString(R.string.traffic_check));
			intent.setClass(DiarySearchActivity.this, WebViewActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}

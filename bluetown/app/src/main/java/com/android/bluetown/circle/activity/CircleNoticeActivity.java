package com.android.bluetown.circle.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.bean.ZoneNotice;

public class CircleNoticeActivity extends TitleBarActivity {
	private WebView noticeContent;
	private TextView noticeTitle,noticeTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_park_notice);
		ZoneNotice zoneNotice=(ZoneNotice) getIntent().getParcelableExtra("zoneNotice");
		noticeContent=(WebView)findViewById(R.id.noticeContent);
		noticeTitle=(TextView)findViewById(R.id.noticeTitle);
		noticeTime=(TextView)findViewById(R.id.noticeTime);
		noticeTitle.setText(zoneNotice.getNoticeName());
		noticeTime.setText(zoneNotice.getCreateTime());
		noticeContent.loadDataWithBaseURL("", zoneNotice.getContent(), "text/html", "UTF-8", "");
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.park_notice);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}
	

}

package com.android.bluetown;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.view.SwitchView;

import android.os.Bundle;
import android.view.View;

public class MessageSetupActivity extends TitleBarActivity  {
	private SwitchView newfriend;
	private SwitchView receivecomment;
	private SwitchView activitynotice;
	private Set<Integer> i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_message_setup);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("消息设置");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		newfriend=(SwitchView)findViewById(R.id.switch_new_friend);
		receivecomment=(SwitchView)findViewById(R.id.switch_receive_comment);
		activitynotice=(SwitchView)findViewById(R.id.switch_activity_notice);
		
	}
	
}
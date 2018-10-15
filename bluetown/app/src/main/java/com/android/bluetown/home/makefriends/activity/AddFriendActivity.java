package com.android.bluetown.home.makefriends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.sortlistview.ClearEditText;

/**
 * 加好友
 * 
 * @author shenyz
 * 
 */
public class AddFriendActivity extends TitleBarActivity implements
		OnClickListener {
	private LinearLayout nearlyPersonLy;
	private ClearEditText mEditText;
	private Button mSearchBtn;
	private String condition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_add_friend);
		BlueTownExitHelper.addActivity(this);
		initUIView();

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.make_friends);
		setTitleLayoutBg(R.color.chat_tab_join_friend_color);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 
	 * @Title: initUIView
	 * @Description: TODO(初始化界面組件)
	 * @throws
	 */
	private void initUIView() {
		mEditText = (ClearEditText) findViewById(R.id.searchContent);
		mSearchBtn = (Button) findViewById(R.id.searchBtn);
		mSearchBtn.setBackgroundResource(R.drawable.chat_add_friend_btn_bg);
		nearlyPersonLy = (LinearLayout) findViewById(R.id.nearlyPersonLy);
		mSearchBtn.setOnClickListener(this);
		nearlyPersonLy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nearlyPersonLy:
			startActivity(new Intent(AddFriendActivity.this,
					NearlyPersonActivity.class));
			break;
		case R.id.searchBtn:
			condition = mEditText.getText().toString();
			Intent intent = new Intent();
			intent.putExtra("condition", condition);
			intent.setClass(AddFriendActivity.this, FriendSearchActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}

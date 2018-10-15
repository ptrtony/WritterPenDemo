package com.android.bluetown.surround;


import android.os.Bundle;
import android.view.View;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;

public class MyMessageActivity extends TitleBarActivity {

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.msg);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_my_msg);
		
	}

}

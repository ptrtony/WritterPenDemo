package com.android.bluetown.home.makefriends.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownExitHelper;

public class SubConversationListActivtiy extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.subconversationlist);
		BlueTownExitHelper.addActivity(this);
		((TextView) findViewById(R.id.conversationTitle)).setText("我的群组");
		((ImageView) findViewById(R.id.back))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SubConversationListActivtiy.this.finish();
					}
				});
	}

}

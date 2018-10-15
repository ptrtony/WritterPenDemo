package com.android.bluetown;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.bluetown.app.BlueTownExitHelper;

public class UseragreementActivity extends Activity {
         @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_useragreement);
        	BlueTownExitHelper.addActivity(this);
        	initTitle();
        }

		private void initTitle() {
			TextView textView = (TextView) findViewById(R.id.titleMiddle);
			textView.setText("用户协议");
			TextView textBack = (TextView) findViewById(R.id.titleLeft);
			textBack.setVisibility(View.VISIBLE);
			textBack.setText("");
			textBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			
		}
}

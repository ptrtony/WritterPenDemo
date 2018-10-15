package com.example.pageturning;

import com.example.pageturning.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	Intent intent = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						intent = new Intent(MainActivity.this,
								PageTurningSimpleTest.class);
						startActivity(intent);
					}
				});

		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						intent = new Intent(MainActivity.this,
								PageTurningBTest.class);
						startActivity(intent);
					}
				});
		
		findViewById(R.id.button3).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						intent = new Intent(MainActivity.this,
								PageTurningCompleteTest.class);
						startActivity(intent);
					}
				});
	}
}
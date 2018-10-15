package com.android.bluetown;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.bluetown.app.BlueTownExitHelper;

public class AboutsoftwareActivity extends Activity implements OnClickListener{
	private TextView tvLianxi;
	private static final int REQUEST_ASK_CALL_CODE = 0x111111;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_aboutsoftware);
		BlueTownExitHelper.addActivity(this);
		initTitle();
		initView();
	}

	private void initView() {
		tvLianxi=(TextView)findViewById(R.id.tvLianxi);
		findViewById(R.id.rl_call_service_hotline).setOnClickListener(this);
		findViewById(R.id.tv_useragreement).setOnClickListener(this);
		tvLianxi.setText("版本信息：V"+getVersion());
		
	}

	private void initTitle() {
		TextView textView = (TextView) findViewById(R.id.titleMiddle);
		textView.setText("关于软件");
		TextView textBack = (TextView) findViewById(R.id.titleLeft);
		textBack.setVisibility(View.VISIBLE);
		textBack.setText("");
		textBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	public String getVersion() {
		try {
			PackageManager manager = AboutsoftwareActivity.this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					 AboutsoftwareActivity.this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_useragreement:
				startActivity(new Intent(this, UseragreementActivity.class));
			break;
			case R.id.rl_call_service_hotline:
				onCall();
			break;
		default:
			break;
		}
		
	}

	private void onCall(){
		if (Build.VERSION.SDK_INT>=23){
			int checkPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
			if (checkPhonePermission!=PackageManager.PERMISSION_GRANTED){
				ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_ASK_CALL_CODE);
				return;
			}else{
				callPhone();
			}
		}else{
			callPhone();
		}
	}

	private void callPhone(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:"+"0574-87908050"));
		startActivity(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case REQUEST_ASK_CALL_CODE:
				if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
					callPhone();
				}else{
				}
				break;
			default:super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}

	}
}

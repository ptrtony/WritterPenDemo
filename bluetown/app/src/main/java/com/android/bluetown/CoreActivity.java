package com.android.bluetown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.fragment.CoreFragment;


public class CoreActivity extends TitleBarActivity {
	private CoreFragment core_fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_core);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		titleLayout.setVisibility(View.GONE);
		setBackImageView();
		setTitleView("扫一扫");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		core_fragment=new CoreFragment();
		showFragment(core_fragment);
	}

	
	public void showFragment(Fragment newFragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.tab_content, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}



	
}


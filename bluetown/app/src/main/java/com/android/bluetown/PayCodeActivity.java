package com.android.bluetown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.fragment.CoreFragment;
import com.android.bluetown.fragment.PayCodeFragment;

public class PayCodeActivity extends TitleBarActivity implements OnClickListener {
	private PayCodeFragment pf;
	private CoreFragment cf;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_paycode);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		titleLayout.setVisibility(View.GONE);
		setBackImageView();
		setTitleView("付款码");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		pf=new PayCodeFragment();		
		showFragment(pf);
		findViewById(R.id.paycode).setOnClickListener(this);
		findViewById(R.id.core).setOnClickListener(this);
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



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.paycode:
			showFragment(pf);
			break;
		case R.id.core:
			if(cf==null){
				cf=new CoreFragment();
				showFragment(cf);
			}else{
				showFragment(cf);
			}
			break;
		}
	}
	
}

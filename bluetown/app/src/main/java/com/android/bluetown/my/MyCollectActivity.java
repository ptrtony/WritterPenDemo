package com.android.bluetown.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.bluetown.CompanyInformationActivity;
import com.android.bluetown.GoodsActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.surround.CollectMerchantActivity;

/**
 * @author hedi
 * @data:  2016年4月29日 下午3:46:07 
 * @Description:  我的收藏 
 */
public class MyCollectActivity extends TitleBarActivity implements
OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_my_collect);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.company:
			startActivity(new Intent(this, CompanyInformationActivity.class));
			break;
		case R.id.canting:
			startActivity(new Intent(this, CollectMerchantActivity.class));
			break;
		case R.id.goods:
			startActivity(new Intent(this, GoodsActivity.class));
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("我的收藏");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		findViewById(R.id.canting).setOnClickListener(this);
		findViewById(R.id.company).setOnClickListener(this);
		findViewById(R.id.goods).setOnClickListener(this);
	}

}

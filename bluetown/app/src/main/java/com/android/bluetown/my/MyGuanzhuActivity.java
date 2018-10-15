package com.android.bluetown.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import com.android.bluetown.CompanyActivity;
import com.android.bluetown.MyPostActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * @author hedi
 * @data:  2016年4月29日 下午3:46:20 
 * @Description:  我的收藏 
 */
public class MyGuanzhuActivity extends TitleBarActivity implements
OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_my_guanzhu);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.company:
			startActivity(new Intent(this, CompanyActivity.class));
			break;
		case R.id.tiezi:
			startActivity(new Intent(this, MyPostActivity.class));
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("我的关注");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		findViewById(R.id.company).setOnClickListener(this);
		findViewById(R.id.tiezi).setOnClickListener(this);
	}

}

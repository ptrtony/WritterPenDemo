package com.android.bluetown.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.pref.SharePrefUtils;

/**
 * @author hedi
 * @data:  2016年5月28日 下午5:35:45 
 * @Description:  TODO<认证成功> 
 */
public class AuthenticationSuccessActivity extends TitleBarActivity implements
		OnClickListener {
	private TextView real_name,id_card,choose_garden,choose_company;
	private SharePrefUtils prefUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_authentication_success);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			startActivity(new Intent(AuthenticationSuccessActivity.this,
					AuthenticationActivity.class));
			AuthenticationSuccessActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("认证");
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView("重新认证");
		righTextLayout.setOnClickListener(this);
	}

	private void initView() {
		real_name=(TextView)findViewById(R.id.tv_company1);
		id_card=(TextView)findViewById(R.id.tv_company2);
		choose_garden=(TextView)findViewById(R.id.tv_company3);
		choose_company=(TextView)findViewById(R.id.tv_company4);
		real_name.setText(prefUtils.getString(SharePrefUtils.REALNAME, ""));
		id_card.setText(prefUtils.getString(SharePrefUtils.IDCARD, "").replaceAll("(\\d{3})\\d{12}(\\d{3})","$1****************$2"));
		choose_garden.setText(prefUtils.getString(SharePrefUtils.GARDEN, ""));
		choose_company.setText(prefUtils.getString(SharePrefUtils.COMPANYNAME, ""));
	}

}

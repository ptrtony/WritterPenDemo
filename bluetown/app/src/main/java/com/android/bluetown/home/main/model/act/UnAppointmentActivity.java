package com.android.bluetown.home.main.model.act;

import java.util.List;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.pref.SharePrefUtils;

/**
 * 
 * @ClassName: UnAppointmentActivity
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: shenyz
 * @date: 2015年8月20日 下午4:49:12
 * 
 */
public class UnAppointmentActivity extends TitleBarActivity implements 
		OnClickListener{
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_un_appoint);
		BlueTownExitHelper.addActivity(this);
		prefUtils=new SharePrefUtils(this);
		db = FinalDb.create(this);
	}

	public void initTitle() {
		setBackImageView();
		setTitleView(R.string.guest_order);
		setMainLayoutBg(R.color.title_bg_blue);
		setRightImageView(R.drawable.ic_history);
		rightImageLayout.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
	}
		if (!TextUtils.isEmpty(userId)) {
			startActivity(new Intent(UnAppointmentActivity.this,
					GuestAppointmentHistoryActivity.class));
		} else {
			TipDialog.showDialogNoClose(UnAppointmentActivity.this,
			     R.string.tip, R.string.confirm,	R.string.login_info_tip,
				 LoginActivity.class);
		}
	}
}

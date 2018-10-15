package com.android.bluetown.my;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.bluetown.HomeActivity;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.R.color;
import com.android.bluetown.R.id;
import com.android.bluetown.R.layout;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mywallet.activity.BalanceDetailActivity;
import com.android.bluetown.mywallet.activity.MyWalletSetPSWActivity;
import com.android.bluetown.mywallet.activity.TransferActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;


/**
 * @author hedi
 * @data:  2016-4-25 下午4:00:18 
 * @Description:  修改信息
 */
public class MyInfoChangeActivity extends TitleBarActivity implements OnClickListener{
	private EditText change;
	private ImageView delete;
	private FinalDb db;
	private String userId;
	private SharePrefUtils prefUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(layout.ac_myinfo_change);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initView();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(getIntent().getStringExtra("title"));
		setTitleLayoutBg(color.title_bg_blue);
		setRighTextView("完成");
		righTextLayout.setOnClickListener(this);
		
	}
	
	private void initView(){
		change=(EditText)findViewById(id.change);
		delete=(ImageView)findViewById(id.delete);
		delete.setOnClickListener(this);
		change.setText(getIntent().getStringExtra("content"));
		db = FinalDb.create(this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() > 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}

		}
		change.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					delete.setVisibility(View.GONE);
				} else {
					delete.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case id.rightTextLayout:
			if(!change.getText().toString().trim().isEmpty()){
				sendinfo();
			}else{
				TipDialog.showDialog(MyInfoChangeActivity.this, R.string.tip,
						R.string.confirm, "输入信息不能为空");
			}
			break;
		case id.delete:
			change.setText("");
			break;
		}
	}
	private void sendinfo() {
		// TODO Auto-generated method stub
		params.put("muid", userId);
		params.put("nickName", change.getText().toString());
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MobiMemberAction_updateSelfMessage, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								prefUtils.setString(SharePrefUtils.NICK_NAME, change.getText().toString());
								Intent resultIntent = MyInfoChangeActivity.this.getIntent();
						 		resultIntent.putExtra("nickname",change.getText().toString());
						 		MyInfoChangeActivity.this.setResult(Activity.RESULT_OK, resultIntent);
						 		MyInfoChangeActivity.this.finish();
								
							} else {
								Toast.makeText(MyInfoChangeActivity.this,
										json.optString("repMsg"),
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}
	

}

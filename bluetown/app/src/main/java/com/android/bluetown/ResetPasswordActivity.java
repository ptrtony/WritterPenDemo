package com.android.bluetown;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CanteenBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.my.SettingActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.MD5Util;
import com.android.bluetown.utils.ParseJSONTools;

public class ResetPasswordActivity extends TitleBarActivity implements
		OnClickListener {
	private EditText old_password, new_password,repeat_password;
	private TextView confirm, cant_confirm;
	private SharePrefUtils prefUtils;
	private FinalDb db;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_reset_password);
		BlueTownExitHelper.addActivity(this);
		prefUtils = new SharePrefUtils(this);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			Intent intent = new Intent();
			intent.putExtra("flag", getString(R.string.find_password));
			intent.setClass(ResetPasswordActivity.this,
					RegisterVerifyFindPwdActivty.class);
			startActivity(intent);
			break;
		case R.id.confirm:
			if(!new_password.getText().toString().equals(repeat_password.getText().toString())){
				showAlertDialog("确定","输入密码不一致，请重新输入");
				return;
			}
			String newPwd = new_password.getText().toString();
			if (newPwd.length()<6){
				showAlertDialog("确定","密码不能少于6位");
				return;
			}
			send(old_password.getText().toString(),new_password.getText().toString());
			break;
		default:
			break;
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("修改密码");
		setTitleLayoutBg(R.color.title_bg_blue);
		setRighTextView("忘记密码");
		righTextLayout.setOnClickListener(this);
	}
	public void showAlertDialog(String trimp,String message){
		new PromptDialog.Builder(ResetPasswordActivity.this)
				.setViewStyle(
						PromptDialog.VIEW_STYLE_NORMAL)
				.setMessage(message)
				.setButton1(
						trimp,
						new PromptDialog.OnClickListener() {

							@Override
							public void onClick(
									Dialog dialog,
									int which) {
								// TODO Auto-generated
								// method stub
								dialog.cancel();
								new_password.setText("");
								repeat_password.setText("");
							}
						}).show();
	}


	private void initView() {
		old_password = (EditText)findViewById(R.id.old_password);
		new_password = (EditText)findViewById(R.id.new_password);
		repeat_password = (EditText)findViewById(R.id.repeat_password);
		confirm = (TextView)findViewById(R.id.confirm);
		cant_confirm = (TextView)findViewById(R.id.cant_confirm);
		db = FinalDb.create(ResetPasswordActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		confirm.setOnClickListener(this);
		cant_confirm.setOnClickListener(this);
		old_password.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0 || new_password.getText().toString().equals("")
						|| new_password.getText().toString() == null
						|| repeat_password.getText().toString().equals("")
						|| repeat_password.getText().toString() == null) {
					confirm.setVisibility(View.GONE);
					cant_confirm.setVisibility(View.VISIBLE);
				} else {
					confirm.setVisibility(View.VISIBLE);
					cant_confirm.setVisibility(View.GONE);
				}
			}
		});
		new_password.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0 || old_password.getText().toString().equals("")
						|| old_password.getText().toString() == null
						|| repeat_password.getText().toString().equals("")
						|| repeat_password.getText().toString() == null) {
					confirm.setVisibility(View.GONE);
					cant_confirm.setVisibility(View.VISIBLE);
				} else {
					confirm.setVisibility(View.VISIBLE);
					cant_confirm.setVisibility(View.GONE);
				}
			}
		});
		repeat_password.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0 || old_password.getText().toString().equals("")
						|| old_password.getText().toString() == null
						|| new_password.getText().toString().equals("")
						|| new_password.getText().toString() == null) {
					confirm.setVisibility(View.GONE);
					cant_confirm.setVisibility(View.VISIBLE);
				} else {
					confirm.setVisibility(View.VISIBLE);
					cant_confirm.setVisibility(View.GONE);
				}
			}
		});
	}
	private void send(String oldpwd,String newPwd) {
		params.put("userId", userId);
		params.put("oldPwd", MD5Util.encoder(oldpwd));
		params.put("newPwd", MD5Util.encoder(newPwd));
		httpInstance.post(Constant.HOST_URL + Constant.Interface.MobiMemberAction_updatePassWord,
				params, new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								ResetPasswordActivity.this.finish();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					
				});

	}
}

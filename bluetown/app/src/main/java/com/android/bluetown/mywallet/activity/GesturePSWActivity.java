package com.android.bluetown.mywallet.activity;

import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.LockAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.GestureLockView;
import com.android.bluetown.view.GestureLockView.OnGestureFinishListener;





public class GesturePSWActivity extends TitleBarActivity  {
	private GridView gv_lock;//提示手势图片

	private TextView gv_textview;//提示语

	private GestureLockView gestureLockView;//手势密码锁

	private LockAdapter lockAdapter;

	private boolean  isSetting;//是否绘制

	private TranslateAnimation animation;

	private int limitNum=4;
	
	private FinalDb db;
	private String userId;
	private SharePrefUtils prefUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_gesture_psw);
		BlueTownExitHelper.addActivity(this);
		initView();
		addListener();
	}
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView("设置手势密码");
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	private void initView(){
		gv_lock=(GridView)findViewById(R.id.gv_lock);	
		lockAdapter = new LockAdapter(this);
		gv_lock.setAdapter(lockAdapter);
		gv_textview=(TextView)findViewById(R.id.gv_textview);
		gv_textview.setText("请绘制手势密码");
		gv_textview.setVisibility(View.VISIBLE);
		gv_textview.setTextColor(getResources().getColor(R.color.font_black));
		gestureLockView=(GestureLockView)findViewById(R.id.gestureLockView);	
		gestureLockView.setLimitNum(limitNum);
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(GesturePSWActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
	}
	private void addListener() {
		animation = new TranslateAnimation(-20, 20, 0, 0);
		animation.setDuration(50);
		animation.setRepeatCount(2);
		animation.setRepeatMode(Animation.REVERSE);
		gestureLockView.setOnGestureFinishListener(new OnGestureFinishListener() {
			@Override
			public void OnGestureFinish(boolean success, String key) {
				if(success){
					lockAdapter.setKey(key);
					if(!isSetting){
						gv_textview.setText("再次绘制");
						gestureLockView.setKey(key);
						isSetting=true;
					}else{
						gv_textview.setTextColor(getResources().getColor(R.color.font_black));
						gv_textview.setText("绘制成功");
						setpassword(key);
						
					}

				}else{
					if(key.length()>=limitNum)
						gv_textview.setText("绘制的密码与上次不一致！");
					else
						gv_textview.setText("绘制的个数不能低于"+limitNum+"个");
					gv_textview.startAnimation(animation);
					gv_textview.setTextColor(getResources().getColor(R.color.font_red));
				}

			}
		});

	}
	private void setpassword(final String passWord) {
		// TODO Auto-generated method stub
		params.put("uid", userId);
		params.put("handPassword", (passWord));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MobiMemberAction_handPassword, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						JSONObject json;
						try {
							json = new JSONObject(s);
							String repCode = json.optString("repCode");
							if (repCode.equals(Constant.HTTP_SUCCESS)) {
								prefUtils.setString(SharePrefUtils.HANDPASSWORD, (passWord));
								Toast.makeText(GesturePSWActivity.this,
										"设置成功", Toast.LENGTH_SHORT).show();
								Intent resultIntent = GesturePSWActivity.this.getIntent();
								GesturePSWActivity.this.setResult(Activity.RESULT_OK, resultIntent);
								GesturePSWActivity.this.finish();
							} else {
								Toast.makeText(GesturePSWActivity.this,
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

package com.android.bluetown.my;

import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.bluetown.AboutsoftwareActivity;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.ResetPasswordActivity;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mywallet.activity.GesturePSWActivity;
import com.android.bluetown.mywallet.activity.GesturePSWCheckActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.surround.OnlinePayActivity;
import com.android.bluetown.surround.OrderDetailActivity;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.ToastManager;
import com.android.bluetown.view.SwitchView;
import com.android.bluetown.view.SwitchView.OnStateChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends TitleBarActivity implements
        OnClickListener {
    private Switch gesture;
    private Switch switch_push;
    private SwitchView sound;
    private TextView change_gesture_psw;
    private FinalDb db;
    private SharePrefUtils prefUtils;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_setting);
        BlueTownExitHelper.addActivity(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.change_psw:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.change_gesture_psw:
                intent = (new Intent(this, GesturePSWCheckActivity.class));
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.about_app:
                startActivity(new Intent(this, AboutsoftwareActivity.class));
                break;
            case R.id.help:
//			startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.clear_cache:
                ToastManager.getInstance(SettingActivity.this).showText("清理成功");
                break;
            case R.id.logout:
                String userId = "";
                List<MemberUser> users = db.findAll(MemberUser.class);
                if (users != null && users.size() != 0) {
                    MemberUser user = users.get(0);
                    if (user != null) {
                        userId = user.getMemberId();
                    }
                }
                if (!TextUtils.isEmpty(userId)) {
                    // 删除登录保存的登录信息
                    db.deleteAll(MemberUser.class);
                    prefUtils.setString(SharePrefUtils.GARDEN, "");
                    prefUtils.setString(SharePrefUtils.GARDEN_ID, "");
                    prefUtils.setString(SharePrefUtils.TOKEN, "");
                    prefUtils.setString(SharePrefUtils.PAYPASSWORD, "");
                    prefUtils.setString(SharePrefUtils.HANDPASSWORD, "");
                    prefUtils.setString(SharePrefUtils.PASSWORD, "");
                    prefUtils.setString(SharePrefUtils.NOPASSWORDPAY_COUNT, "");
                    prefUtils.setString(SharePrefUtils.NOPASSWORDPAY, "");
                    prefUtils.setString(SharePrefUtils.COMPANYNAME, "");
                    prefUtils.setString(SharePrefUtils.REALNAME, "");
                    prefUtils.setString(SharePrefUtils.IDCARD, "");
                    prefUtils.setString(SharePrefUtils.PPID, "");
                    prefUtils.setString(SharePrefUtils.OOID, "");
                    prefUtils.setString(SharePrefUtils.CHECKSTATE, "");
                    prefUtils.setString(SharePrefUtils.STAMPIMG, "");
                    prefUtils.setString(SharePrefUtils.HEAD_IMG, "");
                    prefUtils.setString(SharePrefUtils.NICK_NAME, "");
                    prefUtils.setString(SharePrefUtils.CODE, "");
                    prefUtils.setString(SharePrefUtils.LL_ID, "");
                    prefUtils.setString(SharePrefUtils.KEY_LIST, "");
                    prefUtils.setString(SharePrefUtils.KEY_LIST_TIME, "");
                    prefUtils.setString(SharePrefUtils.RONG_TOKEN, "");
                    setDirectPush("");
                    // 好友验证消息未读消息的状态
                    BlueTownApp.isScanUnReadMsg = false;
                    // 好友验证消息未读消息数(交友)
                    BlueTownApp.unReadMsgCount = 0;
                    // 活动中心消息未读消息数
                    BlueTownApp.actionMsgCount = 0;
                    // 跳蚤市场消息未读消息数
                    BlueTownApp.fleaMarketMsgCount = 0;
                    // 设置用户退出了登录（退出状态）
                    prefUtils.setString(SharePrefUtils.USER_STATE, "1");
                    Intent refreshintent = new Intent(
                            "com.android.bm.refresh.new.msg.action");
                    sendBroadcast(refreshintent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(SettingActivity.this, LoginActivity.class);
                    // 退出登录 为游客身份
                    startActivity(intent);
                } else {
                    TipDialog.showDialogNoClose(SettingActivity.this, R.string.tip,
                            R.string.confirm, R.string.login_info_tip,
                            LoginActivity.class);
                }

                break;

        }
    }

    private void setDirectPush(final String userId) {
        JPushInterface.setAlias(getApplicationContext(), userId,
                new TagAliasCallback() {

                    @Override
                    public void gotResult(int arg0, String arg1,
                                          Set<String> arg2) {
                        // TODO Auto-generated method
                        // stub
                        // 写入标签名

                    }
                });
    }

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleView("设置");
        setTitleLayoutBg(R.color.title_bg_blue);
        rightImageLayout.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        prefUtils = new SharePrefUtils(this);
        gesture = findViewById(R.id.switch_gesture_psw);
        switch_push = findViewById(R.id.switch_push);
        sound = (SwitchView) findViewById(R.id.switch_sound);
        change_gesture_psw = (TextView) findViewById(R.id.change_gesture_psw);
        findViewById(R.id.change_psw).setOnClickListener(this);
        findViewById(R.id.change_gesture_psw).setOnClickListener(this);
        findViewById(R.id.about_app).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        findViewById(R.id.clear_cache).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        if (prefUtils.getBoolean(SharePrefUtils.IS_PUSH, true)) {
            switch_push.setChecked(true);
        } else {
            switch_push.setChecked(false);
        }
        if (prefUtils.getString(SharePrefUtils.HANDPASSWORD, "").equals("")
                || prefUtils.getString(SharePrefUtils.HANDPASSWORD, "") == null
                || prefUtils.getString(SharePrefUtils.HANDPASSWORD, "").equals(
                "null")) {
            gesture.setChecked(false);
            change_gesture_psw.setVisibility(View.GONE);
        } else {
            gesture.setChecked(true);
            change_gesture_psw.setVisibility(View.VISIBLE);
        }
        db = FinalDb.create(SettingActivity.this);
        List<MemberUser> users = db.findAll(MemberUser.class);
        if (users != null && users.size() != 0) {
            MemberUser user = users.get(0);
            if (user != null) {
                userId = user.getMemberId();
            }
        }

        gesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gesture.setChecked(false);
                    change_gesture_psw.setVisibility(View.GONE);
                    Intent intent = (new Intent(SettingActivity.this,
                            GesturePSWActivity.class));
                    startActivityForResult(intent, 2);
                } else {
                    gesture.setChecked(true);
                    change_gesture_psw.setVisibility(View.VISIBLE);
                    Intent intent = (new Intent(SettingActivity.this,
                            GesturePSWCheckActivity.class));
                    intent.putExtra("type", 2);
                    startActivityForResult(intent, 1);
                }
            }
        });

        switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch_push.setChecked(true);
                    prefUtils.setBoolean(SharePrefUtils.IS_PUSH, true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JPushInterface.resumePush(getApplicationContext());
                        }
                    }).start();
                } else {
                    switch_push.setChecked(false);
                    prefUtils.setBoolean(SharePrefUtils.IS_PUSH, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JPushInterface.stopPush(getApplicationContext());
                        }
                    }).start();

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
                                prefUtils.setString(
                                        SharePrefUtils.HANDPASSWORD, "");
                            } else {
                                Toast.makeText(SettingActivity.this,
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (Activity.RESULT_OK == resultCode) {
                    setpassword("");
                    gesture.setChecked(false);
                    change_gesture_psw.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (Activity.RESULT_OK == resultCode) {
                    gesture.setChecked(true);
                    change_gesture_psw.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }

    }

}
package com.android.bluetown;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.message.CSUpdateMessage;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.TokenBean;
import com.android.bluetown.custom.dialog.LoadingDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.result.RegisterResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DeviceUtil;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.view.ClearEditText;
import com.android.bluetown.wrapper.LoginWrapper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName: LoginActivity
 * @Description:TODO(用户登录页面)
 * @author: shenyz
 * @date: 2015年7月17日 上午11:58:47
 */
public class LoginActivity extends TitleBarActivity implements OnClickListener, TextWatcher {
//    public static final int TOKEN_ERROR = 0;
//    public static final int SUCCESS = 1;
//    public static final int ERROR = 2;
    /**
     * 账号和密码的输入框
     */
    private ClearEditText accounText;
    private EditText passwordText;
    /**
     * 登录、注册按钮
     */
    private Button loginBtn, registerBtn;
    /**
     * 忘记密码
     */
    private TextView forgetPwd;
    /**
     * 记住密码
     */
//    private CheckBox rememberPwd;
    /**
     * 清除密码
     */
//    private ImageView ivClear;
    /**
     * 显示密码
     */
    private ImageView ivInVisibelPass;

    /**
     * 判断密码是否显示或者隐藏
     */
    private boolean isVisibelPs;
    /**
     * SharePrefUtils对象
     */
    private SharePrefUtils sharePrefUtils;
//    private FinalDb db;
//    private double latitude = 0.0;
//    private double longitude = 0.0;
//    //输入手机号并获取焦点 显示清除按钮
//    private boolean isClear = true;
//    // 首页注册成功或企业用户注册正在审核跳转还是从其他界面跳转的标志
//    private String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_login);
        BlueTownExitHelper.addActivity(this);
        initViews();
        setLoginButtonClick();
        loginStatus();
    }

    /**
     * 被迫下线的操作
     */
    private void loginStatus() {
        Intent intent = getIntent();
        if (intent != null) {
            int type = intent.getIntExtra("type", 0);
            if (type == 3) {
                Intent intent1 = new Intent();
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setClass(LoginActivity.this, LoginStatusDialogActivity.class);
                intent1.putExtra("type", 3);
                startActivity(intent1);
            } else if (type == 2) {
                Intent intent1 = new Intent();
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setClass(LoginActivity.this, LoginStatusDialogActivity.class);
                intent1.putExtra("type", 2);
                startActivity(intent1);
            }else if (type==1){
                Intent intent1 = new Intent();
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setClass(LoginActivity.this, LoginStatusDialogActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * @throws
     * @Title: initViews
     * @Description: TODO(初始化界面并设置相应组件的点击事件)
     */
    private void initViews() {
//        try {
//            if (getIntent().getStringExtra("flag") != null) {
//                flag = getIntent().getStringExtra("flag");
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        sharePrefUtils = new SharePrefUtils(this);
//        db = FinalDb.create(this);
        accounText = findViewById(R.id.account);
        passwordText = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        registerBtn = (Button) findViewById(R.id.register);
        forgetPwd = (TextView) findViewById(R.id.forgetPwd);
//        ivClear = findViewById(R.id.iv_clear);
        ivInVisibelPass = findViewById(R.id.iv_invisible_password);
//        rememberPwd = (CheckBox) findViewById(R.id.rememberPwd);
        accounText.addTextChangedListener(this);
        passwordText.addTextChangedListener(this);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);
//        ivClear.setOnClickListener(this);
        ivInVisibelPass.setOnClickListener(this);
        int padding = DisplayUtils.dip2px(this, 20);
//        rememberPwd.setPadding(padding, 0, 0, 0);
        backImageLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().disconnect();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

        isVisibelPs = sharePrefUtils.getBoolean("isVisibelPs", false);
        if (isVisibelPs) {
            ivInVisibelPass.setImageDrawable(getDrawable(R.drawable.icon_visibility));
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ivInVisibelPass.setImageDrawable(getDrawable(R.drawable.icon_visibility_off));
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }


    }

    /**
     * @Title: initTitle
     * @Description: 初始化标题栏
     */
    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
//        setBackImageView();
//        setTitleView(R.string.login);
//        rightImageLayout.setVisibility(View.INVISIBLE);
//        setMainLayoutBg(R.drawable.app_pic_bg);
//        setBottomLine();
        setTitleState();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login:
                String account = accounText.getText().toString();
                String password = passwordText.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    TipDialog.showDialog(LoginActivity.this, R.string.tip,
                            R.string.confirm, R.string.account_empty);
                    return;
                }
                if (account.length() < 11) {
                    TipDialog.showDialog(LoginActivity.this, R.string.tip,
                            R.string.confirm, R.string.phone_error);
                    return;

                }
                if (!isTelPhoto(account)) {
                    TipDialog.showDialog(LoginActivity.this, R.string.tip,
                            R.string.confirm, R.string.account_format_error);
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    TipDialog.showDialog(LoginActivity.this, R.string.tip,
                            R.string.confirm, R.string.password_empty);
                    return;
                }

                if (!password.matches("[^\u4E00-\u9FA5]{6,16}")) {
                    TipDialog.showDialog(LoginActivity.this, R.string.tip,
                            R.string.confirm, R.string.password_format_error);
                    return;
                }
                LoginWrapper loginWrapper = new LoginWrapper(this);
                loginWrapper.login(account, password);
                break;
            case R.id.register:
                // 跳转用户注册界面
                intent.putExtra("flag", getString(R.string.register));
                intent.putExtra("userType", Constant.NORMAL_USER);
                intent.setClass(LoginActivity.this,
                        RegisterVerifyFindPwdActivty.class);
                startActivity(intent);
                break;
            case R.id.forgetPwd:
                // 跳转至找回密码页面
                intent.putExtra("flag", getString(R.string.find_password));
                intent.setClass(LoginActivity.this,
                        RegisterVerifyFindPwdActivty.class);
                startActivity(intent);
                break;

            case R.id.iv_invisible_password:
                if (isVisibelPs) {
                    sharePrefUtils.setBoolean("isVisibelPs", false);
                    isVisibelPs = false;
                    ivInVisibelPass.setImageDrawable(getDrawable(R.drawable.icon_visibility_off));
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    sharePrefUtils.setBoolean("isVisibelPs", true);
                    isVisibelPs = true;
                    ivInVisibelPass.setImageDrawable(getDrawable(R.drawable.icon_visibility));
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                if (!TextUtils.isEmpty(passwordText.getText().toString().intern())) {
                    passwordText.setSelection(passwordText.getText().toString().intern().length());
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param mobiles 电话号码
     * @return
     * @throws
     * @Title: isTelPhoto
     * @Description: TODO(判断电话号码是否有效)
     */
    private boolean isTelPhoto(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RongIM.getInstance().disconnect();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        setLoginButtonClick();

    }

    private void setLoginButtonClick() {
        if (!TextUtils.isEmpty(accounText.getText().toString()) && !TextUtils.isEmpty(passwordText.getText().toString())) {
            loginBtn.setBackground(getDrawable(R.drawable.bg_click_button));
            loginBtn.setClickable(true);
        } else {
            loginBtn.setBackground(getDrawable(R.drawable.bg_unclick_button));
            loginBtn.setClickable(false);
        }
    }


}

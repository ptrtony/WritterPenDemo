package com.android.bluetown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.RegisterResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.Log;
import com.android.bluetown.view.TimeButton;

/**
 * @ClassName: RegisterVerifyActivty
 * @Description:TODO(用户注册--手机验证页面或找回密码页面)
 * @author: shenyz
 * @date: 2015年7月23日 下午2:51:34
 */
public class RegisterVerifyFindPwdActivty extends TitleBarActivity implements
        OnClickListener, TextWatcher {
    private static final String TAG = "RegisterVerifyFindPwdActivty";
    /**
     * 手机号、短信验证码的EditText
     */
    private EditText mobileNumText, verifyText;
    /**
     * 发送验证码的TimeButton
     */
    private TimeButton sendVerifyCodeBtn;
    /**
     * 下一步 Button--由注册界面跳转的,确认Button--点击找回密码跳转的
     */
    private Button nextBtn;
    /** 是否同意注册协议的CheckBox */
//	private CheckBox registerPro;
    /**
     * 点击跳转的注册协议页面的TextView
     */
    private LinearLayout userPro;
    /**
     * 是注册手机验证页面还是找回密码页面
     */
    private String flag;
    /**
     * 用户类型
     */
    private int userType;
    /**
     * 找回密码页面还是注册页面 的title
     */
    private TextView title;
    /**
     * 注册接口还是找回密码接口
     */
    private String url;
    /**
     * 获取的手机验证码
     */
    private String telAuthCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_register_verify);
        BlueTownExitHelper.addActivity(this);
        initViews();
        initTitleContent();
        setNextButtonClick();
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.android.register.time");
        registerReceiver(receiver, filter);
    }

    /**
     * @throws
     * @Title: initViews
     * @Description: TODO(初始化界面组件并设置点击事件)
     */
    private void initViews() {
        mobileNumText = (EditText) findViewById(R.id.mobileNum);
        verifyText = (EditText) findViewById(R.id.verifyCode);
        sendVerifyCodeBtn = (TimeButton) findViewById(R.id.sengVerifyCode);
        title = findViewById(R.id.tv_title);
//		registerPro = (CheckBox) findViewById(R.id.registerPro);
        mobileNumText.addTextChangedListener(this);
        verifyText.addTextChangedListener(this);
        userPro = findViewById(R.id.userPro);
        findViewById(R.id.backlayout).setOnClickListener(this);
        nextBtn = (Button) findViewById(R.id.next);
        sendVerifyCodeBtn.setOnClickListener(this);
        sendVerifyCodeBtn.setIsOK(false);
        userPro.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    /**
     * @Title: initTitle
     * @Description: 初始化注册手机验证页面的标题栏
     */
    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
//		setBackImageView();
        setTitleState();
        rightImageLayout.setVisibility(View.INVISIBLE);
        setMainLayoutBg(R.drawable.app_pic_bg);
//		setBottomLine();
    }

    /**
     * @throws
     * @Title: initTitleContent
     * @Description: TODO(初始化标题栏的内容)
     */
    private void initTitleContent() {
        flag = getIntent().getStringExtra("flag");
        userType = getIntent().getIntExtra("userType", 0);
        if (flag.equals(getString(R.string.register))) {
            // 注册界面
//			registerPro.setVisibility(View.VISIBLE);
            title.setText("注册");
            userPro.setVisibility(View.VISIBLE);
            nextBtn.setText(R.string.next);
            sendVerifyCodeBtn.setTextAfter("s").setTextBefore("获取验证码")
                    .setLenght(60 * 1000);

        } else if (flag.equals(getString(R.string.find_password))) {
            // 忘记密码-找回密码界面
//			registerPro.setVisibility(View.GONE);
            userPro.setVisibility(View.GONE);
            nextBtn.setText(R.string.confirm);
            title.setText("忘记密码");
            sendVerifyCodeBtn.setTextAfter("s").setTextBefore("获取验证码")
                    .setLenght(60 * 1000);
        }
        setTitleView("");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.sengVerifyCode:
                getVerifyCode();
                break;
            case R.id.userPro:
//			if (registerPro.isChecked() == false) {
                Intent intent = new Intent(this, RegisterProActivity.class);
                // 注册协议
                startActivityForResult(intent, 11);
//			} else {
//				Intent intent = new Intent(this, RegisterProActivity.class);
//				startActivity(intent);
//			}
                break;
            case R.id.next:
                next();
                break;
            case R.id.backlayout:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * @throws
     * @Title: getVerifyCode
     * @Description: TODO(获取短信验证码)
     */
    private void getVerifyCode() {
        // TODO Auto-generated method stub
        mobileNumText.setEnabled(false);
        mobileNumText.setClickable(false);
        if (flag.equals(getString(R.string.register))) {
            url = Constant.HOST_URL + Constant.Interface.REGISTER_SEND_MSG;
        } else if (flag.equals(getString(R.string.find_password))) {
            url = Constant.HOST_URL + Constant.Interface.FORGOT_PWD_AUTH_CODE;
        }

        String telephotoNum = mobileNumText.getText().toString();
        if (TextUtils.isEmpty(telephotoNum)) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm, R.string.account_empty);
            sendVerifyCodeBtn.setIsOK(false);
            mobileNumText.setEnabled(true);
            mobileNumText.setClickable(true);
            return;
        }

        if (telephotoNum.length() < 11) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm, R.string.phone_error);
            sendVerifyCodeBtn.setIsOK(false);
            mobileNumText.setEnabled(true);
            mobileNumText.setClickable(true);
            return;

        }

        if (!isTelPhoto(telephotoNum)) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    SweetAlertDialog.ERROR_TYPE, R.string.error_phone);
            sendVerifyCodeBtn.setIsOK(false);
            mobileNumText.setEnabled(true);
            mobileNumText.setClickable(true);
            return;
        }

        if (checkLess(telephotoNum, 11, R.string.phone_error)) {
            // 注册和忘记密码-找回密码 (获取验证码)
            params.put("telphone", telephotoNum);
            // 用户注册获取验证码和找回密码获取验证码同一个接口
            httpInstance.post(url, params, new AbsHttpStringResponseListener(
                    this, null) {
                @Override
                public void onSuccess(int i, String s) {
                    RegisterResult result = (RegisterResult) AbJsonUtil
                            .fromJson(s, RegisterResult.class);
                    if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                        telAuthCode = result.getCheckCode();
                        Log.d(TAG, "验证码：" + telAuthCode);
                        sendVerifyCodeBtn.setIsOK(true);

                    } else {
                        TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                                R.string.tip, R.string.confirm,
                                result.getRepMsg());
                        sendVerifyCodeBtn.setIsOK(false);
                    }
                    mobileNumText.setEnabled(true);
                    mobileNumText.setClickable(true);
                }

                @Override
                public void onFailure(int i, String s, Throwable throwable) {
                    super.onFailure(i, s, throwable);
                    sendVerifyCodeBtn.setIsOK(false);
                    mobileNumText.setEnabled(true);
                    mobileNumText.setClickable(true);
                }
            });
        } else {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm,
                    R.string.account_format_error);
            sendVerifyCodeBtn.setIsOK(false);
            mobileNumText.setEnabled(true);
            mobileNumText.setClickable(true);
        }

    }

    /**
     * @param s     提示信息
     * @param num   字符最少长度
     * @param error 错误代码
     * @return boolean 返回类型
     * @throws
     * @Title: checkLess
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private boolean checkLess(String s, int num, int error) {
        if (s.length() < num) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm, error);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return void 返回类型
     * @throws
     * @Title: next
     * @Description: TODO(注册的下一级页面或找回密码的页面 文件定制)
     */
    private void next() {
        // TODO Auto-generated method stub
        String telephotoNum = mobileNumText.getText().toString();
        String telephotoAuthCode = verifyText.getText().toString();
        if (TextUtils.isEmpty(telephotoNum)) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm, R.string.account_empty);
            return;
        }

        if (telephotoNum.length() < 11) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm, R.string.phone_error);
            return;

        }
        if (!isTelPhoto(telephotoNum)) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm,
                    R.string.account_format_error);
            return;
        }
        // 验证码是否为空
        if (TextUtils.isEmpty(telephotoAuthCode)) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm,
                    R.string.empty_msg_verify_code);
            return;
        }
        // 获取的短信验证
        if (telAuthCode == null || telAuthCode.equals("")) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm,
                    R.string.error_msg_verify_code);
            return;
        }
        // 输入验证码是否正确
        if (!TextUtils.isEmpty(telAuthCode)
                && !telAuthCode.equals(telephotoAuthCode)) {
            TipDialog.showDialog(RegisterVerifyFindPwdActivty.this,
                    R.string.tip, R.string.confirm,
                    R.string.error_msg_verify_code);
            return;
        }
        // 注册协议已选中(注册界面)
//		if (registerPro.isChecked() == true) {
        Intent intent = new Intent();
        intent.putExtra("flag", flag);
        intent.putExtra("telnum", telephotoNum);
        intent.putExtra("userType", userType);
        intent.setClass(RegisterVerifyFindPwdActivty.this,
                InputPasswordActivity.class);
        startActivity(intent);

//		} else {
//			// 没有选中注册协议(注册协议显示--注册页面(注册页面--必须选中注册协议))
//			if (registerPro.getVisibility() == View.VISIBLE) {
//				Toast.makeText(RegisterVerifyFindPwdActivty.this, "查看注册协议",
//						Toast.LENGTH_SHORT).show();
//			} else if (registerPro.getVisibility() == View.GONE) {
//				// 找回密码界面
//				Intent intent = new Intent();
//				intent.putExtra("flag", flag);
//				intent.putExtra("telnum", telephotoNum);
//				intent.putExtra("userType", userType);
//				intent.setClass(RegisterVerifyFindPwdActivty.this,
//						InputPasswordActivity.class);
//				startActivity(intent);
//			}
//
//		}

    }

    /**
     * @param mobiles
     * @return
     * @throws
     * @Title: isTelPhoto
     * @Description: TODO(判断电话号码是否有效)
     */
    private boolean isTelPhoto(String mobiles) {
        Pattern p = Pattern
                .compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|17[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
//		registerPro.setChecked(true);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("action.android.register.time")) {
                long time = intent.getLongExtra("time", 0);
                if (time == 0) {
                    mobileNumText.setEnabled(true);
                    mobileNumText.setClickable(true);
                }
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    ;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setNextButtonClick();
    }

    private void setNextButtonClick() {
        if (!TextUtils.isEmpty(mobileNumText.getText().toString()) && !TextUtils.isEmpty(verifyText.getText().toString())) {
            nextBtn.setBackground(getDrawable(R.drawable.bg_click_button));
            nextBtn.setClickable(true);
        } else {
            nextBtn.setBackground(getDrawable(R.drawable.bg_unclick_button));
            nextBtn.setClickable(false);
        }
    }
}

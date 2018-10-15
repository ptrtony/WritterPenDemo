package com.android.bluetown;
import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.BlueAnthenResponse;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.wrapper.LoginWrapper;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.receiver.RongCloudEvent;
import com.android.bluetown.result.RegisterResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DeviceUtil;
import com.android.bluetown.view.ClearEditText;
import net.tsz.afinal.FinalDb;
import static com.android.bluetown.pref.SharePrefUtils.SELECT_USER_TYPE;
import static com.android.bluetown.utils.Constant.HOST_URL1;
import static com.android.bluetown.utils.Constant.Interface.BLUE_APP_AUTHRIENTY;
/**
 * 
 * @ClassName: RegisterActivity
 * @Description:TODO(输入密码--注册输入密码和找回密码输入密码)
 * @author: shenyz
 * @date: 2015年7月17日 上午11:59:42
 * 
 */

@SuppressWarnings("unused")
public class InputPasswordActivity extends TitleBarActivity implements
		OnClickListener, TextWatcher {
	public static final int TOKEN_ERROR = 0;
	public static final int SUCCESS = 1;
	public static final int ERROR = 2;
	private ClearEditText usernameText;
	/** 密码和确认密码的EditText以及昵称*/
	private EditText pwdText, confirmPwdText;
	/** 所属园区 */
//	private TextView gardenSelect;
	/** 确认Button或下一步按钮 */
	private Button confirmButton;
	/** 注册输入密码页面-显示性别的布局、所属园区 */
	private LinearLayout registerInputPwdLy;
	/** 性别RadioGroup */
	private RadioGroup sexGroup;
	/** RadioButton 男 RadioButton 女 */
	private RadioButton manButton, womenButton;
	/** 用户类型-企业用户、普通用户、商户 */
	private int userType;
	/** 注册设置密码或重置密码 */
	private String flag;
	/** 注册手机号 */
	private String telPhoneNum;
	/** 用户性别 */
	private String sex = "0";
	private ArrayList<String> gardenList;
	private GardenPickerRecevier recevier;
	/** 园区id */
	private String gardenId = "113110000020";
	/**用户昵称 */
	private ImageView ivSettingPassword,ivConfirmPassword;
	/**密码的显示和隐藏 重新输入的密码的显示和隐藏*/
	private boolean isPassword,isConfirmPassword;
	private LinearLayout mBack;
	/**
	 * 用户id
     */
	private String userId;
	private AbHttpUtil abHttpUtil;
	private SharePrefUtils prefUtils;
	private FinalDb db;
	private double latitude = 0.0;
	private double longitude = 0.0;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case TOKEN_ERROR:
					break;
				case SUCCESS:
					RongCloudEvent.getInstance().setOtherListener();
					FinalDb db = FinalDb.create(InputPasswordActivity.this);
					List<MemberUser> users = db.findAll(MemberUser.class);
					if (users != null && users.size() != 0) {
						MemberUser user = users.get(0);
						if (user != null) {
							String userId = user.getMemberId();
							String nickname = user.getNickName();
							String headImage = user.getHeadImg();
							// 将该用户保存到数据库
							List<FriendsBean> friendList = db
									.findAllByWhere(FriendsBean.class, " userId=\""
											+ userId + "\"");
							if (friendList.size() == 0) {
								FriendsBean friend = new FriendsBean();
								friend.setUserId(userId);
								friend.setHeadImg(headImage);
								friend.setNickName(nickname);
								db.save(friend);
							}
						}
					}
					Intent intent = new Intent();
					intent.setClass(InputPasswordActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
					break;
				case ERROR:
					break;
				default:
					break;
			}
		}

	};
	private SweetAlertDialog sweetAlertDialog1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_input_password);
		BlueTownExitHelper.addActivity(this);
		db = FinalDb.create(this);
		initViews();
		registerGardenRecevier();
		setConfirmButtomClick();
		prefUtils = new SharePrefUtils(this);

		if (params == null) {
			params = new AbRequestParams();
		}
		if (httpInstance == null) {
			httpInstance = com.android.bluetown.network.AbHttpUtil.getInstance(this);
			httpInstance.setEasySSLEnabled(true);
		}
	}

	/**
	 * 
	 * @Title: initViews
	 * @Description: TODO(初始化界面组件并设置组件的点击事件)
	 * @throws
	 */
	private void initViews() {
		recevier = new GardenPickerRecevier();
		gardenList = new ArrayList<String>();
		flag = getIntent().getStringExtra("flag");
		userType = getIntent().getIntExtra("userType", 0);
		telPhoneNum = getIntent().getStringExtra("telnum");
		pwdText = (EditText) findViewById(R.id.password);
		confirmPwdText = (EditText) findViewById(R.id.confirmPwd);

		usernameText = findViewById(R.id.username);
//		gardenSelect = (TextView) findViewById(R.id.gardenSelect);
		confirmButton = (Button) findViewById(R.id.confirm);
//		gardenLy = (LinearLayout) findViewById(R.id.gardenLy);
		registerInputPwdLy = (LinearLayout) findViewById(R.id.registerInputPwdLy);
		sexGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
		womenButton = (RadioButton) findViewById(R.id.women);
		manButton = (RadioButton) findViewById(R.id.man);
		ivSettingPassword = findViewById(R.id.iv_setting_password);
		ivConfirmPassword = findViewById(R.id.iv_confirmPwd);
		findViewById(R.id.backlayout).setOnClickListener(this);
		ivSettingPassword.setOnClickListener(this);
		ivConfirmPassword.setOnClickListener(this);
		confirmButton.setOnClickListener(this);
		womenButton.setOnClickListener(this);
		manButton.setOnClickListener(this);
//		gardenSelect.setOnClickListener(this);
		usernameText.addTextChangedListener(this);
		pwdText.addTextChangedListener(this);
		confirmPwdText.addTextChangedListener(this);
		if (flag.equals(getString(R.string.register))) {
			// 注册界面--输入密码
			registerInputPwdLy.setVisibility(View.VISIBLE);
			usernameText.setVisibility(View.VISIBLE);
			findViewById(R.id.view_divider_nick).setVisibility(View.VISIBLE);
//			setTitleView(R.string.input_password);

		} else if (flag.equals(getString(R.string.find_password))) {
			// 忘记密码-找回密码
			usernameText.setVisibility(View.INVISIBLE);
			registerInputPwdLy.setVisibility(View.INVISIBLE);
			findViewById(R.id.view_divider_nick).setVisibility(View.INVISIBLE);
//			gardenLy.setVisibility(View.GONE);
//			setTitleView(R.string.find_password);
		}
	}

	/**
	 * 注册设置园区的广播
	 */
	private void registerGardenRecevier() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.garden.choice.action");
		registerReceiver(recevier, filter);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
//	 * @see com.bm.wisdomcity.base.TitleBarActivity#initTitle()
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.man:
			manButton.setChecked(true);
			womenButton.setChecked(false);
			sex = "0";
			break;
		case R.id.women:
			manButton.setChecked(false);
			womenButton.setChecked(true);
			sex = "1";
			break;
//		case R.id.gardenSelect:
//			// 获取所在城市的园区列表并选择
//			GardenPickerDialog gardenDialog = new GardenPickerDialog(
//					InputPasswordActivity.this);
//			gardenDialog.show();
//			break;
		case R.id.confirm:
			String password = pwdText.getText().toString();
			String confirmPwd = confirmPwdText.getText().toString();
//			String belongPark = gardenSelect.getText().toString();
			String nickname = usernameText.getText().toString();
			if (TextUtils.isEmpty(password)) {
				TipDialog.showDialog(InputPasswordActivity.this, R.string.tip,
						R.string.confirm, R.string.password_empty);
				return;
			}
			if (password.length() < 6 || password.length() > 16) {
				TipDialog.showDialog(InputPasswordActivity.this, R.string.tip,
						R.string.confirm, R.string.less_password);
				return;
			}
			if (TextUtils.isEmpty(confirmPwd)) {
				TipDialog.showDialog(InputPasswordActivity.this, R.string.tip,
						R.string.confirm, R.string.empty_confirm_password);
				return;
			}

			if (flag.equals(getString(R.string.register))) {
//				if (TextUtils.isEmpty(belongPark)){
//					TipDialog.showDialog(InputPasswordActivity.this, R.string.tip,
//							R.string.confirm, R.string.select_garden);
//					return;
//				}
				if (TextUtils.isEmpty(nickname)){
					TipDialog.showDialog(InputPasswordActivity.this, R.string.tip,
							R.string.confirm, R.string.input_nickname);
					return;
				}
				if (!password.equals(confirmPwd)) {
					TipDialog.showDialog(InputPasswordActivity.this,
							R.string.tip, R.string.confirm,
							R.string.diff_password);
					return;
				} else {
					switch (userType) {
					case 1:
						Intent intent = new Intent();
						intent.putExtra("userType", userType);
						intent.putExtra("telphone", telPhoneNum);
						intent.putExtra("password", password);
						intent.putExtra("gardenId", "1");
						intent.putExtra("sex", sex);
//						intent.putExtra("username",nickname);
						intent.setClass(InputPasswordActivity.this,
								PerfectinfoActivity.class);
						startActivity(intent);
						break;
					case 2:
						register(password,nickname);
						break;
					case 3:

						break;
					default:
						break;
					}
				}
			} else if (flag.equals(getString(R.string.find_password))) {
				if (!password.equals(confirmPwd)) {
					TipDialog.showDialog(InputPasswordActivity.this,
							R.string.tip, R.string.confirm,
							R.string.diff_password);
					return;
				} else {
					// 忘记密码-找回密码
					findPassword(password);
				}

			}

			break;
			case R.id.iv_setting_password:
				if (isPassword){
					isPassword = false;
					ivSettingPassword.setImageDrawable(getDrawable(R.drawable.icon_visibility_off));
					pwdText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					isPassword = true;
					ivSettingPassword.setImageDrawable(getDrawable(R.drawable.icon_visibility));
					pwdText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				if (!TextUtils.isEmpty(pwdText.getText().toString().intern())){
					pwdText.setSelection(pwdText.getText().toString().intern().length());
				}
				break;
			case R.id.iv_confirmPwd:
				if (isConfirmPassword){
					isConfirmPassword = false;
					ivConfirmPassword.setImageDrawable(getDrawable(R.drawable.icon_visibility_off));
					confirmPwdText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					isConfirmPassword = true;
					ivConfirmPassword.setImageDrawable(getDrawable(R.drawable.icon_visibility));
					confirmPwdText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				if (!TextUtils.isEmpty(confirmPwdText.getText().toString().intern())){
					confirmPwdText.setSelection(confirmPwdText.getText().toString().intern().length());
				}
				break;
			case R.id.backlayout:
				finish();
				break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: register
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param password
	 * @throws
	 */
	private void register(final String password,String nickname) {
		// 接口判断(注册)
		params.put("type", userType + "");
		params.put("telphone", telPhoneNum);
		params.put("password", password);
		params.put("gardenId", gardenId);
		params.put("sex", sex);
		params.put("nickname",nickname);
		params.put("pttId", DeviceUtil.toMD5UniqueId(InputPasswordActivity.this));
		httpInstance.post(Constant.HOST_URL + Constant.Interface.REGISTER,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						RegisterResult result = (RegisterResult) AbJsonUtil
								.fromJson(s, RegisterResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							prefUtils.setBoolean("isLoging",true);
//							Intent intent = new Intent(InputPasswordActivity.this,SelectIdentityActivity.class);
							MemberUser user = new MemberUser();
							user.setMemberId(result.getMemberId());
							db.save(user);
//							Bundle bundle = new Bundle();
//							bundle.putString("username",telPhoneNum);
//							bundle.putString("password",password);
//							intent.putExtras(bundle);
//							startActivity(intent,bundle);
							verifyBlueUser(password);
						} else {
							TipDialog.showDialog(InputPasswordActivity.this,
									SweetAlertDialog.ERROR_TYPE,
									result.getRepMsg());
						}

					}
				});
	}

	private void findPassword(final String password) {
		params.put("telphone", telPhoneNum);
		params.put("password", password);
		// 用户注册获取验证码和找回密码获取验证码同一个接口
		httpInstance.post(Constant.HOST_URL + Constant.Interface.FORGOT_PWD,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// 重置密码
							startSuccessActivity(
									R.string.find_password_success,
									"modifypassword", password, telPhoneNum);
						} else {
							TipDialog.showDialog(InputPasswordActivity.this,
									SweetAlertDialog.ERROR_TYPE,
									result.getRepMsg());
						}

					}

				});
	}

	private void startSuccessActivity(int dialogContent, final String flag,
			final String password, final String username) {
		SweetAlertDialog dialog = new SweetAlertDialog(this);
		dialog.setTitleText(getString(R.string.tip));
		dialog.setConfirmText(getString(R.string.confirm));
		dialog.isShowCancelButton();
		dialog.setContentText(getString(dialogContent));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				// 密码修改成功后,注册成功之后，回到首页，需要是登录状态，而不是还要重新登录
				sweetAlertDialog.dismiss();
				sweetAlertDialog1 = sweetAlertDialog;
				LoginWrapper loginWrapper = new LoginWrapper(InputPasswordActivity.this);
				loginWrapper.login(telPhoneNum, password);
			}
		});
		dialog.show();
	}

	/**
	 * 
	 * @Title: checkLess
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @param s
	 *            提示信息
	 * @param num
	 *            字符最少长度
	 * @param error
	 *            错误代码
	 * @return boolean 返回类型
	 * @throws
	 */
	private boolean checkLess(String s, int num, int error) {
		if (s.length() < num) {
			TipDialog.showDialog(InputPasswordActivity.this, R.string.tip,
					R.string.confirm, error);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		setConfirmButtomClick();
	}

	private void setConfirmButtomClick() {
		if (flag.equals(getString(R.string.register))){
			if (!TextUtils.isEmpty(confirmPwdText.getText().toString().intern())&&!TextUtils.isEmpty(pwdText.getText().toString().intern())&&!TextUtils.isEmpty(usernameText.getText().toString().intern())){
				confirmButton.setBackground(getDrawable(R.drawable.bg_click_button));
				confirmButton.setClickable(true);
			}else{
				confirmButton.setBackground(getDrawable(R.drawable.bg_unclick_button));
				confirmButton.setClickable(false);
			}
		}else{
			if (!TextUtils.isEmpty(confirmPwdText.getText().toString().intern())&&!TextUtils.isEmpty(pwdText.getText().toString().intern())){
				confirmButton.setBackground(getDrawable(R.drawable.bg_click_button));
				confirmButton.setClickable(true);
			}else{
				confirmButton.setBackground(getDrawable(R.drawable.bg_unclick_button));
				confirmButton.setClickable(false);
			}
		}
	}

	private class GardenPickerRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.garden.choice.action")) {
				String garden = intent.getStringExtra("garden");
				gardenId = intent.getStringExtra("gardenId");
//				gardenSelect.setText(garden);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(recevier);
	}
	/**
	 * 验证是否为深蓝公寓用户
	 */
	private void verifyBlueUser(String password){
		Intent intent = new Intent(InputPasswordActivity.this,SelectIdentityActivity.class);
		Bundle bundle = new Bundle();
		params.put("param",telPhoneNum);
		httpInstance.post(HOST_URL1 + BLUE_APP_AUTHRIENTY, params, new AbsHttpStringResponseListener(InputPasswordActivity.this) {
			@Override
			public void onSuccess(int i, String s) {
				BlueAnthenResponse response = (BlueAnthenResponse) AbJsonUtil.fromJson(s,BlueAnthenResponse.class);
				if (response.rep_code.equals(Constant.HTTP_SUCCESS)&&response.result){
					prefUtils.setBoolean(SELECT_USER_TYPE, true);
					setTranslateIdentity(intent,bundle,password,true);
				}else{
					prefUtils.setBoolean(SELECT_USER_TYPE, false);
					setTranslateIdentity(intent,bundle,password,false);
				}
			}
			@Override
			public void onFailure(int i, String s, Throwable throwable) {
				super.onFailure(i, s, throwable);
				setTranslateIdentity(intent,bundle,password,false);
				prefUtils.setBoolean(SELECT_USER_TYPE, false);
			}
		});
	}

	//跳转到身份页面
	private void setTranslateIdentity(Intent intent,Bundle bundle,String password,boolean isIdentity){
		bundle.putBoolean("isBlueIdentity",isIdentity);
		bundle.putString("username",telPhoneNum);
		bundle.putString("password",password);
		intent.putExtras(bundle);
		startActivity(intent,bundle);
	}
}

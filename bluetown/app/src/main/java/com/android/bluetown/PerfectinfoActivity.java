package com.android.bluetown;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.RegisterResult;
import com.android.bluetown.utils.Constant;

/**
 * 
 * @ClassName: PerfectinfoActivity
 * @Description:TODO(用户注册-企业用户-完善资料)
 * @author: shenyz
 * @date: 2015年7月23日 下午2:12:28
 * 
 */
public class PerfectinfoActivity extends TitleBarActivity implements
		OnClickListener {
	public static final int TOKEN_ERROR = 0;
	public static final int SUCCESS = 1;
	public static final int ERROR = 2;
	/** 企业法人姓名、企业联系方式、营业执照编码、企业名称 EditText */
	private EditText companyLegalPersonName, companyLinkMethod,
			bussinessLicenceNum, companyName;
	/** 联系方式TextView */
	private TextView linkPhone;
	/** 提交Button */
	private Button commitBtn;

	private int userType;
	private String telphone;
	private String password;
	private String sex;
	private String gardenId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_perfect_info);
		BlueTownExitHelper.addActivity(this);
		initViews();
		initParams();
	}

	private void initParams() {
		userType = getIntent().getIntExtra("userType", 0);
		telphone = getIntent().getStringExtra("telphone");
		password = getIntent().getStringExtra("password");
		sex = getIntent().getStringExtra("sex");
		gardenId = getIntent().getStringExtra("gardenId");
	}

	/**
	 * 
	 * @Title: initViews
	 * @Description: TODO(初始化界面并设置按钮的点击事件)
	 * @throws
	 */
	private void initViews() {
		companyLegalPersonName = (EditText) findViewById(R.id.companyLegalPersonName);
		companyLinkMethod = (EditText) findViewById(R.id.companyLinkMethod);
		bussinessLicenceNum = (EditText) findViewById(R.id.businessLicenseNumber);
		companyName = (EditText) findViewById(R.id.companyName);
		linkPhone = (TextView) findViewById(R.id.linkPhone);
		commitBtn = (Button) findViewById(R.id.companyUserCommit);
		commitBtn.setOnClickListener(this);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.perfect_info);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setMainLayoutBg(R.drawable.app_pic_bg);
//		setBottomLine();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.companyUserCommit:
			String comLegalPersonName = companyLegalPersonName.getText()
					.toString();
			String comLinkMethod = companyLinkMethod.getText().toString();
			String busLicenceNum = bussinessLicenceNum.getText().toString();
			String comName = companyName.getText().toString();
			if (TextUtils.isEmpty(comLegalPersonName)) {
				TipDialog.showDialog(PerfectinfoActivity.this,
						SweetAlertDialog.ERROR_TYPE,
						R.string.empty_company_legal_person_name);
				return;
			}
			if (TextUtils.isEmpty(comLinkMethod)) {
				TipDialog.showDialog(PerfectinfoActivity.this,
						SweetAlertDialog.ERROR_TYPE,
						R.string.empty_company_link_method);
				return;
			}
			if (TextUtils.isEmpty(busLicenceNum)) {
				TipDialog.showDialog(PerfectinfoActivity.this,
						SweetAlertDialog.ERROR_TYPE,
						R.string.empty_business_license_number);
				return;
			}
			if (TextUtils.isEmpty(comName)) {
				TipDialog.showDialog(PerfectinfoActivity.this,
						SweetAlertDialog.ERROR_TYPE,
						R.string.empty_company_name);
				return;
			}
			commitApprovalInfo(comLegalPersonName, comLinkMethod,
					busLicenceNum, comName);
			break;

		default:
			break;
		}
	}

	private void commitApprovalInfo(String comLegalPersonName,
			String comLinkMethod, String busLicenceNum, String comName) {
		params.put("type", userType + "");
		params.put("telphone", telphone);
		params.put("password", password);
		params.put("gardenId", gardenId);
		params.put("sex", sex);
		params.put("businessName", comLegalPersonName);
		params.put("businessTell", comLinkMethod);
		params.put("businessNumber", busLicenceNum);
		params.put("companyName", comName);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.REGISTER,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						RegisterResult result = (RegisterResult) AbJsonUtil
								.fromJson(s, RegisterResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// 游客身份
							TipDialog.showDialogClearTop(
									PerfectinfoActivity.this, R.string.tip,
									R.string.confirm, R.string.commit_tip,
									MainActivity.class);
						} else {
							TipDialog.showDialog(PerfectinfoActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}

					}
				});
	}

}

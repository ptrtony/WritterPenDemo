package com.android.bluetown.home.hot.model.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.ProcedureApprovalHistoryActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ProcedureApprovalBean;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.Result;
import com.android.bluetown.spinner.custom.AbstractSpinerAdapter;
import com.android.bluetown.spinner.custom.SpinerPopWindow;
import com.android.bluetown.utils.Constant;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: ProcedureApprovalActivity
 * @Description:TODO(HomeActivity-园区内部手续报批)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:41:13
 * 
 */
public class ProcedureApprovalActivity extends TitleBarActivity implements
		OnClickListener {
	private TextView type, zoneTel, realEstateTel;
	private List<String> typesList;
	private SpinerPopWindow typePop;
	private ProcedureApprovalBean result;
	private EditText approvalContent;
	private String tid,gardenId;
	private EditText tvTitle;
	private Button bookBtn;
	private String userId;
	private int userType;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_procedure_approval);
		BlueTownExitHelper.addActivity(this);
		initView();
		initFaultType();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				userType = user.getUserType();
			}

		}
		if (!TextUtils.isEmpty(userId)) {
			bookBtn.setClickable(true);
			bookBtn.setBackgroundResource(R.drawable.blue_darker_btn_bg);
		} else {
			bookBtn.setClickable(false);
			bookBtn.setBackgroundResource(R.drawable.gray_btn_bg1);
			TipDialog.showDialogNoClose(ProcedureApprovalActivity.this,
					R.string.tip, R.string.confirm, R.string.login_info_tip,
					LoginActivity.class);
		}
	}
	private void showSpinWindow(SpinerPopWindow mFaultTypePop,
			TextView faultType2) {
		// TODO Auto-generated method stub
		mFaultTypePop.setWidth(faultType2.getWidth());
		mFaultTypePop.showAsDropDown(faultType2);
	}

	private void initFaultType() {
		typesList = new ArrayList<String>();
		params.put("gardenId", prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.PROCEDURE_APPROVAL_TYPE, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int statusCode, String content) {
						result = (ProcedureApprovalBean) AbJsonUtil.fromJson(
								content, ProcedureApprovalBean.class);
						for (int i = 0; i < result.getData().size(); i++) {
							typesList
									.add(result.getData().get(i).getTypeName());
						}
						typePop = new SpinerPopWindow(
								ProcedureApprovalActivity.this, type);
						typePop.refreshData(typesList, 0);
						typePop.setItemListener(new onSipnnerItemClick(
								typesList, type));
					}
				});

	}

	class onSipnnerItemClick implements
			AbstractSpinerAdapter.IOnItemSelectListener {
		private List<String> data;
		private TextView textView;

		public onSipnnerItemClick() {
			// TODO Auto-generated constructor stub
		}

		public onSipnnerItemClick(List<String> data, TextView textView) {
			// TODO Auto-generated constructor stub
			this.data = data;
			this.textView = textView;
		}

		@Override
		public void onItemClick(int pos) {
			// TODO Auto-generated method stub
			tid = result.getData().get(pos).getTid();
			setItemData(data, textView, pos);
		}

		private void setItemData(List<String> data, TextView textView, int pos) {
			if (pos >= 0 && pos <= data.size()) {
				String value = data.get(pos);
				textView.setText(value);
			}
		}
	}

	private void initView() {
		prefUtils = new SharePrefUtils(this);
		gardenId=prefUtils.getString(SharePrefUtils.GARDEN_ID, "");
		type = (TextView) findViewById(R.id.type);
		zoneTel = (TextView) findViewById(R.id.zoneTel);
		realEstateTel = (TextView) findViewById(R.id.realEstateTel);
		approvalContent = (EditText) findViewById(R.id.approvalContent);
		tvTitle = (EditText) findViewById(R.id.title1);
		type.setOnClickListener(this);
		bookBtn = (Button) findViewById(R.id.bookBtn);
		bookBtn.setOnClickListener(this);
		zoneTel.setOnClickListener(this);
		realEstateTel.setOnClickListener(this);
		db = FinalDb.create(this);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.zone_procedure_approval);
		setRightImageView(R.drawable.ic_history);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rightImageLayout:
		if (userType != 1) {
				TipDialog
						.showDialog(ProcedureApprovalActivity.this,
								R.string.tip, R.string.confirm,
								R.string.company_limite);
				return;
			}
			startActivity(new Intent(ProcedureApprovalActivity.this,
					ProcedureApprovalHistoryActivity.class));
			break;
		case R.id.zoneTel:
			if (!TextUtils.isEmpty(zoneTel.getText().toString())) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				// url:统一资源定位符
				// uri:统一资源标示符（更广）
				intent.setData(Uri.parse("tel:" + zoneTel.getText().toString()));
				// 开启系统拨号器
				startActivity(intent);
			}
			break;
		case R.id.realEstateTel:
			if (!TextUtils.isEmpty(realEstateTel.getText().toString())) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				// url:统一资源定位符
				// uri:统一资源标示符（更广）
				intent.setData(Uri.parse("tel:"
						+ realEstateTel.getText().toString()));
				// 开启系统拨号器
				startActivity(intent);
			}
			break;
		case R.id.type:
			showSpinWindow(typePop, type);
			break;
		case R.id.bookBtn:
		
				if (!TextUtils.isEmpty(userId)) {
				bookBtn.setClickable(true);
				bookBtn.setBackgroundResource(R.drawable.blue_darker_btn_bg);
				// 企业用户才有权限
				if (userType == 1) {
					String content1 = approvalContent.getText().toString()
							.trim();
					String title2 = tvTitle.getText().toString().trim();
					String typeId1 = tid;
					// 访问接口交互
					params.put("typeId", typeId1);
					params.put("userId", userId);
					params.put("title", title2);
					params.put("content", content1);
					httpInstance.post(Constant.HOST_URL
							+ Constant.Interface.PROCEDURE_APPROVAL, params,
							new AbsHttpStringResponseListener(this, null) {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									Result result1 = (Result) AbJsonUtil
											.fromJson(content, Result.class);
									if (result1.getRepCode().contains(
											Constant.HTTP_SUCCESS)) {
										TipDialog
												.showDialogStartNewActivity(
														ProcedureApprovalActivity.this,
														R.string.tip,
														R.string.confirm,
														R.string.book_success,
														ProcedureApprovalHistoryActivity.class);
									}
								}

							});
				} else {

					TipDialog.showDialog(ProcedureApprovalActivity.this,
							R.string.tip, R.string.confirm,
							R.string.company_limite);
				}
			
		}
			break;
		}

	}
}

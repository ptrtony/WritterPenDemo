package com.android.bluetown.home.main.model.act;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.GridViewImgAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.HistoryServiceDeatilsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.HistoryServiceDeatilsData;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.android.bluetown.view.NoScrollGridView;

/**
 * 
 * @ClassName: HistoryServiceDeatilsActivity
 * @Description:TODO(自助报修-历史维修报修详情)
 * @author: shenyz
 * @date: 2015年8月6日 下午1:01:13
 * 
 */
public class HistoryServiceDeatilsActivity extends TitleBarActivity implements
		OnClickListener {
	/** 报修时间和报修单的状态 */
	private TextView faultServiceDate, faultStatus;
	/** 报修故障类型 */
	private TextView faultType;
	/** 报修上门日期 */
	private TextView serviceDate;
	/** 报修上门时间 */
	private TextView serviceTime;
	/** 地址 */
	private TextView serviceAddress;
	/** 手机号码 */
	private TextView servicePhone;
	/** 报修故障说明 */
	private TextView faultExplain;
	/** 后台已经处理了该报修单 显示该TextView */
	private TextView faultImg;
	/** 提交了报修单，后台未处理了该报修单 显示该LinearLayout 问题图片 */
	private NoScrollGridView faultImgLy;
	/** 已处理显示时间轴的分割线 */
	private View faultImgDivide;
	/** 未处理显示时间轴的分割线 */
	private View faultImgDivide1;
	/** 反馈时间 */
	private TextView dealFeedbackDate;
	/** 处理评分RadioButton */
	private RadioButton one, two, three, four, five;
	/** 处理反馈和处理评分的RelativeLayout（后台已经处理了该报修单 显示这两个RelativeLayout，否则不显示） */
	private RelativeLayout dealFeedbackLy, handleScoreLy;
	/** 后台报修单已经处理 提交评分Button */
	private Button commit;
	private String rid;
	private String grade = "5";
	private String userId;
	private FinalDb db;
	
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_history_service_details1);
		BlueTownExitHelper.addActivity(this);
		rid = getIntent().getStringExtra("rid");
		initViews();
		getHistoryService();
	}

	/**
	 * 
	 * @Title: initViews
	 * @Description: TODO(初始化界面组件)
	 * @throws
	 */
	private void initViews() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		faultServiceDate = (TextView) findViewById(R.id.faultServiceDate);
		faultStatus = (TextView) findViewById(R.id.faultStatus);
		faultType = (TextView) findViewById(R.id.serviceType);
		serviceDate = (TextView) findViewById(R.id.serviceDate);
		serviceTime = (TextView) findViewById(R.id.serviceTime);
		serviceAddress = (TextView) findViewById(R.id.serviceAddress);
		servicePhone = (TextView) findViewById(R.id.servicePhone);
		faultExplain = (TextView) findViewById(R.id.faultExplain);
		faultImg = (TextView) findViewById(R.id.faultImg);
		faultImgLy = (NoScrollGridView) findViewById(R.id.faultImgLy);
		faultImgDivide = (View) findViewById(R.id.divide1);
		faultImgDivide1 = (View) findViewById(R.id.divide2);
		dealFeedbackDate = (TextView) findViewById(R.id.dealFeedbackDate);
		one = (RadioButton) findViewById(R.id.one);
		two = (RadioButton) findViewById(R.id.two);
		three = (RadioButton) findViewById(R.id.three);
		four = (RadioButton) findViewById(R.id.four);
		five = (RadioButton) findViewById(R.id.five);
		dealFeedbackLy = (RelativeLayout) findViewById(R.id.dealFeedbackLy);
		handleScoreLy = (RelativeLayout) findViewById(R.id.handleScoreLy);
		commit = (Button) findViewById(R.id.dealCommit);
		commit.setOnClickListener(this);
		int padding = DisplayUtils.dip2px(this, 20);
		one.setPadding(padding, 0, 0, 0);
		two.setPadding(padding, 0, 0, 0);
		three.setPadding(padding, 0, 0, 0);
		four.setPadding(padding, 0, 0, 0);
		five.setPadding(padding, 0, 0, 0);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.history_self_service_details);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dealCommit:
			if (one.isChecked() || two.isChecked() || three.isChecked()
					|| four.isChecked() || five.isChecked()) {
				repairgrade();
			} else {
				TipDialog.showDialog(HistoryServiceDeatilsActivity.this,
						SweetAlertDialog.ERROR_TYPE, R.string.un_grade);
			}
			break;
		case R.id.one:
			one.setChecked(true);
			two.setChecked(false);
			three.setChecked(false);
			four.setChecked(false);
			five.setChecked(false);
			grade = "1";
			break;
		case R.id.two:
			one.setChecked(false);
			two.setChecked(true);
			three.setChecked(false);
			four.setChecked(false);
			five.setChecked(false);
			grade = "2";
			break;
		case R.id.three:
			one.setChecked(false);
			two.setChecked(false);
			three.setChecked(true);
			four.setChecked(false);
			five.setChecked(false);
			grade = "3";
			break;
		case R.id.four:
			one.setChecked(false);
			two.setChecked(false);
			three.setChecked(false);
			four.setChecked(true);
			five.setChecked(false);
			grade = "4";
			break;
		case R.id.five:
			one.setChecked(false);
			two.setChecked(false);
			three.setChecked(false);
			four.setChecked(false);
			five.setChecked(true);
			grade = "5";
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	private void getHistoryService() {
		params.put("userId",userId);
		params.put("rid", rid);

		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.GETREPAIRS_DETAIL, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						HistoryServiceDeatilsData result = (HistoryServiceDeatilsData) AbJsonUtil
								.fromJson(s, HistoryServiceDeatilsData.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							// state:报修状态(0:处理中，1：已处理)
							HistoryServiceDeatilsBean details = result
									.getData();
							String state = details.getState();
							// 根据grade判断用户是否已经评分
							String gradeFlag = details.getGrade();
							List<String> pictruesList = details
									.getPictruesList();
							if (state.equals("0")) {
								faultStatus.setText(R.string.deal_with);
								faultStatus.setTextColor(getResources()
										.getColor(R.color.font_orange));
								dealFeedbackLy.setVisibility(View.GONE);
								handleScoreLy.setVisibility(View.GONE);
								commit.setVisibility(View.GONE);
								faultImg.setVisibility(View.GONE);
								faultImgLy.setVisibility(View.VISIBLE);
								faultImgDivide.setVisibility(View.GONE);
								faultImgDivide1.setVisibility(View.GONE);
								if (pictruesList != null
										&& pictruesList.size() > 0) {
									// 最多上传5张故障图片
									GridViewImgAdapter adapter = new GridViewImgAdapter(
											context, pictruesList);
									faultImgLy.setAdapter(adapter);
									faultImgLy.setSelector(new ColorDrawable(
											Color.TRANSPARENT));
									adapter.notifyDataSetChanged();
								}
								dealResult(details);

							} else if (state.equals("1")) {
								faultStatus.setText(R.string.dealed);
								faultStatus.setTextColor(getResources()
										.getColor(R.color.font_gray));
								dealFeedbackLy.setVisibility(View.VISIBLE);
								if (!TextUtils.isEmpty(gradeFlag)) {
									handleScoreLy.setVisibility(View.GONE);
									commit.setVisibility(View.GONE);
								} else {
									handleScoreLy.setVisibility(View.VISIBLE);
									commit.setVisibility(View.VISIBLE);
								}
								if (pictruesList.size() > 0) {
									faultImg.setText(R.string.unloaded);
								} else {
									faultImg.setText(R.string.un_unload);
								}
								faultImg.setVisibility(View.VISIBLE);
								faultImgLy.setVisibility(View.GONE);
								faultImgDivide.setVisibility(View.VISIBLE);
								faultImgDivide1.setVisibility(View.VISIBLE);
								dealResult(details);

							}

						}
					}

				});
	}

	private void dealResult(HistoryServiceDeatilsBean details) {
		faultServiceDate.setText(details.getCreateTime());
		faultType.setText(details.getTypeName());
		serviceDate.setText(details.getTime());
		if (details.getSelection().equals("0")) {
			serviceTime.setText(R.string.self_service_day_all);
		} else if (details.getSelection().equals("1")) {
			serviceTime.setText(R.string.self_service_noon);
		} else if (details.getSelection().equals("2")) {
			serviceTime.setText(R.string.self_service_afternoon);
		}
		serviceAddress.setText(details.getAddress());
		servicePhone.setText(details.getTell());
		faultExplain.setText(details.getRemark());
		// 缺少处理反馈时间
		dealFeedbackDate.setText(details.getManageDate());
	}

	private void repairgrade() {
		params.put("rid", rid);
		params.put("grade", grade);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.REPAIRGRADE,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							TipDialog.showDialogStartNewActivity(
									HistoryServiceDeatilsActivity.this,
									R.string.tip, R.string.confirm,
									R.string.grade_success,
									SelfHelpServiceActivity.class);
						} else {
							TipDialog.showDialogFinishSelf(
									HistoryServiceDeatilsActivity.this,
									R.string.tip, R.string.confirm,
									R.string.grades_fail);
						}
					}
				});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		grade = null;
	}
}

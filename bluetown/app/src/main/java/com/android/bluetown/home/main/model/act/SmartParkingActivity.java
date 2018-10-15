package com.android.bluetown.home.main.model.act;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.SmartParkAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.RemindCarport;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.RemindCarportResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.ListViewForScrollView;

/**
 * 
 * @ClassName: SmartParkingActivity
 * @Description:TODO(HomeActivity-智能停车)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:33:08
 * 
 */
public class SmartParkingActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener {
	private AbPullToRefreshView mPullRefreshView;
	private ListViewForScrollView mListViewForScrollView;
	private SmartParkAdapter adapter;
	private List<RemindCarport> typesList;
	private String userId;
	private List<MemberUser> users;
	private FinalDb db;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	private SharePrefUtils prefUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_smart_parking);
		BlueTownExitHelper.addActivity(this);
		initView();
		getRemindCarport();
	}

	private void initView() {
		db = FinalDb.create(this);
		prefUtils = new SharePrefUtils(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View topview = inflater.inflate(R.layout.smart_parking_top, null);
		mPullRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);
		mListViewForScrollView = (ListViewForScrollView) this
				.findViewById(R.id.parkNoList);
		mListViewForScrollView.addHeaderView(topview);
		mListViewForScrollView.setAdapter(null);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setLoadMoreEnable(false);

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
			}
		}

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
		setTitleView("访客预约");
		setRightImageView(R.drawable.ic_history);
		rightImageLayout.setOnClickListener(this);
		setTitleLayoutBg(R.color.title_bg_blue);
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		if (typesList != null) {
			typesList.clear();
		}
		getRemindCarport();
	}

	private void getRemindCarport() {
		// TODO Auto-generated method stub
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.REMIND_CARPORT, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						RemindCarportResult result = (RemindCarportResult) AbJsonUtil
								.fromJson(arg1, RemindCarportResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							typesList = result.getData().getStallList();
							if (typesList != null && typesList.size() > 0) {
								adapter = new SmartParkAdapter(
										SmartParkingActivity.this, typesList);
								mListViewForScrollView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								mPullRefreshView.onHeaderRefreshFinish();
								mPullRefreshView.onFooterLoadFinish();
							}
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							mPullRefreshView.onHeaderRefreshFinish();
							mPullRefreshView.onFooterLoadFinish();
							Toast.makeText(SmartParkingActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						} else if (result.getRepCode().contains("777777")) {
							new PromptDialog.Builder(SmartParkingActivity.this)
									.setViewStyle(
											PromptDialog.VIEW_STYLE_NORMAL)
									.setMessage(result.getRepMsg())
									.setCancelable(false)
									.setButton1("确定",
											new PromptDialog.OnClickListener() {

												@Override
												public void onClick(
														Dialog dialog, int which) {
													// TODO
													// Auto-generated
													// method stub
													SmartParkingActivity.this
															.finish();
													dialog.cancel();
												}
											}).show();
						} else {
							mPullRefreshView.onHeaderRefreshFinish();
							mPullRefreshView.onFooterLoadFinish();
							TipDialog.showDialog(SmartParkingActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());

						}
					}

					@Override
					public void onFailure(int i, String s, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(i, s, throwable);
						TipDialog.showDialog(SmartParkingActivity.this,
								R.string.tip, R.string.confirm, "第三方道闸系统故障！");
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rightImageLayout:
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(SmartParkingActivity.this,
						GuestAppointmentHistoryActivity.class));
			} else {
				TipDialog.showDialogNoClose(SmartParkingActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}
			break;
		default:
			break;
		}

	}

}

package com.android.bluetown.home.hot.model.act;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.ImagePagerActivity;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CompanyInfoPublishBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.main.model.act.CompanyDetailsActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.slideview.SlidingPlayViewWithDot;
import com.android.bluetown.view.slideview.SlidingPlayViewWithDot.AbOnItemClickListener;

/**
 * 
 * @ClassName: DemandDetailsActivity
 * @Description:TODO(HomeActivity-企业发布信息--需求详情)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:34:18
 * 
 */
@SuppressLint("ResourceAsColor")
public class DemandDetailsActivity extends TitleBarActivity implements
		OnClickListener {
	private SlidingPlayViewWithDot slidePlayView;
	private TextView companyName, deadlineDate, demandContentTitleContent,
			demandContent;
	private Button collectDemand;
	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
	/** 存放轮播图的ListView */
	private ArrayList<String> slideList;
	/** 轮播图的原始图片资源 */
	private ArrayList<String> orgImgList;
	private CompanyInfoPublishBean publishBean;
	private int position;

	private Handler handler;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_info_public_detail);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		setSlidePlayView();
	}

	private void initUIView() {
		handler = BlueTownApp.getHanler();

		publishBean = (CompanyInfoPublishBean) getIntent()
				.getSerializableExtra("infodetails");
		position = getIntent().getIntExtra("position", 0);
		mInflater = LayoutInflater.from(this);
		slidePlayView = (SlidingPlayViewWithDot) findViewById(R.id.companyInfoPublishSlideView);
		companyName = (TextView) findViewById(R.id.companyName);
		deadlineDate = (TextView) findViewById(R.id.deadlineDate);
		demandContentTitleContent = (TextView) findViewById(R.id.demandContentTitleContent);
		demandContent = (TextView) findViewById(R.id.demandContent);
		collectDemand = (Button) findViewById(R.id.collectDemandBtn);
		collectDemand.setOnClickListener(this);
		companyName.setOnClickListener(this);
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
		setTitleView(R.string.demand_details);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 
	 * @Title: setSlidePlayView
	 * @Description: TODO(设置轮播图的数据)
	 * @throws
	 */
	private void setSlidePlayView() {
		/**
		 * 图片轮番
		 */
		slideList = (ArrayList<String>) publishBean.getPicturesList();
		// 点击轮播图放大的图片资源
		orgImgList = (ArrayList<String>) publishBean.getOrgImgList();
		if (slideList != null && slideList.size() > 0) {
			for (int i = 0; i < slideList.size(); i++) {
				View mPlayView = mInflater.inflate(R.layout.item_play_view,
						null);
				ImageView slideImg = (ImageView) mPlayView
						.findViewById(R.id.slideImg);
				TextView companyType = (TextView) mPlayView
						.findViewById(R.id.companyType);
				TextView companyName = (TextView) mPlayView
						.findViewById(R.id.companyName);
				TextView imgCount = (TextView) mPlayView
						.findViewById(R.id.imgCount);
				LinearLayout natitleLy = (LinearLayout) mPlayView
						.findViewById(R.id.natitleLy);
				natitleLy.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				natitleLy.setVisibility(View.INVISIBLE);
				int needType = Integer.parseInt(publishBean.getNeedType());
				switch (needType) {

				case 0:
					companyType.setText("#"
							+ getString(R.string.demand_recruit) + "#");
					break;
				case 1:
					companyType.setText("#" + getString(R.string.demand_study)
							+ "#");
					break;
				case 2:
					companyType.setText("#" + getString(R.string.demand_sale)
							+ "#");
					break;
				case 3:
					companyType.setText("#" + getString(R.string.demand_peo)
							+ "#");
					break;
				case 4:
					companyType.setText("#" + getString(R.string.demand_outing)
							+ "#");
					break;
				case 5:
					companyType.setText("#"
							+ getString(R.string.demand_meeting) + "#");
					break;
				case 6:
					companyType.setText("#"
							+ getString(R.string.demand_makefriends) + "#");
					break;
				case 7:
					companyType.setText("#" + getString(R.string.demand_others)
							+ "#");
					break;
				default:
					break;
				}
				companyName.setText(publishBean.getTitle());
				// imgCount.setText(i + 1 + "/" + slideList.size());
				finalBitmap.display(slideImg, slideList.get(i));
				slidePlayView.addView(mPlayView);
			}

			slidePlayView.startPlay();
		}
		companyName.setText(publishBean.getCompanyName());
		deadlineDate.setText(publishBean.getEndTime());
		demandContentTitleContent.setText(publishBean.getTitle());
		demandContent.setText(publishBean.getContent());
		String isCollect = publishBean.getIsCollect();
		if (!TextUtils.isEmpty(isCollect)) {
			// 0未收藏 1已收藏 actionType=6
			if (isCollect.equals("0")) {
				collectDemand.setText(getString(R.string.collect_demand));
			} else {
				collectDemand.setText(getString(R.string.collected));
			}
		}
		slidePlayView.setOnItemClickListener(new AbOnItemClickListener() {

			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DemandDetailsActivity.this,
						ImagePagerActivity.class);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, orgImgList);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
				startActivity(intent);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.collectDemandBtn:
			collectCompany();
			break;
		case R.id.companyName:
			Intent intent = new Intent();
			intent.setClass(DemandDetailsActivity.this,
					CompanyDetailsActivity.class);
			intent.putExtra("bid", publishBean.getBid());
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void collectCompany() {
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */

		if (TextUtils.isEmpty(userId)) {
			TipDialog.showDialogNoClose(DemandDetailsActivity.this,
					R.string.tip, R.string.confirm, R.string.login_info_tip,
					LoginActivity.class);
		} else {
			params.put("userId", userId);
			params.put("actionId", publishBean.getNid());
			params.put("actionType", Constant.COLLOCT + "");
			httpInstance.post(Constant.HOST_URL
					+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
					new AbsHttpStringResponseListener(this, null) {
						@Override
						public void onSuccess(int i, String s) {
							UserOperationResult result = (UserOperationResult) AbJsonUtil
									.fromJson(s, UserOperationResult.class);
							if (result.getRepCode().contains(
									Constant.HTTP_SUCCESS)) {
								Message message = handler.obtainMessage();
								if (result.getData().equals("关注成功")) {
									collectDemand
											.setText(getString(R.string.collected));
									publishBean.setIsCollect("1");
									message.arg1 = position;
									message.what = CompanyInfoPublishBean.DEMAND_COLLECT;
									handler.sendMessage(message);
								} else {
									collectDemand
											.setText(getString(R.string.collect_demand));
									publishBean.setIsCollect("0");
									message.arg1 = position;
									message.what = CompanyInfoPublishBean.CANCEL_DEMAND_COLLECT;
									handler.sendMessage(message);
								}
							} else {
								TipDialog.showDialog(context, R.string.tip,
										R.string.confirm, result.getRepMsg());
							}

						}
					});

		}

	}

}

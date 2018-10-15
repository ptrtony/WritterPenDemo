package com.android.bluetown.home.main.model.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.tsz.afinal.FinalDb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.ActionCenterItemBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.TakePartPerson;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.ActionCenterDetailsResult;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;

//活动详情
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ActionCenterDetailsActivity extends TitleBarActivity implements
		OnClickListener {
	private String data;
	private TextView tv_name, tv_phone, tv_phone1, tv_phone3, tv_useragreement,
			tv_useragreement1, tv_useragreement5, tv_useragreement6,
			personCount;
	private WebView eventDetails;
	private LinearLayout takePartPersonLayout, actionDetailsLy;
	private Button applyBtn;
	private List<TakePartPerson> list;
	private String aid;
	private String startTime, endTime;
	private FinalDb db;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_action_center_details);
		BlueTownExitHelper.addActivity(this);
		initView();
	}

	private void initView() {

		aid = getIntent().getStringExtra("aid");
		db = FinalDb.create(this);
		applyBtn = (Button) findViewById(R.id.btn_apply);
		applyBtn.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone1 = (TextView) findViewById(R.id.tv_phone1);
		tv_phone3 = (TextView) findViewById(R.id.tv_phone3);
		tv_useragreement = (TextView) findViewById(R.id.tv_useragreement);
		tv_useragreement1 = (TextView) findViewById(R.id.tv_useragreement1);
		tv_useragreement5 = (TextView) findViewById(R.id.tv_useragreement5);
		tv_useragreement6 = (TextView) findViewById(R.id.tv_useragreement6);
		personCount = (TextView) findViewById(R.id.personCount);
		takePartPersonLayout = (LinearLayout) findViewById(R.id.takePartPersonLy);
		actionDetailsLy = (LinearLayout) findViewById(R.id.actionDetailsLy);
		eventDetails = (WebView) findViewById(R.id.eventDetail);

	}

	private void getData(String userId) {
		// aid:活动id userId
		params.put("aid", aid);
		params.put("userId", userId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.ACTION_CENTER_DETAILS, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						ActionCenterDetailsResult result = (ActionCenterDetailsResult) AbJsonUtil
								.fromJson(s, ActionCenterDetailsResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(ActionCenterDetailsActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	private void dealResult(ActionCenterDetailsResult result) {
		ActionCenterItemBean centerItem = result.getData()
				.getActionCenterInfo();
		tv_name.setText(centerItem.getActionName());
		startTime = centerItem.getStartTime();
		endTime = centerItem.getEndTime();
		tv_phone.setText(startTime);
		tv_phone1.setText(endTime);
		tv_phone3.setText("地址：" + centerItem.getActionAddress());
		tv_useragreement.setText("人数：" + centerItem.getNumber());
		tv_useragreement1.setText("组织方：" + centerItem.getOrganisers());
		tv_useragreement5.setText("联系人：" + centerItem.getContact());
		tv_useragreement6.setText("联系电话：" + centerItem.getTell());
		setActionDetailsContent(centerItem);
		list = result.getData().getUsers().getRows();
		for (int i = 0; i < list.size(); i++) {
			final TakePartPerson person = list.get(i);
			View view = LayoutInflater.from(ActionCenterDetailsActivity.this)
					.inflate(R.layout.item_image_text, null);
			LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.rightMargin = 10;
			view.setLayoutParams(params);
			RoundedImageView mImageView = (RoundedImageView) view
					.findViewById(R.id.applyImg);
			TextView applyPersonName = (TextView) view
					.findViewById(R.id.applyName);
			takePartPersonLayout.addView(view);
			applyPersonName.setText(person.getNickName());
			finalBitmap.display(mImageView, person.getHeadImg());
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("otherUserId", person.getUserId());
					intent.setClass(ActionCenterDetailsActivity.this,
							OthersInfoActivity.class);
					startActivity(intent);
				}
			});
		}
		personCount.setText(list.size() + "人报名");
		if (centerItem.getIsSignUp().equals("1")) {
			applyBtn.setText(R.string.taked_part_in);
		} else {
			applyBtn.setText(R.string.take_part_in);
		}
	}

	/**
	 * 设置活动详情内容
	 * 
	 * @param centerItem
	 */
	private void setActionDetailsContent(ActionCenterItemBean centerItem) {
		data = centerItem.getContent();
		if (data != null && !data.isEmpty()) {
			actionDetailsLy.setVisibility(View.VISIBLE);
			data = "<div style=\"line-height:150%;font-size:9pt;\">" + data
					+ "</div>";
			data = data.replaceAll("color:black;", "color:#999999");
			Document doc_Dis = Jsoup.parse(data);
			Elements ele_Img = doc_Dis.getElementsByTag("img");
			if (ele_Img.size() != 0) {
				for (Element e_Img : ele_Img) {
					e_Img.attr("width", "100%");
					e_Img.attr("height", "auto");
				}
			}
			String newHtmlContent = doc_Dis.toString();
			eventDetails.loadDataWithBaseURL("", newHtmlContent, "text/html",
					"UTF-8", "");
		} else {
			actionDetailsLy.setVisibility(View.GONE);
		}
	}

	public void initTitle() {
		setBackImageView();
		setTitleLayoutBg(R.color.app_blue);
		setTitleView(R.string.action_center_details);
		rightImageLayout.setVisibility(View.INVISIBLE);
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		if (TextUtils.isEmpty(userId)) {
			applyBtn.setClickable(false);
			applyBtn.setBackground(getResources().getDrawable(
					R.drawable.gray_btn_bg1));
			TipDialog.showDialogNoClose(ActionCenterDetailsActivity.this,
					R.string.tip, R.string.confirm, R.string.login_info_tip,
					LoginActivity.class);
		} else {
			applyBtn.setClickable(true);
			applyBtn.setBackground(getResources().getDrawable(
					R.drawable.blue_darker_btn_bg));
		}
		if (takePartPersonLayout != null) {
			takePartPersonLayout.removeAllViews();
		}
		if (list != null) {
			list.clear();
		}
		getData(userId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_apply:
			List<MemberUser> users = db.findAll(MemberUser.class);
			if (users != null && users.size() != 0) {
				MemberUser user = users.get(0);
				if (user != null) {
					userId = user.getMemberId();
				}
			}
			if (TextUtils.isEmpty(userId)) {
				applyBtn.setClickable(false);
				applyBtn.setBackground(getResources().getDrawable(
						R.drawable.gray_darker_btn_bg));
				TipDialog.showDialogNoClose(ActionCenterDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			} else {
				applyBtn.setClickable(true);
				applyBtn.setBackground(getResources().getDrawable(
						R.drawable.blue_darker_btn_bg));
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				String todayDateTime = format.format(new Date());
				String dateString;
				try {
					dateString = format.format(format.parse(endTime + ":00"));
					if (!compareTime(format, todayDateTime, dateString)) {
						takePartIn(userId);
					} else {
						TipDialog.showDialog(ActionCenterDetailsActivity.this,
								R.string.tip, R.string.confirm,
								R.string.action_ended);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			break;

		default:
			break;
		}

	}

	/**
	 * 
	 * @Title: compareTime
	 * @Description: TODO(java比较时间的大小)
	 * @param todayDate
	 * @param endTime
	 * @return
	 * @throws
	 */
	private boolean compareTime(SimpleDateFormat format, String todayDate,
			String endTime) {
		// TODO Auto-generated method stub
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(format.parse(todayDate));
			c2.setTime(format.parse(endTime));
		} catch (ParseException e) {
			System.err.println("时间解析异常");
		}
		int result = c1.compareTo(c2);
		if (result == 0) {
			System.out.println("c1==c2");
			return false;
		} else if (result < 0) {
			System.out.println("c1<c2");
			return false;
		} else {
			System.out.println("c1>c2");
			return true;
		}

	}

	private void takePartIn(final String userId) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞
		 * 8：加入圈子，9：报名
		 */
		params.put("userId", userId);
		params.put("actionId", aid);
		params.put("actionType", Constant.TAKE_PART_IN + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								applyBtn.setText(getString(R.string.taked_part_in));
								if (list != null) {
									list.clear();
								}
							} else {
								applyBtn.setText(getString(R.string.take_part_in));
							}
							if (takePartPersonLayout != null) {
								takePartPersonLayout.removeAllViews();
							}
							if (list != null) {
								list.clear();
							}
							getData(userId);
						} else {
							TipDialog.showDialog(
									ActionCenterDetailsActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}

					}
				});

	}

}

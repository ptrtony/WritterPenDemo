package com.android.bluetown.circle.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.PostAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.CircleBean;
import com.android.bluetown.bean.CircleParams;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.PostBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.CircleDetailsResult;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: CircleActivity
 * @Description:TODO(圈子详情)
 * @author: shenyz
 * @date: 2015年8月18日 下午4:43:08
 * 
 */
public class CircleDetailsActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
	private LayoutInflater mInflater;
	private ListView postListView;
	private LinearLayout topCircleListView;
	private RoundedImageView circleLogo;
	private TextView circleTitle, circlePersons, circleContent;
	private Button joinIn;
	private AbPullToRefreshView mPullToRefreshView;
	private PostAdapter mAdapter;
	private ArrayList<PostBean> list;

	private String groupId;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	private int size = 10;
	private CircleDetailsResult result;
	private ArrayList<PostBean> topList;
	private int joinPersons, position;
	private String isAddCircle;
	private CircleBean circleBean;
	private Handler handler;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_circle_details);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		getData();
	}

	private void initUIView() {
		handler = BlueTownApp.getHanler();
		
		list = new ArrayList<PostBean>();
		position = getIntent().getIntExtra("position", 0);
		groupId = getIntent().getStringExtra("groupId");
		mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.circle_details_top, null);
		topCircleListView = (LinearLayout) view
				.findViewById(R.id.topCircleList);
		circleLogo = (RoundedImageView) view.findViewById(R.id.circleLogo);
		circleTitle = (TextView) view.findViewById(R.id.circleTitle);
		circlePersons = (TextView) view.findViewById(R.id.circlePersons);
		circleContent = (TextView) view.findViewById(R.id.circleContent);
		joinIn = (Button) view.findViewById(R.id.joinIn);
		postListView = (ListView) findViewById(R.id.postList);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		postListView.addHeaderView(view);
		postListView.setAdapter(null);
		postListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		joinIn.setOnClickListener(this);
		db = FinalDb.create(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getData() {
	users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		params.put("userId", userId);
		params.put("groupId", groupId);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(
				Constant.HOST_URL + Constant.Interface.CIRCLE_DETAILS, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						result = (CircleDetailsResult) AbJsonUtil.fromJson(s,
								CircleDetailsResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							mPullToRefreshView.onFooterLoadFinish();
							mPullToRefreshView.onHeaderRefreshFinish();
							mAdapter.notifyDataSetChanged();
							Toast.makeText(CircleDetailsActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	protected void dealResult(CircleDetailsResult result) {
		circleBean = result.getData().getCircleInfo();
		finalBitmap.display(circleLogo, circleBean.getGroupImg());
		circleTitle.setText(circleBean.getGroupName());
		if (!TextUtils.isEmpty(circleBean.getCount())) {
			joinPersons = Integer.parseInt(circleBean.getCount());
		}
		// 是否加入圈子
		isAddCircle = circleBean.getAid();
		circlePersons.setText(joinPersons + "");
		circleContent.setText(circleBean.getState());
		if (page == 1) {
			setTopPostData(result);
		}
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			mPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			mPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (mAdapter==null) {
			mAdapter = new PostAdapter(this, list);
			postListView.setAdapter(mAdapter);
		}else {
			mAdapter.notifyDataSetChanged();	
		}
	}

	private void setTopPostData(CircleDetailsResult result) {
		if (!TextUtils.isEmpty(result.getData().getCircleInfo().getAid())) {
			joinIn.setText(R.string.joined_in);
		} else {
			joinIn.setText(R.string.add_info);
		}
		if (topList == null) {
			topList = new ArrayList<PostBean>();
		}
		topList.clear();
		topCircleListView.removeAllViews();
		if (result.getData().getRows() != null
				&& result.getData().getRows().size() > 0) {
			topList = (ArrayList<PostBean>) result.getData().getTop();
			if (topList.size() > 0) {
				// 置顶帖子最多显示条数
				for (int i = 0; i < topList.size(); i++) {
					setPostContent(topList, i);
				}

			}
		}

	}

	private void setPostContent(final ArrayList<PostBean> topList, final int i) {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);
		layout.setLayoutParams(params);
		TextView top = new TextView(this);
		LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		topParams.setMargins(10, 10, 10, 10);
		top.setLayoutParams(topParams);
		top.setBackgroundResource(R.drawable.blue_darker_btn_bg);
		top.setPadding(15, 10, 15, 10);
		top.setText(R.string.top);
		top.setGravity(Gravity.CENTER);
		top.setTextSize(14);
		top.setTextColor(getResources().getColor(R.color.white));
		TextView topPostContent = new TextView(this);
		LinearLayout.LayoutParams topPostLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		topPostLayoutParams.setMargins(15, 0, 0, 0);
		topPostContent.setLayoutParams(topPostLayoutParams);
		topPostContent.setText(topList.get(i).getManagementName());
		topPostContent.setMaxLines(1);
		topPostContent.setEllipsize(TruncateAt.END);
		topPostContent.setTextSize(14);
		topPostContent.setGravity(Gravity.CENTER_VERTICAL);
		topPostContent.setTextColor(getResources().getColor(R.color.font_gray));
		View lineView = new View(this);
		lineView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lineView.setBackgroundColor(getResources().getColor(
				R.color.compnay_detail_divier));
		layout.addView(top);
		layout.addView(topPostContent);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("mid", topList.get(i).getMid());
				intent.setClass(CircleDetailsActivity.this,
						PostDetailsActivity.class);
				startActivity(intent);
			}
		});
		topCircleListView.addView(layout);
		topCircleListView.addView(lineView);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.circle_details);
		setRightImageView(R.drawable.ic_edit);
		rightImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
				switch (v.getId()) {
		case R.id.rightImageView:
			if (userId != null && !userId.isEmpty()) {
				if (!TextUtils.isEmpty(isAddCircle)) {
					Intent intent = new Intent();
					intent.putExtra("groupId", groupId);
					intent.setClass(CircleDetailsActivity.this,
							PublishPostActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.bottom_in,0);
				} else {
					TipDialog.showDialog(CircleDetailsActivity.this,
							R.string.tip, R.string.dialog_ok,
							R.string.add_circle_tip);
				}

			} else {
				TipDialog.showDialogNoClose(CircleDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		case R.id.joinIn:
			if (userId != null &&!userId.isEmpty()) {
				joinIn(userId);
			} else {
				TipDialog.showDialogNoClose(CircleDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		default:
			break;
		}
	}

	private void joinIn(final String userId) {
		// userId 用户id
		// actionID 收藏(关注或点赞)对象的id
		// actionType 5：关注，6：收藏，7：点赞 8:加入圈子
		params.put("userId", userId);
		params.put("actionId", groupId);
		params.put("actionType", Constant.ADD_CIRCLE + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								circleBean.setAid(userId);
								joinIn.setText(R.string.joined_in);
								joinPersons = joinPersons + 1;
								circlePersons.setText(joinPersons + "");
								Message message = handler.obtainMessage();
								message.what = CircleParams.ADD_CIRCLE;
								message.arg1 = position;
								message.obj = circleBean;
								handler.sendMessage(message);
								Toast.makeText(context,
										R.string.add_circle_success,
										Toast.LENGTH_SHORT).show();
							} else {
								joinIn.setText(R.string.add_info);
								circleBean.setAid("");
								if (joinPersons != 0) {
									joinPersons = joinPersons - 1;
								}
								circlePersons.setText(joinPersons + "");
								Message message = handler.obtainMessage();
								message.what = CircleParams.ADD_CIRCLE;
								message.arg1 = position;
								message.obj = circleBean;
								handler.sendMessage(message);
								Toast.makeText(context,
										R.string.quit_circle_success,
										Toast.LENGTH_SHORT).show();
							}

						} else {
							TipDialog.showDialog(CircleDetailsActivity.this,
									SweetAlertDialog.SUCCESS_TYPE,
									result.getRepMsg());
						}

					}
				});
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		listStatus = Constant.ListStatus.LOAD;
		page++;
		getData();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		// TODO Auto-generated method stub
		list.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position != 0 && position != -1) {
			Intent intent = new Intent();
			intent.putExtra("mid", list.get(position - 1).getMid());
			intent.setClass(CircleDetailsActivity.this,
					PostDetailsActivity.class);
			startActivity(intent);
		}
	}
}

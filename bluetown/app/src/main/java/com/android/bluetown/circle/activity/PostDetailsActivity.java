package com.android.bluetown.circle.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.android.bluetown.adapter.PostReplyAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.PostBean;
import com.android.bluetown.bean.ReplyPostBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.main.model.act.OthersInfoActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.PostDetailsResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: CircleActivity
 * @Description:TODO(圈子)
 * @author: shenyz
 * @date: 2015年8月18日 下午4:43:08
 * 
 */
public class PostDetailsActivity extends TitleBarActivity implements
OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
OnFooterLoadListener {
	private LayoutInflater mInflater;
	private RoundedImageView circleLogo;
	private TextView postUser, postUserType, postPublishDate, postTitle,
	postContent, postPriaseCount, attention, replyPost;
	private Button addFriends, replyBtn;
	private LinearLayout postImgs;
	private ListView mListView;
	private AbPullToRefreshView mPullToRefreshView;
	private EditText replyContent;
	private PostReplyAdapter mAdapter;
	private ArrayList<ReplyPostBean> list;
	private String mid;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	protected int size = 10;
	// 关注帖子数
	private int postAttentionCount = 0;
	// 发帖人Id
	private String friendsId;

	// 是否为好友的标志
	private String isFriend;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_circle_post_details);
		BlueTownExitHelper.addActivity(this);
		initUIView();

	}

	private void initUIView() {
		list = new ArrayList<ReplyPostBean>();
		mid = getIntent().getStringExtra("mid");
		mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.post_details_top, null);
		circleLogo = (RoundedImageView) view.findViewById(R.id.circleLogo);
		postUser = (TextView) view.findViewById(R.id.postUser);
		postUserType = (TextView) view.findViewById(R.id.postUserType);
		postPublishDate = (TextView) view.findViewById(R.id.postPublishDate);
		postTitle = (TextView) view.findViewById(R.id.postTitle);
		postContent = (TextView) view.findViewById(R.id.postContent);
		postPriaseCount = (TextView) view.findViewById(R.id.postPriaseCount);
		attention = (TextView) view.findViewById(R.id.attention);
		replyPost = (TextView) view.findViewById(R.id.replyPost);
		addFriends = (Button) view.findViewById(R.id.addFriends);
		postImgs = (LinearLayout) view.findViewById(R.id.postImgs);
		mListView = (ListView) findViewById(R.id.postReplyList);
		replyBtn = (Button) findViewById(R.id.replyBtn);
		replyContent = (EditText) findViewById(R.id.replyContent);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.addHeaderView(view);
		mListView.setAdapter(null);
		mListView.setOnItemClickListener(this);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);
		replyBtn.setOnClickListener(this);
		attention.setOnClickListener(this);
		addFriends.setOnClickListener(this);
		postPriaseCount.setOnClickListener(this);
		db = FinalDb.create(this);
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.post_details);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void getData() {
		params.put("userId", userId);
		params.put("mid", mid);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.POST_DETAILS,
				params, new AbsHttpStringResponseListener(this, null) {
			@Override
			public void onSuccess(int i, String s) {
				PostDetailsResult result = (PostDetailsResult) AbJsonUtil
						.fromJson(s, PostDetailsResult.class);
				if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
					dealResult(result);
				} else if (result.getRepCode().contains(
						Constant.HTTP_NO_DATA)) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mPullToRefreshView.onFooterLoadFinish();
							mPullToRefreshView.onHeaderRefreshFinish();
							Toast.makeText(PostDetailsActivity.this,
									R.string.no_data, Toast.LENGTH_LONG)
									.show();
						}
					});
				}

			}
		});
	}

	/**
	 * 设置评论时间
	 * 
	 * @param mHolder
	 * @param item
	 */
	private void setCommentTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String todayDateTime = format.format(new Date());
		try {
			Date nowTime = format.parse(todayDateTime);
			Date createTime = format.parse(time);
			// 相差的毫秒数
			long diff = nowTime.getTime() - createTime.getTime();
			// 秒
			long timeInterval = diff / 1000;
			if (timeInterval < 60) {
				postPublishDate.setText("刚刚");
			} else if ((timeInterval / 60) < 60) {
				postPublishDate.setText((timeInterval / 60) + "分钟前");
			} else if ((timeInterval / (60 * 60)) < 24) {
				postPublishDate.setText((timeInterval / (60 * 60)) + "小时前");
			} else {
				postPublishDate.setText(time);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void dealResult(PostDetailsResult result) {
		PostBean postBean = result.getData().getManagementInfo();
		if (mAdapter == null) {
			if (!TextUtils.isEmpty(postBean.getHeadImg())) {
				finalBitmap.display(circleLogo, postBean.getHeadImg());
			}
			postUser.setText(postBean.getNickName());
			setCommentTime(postBean.getCreateTime());
			postTitle.setText(postBean.getManagementName());
			postContent.setText(postBean.getContent());
			String isAttention = postBean.getIsCollect();
			if (!TextUtils.isEmpty(isAttention)) {
				if (isAttention.equals("1")) {
					attention.setText(R.string.attentioned);
				} else {
					attention.setText(R.string.attention);
				}
			}
			String loginUserId = "";
			users = db.findAll(MemberUser.class);
			if (users != null && users.size() != 0) {
				MemberUser user = users.get(0);
				if (user != null) {
					loginUserId = user.getMemberId();
				}
			}
			if (!TextUtils.isEmpty(postBean.getUserId())
					&& !TextUtils.isEmpty(loginUserId)) {
				if (postBean.getUserId().equals(loginUserId)) {
					addFriends.setVisibility(View.GONE);
				} else {
					addFriends.setVisibility(View.VISIBLE);
				}
			}
		}
		String isPraise = postBean.getIsPraise();
		// （0未点赞，1已点赞）
		if (!TextUtils.isEmpty(isPraise)) {
			if (isPraise.equals("1")) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.like_p);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界
				postPriaseCount
				.setCompoundDrawables(drawable, null, null, null);
			} else {
				Drawable drawable = getResources().getDrawable(R.drawable.like);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界
				postPriaseCount
				.setCompoundDrawables(drawable, null, null, null);
			}
		}

		// 发帖人的Id
		friendsId = postBean.getUserId();
		// isFriend：状态；(0:不是好友;1:是好友)
		isFriend = postBean.getIsFriend();
		if (!TextUtils.isEmpty(isFriend)) {
			if (isFriend.equals("0")) {
				addFriends.setText(R.string.add_friend);
			} else {
				addFriends.setText(R.string.delete_friend);
			}
		} else {
			addFriends.setText(R.string.add_friend);
		}

		if (!TextUtils.isEmpty(postBean.getApplyNumber())) {
			postAttentionCount = Integer.parseInt(postBean.getApplyNumber());
		}
		postPriaseCount.setText(postAttentionCount + "");
		List<String> postImgList = postBean.getPicturesList();
		postImgs.removeAllViews();
		if (postImgList != null && postImgList.size() > 0) {
			for (int i = 0; i < postImgList.size(); i++) {
				ImageView imageView = new ImageView(this);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				DisplayMetrics dm = new DisplayMetrics();
				// 获取屏幕信息
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int imgWidth = (int) ((dm.widthPixels) * (0.7));
				imageLoader.displayImage(postImgList.get(i), imageView,
						BlueTownApp.defaultOptions);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						imgWidth, imgWidth);
				params.gravity = Gravity.CENTER_HORIZONTAL;
				params.setMargins(0, 10, 0, 0);
				imageView.setLayoutParams(params);
				postImgs.addView(imageView);
			}
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
		replyPost.setText("回帖(" + list.size() + ")");
		if (mAdapter == null) {
			mAdapter = new PostReplyAdapter(this, list);
			mListView.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
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

		getData();

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
		case R.id.replyBtn:
			if (userId != null && !userId.isEmpty()) {
				String commentContent = replyContent.getText().toString();
				if (TextUtils.isEmpty(commentContent)) {
					TipDialog.showDialog(PostDetailsActivity.this,
							R.string.tip, R.string.dialog_ok,
							R.string.follow_post_content);
					return;
				}
				followPost(userId, commentContent);
			} else {
				TipDialog.showDialogNoClose(PostDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		case R.id.attention:
			if (userId != null && !userId.isEmpty()) {
				attention(userId);
			} else {
				TipDialog.showDialogNoClose(PostDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}
			break;
		case R.id.addFriends:
			if (userId != null && !userId.isEmpty()) {
				if (isFriend.equals("1")) {
					showDialog(PostDetailsActivity.this, R.string.tip,
							R.string.dialog_ok, R.string.cancel,
							R.string.confirm_delete_title, userId);
				} else {
					addFriendsOrDelete(Constant.ADD_FRIENDS, userId);
				}
			} else {
				TipDialog.showDialogNoClose(PostDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}
			break;
		case R.id.postPriaseCount:
			if (userId != null && !userId.isEmpty()) {
				praise(userId);
			} else {
				TipDialog.showDialogNoClose(PostDetailsActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * addFriendsOrDelete userId：当前用户id（必填） friendId:添加的好友id（必填）
	 * type:必填(0:添加好友，1：删除好友)
	 */
	private void addFriendsOrDelete(final int type, String userId) {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("friendId", friendsId);
		params.put("type", type + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.ADD_FRIEND,
				params, new AbsHttpStringResponseListener(this, null) {
			@Override
			public void onSuccess(int i, String s) {
				Result result = (Result) AbJsonUtil.fromJson(s,
						Result.class);
				if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
					if (type == 0) {
						Toast.makeText(context,
								"已发送验证请求",
								Toast.LENGTH_SHORT).show();
						// 等待对方审核
						addFriends.setText(R.string.add_friend);
					} else {
						addFriends.setText(R.string.add_friend);
						Toast.makeText(context, "删除成功！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					TipDialog.showDialog(PostDetailsActivity.this,
							R.string.tip, R.string.dialog_ok,
							result.getRepMsg());
				}

			}

		});

	}

	private void praise(String userId) {
		// userId 用户id
		// actionID 收藏(关注或点赞)对象的id
		// actionType 5：关注，6：收藏，7：点赞 8:加入圈子
		params.put("userId", userId);
		params.put("actionId", mid);
		params.put("actionType", Constant.PRIASE + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpStringResponseListener(this, null) {
			@Override
			public void onSuccess(int i, String s) {
				UserOperationResult result = (UserOperationResult) AbJsonUtil
						.fromJson(s, UserOperationResult.class);
				if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
					if (result.getData().equals("关注成功")) {
						Drawable drawable = context.getResources()
								.getDrawable(R.drawable.like_p);
						drawable.setBounds(0, 0,
								drawable.getMinimumWidth(),
								drawable.getMinimumHeight()); // 设置边界
						postPriaseCount.setCompoundDrawables(drawable,
								null, null, null);
						postAttentionCount = postAttentionCount + 1;
						postPriaseCount
						.setText(postAttentionCount + "");
					} else {
						Drawable drawable = context.getResources()
								.getDrawable(R.drawable.like);
						drawable.setBounds(0, 0,
								drawable.getMinimumWidth(),
								drawable.getMinimumHeight()); // 设置边界
						postPriaseCount.setCompoundDrawables(drawable,
								null, null, null);
						if (postAttentionCount != 0) {
							postAttentionCount = postAttentionCount - 1;
						}
						postPriaseCount
						.setText(postAttentionCount + "");

					}

				} else {
					TipDialog.showDialog(PostDetailsActivity.this,
							SweetAlertDialog.ERROR_TYPE,
							result.getRepMsg());
				}

			}
		});
	}

	private void attention(String userId) {
		// userId 用户id
		// actionID 收藏(关注或点赞)对象的id
		// actionType 5：关注，6：收藏，7：点赞 8:加入圈子
		params.put("userId", userId);
		params.put("actionId", mid);
		params.put("actionType", Constant.ATTENTION + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpStringResponseListener(this, null) {
			@Override
			public void onSuccess(int i, String s) {
				UserOperationResult result = (UserOperationResult) AbJsonUtil
						.fromJson(s, UserOperationResult.class);
				if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
					if (result.getData().equals("关注成功")) {
						attention.setText(R.string.attentioned);
					} else {
						attention.setText(R.string.attention);
					}
				} else {
					TipDialog.showDialog(PostDetailsActivity.this,
							SweetAlertDialog.ERROR_TYPE,
							result.getRepMsg());
				}

			}
		});
	}

	private void followPost(String userId, String commentContent) {
		/*
		 * managementId 帖子id userId 用户id commentContent 跟帖内容
		 */
		params.put("userId", userId);
		params.put("managementId", mid);
		params.put("commentContent", commentContent);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.FOLLOW_POST,
				params, new AbsHttpStringResponseListener(this, null) {
			@Override
			public void onSuccess(int i, String s) {
				Result result = (Result) AbJsonUtil.fromJson(s,
						Result.class);
				if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
					showDialog(PostDetailsActivity.this, R.string.tip,
							R.string.confirm, R.string.commit_success);
				} else {
					TipDialog.showDialog(PostDetailsActivity.this,
							SweetAlertDialog.ERROR_TYPE,
							result.getRepMsg());
				}

			}
		});
	}

	private void showDialog(final Context context, int titleId,
			int confirmTextId, int contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
		.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.isShowCancelButton();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				// 清除评论框的内容
				replyContent.setText("");
				if (list != null) {
					list.clear();
				}
				page = 1;
				getData();
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showDialog(final Context context, int titleId,
			int confirmTextId, int cancelTextId, int contentStr,
			final String userId) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
		.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				addFriendsOrDelete(Constant.DELETE_FRIENDS, userId);
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
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
			intent.putExtra("otherUserId", list.get(position - 1).getUserId());
			intent.setClass(PostDetailsActivity.this, OthersInfoActivity.class);
			startActivity(intent);
		}
	}

}

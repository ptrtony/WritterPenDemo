package com.android.bluetown.home.main.model.act;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation.ConversationType;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.util.dct.IFDCT;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.ImagePagerActivity;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ProductGridAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ProductBean;
import com.android.bluetown.bean.UserInfo;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;

import com.android.bluetown.result.OtherInfoResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.HeaderGridView;
import com.android.bluetown.view.RoundedImageView;

/**
 * 
 * @ClassName: OthersActivity
 * @Description:TODO(跳蚤市场-联系卖家-他人资料)
 * @author: shenyz
 * @date: 2015年8月12日 上午10:05:19
 * 
 */
public class OthersInfoActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
	private LayoutInflater mInflater;
	private AbPullToRefreshView mPullToRefreshView;
	private HeaderGridView mGridView;
	private Button mAddFriend;
	private ImageView userSex;
	private RoundedImageView userImg;
	private TextView tel, mail, userName, userNick, userBirthday;
//	private ProductGridAdapter myGridViewAdapter = null;
	private ArrayList<ProductBean> mProductList = new ArrayList<ProductBean>();
	private String flag;
	private String friendsId;
	private String userImgStr;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_others_info);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		getData();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBgRes(R.drawable.other_top_bg1);
		setTitleView(R.string.others_info);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	private void initUIView() {
		// TODO Auto-generated method stub
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		friendsId = getIntent().getStringExtra("otherUserId");
		mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.others_info_top, null);
		userImg = (RoundedImageView) view.findViewById(R.id.userImg);
		userSex = (ImageView) view.findViewById(R.id.userSex);
		tel = (TextView) view.findViewById(R.id.tel);
		mail = (TextView) view.findViewById(R.id.mail);
		userName = (TextView) view.findViewById(R.id.userName);
		userNick = (TextView) view.findViewById(R.id.userNick);
		userBirthday = (TextView) view.findViewById(R.id.userBirthday);
		mAddFriend = (Button) view.findViewById(R.id.addfriend);
		mPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mGridView = (HeaderGridView) findViewById(R.id.publishproductGridView);
		mGridView.addHeaderView(view);
		mGridView.setAdapter(null);
		userImg.setOnClickListener(this);
		tel.setOnClickListener(this);
		mAddFriend.setOnClickListener(this);
//		mGridView.setOnItemClickListener(this);
		// 设置监听器
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterLoadListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (userId == null || userId.isEmpty()) {
			users = db.findAll(MemberUser.class);
			if (users != null && users.size() != 0) {
				MemberUser user = users.get(0);
				if (user != null) {
					userId = user.getMemberId();
				}
			}
		}
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO()
	 * @throws
	 */
	private void getData() {
		// TODO Auto-generated method stub
		params.put("userId", userId);
		params.put("friendId", friendsId);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.OTHERS,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						OtherInfoResult result = (OtherInfoResult) AbJsonUtil
								.fromJson(s, OtherInfoResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							setUserInfo(result);
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							mPullToRefreshView.onFooterLoadFinish();
							mPullToRefreshView.onHeaderRefreshFinish();
							Toast.makeText(OthersInfoActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						}

					}

					private void setUserInfo(OtherInfoResult result) {
						UserInfo info = result.getData().getUserInfo();
						if (info != null) {
							if (!TextUtils.isEmpty(info.getHeadImg())) {
								finalBitmap.display(userImg, info.getHeadImg());
							} else {
								userImg.setImageResource(R.drawable.ic_msg_empty);
							}
							userImgStr = info.getHeadImg();
							String sex = info.getSex();
							if (sex.equals("0")) {
								userSex.setImageResource(R.drawable.ic_sex_man);
							} else if (sex.equals("1")) {
								userSex.setImageResource(R.drawable.ic_sex);
							}
							if (userId != null && !"".equals(userId)) {
								if (userId.equals(info.getUserId())) {
									mAddFriend.setVisibility(View.GONE);
								} else {
									mAddFriend.setVisibility(View.VISIBLE);
								}
							} else {
								mAddFriend.setVisibility(View.VISIBLE);
							}

							try {
								// state：状态；(0:不是好友;1:是好友)
								flag = info.getState();
								if (flag.equals("0")) {
									mAddFriend.setText(R.string.add_friend);
								} else {
									mAddFriend.setText(R.string.delete_friend);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							userBirthday.setText(info.getBirthday());
							userName.setText(info.getName());
							userNick.setText(info.getNickName());
							tel.setText(info.getTelphone());
							mail.setText(info.getEmail());
						} else {
							userImg.setImageResource(R.drawable.ic_msg_empty);
						}

					}

				});

	}

	/**
	 * addFriendsOrDelete userId：当前用户id（必填） friendId:添加的好友id（必填）
	 * type:必填(0:添加好友，1：删除好友)
	 */
	private void addFriendsOrDelete(final int type) {
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
										getString(R.string.apply_for_success),
										Toast.LENGTH_SHORT).show();
							} else {
								// RongIM.getInstance().getRongIMClient().clearMessages删
								// 消息内容
								// RongIM.getInstance()
								// .getRongIMClient()
								// .clearMessages(
								// ConversationType.PRIVATE,
								// friendsId);
								// 删除会话列表中的会话 removeConversation
								RongIM.getInstance()
										.getRongIMClient()
										.removeConversation(
												ConversationType.PRIVATE,
												friendsId);
								Toast.makeText(context, "删除成功！",
										Toast.LENGTH_SHORT).show();
							}
							getData();
						} else {
							TipDialog.showDialog(OthersInfoActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}

					}

				});

	}

	private void dealResult(OtherInfoResult result) {
		// TODO Auto-generated method stub

		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			mProductList.addAll(result.getData().getCommoditys().getRows());

			mPullToRefreshView.onFooterLoadFinish();

			break;
		case Constant.ListStatus.INIT:
			mProductList.clear();
			mProductList.addAll(result.getData().getCommoditys().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			mProductList.clear();
			mProductList.addAll(result.getData().getCommoditys().getRows());

			mPullToRefreshView.onHeaderRefreshFinish();

			break;
		}

//		if (myGridViewAdapter == null) {
//			// 使用自定义的Adapter
//			myGridViewAdapter = new ProductGridAdapter(this, mProductList,
//					false);
//			mGridView.setAdapter(myGridViewAdapter);
//		}
//		myGridViewAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addfriend:
			if (userId != null && !userId.isEmpty()) {
				if (flag.equals("1")) {
					showDialog(OthersInfoActivity.this,
							R.string.confirm_delete_title, R.string.dialog_ok,
							R.string.cancel, R.string.confirm_delete_content);
				} else {
					addFriendsOrDelete(Constant.ADD_FRIENDS);
				}
			} else {
				TipDialog.showDialogNoClose(OthersInfoActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		case R.id.userImg:
			ArrayList<String> urls = new ArrayList<>();
			urls.add(userImgStr);
			Intent intent = new Intent(OthersInfoActivity.this,
					ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
			startActivity(intent);
			break;
		case R.id.tel:
			if (!TextUtils.isEmpty(tel.getText().toString())) {
				Intent telIntent = new Intent();
				telIntent.setAction(Intent.ACTION_CALL);
				// url:统一资源定位符
				// uri:统一资源标示符（更广）
				telIntent.setData(Uri.parse("tel:" + tel.getText().toString()));
				// 开启系统拨号器
				startActivity(telIntent);
			}

			break;
		default:
			break;
		}
	}

	private void showDialog(final Context context, int titleId,
			int confirmTextId, int cancelTextId, int contentStr) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				addFriendsOrDelete(Constant.DELETE_FRIENDS);
				sweetAlertDialog.dismiss();
			}
		});
		dialog.setCancelClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				sweetAlertDialog.dismiss();
			}
		});
		dialog.show();
	}

//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		if (position >= 2) {
//			Intent intent = new Intent();
//			intent.putExtra("cid", mProductList.get(position - 2).getCid());
//			intent.setClass(OthersInfoActivity.this,
//					ProductDetailActivity.class);
//			startActivity(intent);
//		}
//	}

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
		listStatus = Constant.ListStatus.REFRESH;
		if (mProductList != null) {
			mProductList.clear();
		}
		page = 1;
		getData();
	}

}

package com.android.bluetown;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.adapter.PostAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.PostBean;
import com.android.bluetown.circle.activity.PostDetailsActivity;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.leftslide.listview.SwipeMenu;
import com.android.bluetown.leftslide.listview.SwipeMenuCreator;
import com.android.bluetown.leftslide.listview.SwipeMenuItem;
import com.android.bluetown.leftslide.listview.SwipeMenuListView;
import com.android.bluetown.leftslide.listview.SwipeMenuListView.OnMenuItemClickListener;
import com.android.bluetown.leftslide.listview.SwipeMenuListView.OnSwipeListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.CircleDetailsResult;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;

//我关注的帖子
public class MyPostActivity extends TitleBarActivity implements 
			OnMenuItemClickListener,OnItemClickListener, SwipeMenuCreator,
		OnSwipeListener,OnHeaderRefreshListener,OnFooterLoadListener{
	private SwipeMenuListView mListView;
	private AbPullToRefreshView mPullRefreshView;
	private PostAdapter mAdapter;
	private ArrayList<PostBean> list;

	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	private int size=10;

	private CircleDetailsResult result;
	private String userId;
	private String gardenId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_mypost);
		BlueTownExitHelper.addActivity(this);
		initUIView();
	}

	private void initUIView() {
		list = new ArrayList<PostBean>();
		
		mPullRefreshView=(AbPullToRefreshView)findViewById(R.id.mPullRefreshView);
		mListView = (SwipeMenuListView) findViewById(R.id.companyInfoList);
		mListView.setOnItemClickListener(this);
		// step 1. create a MenuCreator and set creator
		mListView.setMenuCreator(this);
		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(this);
		// set SwipeListener
		mListView.setOnSwipeListener(this);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setOnFooterLoadListener(this);
		FinalDb db = FinalDb.create(MyPostActivity.this);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
				gardenId=user.getGardenId();
			}
		}
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.my_attention_post);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
	
	private void getData() {
		/**
		userId 用户id(必填)filterMine 过滤关键字(1查询我关注的帖子)(必填,值为1)
		*/
		
		params.put("userId", userId);
		params.put("filterMine", "1");
		params.put("page", page+"");
		params.put("rows", size+"");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.CIRCLE_DETAILS, params, new AbsHttpStringResponseListener(this, null) {
			@Override
			public void onSuccess(int i, String s) {
				result = (CircleDetailsResult) AbJsonUtil.fromJson(s, CircleDetailsResult.class);
				if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
					dealResult(result);
				} else if (result.getRepCode().contains(Constant.HTTP_NO_DATA)) {
					if (mPullRefreshView != null) {
							mPullRefreshView.onFooterLoadFinish();
							mPullRefreshView.onHeaderRefreshFinish();
					}
							Toast.makeText(MyPostActivity.this, R.string.no_data, Toast.LENGTH_LONG).show();
					
				}

			}
			@Override
			public void onFailure(int arg0, String arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				mPullRefreshView.onFooterLoadFinish();
				mPullRefreshView.onHeaderRefreshFinish();
			}
		});
	}

	protected void dealResult(CircleDetailsResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			
			mPullRefreshView.onFooterLoadFinish();
			
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			
			mPullRefreshView.onHeaderRefreshFinish();
		
			break;
		}
	if (mAdapter == null) {
		mAdapter = new PostAdapter(this, list);
		mListView.setAdapter(mAdapter);
	   }
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("mid", list.get(position).getMid());
		intent.setClass(MyPostActivity.this, PostDetailsActivity.class);
		startActivity(intent);
	}

	@Override
	public void create(SwipeMenu menu) {
		// TODO Auto-generated method stub
		// create "cancel collect" item
		SwipeMenuItem collectItem = new SwipeMenuItem(this);
		// set item background
		collectItem.setBackground(R.drawable.cancel_collect_bg);
		// set item width
		collectItem.setWidth(LayoutParams.WRAP_CONTENT);
		// set item title
		collectItem.setTitle(getString(R.string.cancel_attentionsuccess));
		// set item title fontsize
		collectItem.setTitleSize(16);
		// set item title font color
		collectItem.setTitleColor(Color.WHITE);
		// add to menu
		menu.addMenuItem(collectItem);
	}

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		// TODO Auto-generated method stub
		cancelCollectPost(userId, list.get(position));
		return false;
	}
	
	private void cancelCollectPost(String userId, final PostBean item) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */
		params.put("userId", userId);
		params.put("actionId", item.getMid());
		params.put("actionType", Constant.ATTENTION + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.CIRCLE_TYPEW_INFO, params, new AbsHttpStringResponseListener(
				MyPostActivity.this, null) {
			@Override
			public void onSuccess(int i, String s) {
				UserOperationResult result = (UserOperationResult) AbJsonUtil.fromJson(s, UserOperationResult.class);
				if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
					if (!result.getData().equals("关注成功")) {
						TipDialog.showDialog(MyPostActivity.this, SweetAlertDialog.SUCCESS_TYPE, R.string.cancel_success);
						list.remove(item);
						mAdapter.notifyDataSetChanged();
						if (list != null && list.size() == 0) {
							mListView.setVisibility(View.GONE);
						}
					}
				} else {
					TipDialog.showDialog(MyPostActivity.this, SweetAlertDialog.SUCCESS_TYPE, result.getRepMsg());
				}

			}
		});

	}

	@Override
	public void onSwipeEnd(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSwipeStart(int position) {
		// TODO Auto-generated method stub

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

}

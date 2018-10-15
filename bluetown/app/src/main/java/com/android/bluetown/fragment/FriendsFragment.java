package com.android.bluetown.fragment;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.adapter.MakeFriendsAdapter;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.FriendResult;
import com.android.bluetown.utils.Constant;

/**
 * FriendsFragment 好友列表
 * 
 * @author shenyz
 * 
 */
public class FriendsFragment extends AbsFragment implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView abPullToRefreshView;
	private ListView mListView;
	private MakeFriendsAdapter mAdapter;
	private ArrayList<FriendsBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size =30;
	private String userId;
	private FinalDb db;

	public FriendsFragment() {
		// TODO Auto-generated constructor stub
	}

	public FriendsFragment(String userId) {
		// TODO Auto-generated constructor stub
		this.userId = userId;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		db = FinalDb.create(getActivity());
		return inflater.inflate(R.layout.fragment_listview, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		getData();
	}

	private void initViews(View view) {
		// TODO Auto-generated method stub
		abPullToRefreshView = (AbPullToRefreshView) view
				.findViewById(R.id.mPullRefreshView);
		list = new ArrayList<FriendsBean>();
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		abPullToRefreshView.setOnFooterLoadListener(this);
		abPullToRefreshView.setOnHeaderRefreshListener(this);

	}

	private void getData() {
		params.put("userId", userId);
		params.put("type", "0");
		params.put("condition", "");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.FRIEND_LIST,
				params, new AbsHttpStringResponseListener(getActivity(), null) {
					@Override
					public void onSuccess(int i, String s) {
						FriendResult result = (FriendResult) AbJsonUtil
								.fromJson(s, FriendResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (abPullToRefreshView != null) {
								abPullToRefreshView.onHeaderRefreshFinish();
								abPullToRefreshView.onFooterLoadFinish();
							}
							Toast.makeText(getActivity(), R.string.no_data,
									Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String userId = list.get(arg2).getUserId();
		String nickname = list.get(arg2).getNickName();
		String headImg = list.get(arg2).getHeadImg();
		// 用户已经完善了用户信息，缓存数据到本地
		if (TextUtils.isEmpty(nickname)) {
			nickname = userId;
		}
		if (TextUtils.isEmpty(headImg)) {
			headImg = "";
		}
		// 将该用户保存到数据库
		List<FriendsBean> friendList = db.findAllByWhere(FriendsBean.class,
				" userId=\"" + userId + "\"");
		if (friendList.size() == 0) {
			FriendsBean friend = new FriendsBean();
			friend.setUserId(userId);
			friend.setHeadImg(headImg);
			friend.setNickName(nickname);
			db.save(friend);
		}
		RongIM.getInstance().startPrivateChat(getActivity(), userId, nickname);

	}

	protected void dealResult(FriendResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getFriend().getRows());
			abPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getFriend().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getFriend().getRows());
			abPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (mAdapter==null) {
			mAdapter = new MakeFriendsAdapter(getActivity(), list, 0);
			mListView.setAdapter(mAdapter);
		}else {
			mAdapter.notifyDataSetChanged();
		}
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

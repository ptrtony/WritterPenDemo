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
import com.android.bluetown.bean.GroupsBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.GroupResult;
import com.android.bluetown.utils.Constant;

/**
 * FriendsGroupsFragment 群列表
 * 
 * @author shenyz
 * 
 */
public class GroupsFragment extends AbsFragment implements OnItemClickListener,
		OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView abPullToRefreshView;
	private ListView mListView;
	private MakeFriendsAdapter mAdapter;
	private ArrayList<GroupsBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 30;
	private String userId;
	private FinalDb db;

	public GroupsFragment() {
		// TODO Auto-generated constructor stub
	}

	public GroupsFragment(String userId) {
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
		list = new ArrayList<GroupsBean>();
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
		params.put("size", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.GROUP_LIST,
				params, new AbsHttpStringResponseListener(getActivity(), null) {
					@Override
					public void onSuccess(int i, String s) {
						GroupResult result = (GroupResult) AbJsonUtil.fromJson(
								s, GroupResult.class);
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

	protected void dealResult(GroupResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			abPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			abPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (mAdapter == null) {
			mAdapter = new MakeFriendsAdapter(getActivity(), list, 1);
			mListView.setAdapter(mAdapter);
		} else {
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String mid = list.get(arg2).getMid();
		String flockName = list.get(arg2).getFlockName();
		String flockImage = list.get(arg2).getFlockImg();
		// 将该群信息保存到数据库
		List<GroupsBean> groupList = db.findAllByWhere(GroupsBean.class,
				" mid=\"" + mid + "\"");
		if (groupList.size() == 0) {
			GroupsBean groupBean = new GroupsBean();
			groupBean.setMid(mid);
			groupBean.setFlockImg(flockImage);
			groupBean.setFlockName(flockName);
			db.save(groupBean);
		} else {
			String flockImg = groupList.get(0).getFlockImg();
			if (TextUtils.isEmpty(flockImg) || !flockImg.equals(flockImage)) {
				db.deleteById(GroupsBean.class, " mid=\"" + mid + "\"");
				GroupsBean groupBean = new GroupsBean();
				groupBean.setMid(mid);
				groupBean.setFlockImg(flockImage);
				groupBean.setFlockName(flockName);
				db.save(groupBean);
			}

		}
		RongIM.getInstance().startGroupChat(getActivity(), mid, flockName);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

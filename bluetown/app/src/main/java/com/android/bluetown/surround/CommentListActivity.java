package com.android.bluetown.surround;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.CommentListAdapter;
import com.android.bluetown.bean.CommentBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.CommentListResult;
import com.android.bluetown.utils.Constant;

/**
 * 商家点评列表
 * 
 * @author shenyz
 * 
 */
public class CommentListActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener {
	private AbPullToRefreshView mAbPullToRefreshView;
	private ListView mListView;
	private CommentListAdapter adapter;
	private List<CommentBean> list;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String meid;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBgRes(R.color.title_bg_blue);
		setTitleView(R.string.comment_list);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_action_center);
		initViews();
		getData();
	}

	/**
	 * 初始化界面組件
	 */
	private void initViews() {
		meid = getIntent().getStringExtra("meid");
		mListView = (ListView) findViewById(R.id.companyInfoList);
		mListView.setBackgroundColor(getResources().getColor(
				R.color.app_bg_color));
		mListView.setDivider(getResources().getDrawable(R.color.app_bg_color));
		list = new ArrayList<CommentBean>();
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
	}

	private void getData() {
		params.put("meid", meid);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL + Constant.Interface.COMMENT_LIST,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						CommentListResult result = (CommentListResult) AbJsonUtil
								.fromJson(s, CommentListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {

							if (list != null && list.size() > 0) {
								mAbPullToRefreshView.onHeaderRefreshFinish();
								mAbPullToRefreshView.onFooterLoadFinish();
							}

							Toast.makeText(CommentListActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						} else {
							mAbPullToRefreshView.onHeaderRefreshFinish();
							mAbPullToRefreshView.onFooterLoadFinish();
						}

					}
				});
	}

	protected void dealResult(CommentListResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			mAbPullToRefreshView.onFooterLoadFinish();

			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (adapter == null) {
			adapter = new CommentListAdapter(this, list);
			mListView.setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
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

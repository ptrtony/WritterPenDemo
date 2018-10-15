package com.android.bluetown.home.hot.model.act;

import java.util.ArrayList;
import java.util.List;

import me.next.tagview.TagCloudView;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ParkYellowPageListAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.LocationInfo;
import com.android.bluetown.bean.ParkYellowPageItemBean;
import com.android.bluetown.bean.TypeBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.YellowPageResult;
import com.android.bluetown.utils.Constant;


/**
 * 
 * @ClassName: ParkYellowPageActivity
 * @Description:TODO(HomeActivity-园区黄页)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:38:28
 * 
 */
public class ParkYellowPageActivity extends TitleBarActivity implements
		OnClickListener, OnHeaderRefreshListener, OnFooterLoadListener,
		OnItemClickListener {
	private LinearLayout typeLy;
	private TextView productType;
	private ImageView productTypeImg;
	private AbPullToRefreshView pullToRefreshView;
	private ListView mListView;
	private ParkYellowPageListAdapter adapter;
	private List<ParkYellowPageItemBean> list;
	private List<String> tagsList;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private double latitude = 0.0;
	private double longitude = 0.0;
	private List<TypeBean> typeList;
	private String tid;
	private SharePrefUtils prefUtils;
	private FinalDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_park_yellow_page);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		getData();
	}

	private void initUIView() {
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
		list = new ArrayList<ParkYellowPageItemBean>();
		tagsList = new ArrayList<String>();
		pullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		mListView = (ListView) findViewById(R.id.parkYellowInfoList);
		mListView.setOnItemClickListener(this);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterLoadListener(this);
		List<LocationInfo> infos = db.findAll(LocationInfo.class);
		LocationInfo info = null;
		if (infos != null && infos.size() != 0) {
			info = infos.get(0);
			if (info != null) {
				latitude = info.getLatitude();
				longitude = info.getLongitude();
			}
		}

	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setRightImageView(R.drawable.menu1);
		setTitleLayoutBg(R.color.title_bg_blue);
		typeLy = (LinearLayout) findViewById(R.id.titleLy1);
		searchBtnLy.setVisibility(View.VISIBLE);
		searchBtnLy.setOnClickListener(this);
		rightImageLayout.setOnClickListener(this);

	}

	private void selectTypePop(List<String> tagsList, LinearLayout typeLy,
			final ImageView typeImg, final TextView typeTextView) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.type_select_layout, null);
		// 创建PopupWindow对象
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		final TagCloudView selecTagCloudView = (TagCloudView) view
				.findViewById(R.id.tag_cloud_view);
		final LinearLayout typeBlankLy = (LinearLayout) view
				.findViewById(R.id.typeBlankLy);
		selecTagCloudView.setTags(tagsList);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		pop.showAsDropDown(typeLy);
		typeBlankLy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
			}
		});
		selecTagCloudView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int position) {
				// TODO Auto-generated method stub
				if (position == -1) {
					Toast.makeText(getApplicationContext(), "沒有这个标签",
							Toast.LENGTH_SHORT).show();
				} else {
					if (position != 0) {
						tid = typeList.get(position).getTid();
					} else {
						tid = "";
					}
					if (list != null && list.size() > 0) {
						list.clear();
					}
					page=1;
					getData();
					pop.dismiss();
				}
			}
		});
	}

	private void getData() {
		/**
		 * longitude(必填) latitude(必填) tid黄页类型id(可填)
		 */
		params.put("tid", tid);
		params.put("gardenId",
				prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		params.put("longitude", longitude + "");
		params.put("latitude", latitude + "");
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.YELLOW_PAGE_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						YellowPageResult result = (YellowPageResult) AbJsonUtil
								.fromJson(s, YellowPageResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							typeList = result.getData().getTypeList();
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {

							if (pullToRefreshView != null) {
								pullToRefreshView.onHeaderRefreshFinish();
								pullToRefreshView.onFooterLoadFinish();
							}
							if (adapter!=null) {
								adapter.notifyDataSetChanged();	
							}
							Toast.makeText(ParkYellowPageActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	protected void dealResult(YellowPageResult result) {
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			list.addAll(result.getData().getRows());
			pullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			list.clear();
			list.addAll(result.getData().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			list.clear();
			list.addAll(result.getData().getRows());
			pullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (adapter == null) {
			adapter = new ParkYellowPageListAdapter(this, list);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("content", list.get(arg2).getScontent());
		intent.setClass(ParkYellowPageActivity.this,
				ParkYellowDetailsActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightImageLayout:
			tagsList.clear();
			if (typeList != null && typeList.size() > 0) {
				for (int i = 0; i < typeList.size(); i++) {
					tagsList.add(typeList.get(i).getType());
				}
			}
			selectTypePop(tagsList, typeLy, productTypeImg, productType);
			break;
		case R.id.searchBtnLy:
			Intent intent = new Intent();
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			intent.setClass(ParkYellowPageActivity.this,
					ParkYellowSearchActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}

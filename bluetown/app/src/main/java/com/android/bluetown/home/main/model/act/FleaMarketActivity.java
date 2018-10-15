package com.android.bluetown.home.main.model.act;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.next.tagview.TagCloudView;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalDb;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ProductGridAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ProductBean;
import com.android.bluetown.bean.TypeBean;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.FleaMarketListResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.Utils;
import com.android.bluetown.view.DropDownMenu;

/**
 * 
 * @ClassName: FleaMarketActivity
 * @Description:TODO(HomeActivity-跳蚤市场)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:26:55
 * 
 */
public class FleaMarketActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener, OnHeaderRefreshListener,
		OnFooterLoadListener {
//	private LinearLayout productTypeLy, orderNormalTypeLy, typeLy;
//	private ImageView orderNormalTypeImg, productTypeImg;
//	private TextView productType, orderNormalType;
	private GridView productGridView;
	private AbPullToRefreshView mAbPullToRefreshView;
	private ProductGridAdapter myGridViewAdapter = null;
	private ArrayList<ProductBean> mProductList = new ArrayList<ProductBean>();
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private ArrayList<String> productTypeList;
	private ArrayList<String> productTypeIdList;
	private List<TypeBean> typeList;
	private String tid, order;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private SharePrefUtils prefUtils;
	private String[] filerStr = new String[]{"产品分类","默认排序"};
	private List<View> filterViews = new ArrayList<>();
	private View contentView;
	private DropDownMenu mDropDownMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_flea_market);
		BlueTownExitHelper.addActivity(this);
		initUIViews();
		getData();
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
		setRighTextView(R.string.public_goods);
		setTitleLayoutBg(R.color.title_bg_blue);
		searchBtnLy.setVisibility(View.VISIBLE);
		righTextLayout.setOnClickListener(this);
		searchBtnLy.setOnClickListener(this);
	}

	private void initUIViews() {
		try {
			String push = getIntent().getStringExtra("push");
			if (!TextUtils.isEmpty(push)) {
				// 点击查看清空跳蚤市场的推送的消息数
				if (BlueTownApp.fleaMarketMsgCount != 0) {
					BlueTownApp.fleaMarketMsgCount = 0;
					Intent refreshintent = new Intent(
							"com.android.bm.refresh.new.msg.action");
					sendBroadcast(refreshintent);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prefUtils = new SharePrefUtils(this);
		db = FinalDb.create(this);
//		typeLy = (LinearLayout) findViewById(R.id.typeLy);
//		productTypeLy = (LinearLayout) findViewById(R.id.productTypeLy);
//		orderNormalTypeLy = (LinearLayout) findViewById(R.id.orderNormalTypeLy);
//		productType = (TextView) findViewById(R.id.productType);
//		orderNormalType = (TextView) findViewById(R.id.orderNormalType);
//		orderNormalTypeImg = (ImageView) findViewById(R.id.orderNormalTypeImg);
//		productTypeImg = (ImageView) findViewById(R.id.productTypeImg);
		mDropDownMenu = findViewById(R.id.dropdownmenu);
		contentView = LayoutInflater.from(this).inflate(R.layout.flea_marked_view,null);
		mAbPullToRefreshView = (AbPullToRefreshView) contentView
				.findViewById(R.id.mPullRefreshView);
		productGridView = (GridView) contentView.findViewById(R.id.productGridView);
//		productTypeLy.setOnClickListener(this);
//		orderNormalTypeLy.setOnClickListener(this);
		productGridView.setOnItemClickListener(this);
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		mDropDownMenu.setTabTexts(Arrays.asList(filerStr));
	}

//	private void initDropDownView() {
//
//		mDropDownMenu.setDropDownMenu(Arrays.asList(filerStr),);
//	}

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
			if (!TextUtils.isEmpty(userId)) {
			righTextLayout.setClickable(true);
			righTextView.setTextColor(getResources().getColor(R.color.white));
		} else {
			righTextLayout.setClickable(false);
			righTextView
					.setTextColor(getResources().getColor(R.color.gray_btn));
		}
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO(
	 * @param page 当前页，默认第1页 （可填项）
	 * @param rows 每页多少条数据，默认10条（可填项） 
	 * @param tid:商品类型；
	 * @param commodityName：商品名称)
	 * @throws
	 */
	private void getData() {
		// TODO Auto-generated method stub
		params.put("tid", tid);
		params.put("commodityName", "");
		params.put("page", page + "");
		params.put("rows", size + "");
		params.put("order", order);
		params.put("gardenId", prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.FLEA_MARKERT_LIST, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						FleaMarketListResult result = (FleaMarketListResult) AbJsonUtil
								.fromJson(s, FleaMarketListResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							if (mAbPullToRefreshView != null) {
								mAbPullToRefreshView.onFooterLoadFinish();
								mAbPullToRefreshView.onHeaderRefreshFinish();
							}
							if (myGridViewAdapter != null) {
								myGridViewAdapter.notifyDataSetChanged();
							}
							Toast.makeText(FleaMarketActivity.this,
									R.string.no_data, Toast.LENGTH_SHORT)
									.show();

						}

					}
					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						// TODO Auto-generated method stub
						mAbPullToRefreshView.onFooterLoadFinish();
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}
				});
	}

	protected void dealResult(FleaMarketListResult result) {
		typeList = result.getData().getTypeList();
		if (productTypeList == null) {
			productTypeList = new ArrayList<String>();
		}
		if (productTypeIdList == null) {
			productTypeIdList = new ArrayList<String>();
		}
		productTypeIdList.clear();
		productTypeList.clear();
		if (typeList != null && typeList.size() > 0) {
			for (int i = 0; i < typeList.size(); i++) {
				productTypeList.add(typeList.get(i).getTypeName());
				productTypeIdList.add(typeList.get(i).getTid());
			}
		}
		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			mProductList.addAll(result.getData().getCommodity().getRows());
			mAbPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			mProductList.clear();
			mProductList.addAll(result.getData().getCommodity().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			mProductList.clear();
			mProductList.addAll(result.getData().getCommodity().getRows());
			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}
		if (myGridViewAdapter == null) {
			// 使用自定义的Adapter
			myGridViewAdapter = new ProductGridAdapter(this, mProductList,
					false);
			productGridView.setAdapter(myGridViewAdapter);
		}
		myGridViewAdapter.notifyDataSetChanged();
		if (filterViews.size()>0)filterViews.clear();
		View productView = selectTypePop(productTypeList,"product");
		View orderView = selectTypePop(Utils.getOrderNormalList(),"order");
		filterViews.add(productView);
		filterViews.add(orderView);
		mDropDownMenu.setDropDownMenu(filterViews,contentView);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rightTextLayout:
			Intent intent = new Intent();
			intent.putStringArrayListExtra("typesId", productTypeIdList);
			intent.putStringArrayListExtra("types", productTypeList);
			intent.setClass(FleaMarketActivity.this,
					PublishProductActivity.class);
			startActivity(intent);
			break;
//		case R.id.productTypeLy:
//			// 标题栏切换状态
//			productTypeImg.setSelected(true);
//			productTypeImg.setImageResource(R.drawable.type_arrow_up);
//			productType.setTextColor(getResources().getColor(R.color.orange));
//			orderNormalTypeImg.setSelected(false);
//			orderNormalTypeImg.setImageResource(R.drawable.type_arrow_down);
//			orderNormalType.setTextColor(getResources().getColor(
//					R.color.font_gray));
//			selectTypePop(productTypeList, typeLy, productTypeImg, productType,
//					"product");
//			break;
//		case R.id.orderNormalTypeLy:
//			// 标题栏切换状态
//			orderNormalTypeImg.setSelected(true);
//			orderNormalTypeImg.setImageResource(R.drawable.type_arrow_up);
//			orderNormalType.setTextColor(getResources()
//					.getColor(R.color.orange));
//			productTypeImg.setSelected(false);
//			productTypeImg.setImageResource(R.drawable.type_arrow_down);
//			productType
//					.setTextColor(getResources().getColor(R.color.font_gray));
//			List<String> tagList = Utils.getOrderNormalList();
//			selectTypePop(tagList, typeLy, orderNormalTypeImg, orderNormalType,
//					"order");
//			break;
		case R.id.searchBtnLy:
			startActivity(new Intent(FleaMarketActivity.this,
					FleaMarketSearchActivity.class));
			break;
		default:
			break;
		}
	}

	private View selectTypePop(final List<String> tagsList,final String flag) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.type_select_layout, null);
//		// 创建PopupWindow对象
//		final PopupWindow pop = new PopupWindow(view,
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		final TagCloudView selecTagCloudView = (TagCloudView) view
				.findViewById(R.id.tag_cloud_view);
		selecTagCloudView.setTags(tagsList);
		selecTagCloudView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int arg0) {
				// TODO Auto-generated method stub
//				typeImg.setImageResource(R.drawable.type_arrow_selector);
//				typeTextView.setTextColor(getResources().getColor(
//						R.color.font_gray));
				if (flag.equals("product")) {
					if (typeList != null && typeList.size() > 0) {
						tid = typeList.get(arg0).getTid();
						mDropDownMenu.setTabText(arg0 == 0 ? filerStr[0] : productTypeList.get(arg0));
						mDropDownMenu.closeMenu();
					}
				} else if (flag.equals("order")) {
					order = arg0 + "";
//					orderNormalType.setText(tagsList.get(arg0));
					mDropDownMenu.setTabText(arg0 == 0 ? filerStr[1] : tagsList.get(arg0));
					mDropDownMenu.closeMenu();
				}
				mProductList.clear();
				page = 1;
				getData();
//				pop.dismiss();
			}
		});
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("cid", mProductList.get(position).getCid());
		intent.setClass(FleaMarketActivity.this, ProductDetailActivity.class);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, android.util.Pair.create(view.findViewById(R.id.productImg),"productimage")).toBundle());
//			overridePendingTransition(0,0);
//		}else{
			startActivity(intent);
//		}

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
//		mProductList.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

}

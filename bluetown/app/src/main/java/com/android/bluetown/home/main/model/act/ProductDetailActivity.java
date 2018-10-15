package com.android.bluetown.home.main.model.act;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
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
import com.android.bluetown.ImagePagerActivity;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.ProductCommentListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.ProductCommentBean;
import com.android.bluetown.bean.ProductDetails;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.ProductDetailsResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;
import com.android.bluetown.view.slideview.SlidingPlayViewWithDot;
import com.android.bluetown.view.slideview.SlidingPlayViewWithDot.AbOnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * 
 * @ClassName: ProductDetailActivity
 * @Description:TODO(商品详情)
 * @author: shenyz
 * @date: 2015年8月11日 下午3:36:25
 * 
 */
public class ProductDetailActivity extends TitleBarActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener, OnClickListener {
	private LayoutInflater mInflater;
	private ListView mListView;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private SlidingPlayViewWithDot slidingPlayView;
	private LinearLayout commently, collectLy, priaseLy, sellerLy, replyLayout;
	private TextView productName, productPrice, collectCount, priaseCount,
			seller, sellerTel, commentContentNum, productInto;
	private ImageView collect, priase, comment;
	private RoundedImageView sellerImg;
	private EditText commentContent;
	private Button commentBtn;
	private ArrayList<ProductCommentBean> commentList = new ArrayList<ProductCommentBean>();
	private ProductCommentListAdapter listAdapter = null;
	private int priaseNum;
	private String cid;
	private String userId, userIdFlg;
	/** 列表初始化状态 */
	protected int listStatus = Constant.ListStatus.INIT;
	/** 默认第一页 */
	protected int page = 1;
	/** 默认每页10条数据 */
	private int size = 10;
	private String lastCommentTime;
	private SimpleDateFormat format;
	private FinalDb db;
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_product_detail);
		// 延迟共享动画的执行
		postponeEnterTransition();
		BlueTownExitHelper.addActivity(this);
		initUIView();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.product_detail);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setTranslationActivity(false);
	}

	private void initUIView() {
		// TODO Auto-generated method stub
		db = FinalDb.create(this);
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cid = getIntent().getStringExtra("cid");
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
		mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.product_detail_top, null);
		slidingPlayView = (SlidingPlayViewWithDot) view
				.findViewById(R.id.productShowView);
		productName = (TextView) view.findViewById(R.id.productName);
		productPrice = (TextView) view.findViewById(R.id.productPrice);
		productInto = (TextView) view.findViewById(R.id.productInto);
		commently = (LinearLayout) view.findViewById(R.id.commently);
		priaseLy = (LinearLayout) view.findViewById(R.id.priasely);
		collectLy = (LinearLayout) view.findViewById(R.id.collectly);
		sellerLy = (LinearLayout) view.findViewById(R.id.sellerLy);
		seller = (TextView) view.findViewById(R.id.seller);
		sellerImg = (RoundedImageView) view.findViewById(R.id.sellerImg);
		collect = (ImageView) view.findViewById(R.id.collect);
		priase = (ImageView) view.findViewById(R.id.priase);
		comment = (ImageView) view.findViewById(R.id.comment);
		collectCount = (TextView) view.findViewById(R.id.collectCount);
		priaseCount = (TextView) view.findViewById(R.id.priaseCount);
		commentContentNum = (TextView) view.findViewById(R.id.commentNum);
		sellerTel = (TextView) view.findViewById(R.id.sellerTel);
		mListView = (ListView) findViewById(R.id.productCommentList);
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		replyLayout = (LinearLayout) findViewById(R.id.replyLayout);
		commentContent = (EditText) findViewById(R.id.replyContent);
		commentBtn = (Button) findViewById(R.id.replyBtn);
		mListView.addHeaderView(view);
		mListView.setAdapter(null);
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		seller.setOnClickListener(this);
		commently.setOnClickListener(this);
		collectLy.setOnClickListener(this);
		priaseLy.setOnClickListener(this);
		collect.setOnClickListener(this);
		priase.setOnClickListener(this);
		comment.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		sellerTel.setOnClickListener(this);
		sellerLy.setOnClickListener(this);

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

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO(商品id:cid（必填）用户id:userId(非必填)))
	 * @throws
	 */
	private void getData() {
		// TODO Auto-generated method stub

		params.put("userId", userId);
		params.put("cid", cid);
		params.put("page", page + "");
		params.put("rows", size + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.PRODUCT_DETAILS, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						ProductDetailsResult result = (ProductDetailsResult) AbJsonUtil
								.fromJson(s, ProductDetailsResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							mAbPullToRefreshView.onFooterLoadFinish();
							mAbPullToRefreshView.onHeaderRefreshFinish();
							Toast.makeText(ProductDetailActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						}

					}

				});
	}

	private void setSlidePlayView(final ProductDetails details) {
		if (slidingPlayView != null) {
			slidingPlayView.removeAllViews();
			slidingPlayView.stopPlay();
		}
		if (details.getPictruesList() != null
				&& details.getPictruesList().size() > 0) {
			for (int i = 0; i < details.getPictruesList().size(); i++) {
				ImageView slideImg = new ImageView(this);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					slideImg.setTransitionName("productimage");
				}
				slideImg.setScaleType(ScaleType.CENTER_CROP);
				Glide.with(this).load(details.getPictruesList().get(i))
						.placeholder(R.drawable.ic_msg_empty)
						.error(R.drawable.ic_msg_empty)
						.skipMemoryCache(true)
						.centerCrop()
						.crossFade()
						.into(new SimpleTarget<GlideDrawable>() {
							@Override
							public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
								slideImg.setImageDrawable(resource);
								scheduleStartPostponedTransition(slideImg);
							}
						});
//				finalBitmap.display(slideImg, details.getPictruesList().get(i));
				slidingPlayView.addView(slideImg);
			}

			slidingPlayView.startPlay();
			slidingPlayView.setOnItemClickListener(new AbOnItemClickListener() {

				@Override
				public void onClick(int position) {
					// TODO Auto-generated method stub
					// 点击显示原始图片
					ArrayList<String> urls = (ArrayList<String>) details
							.getOrgImgList();
					Intent intent = new Intent(ProductDetailActivity.this,
							ImagePagerActivity.class);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
							position);
					startActivity(intent);

				}
			});
		}

	}

	//共享元素
	private void scheduleStartPostponedTransition(final View sharedElement) {
		sharedElement.getViewTreeObserver().addOnPreDrawListener( new ViewTreeObserver.OnPreDrawListener()
		{
			@TargetApi(Build.VERSION_CODES.LOLLIPOP)
			@Override
			public boolean onPreDraw() {
				sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
				startPostponedEnterTransition();
				return true;
			} });
	}


	protected void dealResult(ProductDetailsResult result) {
		ProductDetails details = result.getData().getCommodity();
		commentContentNum.setText(getString(R.string.comment) + "("
				+ result.getData().getComment().getTotal() + ")");
		if (listAdapter == null) {
			if (!TextUtils.isEmpty(details.getFavourNumber())) {
				priaseNum = Integer.parseInt(details.getFavourNumber());
			}
			userIdFlg = details.getUserId();
			if (userId != null && !"".equals(userId)) {
				if (userId.equals(userIdFlg)) {
					mListView.setPadding(0, 0, 0, 0);
					replyLayout.setVisibility(View.GONE);
				} else {
					commentBtn.setText(getString(R.string.comment));
					replyLayout.setVisibility(View.VISIBLE);
				}
			}
			if (!TextUtils.isEmpty(userIdFlg)) {
				if (userId != null && !"".equals(userId)) {
					if (userId.equals(userIdFlg)) {
						// 卖家不能评论，只能回复
						seller.setText(R.string.is_seller);
					} else {
						// 买家，只能评论不能回复
						seller.setText(R.string.link_seller);
					}
				} else {
					// 买家，只能评论不能回复
					seller.setText(R.string.link_seller);
				}
			}

		}
		productName.setText(details.getCommodityName());
		productPrice.setText("￥" + details.getPrice());
		productInto.setText(details.getContent());
		setSlidePlayView(details);
		finalBitmap.display(sellerImg, details.getHeadImg());
		sellerTel.setText(details.getTell());
		if (details.getIsCollect().equals("1")) {
			collectCount.setText(R.string.collect_cancel);
			collect.setImageResource(R.drawable.praise_p);
		} else {
			collectCount.setText(R.string.collect);
			collect.setImageResource(R.drawable.collect);
		}

		if (details.getIsFavour().equals("1")) {
			priaseCount.setText(getString(R.string.priase) + "(" + priaseNum
					+ ")");
			priase.setImageResource(R.drawable.collect_p);
		} else {
			priaseCount.setText(getString(R.string.priase) + "(" + priaseNum
					+ ")");
			priase.setImageResource(R.drawable.priase);
		}

		switch (listStatus) {
		case Constant.ListStatus.LOAD:
			commentList.addAll(result.getData().getComment().getRows());
			mAbPullToRefreshView.onFooterLoadFinish();
			break;
		case Constant.ListStatus.INIT:
			commentList.clear();
			commentList.addAll(result.getData().getComment().getRows());
			break;
		case Constant.ListStatus.REFRESH:
			commentList.clear();
			commentList.addAll(result.getData().getComment().getRows());
			mAbPullToRefreshView.onHeaderRefreshFinish();
			break;
		}

		if (listAdapter == null) {
			// 使用自定义的Adapter
			listAdapter = new ProductCommentListAdapter(this, commentList,
					mListView, userId, userIdFlg, cid);
			mListView.setAdapter(listAdapter);
		}
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sellerTel:
			if (!TextUtils.isEmpty(sellerTel.getText().toString())) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				// url:统一资源定位符
				// uri:统一资源标示符（更广）
				intent.setData(Uri.parse("tel:"
						+ sellerTel.getText().toString()));
				// 开启系统拨号器
				startActivity(intent);
			}

			break;
		case R.id.sellerLy:
			if (!TextUtils.isEmpty(userIdFlg)) {
				Intent intent = new Intent();
				intent.putExtra("otherUserId", userIdFlg);
				intent.setClass(ProductDetailActivity.this,
						OthersInfoActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.collectly:
			if (!TextUtils.isEmpty(userId)) {
				collect(userId);
			} else {
				TipDialog.showDialogNoClose(ProductDetailActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}
			break;
		case R.id.priasely:
			if (!TextUtils.isEmpty(userId)) {
				praise(userId);
			} else {
				TipDialog.showDialogNoClose(ProductDetailActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}
			break;
		case R.id.commently:

			break;
		case R.id.replyBtn:
			if (!TextUtils.isEmpty(userId)) {
				if (!userId.equals(userIdFlg)) {
					if (!TextUtils.isEmpty(lastCommentTime)) {
						// 买家
						if (isOperateComment(lastCommentTime)) {
							commentPop();
						} else {
							TipDialog.showDialog(ProductDetailActivity.this,
									R.string.tip, R.string.confirm,
									R.string.much_comment_tip);
						}
					} else {
						commentPop();
					}
				}

			} else {
				TipDialog.showDialogNoClose(ProductDetailActivity.this,
						R.string.tip, R.string.confirm,
						R.string.login_info_tip, LoginActivity.class);
			}

			break;
		default:
			break;
		}
	}

	/**
	 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
	 */

	private void collect(String userId) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */
		params.put("userId", userId);
		params.put("actionId", cid);
		params.put("actionType", Constant.COLLOCT + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								collectCount.setText(R.string.collect_cancel);
								collect.setImageResource(R.drawable.collect_p);
							} else {
								collectCount.setText(R.string.collect);
								collect.setImageResource(R.drawable.collect);
							}
						} else {
							TipDialog.showDialog(ProductDetailActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}

					}
				});

	}

	/**
	 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
	 */

	private void praise(String userId) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞 8加入圈子
		 */
		params.put("userId", userId);
		params.put("actionId", cid);
		params.put("actionType", Constant.PRIASE + "");
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								// 未点赞过
								if (priaseNum == 0) {
									priaseNum = priaseNum + 1;
									priaseCount
											.setText(getString(R.string.priase)
													+ "(" + priaseNum + ")");
									priase.setImageResource(R.drawable.praise_p);
								} else {
									priaseNum = priaseNum + 1;
									priaseCount
											.setText(getString(R.string.priase)
													+ "(" + priaseNum + ")");
									priase.setImageResource(R.drawable.praise_p);
								}
							} else {
								priaseNum = priaseNum - 1;
								priaseCount.setText(getString(R.string.priase)
										+ "(" + priaseNum + ")");
								priase.setImageResource(R.drawable.priase);
							}
						} else {
							TipDialog.showDialog(ProductDetailActivity.this,
									R.string.tip, R.string.confirm,
									result.getRepMsg());
						}

					}
				});

	}

	private void commentPop() {
		// TODO Auto-generated method stub
		String commentContentStr = commentContent.getText().toString();
		if (TextUtils.isEmpty(commentContentStr)) {
			TipDialog.showDialog(ProductDetailActivity.this, R.string.tip,
					R.string.confirm, R.string.comment_content_empty);
		} else {
			// 用户id:userId(必填) 商品id：commodityId(必填) 评论内容：content(必填)
			params.put("userId", userId);
			params.put("commodityId", cid + "");
			params.put("content", commentContentStr);
			httpInstance.post(Constant.HOST_URL
					+ Constant.Interface.PRODUCT_COMMENT, params,
					new AbsHttpStringResponseListener(
							ProductDetailActivity.this, null) {
						@Override
						public void onSuccess(int i, String s) {
							Result result = (Result) AbJsonUtil.fromJson(s,
									Result.class);
							if (result.getRepCode().contains(
									Constant.HTTP_SUCCESS)) {
								Toast.makeText(ProductDetailActivity.this,
										getString(R.string.comment_success),
										Toast.LENGTH_LONG).show();
								lastCommentTime = format.format(new Date());
								commentContent.setText("");
								getData();
							} else {
								Toast.makeText(ProductDetailActivity.this,
										result.getRepMsg(), Toast.LENGTH_LONG)
										.show();
							}

						}

					});
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
		commentList.clear();
		listStatus = Constant.ListStatus.REFRESH;
		page = 1;
		getData();
	}

	/**
	 * 
	 * @param time
	 *            上次评论的时间
	 * @return
	 */
	private boolean isOperateComment(String time) {

		String todayDateTime = format.format(new Date());
		try {
			Date nowTime = format.parse(todayDateTime);
			Date createTime = format.parse(time);
			// 相差的毫秒数
			long diff = nowTime.getTime() - createTime.getTime();
			// 秒
			long timeInterval = diff / 1000;
			if (timeInterval >= 10) {
				return true;
			}
			return false;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lastCommentTime = "";
	}

}

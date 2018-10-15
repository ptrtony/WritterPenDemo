package com.android.bluetown.surround;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.LoginActivity;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.GridViewImgAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.CommentBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.bean.MerchantDiscountBean;
import com.android.bluetown.bean.OtherMerchant;
import com.android.bluetown.bean.RecommendBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.SweetAlertDialog.OnSweetClickListener;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.CanteenFoodDetailsResult;
import com.android.bluetown.result.CanteenFoodDetailsResult.CanteenDetails;
import com.android.bluetown.result.CanteenFoodDetailsResult.CanteenMerchant;
import com.android.bluetown.result.OtherMerchantDetailsResult;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.FileUtils;
import com.android.bluetown.utils.ShareUtils;
import com.android.bluetown.view.NoScrollGridView;
import com.android.bluetown.view.RoundedImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

/**
 * 餐厅详情、商家详情、其他商家详情
 *
 * @author shenyz
 *
 */
@SuppressLint({ "NewApi", "InflateParams", "SimpleDateFormat" })
public class MerchantDetailsActivity extends TitleBarActivity implements
		OnClickListener {
	private RoundedImageView commentHeadImg;
	private ImageView merchantCoverImg, collectShop, todayDishImg, priase;
	private ImageView[] ivStars = new ImageView[5];
	private TextView merchantShopName, canteenPrice, businessTime,
			merchantAddress, merchantPhone, merchantCommentCount, dishName,
			dishPrice, nickname, commentTime, contentComment,
			merchantInfoTitle, canteenDes;
	private LinearLayout shopImgs;
	private NoScrollGridView commentImgGrid;
	// 订座，点菜
	private Button order, book;
	// 订座点菜、评论、今日推荐/老板推荐菜、点评标题的LinearLayout
	private LinearLayout orderBookLy, commentLy, recommendishesLy,
			commentTitleLy;
	private SharePrefUtils preUtils;
	// flag（食堂、餐厅、其他），商家id，商家名称
	private String flag, mid, merchantName;
	private CanteenMerchant mDetails;
	private OtherMerchant otherMerchant;
	private String userId;
	private FinalDb db;
	private List<MemberUser> users;
	private String isClosed;
	private List<MerchantDiscountBean> discountBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_merchant_details);
		initUIView();
		orderDiscount();
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBgRes(R.color.title_bg_blue);
		// setRightImageView(R.drawable.ic_share);
		// rightImageLayout.setOnClickListener(this);
		rightImageLayout.setVisibility(View.INVISIBLE);
	}

	private void initUIView() {
		db = FinalDb.create(this);
		mid = getIntent().getStringExtra("meid");
		preUtils = new SharePrefUtils(this);
		merchantCoverImg = (ImageView) findViewById(R.id.merchantCoverImg);
		collectShop = (ImageView) findViewById(R.id.collectShop);
		merchantShopName = (TextView) findViewById(R.id.merchantShopName);
		canteenPrice = (TextView) findViewById(R.id.canteenPrice);
		businessTime = (TextView) findViewById(R.id.businessTime);
		merchantAddress = (TextView) findViewById(R.id.merchantAddress);
		merchantPhone = (TextView) findViewById(R.id.merchantPhone);
		merchantCommentCount = (TextView) findViewById(R.id.merchantCommentCount);
		shopImgs = (LinearLayout) findViewById(R.id.shopImgs);
		todayDishImg = (ImageView) findViewById(R.id.todayDishImg);
		priase = (ImageView) findViewById(R.id.priase);
		dishName = (TextView) findViewById(R.id.dishName);
		dishPrice = (TextView) findViewById(R.id.dishPrice);
		canteenDes = (TextView) findViewById(R.id.canteenDes);
		merchantInfoTitle = (TextView) findViewById(R.id.merchantInfoTitle);
		recommendishesLy = (LinearLayout) findViewById(R.id.recommendishesLy);
		ivStars[0] = (ImageView) findViewById(R.id.iv_star_1);
		ivStars[1] = (ImageView) findViewById(R.id.iv_star_2);
		ivStars[2] = (ImageView) findViewById(R.id.iv_star_3);
		ivStars[3] = (ImageView) findViewById(R.id.iv_star_4);
		ivStars[4] = (ImageView) findViewById(R.id.iv_star_5);
		commentHeadImg = (RoundedImageView) findViewById(R.id.commentHeadImg);
		nickname = (TextView) findViewById(R.id.nickname);
		commentTime = (TextView) findViewById(R.id.commentTime);
		contentComment = (TextView) findViewById(R.id.contentComment);
		commentImgGrid = (NoScrollGridView) findViewById(R.id.commentImgList);
		commentLy = (LinearLayout) findViewById(R.id.commentLy);
		commentTitleLy = (LinearLayout) findViewById(R.id.commentTitleLy);
		book = (Button) findViewById(R.id.bookSeat);
		order = (Button) findViewById(R.id.order);
		orderBookLy = (LinearLayout) findViewById(R.id.orderBookLy);
		collectShop.setOnClickListener(this);
		merchantAddress.setOnClickListener(this);
		merchantPhone.setOnClickListener(this);
		merchantCommentCount.setOnClickListener(this);
		book.setOnClickListener(this);
		order.setOnClickListener(this);
		recommendishesLy.setOnClickListener(this);
		flag = BlueTownApp.DISH_TYPE;
		// 餐厅详情
		if (flag.equals("canteen")) {
			setTitleView(R.string.canteen_details);
			commentLy.setVisibility(View.VISIBLE);
			orderBookLy.setVisibility(View.VISIBLE);
			merchantInfoTitle.setText(R.string.dailry_dishes);
			recommendishesLy.setVisibility(View.VISIBLE);
			// 评论暂时去掉
			commentTitleLy.setVisibility(View.GONE);
		} else {
			// 商家详情
			setTitleView(R.string.merchant_details_title);
			// 其他商家详情
			if (flag.equals("other")) {
				commentLy.setVisibility(View.GONE);
				orderBookLy.setVisibility(View.GONE);
				recommendishesLy.setVisibility(View.GONE);
				merchantInfoTitle.setText(R.string.merchant_info);
				commentTitleLy.setVisibility(View.GONE);
			} else {
				// 美食商家详情
				commentLy.setVisibility(View.VISIBLE);
				orderBookLy.setVisibility(View.VISIBLE);
				merchantInfoTitle.setText(R.string.boss_recommend);
				recommendishesLy.setVisibility(View.VISIBLE);
				commentTitleLy.setVisibility(View.GONE);
			}
		}
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
		getMerchantDetails(mid);
		if (BlueTownApp.getDishList() != null) {
			BlueTownApp.getDishList().clear();
			BlueTownApp.setDishesCount(0);
			BlueTownApp.setOriginalPrice("0.00");
			BlueTownApp.setDishesPrice("0.00");
		}
	}

	/**
	 * 获取商家详情
	 *
	 * @param mid
	 */
	private void getMerchantDetails(String mid) {
		// TODO Auto-generated method stub
		params.put("meid", mid);
		params.put("userId", userId);
		String url = "";
		if (flag.equals("canteen")) {
			url = Constant.HOST_URL + Constant.Interface.CANTEEN_DETAILS;
		} else if (flag.equals("other")) {
			url = Constant.HOST_URL + Constant.Interface.OTHER_MERCHANT_DETAILS;
		} else {
			url = Constant.HOST_URL + Constant.Interface.MERCHANT_DETAILS;
		}

		httpInstance.post(url, params, new AbsHttpStringResponseListener(this,
				null) {
			@Override
			public void onSuccess(int i, String s) {
				if (flag.equals("canteen") || flag.equals("food")) {
					CanteenFoodDetailsResult result = (CanteenFoodDetailsResult) AbJsonUtil
							.fromJson(s, CanteenFoodDetailsResult.class);
					if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
						dealResult(result);
					} else if (result.getRepCode().contains(
							Constant.HTTP_NO_DATA)) {
						Toast.makeText(MerchantDetailsActivity.this,
								R.string.no_data, Toast.LENGTH_LONG).show();

					} else {
						Toast.makeText(MerchantDetailsActivity.this,
								result.getRepMsg(), Toast.LENGTH_LONG).show();

					}
				} else {
					OtherMerchantDetailsResult result = (OtherMerchantDetailsResult) AbJsonUtil
							.fromJson(s, OtherMerchantDetailsResult.class);
					if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
						dealOtherResult(result);
					} else if (result.getRepCode().contains(
							Constant.HTTP_NO_DATA)) {
						Toast.makeText(MerchantDetailsActivity.this,
								R.string.no_data, Toast.LENGTH_LONG).show();

					} else {
						Toast.makeText(MerchantDetailsActivity.this,
								result.getRepMsg(), Toast.LENGTH_LONG).show();

					}
				}

			}

			private void dealResult(CanteenFoodDetailsResult result) {
				// TODO Auto-generated method stub
				CanteenDetails canteenDetails = result.getData();
				mDetails = canteenDetails.getMerchant();
				merchantName = mDetails.getMerchantName();
				isClosed=mDetails.getIsClosed();
				result.getData().getRecommend();
				if (!TextUtils.isEmpty(mDetails.getHeadImg())) {
					imageLoader.displayImage(mDetails.getLogoImg(),
							merchantCoverImg);

				} else {
					merchantCoverImg.setImageResource(R.drawable.ic_msg_empty);
				}
				canteenDes.setText("食堂描述:" + mDetails.getContent());
				merchantShopName.setText(mDetails.getMerchantName());
				BlueTownApp.SHARE_TITLE = mDetails.getMerchantName();
				BlueTownApp.SHARE_CONTENT = "这家餐厅很不错，推荐给你！";
				BlueTownApp.SHARE_IMAGE = mDetails.getHeadImg();
				canteenPrice.setText("人均：" + mDetails.getConsumption() + "元/人");
				if (!TextUtils.isEmpty(mDetails.getStartTime())
						&& !TextUtils.isEmpty(mDetails.getEndTime())) {
					businessTime.setText("营业时间：" + mDetails.getStartTime()
							+ "-" + mDetails.getEndTime());
				} else {
					businessTime.setText("营业时间：");
				}
				merchantAddress.setText(mDetails.getMerchantAddress());
				merchantPhone.setText("商家电话：" + mDetails.getMerchantTell());
				String isCollect = result.getData().getIsCollect().trim();
				List<String> merchantList = mDetails.getMerchantImgList();
				List<String> orgImgList = mDetails.getOrgImgList();
				if (merchantList != null && merchantList.size() > 0) {
					shopImgs.removeAllViews();
					shopImgs.setVisibility(View.VISIBLE);
					setShopImages(merchantList, orgImgList);
				} else {
					shopImgs.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(isCollect)) {
					// 已收藏
					if (isCollect.equals("1")) {
						collectShop
								.setImageResource(R.drawable.ic_merchant_collect_p);
					} else {
						collectShop
								.setImageResource(R.drawable.ic_merchant_collect);
					}
				} else {
					collectShop
							.setImageResource(R.drawable.ic_merchant_collect);
				}
				setRecommendData(canteenDetails);
				// setCommentData(canteenDetails);
			}

			/**
			 * 设置推荐数据
			 *
			 * @param canteenDetails
			 */
			private void setRecommendData(CanteenDetails canteenDetails) {
				RecommendBean recommend = canteenDetails.getRecommend();
				if (recommend != null) {
					dishPrice.setVisibility(View.VISIBLE);
					priase.setVisibility(View.VISIBLE);
					if (!TextUtils.isEmpty(recommend.getHomeImg())) {
						imageLoader.displayImage(recommend.getHomeImg(),
								todayDishImg);
					} else {
						imageLoader.displayImage("drawable://"
										+ R.drawable.ic_msg_empty, todayDishImg,
								defaultOptions);
					}

					dishName.setText(recommend.getDishesName());
					if (!TextUtils.isEmpty(recommend.getFavorablePrice())) {
						String favorableprice = "￥"
								+ recommend.getFavorablePrice();
						setMuilFontSize(favorableprice);
					}
				} else {
					dishName.setText(R.string.no_dishes);
					dishPrice.setVisibility(View.INVISIBLE);
					priase.setVisibility(View.INVISIBLE);
				}
			}

			/**
			 * 设置点评 数据
			 *
			 * @param canteenDetails
			 */
			private void setCommentData(CanteenDetails canteenDetails) {
				String commentNumber = canteenDetails.getCommentNumber();
				if (!TextUtils.isEmpty(commentNumber)) {
					merchantCommentCount
							.setText("点评(总共" + commentNumber + "条)");
				} else {
					merchantCommentCount.setText("点评(总共0条)");
				}

				CommentBean comment = canteenDetails.getMerchantComment();
				if (comment != null) {
					commentLy.setVisibility(View.VISIBLE);
					if (!TextUtils.isEmpty(comment.getHeadImg())) {
						imageLoader.displayImage(comment.getHeadImg(),
								commentHeadImg, defaultOptions);
					} else {
						imageLoader.displayImage("drawable://"
										+ R.drawable.ic_msg_empty, commentHeadImg,
								defaultOptions);
					}
					// 评分
					String grade = comment.getStar();
					if (!TextUtils.isEmpty(grade)) {
						showStars(Integer.parseInt(grade));
					}
					nickname.setText(comment.getNickName());
					setCreateTime(commentTime, comment.getCommentTime());
					contentComment.setText(comment.getContent());
					List<String> merchantShopImgs = comment.getCommentImgList();
					if (merchantShopImgs != null && merchantShopImgs.size() > 0) {
						commentImgGrid.setVisibility(View.VISIBLE);
						commentImgGrid.setAdapter(new GridViewImgAdapter(
								MerchantDetailsActivity.this, merchantShopImgs,
								120, 80));
					} else {
						commentImgGrid.setVisibility(View.GONE);
					}
				} else {
					commentLy.setVisibility(View.GONE);
				}
			}

			/**
			 * 设置评论时间
			 *
			 * @param mHolder
			 * @param item
			 */
			private void setCreateTime(TextView dateTextView, String date) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String todayDateTime = format.format(new Date());
				try {
					Date nowTime = format.parse(todayDateTime);
					Date createTime = format.parse(date);
					// 相差的毫秒数
					long diff = nowTime.getTime() - createTime.getTime();
					// 秒
					long timeInterval = diff / 1000;
					if (timeInterval < 60) {
						dateTextView.setText("刚刚");
					} else if ((timeInterval / 60) < 60) {
						dateTextView.setText((timeInterval / 60) + "分钟前");
					} else if ((timeInterval / (60 * 60)) < 24) {
						dateTextView
								.setText((timeInterval / (60 * 60)) + "小时前");
					} else {
						dateTextView.setText(date);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dateTextView.setText(date);
				}
			}

			/**
			 * 设置一个字符串多中字体
			 *
			 * @param favorableprice
			 */
			private void setMuilFontSize(String favorableprice) {
				SpannableString mSpannableString = new SpannableString(
						favorableprice);
				mSpannableString.setSpan(new AbsoluteSizeSpan(20, true),
						favorableprice.indexOf("￥") + 1,
						favorableprice.indexOf("."),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				dishPrice.setText(mSpannableString);
			}

			/**
			 * 展示评论的分数
			 *
			 * @param stars
			 */
			private void showStars(int stars) {
				for (int i = 0; i < ivStars.length; i++) {
					if (i < stars) {
						ivStars[i]
								.setImageResource(R.drawable.ic_comment_start);
					} else {
						ivStars[i].setVisibility(View.GONE);
					}
				}
			}

			/**
			 * 其他商家詳情
			 *
			 * @param result
			 */
			private void dealOtherResult(OtherMerchantDetailsResult result) {
				// TODO Auto-generated method stub
				otherMerchant = result.getData();
				merchantName = otherMerchant.getMerchantName();
				if (!TextUtils.isEmpty(otherMerchant.getHeadImg())) {
					imageLoader.displayImage(otherMerchant.getHeadImg(),
							merchantCoverImg);
				} else {
					merchantCoverImg.setImageResource(R.drawable.ic_msg_empty);
				}
				merchantShopName.setText(otherMerchant.getMerchantName());
				canteenDes.setText(otherMerchant.getContent());
				BlueTownApp.SHARE_TITLE = otherMerchant.getMerchantName();
				BlueTownApp.SHARE_CONTENT = otherMerchant.getMerchantName()
						+ "餐厅很不错哦,推荐给你！";
				BlueTownApp.SHARE_IMAGE = otherMerchant.getHeadImg();
				canteenPrice.setText("人均：" + otherMerchant.getConsumption()
						+ "元/人");
				businessTime.setText("营业时间：" + otherMerchant.getStartTime()
						+ "-" + otherMerchant.getEndTime());
				merchantAddress.setText(otherMerchant.getMerchantAddress());
				merchantPhone.setText("商家电话：" + otherMerchant.getMerchantTell());
				String isCollect = otherMerchant.getIsCollect().trim();
				List<String> merchantList = otherMerchant.getMerchantImgList();
				List<String> orgImgList = otherMerchant.getOrgImgList();
				if (merchantList != null && merchantList.size() > 0) {
					setShopImages(merchantList, orgImgList);
				}
				if (!TextUtils.isEmpty(isCollect)) {
					// 已收藏
					if (isCollect.equals("1")) {
						collectShop
								.setImageResource(R.drawable.ic_merchant_collect_p);
					} else {
						collectShop
								.setImageResource(R.drawable.ic_merchant_collect);
					}
				} else {
					collectShop
							.setImageResource(R.drawable.ic_merchant_collect);
				}
			}

			private void setShopImages(final List<String> merchantList,
									   final List<String> orgImgList) {
				for (int i = 0; i < merchantList.size(); i++) {
					LayoutInflater inflater = LayoutInflater
							.from(MerchantDetailsActivity.this);
					View view = inflater.inflate(R.layout.item_image, null);
					LayoutParams params = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.rightMargin = 10;
					view.setLayoutParams(params);
					ImageView imageView = (ImageView) view
							.findViewById(R.id.img);
					imageLoader.displayImage(merchantList.get(i), imageView);
					shopImgs.addView(view);
					final String clickItem = merchantList.get(i);
					imageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							selectImgsPop(orgImgList,
									merchantList.indexOf(clickItem));
						}
					});

				}

			}
		});
	}

	private void selectImgsPop(final List<String> dishImgList, int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.ac_image_scan, null);
		// 创建PopupWindow对象
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		final ViewPager pager = (ViewPager) view
				.findViewById(R.id.gl_images_view);
		final ImageView iv_arrow_left = (ImageView) view
				.findViewById(R.id.iv_arrow_left);
		final ImageView iv_arrow_right = (ImageView) view
				.findViewById(R.id.iv_arrow_right);
		iv_arrow_left.setVisibility(View.VISIBLE);
		iv_arrow_right.setVisibility(View.VISIBLE);
		final ImageAdapter adapter = new ImageAdapter(dishImgList, pop, pager);
		pager.setAdapter(adapter);
		pager.setCurrentItem(position);
		iv_arrow_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int positions = pager.getCurrentItem();
				if (positions == 0) {
					return;
				}
				positions = positions - 1;
				pager.setCurrentItem(positions);
				adapter.notifyDataSetChanged();
			}
		});
		iv_arrow_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int positions = pager.getCurrentItem();
				if (positions == dishImgList.size() - 1) {
					return;
				}
				positions = positions + 1;
				pager.setCurrentItem(positions);
				adapter.notifyDataSetChanged();
			}
		});

		if ((dishImgList != null && dishImgList.size() == 1)
				|| position == dishImgList.size() - 1) {
			iv_arrow_right.setVisibility(View.GONE);
			iv_arrow_left.setVisibility(View.VISIBLE);
		}
		if (position == 0) {
			iv_arrow_left.setVisibility(View.GONE);
			iv_arrow_right.setVisibility(View.VISIBLE);
		}
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				// 当滑动时，顶部的imageView是通过animation缓慢的滑动
				if (arg0 == 0) {
					iv_arrow_left.setVisibility(View.GONE);
				} else {
					iv_arrow_left.setVisibility(View.VISIBLE);
				}
				if (arg0 == dishImgList.size() - 1) {
					iv_arrow_right.setVisibility(View.GONE);
				} else {
					iv_arrow_right.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		pop.showAsDropDown(titleLayout);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.rightImageLayout:
				// 分享
				ShareUtils.showShare(this, BlueTownApp.SHARE_TITLE,
						BlueTownApp.SHARE_CONTENT, BlueTownApp.SHARE_IMAGE);
				break;
			case R.id.collectShop:
				if (!TextUtils.isEmpty(userId)) {
					collectMerchant(userId);
				} else {
					TipDialog.showDialogNoClose(MerchantDetailsActivity.this,
							R.string.tip, R.string.confirm,
							R.string.login_info_tip, LoginActivity.class);
				}

				break;
			case R.id.merchantAddress:
				Intent intent = new Intent();
				intent.putExtra("flag", flag);
				if (flag.equals("other")) {
					intent.putExtra("object", otherMerchant);
				} else {
					intent.putExtra("object", mDetails);
				}
				intent.setClass(MerchantDetailsActivity.this, MapViewActivity.class);
				startActivity(intent);
				break;
			case R.id.merchantPhone:
				String phone = merchantPhone.getText().toString().trim();
				if (!TextUtils.isEmpty(phone) && phone.length() > 0) {
					showDialog(MerchantDetailsActivity.this, R.string.confirm,
							R.string.call_up_tip, R.string.cancel, phone);
				}
				break;
			case R.id.merchantCommentCount:
				if (!TextUtils.isEmpty(userId)) {
					Intent commentIntent = new Intent();
					commentIntent.putExtra("meid", mid);
					commentIntent.setClass(MerchantDetailsActivity.this,
							CommentListActivity.class);
					startActivity(commentIntent);
				} else {
					TipDialog.showDialogNoClose(MerchantDetailsActivity.this,
							R.string.tip, R.string.confirm,
							R.string.login_info_tip, LoginActivity.class);
				}

				break;
			case R.id.bookSeat:
//			if (!TextUtils.isEmpty(userId)) {
//				// 预定
//				Intent bookIntent = new Intent();
//				bookIntent.putExtra("meid", mid);
//				bookIntent.putExtra("merchantName", merchantName);
//				// 不是继续点菜
//				BlueTownApp.ORDER_TYPE = "other";
//				// 点菜顺序
//				BlueTownApp.ORDER_BY = "0";
//				// 预订-tableactivity--=下一步
//				BlueTownApp.BOOK_OR_ORDER = "0";
//				// 点击预定，将默认值改为只订座
//				BlueTownApp.orderType = "0";
//				bookIntent.setClass(MerchantDetailsActivity.this,
//						TableBookActivity.class);
//				startActivity(bookIntent);
//			} else {
//				TipDialog.showDialogNoClose(MerchantDetailsActivity.this,
//						R.string.tip, R.string.confirm,
//						R.string.login_info_tip, LoginActivity.class);
//			}
				break;
			case R.id.order:
				if (!TextUtils.isEmpty(userId)) {
					// 点菜
					Intent orderIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("meid", mid);
					bundle.putString("merchantName", merchantName);
					bundle.putString("isClosed", isClosed);
					orderIntent.putExtras(bundle);
					// 点菜-tableactivity--完成
					BlueTownApp.BOOK_OR_ORDER = "1";
					// 不是继续点菜
					BlueTownApp.ORDER_TYPE = "other";
					// 点菜顺序
					BlueTownApp.ORDER_BY = "1";
					BlueTownApp.setDishList(null);
					BlueTownApp.setOrderDishList(null);
					BlueTownApp.setDishesCount(0);
					BlueTownApp.ACTIVITY_ACTION = "";
					BlueTownApp.setDishesPrice("0.00");
					BlueTownApp.setOriginalPrice("0.00");
					orderIntent.setClass(MerchantDetailsActivity.this,
							RecommendDishActivity.class);
					startActivity(orderIntent);
				} else {
					TipDialog.showDialogNoClose(MerchantDetailsActivity.this,
							R.string.tip, R.string.confirm,
							R.string.login_info_tip, LoginActivity.class);
				}
				break;
			case R.id.recommendishesLy:
				if (!TextUtils.isEmpty(userId)) {
//				if(isClosed.equals("1")){
//					return;
//				}
					Intent recommendIntent = new Intent();
					recommendIntent.putExtra("meid", mid);
					recommendIntent.putExtra("merchantName", merchantName);
					recommendIntent.putExtra("isClosed", isClosed);
					// 点菜顺序
					BlueTownApp.ORDER_BY = "1";
					// 不是继续点菜
					BlueTownApp.ORDER_TYPE = "other";
					BlueTownApp.BOOK_OR_ORDER = "1";
					BlueTownApp.setDishList(null);
					BlueTownApp.setOrderDishList(null);
					BlueTownApp.setDishesCount(0);
					BlueTownApp.ACTIVITY_ACTION = "";
					BlueTownApp.setDishesPrice("0.00");
					BlueTownApp.setOriginalPrice("0.00");
					recommendIntent.setClass(MerchantDetailsActivity.this,
							RecommendDishActivity.class);
					startActivity(recommendIntent);
				} else {
					TipDialog.showDialogNoClose(MerchantDetailsActivity.this,
							R.string.tip, R.string.confirm,
							R.string.login_info_tip, LoginActivity.class);
				}
				break;
			default:
				break;
		}
	}

	private void collectMerchant(String userId) {
		/*
		 * userId 用户id actionID 收藏(关注或点赞)对象的id actionType 5：关注，6：收藏，7：点赞
		 * 8：加入圈子，9：报名,10:收藏商家
		 */
		params.put("userId", userId);
		params.put("actionId", mid);
		params.put("actionType", Constant.COLLECT_MERCHANT + "");
		httpInstance.post(Constant.HOST_URL
						+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								collectShop
										.setImageResource(R.drawable.ic_merchant_collect_p);
							} else {
								collectShop
										.setImageResource(R.drawable.ic_merchant_collect);
							}
						} else {
							Toast.makeText(MerchantDetailsActivity.this,
									result.getRepMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

	}

	/**
	 * 是否要拨打商家电话
	 *
	 * @param context
	 * @param titleId
	 *            对话框标题
	 * @param confirmTextId
	 *            确认按钮的内容
	 * @param contentStr
	 *            对话框内容
	 */
	public void showDialog(final Context context, int confirmTextId,
						   int contentStr, int cancelTextId, final String phone) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelTextId));
		dialog.setContentText(context.getString(contentStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("tel:" + phone);
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
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

	private void doloadImg(ViewPager viewPager, List<String> dishImgList) {
		int current = viewPager.getCurrentItem();
		String urlString = dishImgList.get(current);
		String name = urlString.substring(urlString.lastIndexOf("/") + 1);
		if (FileUtils.getFilePath(this, name).exists()) {
			showDialog(this, R.string.tip, R.string.confirm, R.string.is_file,
					R.string.cancel, urlString);
		} else {
			FileUtils.loadImg(this, name, urlString);
		}
	}

	public class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		private List<String> dishImgList;
		private PopupWindow popupWindow;
		private ViewPager viewPager;

		public ImageAdapter() {
			// TODO Auto-generated constructor stub
		}

		public ImageAdapter(List<String> dishImgList, PopupWindow popupWindow,
							ViewPager viewPager) {
			this.dishImgList = dishImgList;
			this.popupWindow = popupWindow;
			this.viewPager = viewPager;
			inflater = LayoutInflater.from(MerchantDetailsActivity.this);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return dishImgList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_scan_image,
					view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			String imgUrlString = dishImgList.get(position);
			imageView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					doloadImg(viewPager, dishImgList);
					return false;
				}
			});
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
			ImageLoader.getInstance().displayImage(imgUrlString, imageView,
					BlueTownApp.defaultOptions,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
													FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
								case IO_ERROR:
								case DECODING_ERROR:
								case NETWORK_DENIED:
								case OUT_OF_MEMORY:
								case UNKNOWN:
									message = "图片加载失败";
									break;
							}

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
													  View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

	}

	private void showDialog(final Activity context, int titleId,
							int confirmTextId, int contentStr, int cancelStr,
							final String nameString) {
		SweetAlertDialog dialog = new SweetAlertDialog(context)
				.setContentText(context.getString(contentStr));
		dialog.setTitleText(context.getString(titleId));
		dialog.setConfirmText(context.getString(confirmTextId));
		dialog.setCancelText(context.getString(cancelStr));
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				FileUtils.loadImg(context,
						nameString.substring(nameString.lastIndexOf("/") + 1),
						nameString);
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

	/**
	 * 订单折扣
	 */
	private void orderDiscount(){
		params.put("merchantId",mid);
		httpUtil.get(Constant.HOST_URL1+Constant.Interface.MERCHANT_DISCOUNT,params, new AbsHttpStringResponseListener(MerchantDetailsActivity.this) {
			@Override
			public void onSuccess(int i, String s) {
				discountBean = new Gson().fromJson(s,new TypeToken<List<MerchantDiscountBean>>(){}.getType());
				BlueTownApp.setOrderDiscountList(discountBean);
			}
		});
	}
}

package com.android.bluetown.adapter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.PostBean;
import com.android.bluetown.circle.activity.CircleActivity;
import com.android.bluetown.circle.activity.PostDetailsActivity;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.img.ShowBigPicture;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.UserOperationResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;

import static com.android.bluetown.img.ShowBigPicture.HEIGHT;
import static com.android.bluetown.img.ShowBigPicture.LEFT;
import static com.android.bluetown.img.ShowBigPicture.TOP;
import static com.android.bluetown.img.ShowBigPicture.WIDTH;

/**
 * 
 * @ClassName: PostAdapter
 * @Description:TODO(帖子的Adapter)
 * @author: shenyz
 * @date: 2015年8月19日 上午10:25:28
 * 
 */

public class PostAdapter extends BaseContentAdapter {
	private static final String CLIKE_LIKE = "like";
	private SharePrefUtils sharePrefUtils;
	public PostAdapter() {
		// TODO Auto-generated constructor stub
	}
	public PostAdapter(Context mContext, List<?> data) {
		super(mContext, data);
		sharePrefUtils = new SharePrefUtils(context);
		sharePrefUtils.setBoolean(CLIKE_LIKE,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_post, null);
			mHolder = new ViewHolder();
			mHolder.postTitle = (TextView) convertView
					.findViewById(R.id.postTitle);
			mHolder.postContent = (TextView) convertView
					.findViewById(R.id.postContent);
			mHolder.postUserName = (TextView) convertView
					.findViewById(R.id.tv_circle_nick);
			mHolder.postDate = (TextView) convertView
					.findViewById(R.id.tv_circle_time);
			mHolder.postCommentCount = (TextView) convertView
					.findViewById(R.id.postCommentCount);
			mHolder.postImgs = (LinearLayout) convertView
					.findViewById(R.id.postImgs);
			mHolder.mCricleNick = convertView.findViewById(R.id.iv_head_nick);
			mHolder.postImgs1 = convertView.findViewById(R.id.postImgs1);
			mHolder.postComment = convertView.findViewById(R.id.rl_click_comment);
			mHolder.mCricleNick = convertView.findViewById(R.id.iv_head_nick);
			mHolder.postLike = convertView.findViewById(R.id.rl_click_like);
			mHolder.postTextLike = convertView.findViewById(R.id.tv_like);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);
		return convertView;
	}

	/**
	 * 设置评论时间
	 * 
	 * @param mHolder
	 * @param item
	 */
	private void setCreateTime(ViewHolder mHolder, final PostBean item) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String todayDateTime = format.format(new Date());
		try {
			Date nowTime = format.parse(todayDateTime);
			Date createTime = format.parse(item.getCreateTime());
			// 相差的毫秒数
			long diff = nowTime.getTime() - createTime.getTime();
			// 秒
			long timeInterval = diff / 1000;
			if (timeInterval < 60) {
				mHolder.postDate.setText("刚刚");
			} else if ((timeInterval / 60) < 60) {
				mHolder.postDate.setText((timeInterval / 60) + "分钟前");
			} else if ((timeInterval / (60 * 60)) < 24) {
				mHolder.postDate.setText((timeInterval / (60 * 60)) + "小时前");
			} else {
				mHolder.postDate.setText(item.getCreateTime());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mHolder.postDate.setText(item.getCreateTime());
		}
	}

	private void setData(ViewHolder mHolder, final int position) {
		// TODO Auto-generated method stub
		final PostBean item = (PostBean) getItem(position);
		mHolder.postTitle.setText(item.getManagementName());
		String content = item.getContent();
		setLikeData(mHolder,item);
		if (content != null && !content.isEmpty()) {
			mHolder.postContent.setVisibility(View.VISIBLE);
			mHolder.postContent.setText(content);
		} else {
			mHolder.postContent.setVisibility(View.GONE);
		}
		mHolder.postImgs1.removeAllViews();
		imageLoader.displayImage(item.getHeadImg(),mHolder.mCricleNick);
		mHolder.postUserName.setText(item.getNickName());
		setCreateTime(mHolder, item);
		mHolder.postCommentCount.setText(item.getManagementNumber());
		mHolder.postLike.setTag(position);
		mHolder.postLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!sharePrefUtils.getBoolean(CLIKE_LIKE,false)){
					Animation animation = AnimationUtils.loadAnimation(context,R.anim.zoom_like);
					Drawable drawable = context.getResources()
							.getDrawable(R.drawable.timeline_icon_like);
					drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
					mHolder.postTextLike.setCompoundDrawables(drawable,null, null, null);
					mHolder.postTextLike.setText(Integer.parseInt(item.getApplyNumber())+1+"");
					mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.font_orange));
					mHolder.postTextLike.startAnimation(animation);
					sharePrefUtils.setBoolean(CLIKE_LIKE,true);
					item.setIsPraise("1");
				}else{
					Drawable drawable = context.getResources()
							.getDrawable(R.drawable.timeline_icon_unlike);
					drawable.setBounds(0, 0,
							drawable.getMinimumWidth(),
							drawable.getMinimumHeight()); // 设置边界
					mHolder.postTextLike.setCompoundDrawables(drawable,
							null, null, null);
					if (Integer.parseInt(item.getApplyNumber())==0){
						mHolder.postTextLike.setText("赞");
					}
					mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.text_gray));
					sharePrefUtils.setBoolean(CLIKE_LIKE,false);
					item.setIsPraise("0");
				}

			}
		});

		mHolder.postComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("mid", item.getMid());
				intent.setClass((CircleActivity)context, PostDetailsActivity.class);
				((CircleActivity) context).startActivity(intent);
				((CircleActivity) context).overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
			}
		});

		if (item.getPicturesList().size() == 0) {
//			mHolder.postImgsCount.setVisibility(View.GONE);
			mHolder.postImgs.setVisibility(View.GONE);
			mHolder.postImgs1.setVisibility(View.GONE);

		} else {
//			mHolder.postImgsCount.setVisibility(View.VISIBLE);
			mHolder.postImgs.setVisibility(View.VISIBLE);
//			mHolder.postImgsCount.setText("共" + item.getPicturesList().size()
//					+ "张");
			mHolder.postImgs.removeAllViews();
			final List<String> picturesList = item.getPicturesList();
			int size = picturesList.size();
			// 只显示3张
			if (size > 1) {
				for (int i = 0; i < size; i++) {
					ImageView imageView = new ImageView(context);
					imageView.setTag(i);
					if (!TextUtils.isEmpty(picturesList.get(i))&&picturesList.get(i)!=null){
						imageLoader.displayImage(picturesList.get(i), imageView,
								BlueTownApp.defaultOptions);
					}

					imageView.setScaleType(ScaleType.FIT_XY);
					Resources resources = context.getResources();
					DisplayMetrics dm = resources.getDisplayMetrics();
					int width = dm.widthPixels;
					int imageWidth = width/3;
					int imageHieght = imageWidth;
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							imageWidth, imageHieght, 1.0f);
					params.setMargins(10, 0, 0, 0);
					imageView.setLayoutParams(params);
					imageView.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							toShowBigPicture(imageView,item);
						}
						
					});
					if (size<4&&size>0){
						mHolder.postImgs.addView(imageView);
					}else if (size==4){
						if (i<2){
							mHolder.postImgs.addView(imageView);
						}else{
							mHolder.postImgs1.addView(imageView);
						}
					}else if (size>4){
						if (i<3){
							mHolder.postImgs.addView(imageView);
						}else{
							mHolder.postImgs1.addView(imageView);
						}
					}

				}
			} else {
					ImageView imageView = new ImageView(context);
					imageView.setTag(0);
					if (!TextUtils.isEmpty(picturesList.get(0))&&picturesList.get(0)!=null) {
						imageLoader
								.displayImage(picturesList.get(0), imageView);
					} else {
						imageView.setImageResource(R.color.white);
					}
					imageView.setScaleType(ScaleType.CENTER_CROP);
					int imageWidth = DisplayUtils.dip2px(context, 165);
					int imageHieght = DisplayUtils.dip2px(context, 165);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							imageWidth, imageHieght, 1.0f);
					params.setMargins(10, 0, 0, 0);
					imageView.setLayoutParams(params);
					imageView.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							toShowBigPicture(imageView,item);
						}
						
					});;
					mHolder.postImgs.addView(imageView);
			}
			while (picturesList != null && picturesList.size() > 0
					&& picturesList.contains("")) {
				picturesList.remove("");
			}
		}

	}

	private void toShowBigPicture(ImageView imageView,PostBean item ) {
		int[] screenLocation = new int[2];
		imageView.getLocationOnScreen(screenLocation);
		Intent intent = new Intent((Activity) context, ShowBigPicture.class);
		Bundle bundle=new Bundle();
		bundle.putInt("position", (int) imageView.getTag());
		bundle.putInt(LEFT,screenLocation[0]);
		bundle.putInt(TOP,screenLocation[1]);
		bundle.putInt(WIDTH,imageView.getWidth());
		bundle.putInt(HEIGHT,imageView.getHeight());
		bundle.putSerializable("key", (Serializable) item);
		intent.putExtras(bundle);
		((Activity) context).startActivityForResult(intent, 0);
//		((Activity)context).overridePendingTransition(0, 0);
	}

	static class ViewHolder {
		private TextView postTitle, postContent, postUserName,
				postDate, postCommentCount,postTextLike;
		private LinearLayout postImgs,postImgs1;
		private RelativeLayout postLike,postComment;
		private ImageView mCricleNick;
	}

	private void setLikeData(ViewHolder mHolder,PostBean item){
		if ("0".equals(item.getIsPraise())){
			mHolder.postTextLike.setCompoundDrawablePadding(DisplayUtils.dip2px(context,10));
			mHolder.postTextLike.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.timeline_icon_unlike),null,null,null);
			mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.text_gray));
			mHolder.postTextLike.setText("赞");
			sharePrefUtils.setBoolean(CLIKE_LIKE,false);
		}else if ("1".equals(item.getIsPraise())){
			mHolder.postTextLike.setCompoundDrawablePadding(DisplayUtils.dip2px(context,10));
			mHolder.postTextLike.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.timeline_icon_like),null,null,null);
			mHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.font_red));
			mHolder.postTextLike.setText(item.getApplyNumber());
			sharePrefUtils.setBoolean(CLIKE_LIKE,true);
		}
	}

	private Drawable zoomDrawable(Drawable drawable, double w, double h)
	{
		int width = drawable.getIntrinsicWidth();
		int height= drawable.getIntrinsicHeight();
		// drawable转换成bitmap
		Bitmap oldbmp = drawableToBitmap(drawable);
		// 创建操作图片用的Matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float scaleWidth = ((float)w / width);
		float scaleHeight = ((float)h / height);

		// 设置缩放比例
		matrix.postScale(scaleWidth, scaleHeight);
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		// 把bitmap转换成drawable并返回
		return new BitmapDrawable(newbmp);
	}


	/*  drawable 转换成bitmap */
	private Bitmap drawableToBitmap(Drawable drawable)
	{
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();

		// 取drawable的颜色格式
		Bitmap.Config config =
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
						: Bitmap.Config.RGB_565;
		// 建立对应bitmap
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		// 建立对应bitmap的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		// 把drawable内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}


	private void praise(String userId,String mid,PostBean postBean,ViewHolder mViewHolder) {
		// userId 用户id
		// actionID 收藏(关注或点赞)对象的id
		// actionType 5：关注，6：收藏，7：点赞 8:加入圈子
		params.put("userId", userId);
		params.put("actionId", mid);
		params.put("actionType", Constant.PRIASE + "");
		httpInstance.post(Constant.HOST_URL
						+ Constant.Interface.CIRCLE_TYPEW_INFO, params,
				new AbsHttpStringResponseListener((CircleActivity)context, null) {
					@Override
					public void onSuccess(int i, String s) {
						UserOperationResult result = (UserOperationResult) AbJsonUtil
								.fromJson(s, UserOperationResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							if (result.getData().equals("关注成功")) {
								Animation animation = AnimationUtils.loadAnimation(context,R.anim.zoom_like);
								Drawable drawable = context.getResources()
										.getDrawable(R.drawable.timeline_icon_like);
								drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
								mViewHolder.postTextLike.setCompoundDrawables(drawable,null, null, null);
								mViewHolder.postTextLike.setText(Integer.parseInt(postBean.getApplyNumber())+1+"");
								mViewHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.font_orange));
								mViewHolder.postTextLike.startAnimation(animation);
							} else {
//								Animation animation = AnimationUtils.loadAnimation(context,R.anim.zoom_like);
								Drawable drawable = context.getResources()
										.getDrawable(R.drawable.timeline_icon_unlike);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight()); // 设置边界
								mViewHolder.postTextLike.setCompoundDrawables(drawable,
										null, null, null);
								if (Integer.parseInt(postBean.getApplyNumber())==0){
									mViewHolder.postTextLike.setText("赞");
								}
								mViewHolder.postTextLike.setTextColor(context.getResources().getColor(R.color.text_gray));
//								mViewHolder.postTextLike.startAnimation(animation);
							}

						} else {
							TipDialog.showDialog((CircleActivity)context,
									SweetAlertDialog.ERROR_TYPE,
									result.getRepMsg());
						}

					}
				});
//		notifyDataSetChanged();
	}
}

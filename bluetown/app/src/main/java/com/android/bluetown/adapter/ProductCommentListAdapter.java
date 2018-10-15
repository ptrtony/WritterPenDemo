package com.android.bluetown.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.bean.ProductCommentBean;
import com.android.bluetown.custom.dialog.SweetAlertDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.RoundedImageView;

public class ProductCommentListAdapter extends BaseContentAdapter {
	private ListView listView;
	// 当前登录的用户
	private String userId;
	// 发布当前商品的用户
	private String userIdFlg;
	// 商品id
	private String cid;

	public ProductCommentListAdapter(Context mContext, List<?> data,
			ListView listView, String userId, String userIdFlg, String cid) {
		super(mContext, data);
		// TODO Auto-generated constructor stub
		this.listView = listView;
		this.userId = userId;
		this.userIdFlg = userIdFlg;
		this.cid = cid;

	}

	@Override
	public View getContentView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.item_product_comment, null);
			mHolder = new ViewHolder();
			mHolder.commentContent = (TextView) convertView
					.findViewById(R.id.commentContent);
			mHolder.userImg = (RoundedImageView) convertView
					.findViewById(R.id.userImg);
			mHolder.commentTime = (TextView) convertView
					.findViewById(R.id.commentTime);
			mHolder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			mHolder.replyLy = (LinearLayout) convertView
					.findViewById(R.id.replyContentLy);
			mHolder.replyContent = (TextView) convertView
					.findViewById(R.id.replyContent);
			mHolder.reply = (TextView) convertView.findViewById(R.id.replyBtn);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData(mHolder, position);

		return convertView;
	}

	private void setData(ViewHolder mHolder, int position) {
		// TODO Auto-generated method stub
		final ProductCommentBean item = (ProductCommentBean) getItem(position);
		mHolder.userName.setText(item.getNickName());
		mHolder.commentContent.setText(item.getContent());
		setCommentTime(mHolder, item);
		if (!TextUtils.isEmpty(item.getHeadImg())) {
			imageLoader.displayImage(item.getHeadImg(), mHolder.userImg,
				defaultOptions);
		} else {
		  imageLoader.displayImage("drawable://" + R.drawable.ic_msg_empty,
				mHolder.userImg, defaultOptions);
				
		}
		if (!TextUtils.isEmpty(userId)) {
			if (!TextUtils.isEmpty(userIdFlg)) {
				if (userId.equals(userIdFlg)) {
					// 卖家
					showReplyCotent(mHolder, item);
					if (TextUtils.isEmpty(item.getRcontent())) {
						mHolder.reply.setVisibility(View.VISIBLE);
						mHolder.reply.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								replyPop(item.getCid());
							}
						});
					} else {
						mHolder.reply.setVisibility(View.GONE);
					}
				} else {
					// 买家
					showReplyCotent(mHolder, item);
					mHolder.reply.setVisibility(View.GONE);
				}
			}
		} else {
			// 游客 默认不显示回复按钮
			mHolder.reply.setVisibility(View.GONE);
			if (TextUtils.isEmpty(item.getRcontent())) {
				mHolder.replyLy.setVisibility(View.GONE);
			} else {
				mHolder.replyLy.setVisibility(View.VISIBLE);
				mHolder.replyContent.setText(item.getRcontent());
			}
		}
	}

	/**
	 * 
	 * @Title: selectZonePop
	 * @Description: TODO()
	 * @throws
	 */
	private void replyPop(final String commentId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.reply_layout, null);
		// 创建PopupWindow对象
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, 200, false);
		pop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		final EditText replyContent = (EditText) view
				.findViewById(R.id.replyContent);
		final Button replyBtn = (Button) view.findViewById(R.id.replyBtn);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		pop.showAtLocation(listView, Gravity.BOTTOM, 0, 0);
		replyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String commentContentStr = replyContent.getText().toString();
				if (TextUtils.isEmpty(commentContentStr)) {
					TipDialog.showDialog((Activity) context, SweetAlertDialog.ERROR_TYPE,
							R.string.reply_content_empty);
				} else {
					// 用户id:userId(必填) 评论id：commentId(必填)
					// 回复内容：rcontent(必填),cid：商品id
					params.put("userId", userId);
					params.put("commentId", commentId);
					params.put("cid", cid);
					params.put("rcontent", commentContentStr);
					httpInstance.post(Constant.HOST_URL
							+ Constant.Interface.PRODUCT_REPLY, params,
							new AbsHttpStringResponseListener(context, null) {
								@Override
								public void onSuccess(int i, String s) {
									Result result = (Result) AbJsonUtil
											.fromJson(s, Result.class);
									if (result.getRepCode().contains(
											Constant.HTTP_SUCCESS)) {
										Toast.makeText(context, "回复成功！",
												Toast.LENGTH_LONG).show();
										pop.dismiss();
									} else {
										Toast.makeText(context,
												result.getRepMsg(),
												Toast.LENGTH_LONG).show();
									}

								}

							});
				}
			}
		});
	}

	private void showReplyCotent(ViewHolder mHolder,
			final ProductCommentBean item) {
		if (TextUtils.isEmpty(item.getRcontent())) {
			mHolder.replyLy.setVisibility(View.GONE);
		} else {
			mHolder.replyLy.setVisibility(View.VISIBLE);
			mHolder.replyContent.setText(item.getRcontent());

		}
	}

	/**
	 * 设置评论时间
	 * 
	 * @param mHolder
	 * @param item
	 */
	private void setCommentTime(ViewHolder mHolder,
			final ProductCommentBean item) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String todayDateTime = format.format(new Date());
		try {
			Date nowTime = format.parse(todayDateTime);
			if (!TextUtils.isEmpty(item.getCommentDate())) {
				Date commentTime = format.parse(item.getCommentDate());
				// 相差的毫秒数
				long diff = nowTime.getTime() - commentTime.getTime();
				// 秒
				long timeInterval = diff / 1000;
				if (timeInterval < 60) {
					mHolder.commentTime.setText("刚刚");
				} else if ((timeInterval / 60) < 60) {
					mHolder.commentTime.setText((timeInterval / 60) + "分钟前");
				} else if ((timeInterval / (60 * 60)) < 24) {
					mHolder.commentTime.setText((timeInterval / (60 * 60))
							+ "小时前");
				} else {
					mHolder.commentTime.setText(item.getCommentDate());
				}
			} else {
				mHolder.commentTime.setText(" ");
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		private RoundedImageView userImg;
		private TextView userName;
		private TextView commentContent;
		private TextView commentTime;
		private TextView replyContent;
		private LinearLayout replyLy;
		private TextView reply;
	}

}

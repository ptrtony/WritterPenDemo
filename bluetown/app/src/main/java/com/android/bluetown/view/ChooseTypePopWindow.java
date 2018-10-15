package com.android.bluetown.view;

import java.util.List;

import me.next.tagview.TagCloudView;
import me.next.tagview.TagCloudView.OnTagClickListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.utils.Constant;

/**
 * Created by wangzj01 on 2015/4/11.
 */
public class ChooseTypePopWindow extends PopupWindow {
	private Context context;
	private int displayWidth;
	private int displayHeight;
	private PopupWindow popupWindow;
	private View view;
	public LinearLayout typeLy;
	private TagCloudView chooseTypePopView;
	private ImageView arrowImg;
	public Handler mHandler;
	private List<String> tags;
	private int currentTabIndex;
	private int index;
	// 产品分类与默认排序的标志
	private String flag;

	/**
	 * 
	 * @Title: ChooseTypePopWindow
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param parent
	 * @param context
	 * @param arrowImg
	 * @param typeLy
	 * @param tags
	 * @param flag
	 * @param handler
	 * @throws
	 */
	public ChooseTypePopWindow(View parent, Context context, ImageView arrowImg, LinearLayout typeLy, List<String> tags, String flag, Handler handler) {
		this.context = context;
		this.arrowImg = arrowImg;
		this.typeLy = typeLy;
		this.tags = tags;
		this.flag = flag;
		this.mHandler = handler;
		init();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private void init() {
		displayWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		displayHeight = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
	}

	public void showWindow(View parent, final Context context) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.choose_type_pop, null);
			chooseTypePopView = (TagCloudView) view.findViewById(R.id.chooseTypePop);
			chooseTypePopView.setTags(tags);
			chooseTypePopView.setOnTagClickListener(onTagClickListener);
			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, displayWidth, displayHeight);
			popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
		}

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// WindowManager windowManager = (WindowManager) context
		// .getSystemService(Context.WINDOW_SERVICE);
		popupWindow.showAsDropDown(parent, 0, dip2px(10));
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				arrowImg.setSelected(false);
			}
		});
		typeLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				arrowImg.setSelected(false);
				popupWindow.dismiss();
			}
		});
	}

	private OnTagClickListener onTagClickListener = new OnTagClickListener() {
		@Override
		public void onTagClick(int position) {
			// TODO Auto-generated method stub
			TextView textView = (TextView) chooseTypePopView.getChildAt(position);
			String getTag = textView.getText().toString();
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putSerializable("tagIds", getTag);
			data.putInt("currentIndex", currentTabIndex);
			msg.setData(data);
			if (flag.equals(context.getString(R.string.product_type))) {
				msg.what = Constant.GET_PRODUCT_TAG;
			} else {
				msg.what = Constant.GET_NORMAL_TAG;
			}
			mHandler.sendMessage(msg);
			popupWindow.dismiss();
		}
	};

}

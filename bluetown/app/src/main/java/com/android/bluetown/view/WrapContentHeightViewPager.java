package com.android.bluetown.view;

import com.android.bluetown.utils.DisplayUtils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Viewpager高度自定义，自适应高度
 * 
 * @author shenyz
 * 
 */
public class WrapContentHeightViewPager extends ViewPager {
	private Context context;
	/**
	 * Constructor
	 * 
	 * @param context
	 *            the context
	 */
	public WrapContentHeightViewPager(Context context) {
		super(context);
		this.context=context;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attribute set
	 */
	public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = 0;
		if (getChildCount() != 0) {
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				child.measure(widthMeasureSpec,
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				int h = child.getMeasuredHeight();
				if (h > height)
					height = h;
			}

		} else {
			height = DisplayUtils.dip2px(context, 160);
		}
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}

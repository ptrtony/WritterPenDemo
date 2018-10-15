package com.android.bluetown.view.NumberPayCodeView.BottomSheet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.bluetown.R;
import com.android.bluetown.view.NumberPayCodeView.BaseNumberCodeView;


/**
 * Created by linkaipeng on 16/8/2.
 */
public class BottomSheetNumberCodeView extends BaseNumberCodeView implements View.OnClickListener {

    private LinearLayout mBottomNumberCodeLayout;
    private ImageView mCloseImageView;
    private OnHideBottomLayoutListener mOnHideBottomLayoutListener;
    private float mDisplayHeight;

    public BottomSheetNumberCodeView(Context context) {
        super(context, null);
    }

    public BottomSheetNumberCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected View createView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_bottom_sheet_input_code, null);
        mBottomNumberCodeLayout = (LinearLayout) view.findViewById(R.id.bottom_number_code_layout);
        mCloseImageView = (ImageView) view.findViewById(R.id.close_bottom_number_code_view);
        mCloseImageView.setOnClickListener(this);
        return view;
    }

    @Override
    protected void onResult(String code) {
        if (mCallback != null) {
            mCallback.onResult(code);
        }
//        hideNumberCodeLayout();
    }

    public boolean isNumberCodeLayoutShowing(){
        if (mBottomNumberCodeLayout != null) {
            return mBottomNumberCodeLayout.getVisibility() == View.VISIBLE;
        } else {
            return false;
        }
    }

    public void setOnHideBottomLayoutListener(OnHideBottomLayoutListener onHideLayoutListener){
        mOnHideBottomLayoutListener = onHideLayoutListener;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.close_bottom_number_code_view) {
//            hideNumberCodeLayout();
            if (mOnHideBottomLayoutListener != null) {
                restoreViews();
                mOnHideBottomLayoutListener.onHide();
            }
        }
    }

    public interface OnHideBottomLayoutListener {
        void onHide();
    }
}

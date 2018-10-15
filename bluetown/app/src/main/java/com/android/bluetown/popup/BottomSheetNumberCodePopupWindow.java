package com.android.bluetown.popup;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.android.bluetown.R;
import com.android.bluetown.utils.AlphaUtil;
import com.android.bluetown.view.NumberPayCodeView.BaseNumberCodeView;
import com.android.bluetown.view.NumberPayCodeView.BottomSheet.BottomSheetNumberCodeView;


/**
 * Created by linkaipeng on 16/8/2.
 */
public class BottomSheetNumberCodePopupWindow extends PopupWindow
    implements BaseNumberCodeView.OnInputNumberCodeCallback,
        BottomSheetNumberCodeView.OnHideBottomLayoutListener {

    public static final int REQUEST_CODE_SHOW_BOTTOM_NUMBER_VIEW = 1001;
    public static final String KEY_DATA_NUMBER = "KeyDataNumber";
    private static final String KEY_DATA_IS_PASSWORD = "KeyDataIsPassword";
    private Activity mContext;
    private AlphaUtil mAlphaUtils;
    public void show(View view){
        mAlphaUtils.setBackgroundAlpha(mContext,0.6f);
        if (!isShowing()){
            showAtLocation(view, Gravity.BOTTOM,0,0);
        }
    }

    private BottomSheetNumberCodeView mNumberCodeView;

    public BottomSheetNumberCodePopupWindow(Activity context){
        this.mContext = context;
        initView();
    }
    private void initView(){
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.activity_bottom_sheet_number_code_view,null);
        mNumberCodeView = (BottomSheetNumberCodeView) contentView.findViewById(R.id.bottom_sheet_number_code_view);
        mNumberCodeView.setNumberCodeCallback(this);
        mNumberCodeView.setOnHideBottomLayoutListener(this);
        mNumberCodeView.setIsPassword(true);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.contextMenuAnim);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        mAlphaUtils = new AlphaUtil();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                mAlphaUtils.setBackgroundAlpha(mContext,1.0f);
            }
        });

    }

    @Override
    public void onResult(String code) {
        if (onGetResultListener!=null){
            onGetResultListener.onResultCode(code);
        }
    }

    @Override
    public void onHide() {
        dismiss();
        mAlphaUtils.setBackgroundAlpha(mContext,1.0f);
    }



    /**
     * 获取支付密码
     * @param onResultListener
     */
    public void setOnResultListener(OnGetResultListener onResultListener){
        this.onGetResultListener = onResultListener;
    }
    private OnGetResultListener onGetResultListener;

    public interface OnGetResultListener{
        void onResultCode(String code);
    }
}

package com.android.bluetown.view.NumberPayCodeView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.bluetown.R;

/**
 * Created by Dafen on 2018/7/30.
 */

public class NumberCodeView extends BaseNumberCodeView {

    private TextView mResultTextView;

    public NumberCodeView(Context context) {
        super(context, null);
    }

    public NumberCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View createView() {
        return LayoutInflater.from(mContext).inflate(R.layout.view_input_group_code, null);
    }

    @Override
    protected void onResult(String code) {
        if (mCallback != null) {
            mCallback.onResult(code);
        }
    }
}

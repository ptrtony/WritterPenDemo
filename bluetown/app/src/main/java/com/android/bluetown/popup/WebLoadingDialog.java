package com.android.bluetown.popup;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.bluetown.R;


/**
 * Created by Dafen on 2018/7/25.
 */

public class WebLoadingDialog extends Dialog {

    public WebLoadingDialog(Context context) {
        super(context);
        initView(context);
    }

    public WebLoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected WebLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_web_loading_dialog, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}

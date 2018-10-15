package com.android.bluetown.popup;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.bluetown.R;
import com.android.bluetown.utils.DisplayUtils;

/**
 * Created by Dafen on 2018/6/29.
 */

public class LoadingAlertDialog extends Dialog{

    private AnimationDrawable animationDrawable;
    public LoadingAlertDialog(Context context) {
        super(context);
        init(context);
    }

    public LoadingAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected LoadingAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_progress,null);
        ImageView imageView = view.findViewById(R.id.pb_loading);
        TextView textView = view.findViewById(R.id.tv_loading);
        textView.setText("加载中...");
        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_anim_frame);
        imageView.setImageDrawable(drawable);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.6f;
        window.setAttributes(lp);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(DisplayUtils.dip2px(context,200),DisplayUtils.dip2px(context,200));
        window.setGravity(Gravity.CENTER);
    }
}

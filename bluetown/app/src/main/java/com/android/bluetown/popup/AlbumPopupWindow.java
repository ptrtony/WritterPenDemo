package com.android.bluetown.popup;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.android.bluetown.R;
import com.android.bluetown.utils.AlphaUtil;

/**
 * Created by Dafen on 2018/6/22.
 */

public class AlbumPopupWindow extends PopupWindow implements View.OnClickListener {
    private Activity activity;
    private Boolean isShowing = false;
    private Button camereButton,albumButton,cancelButton;
    private OnClickListener onListener;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_cam:
                if (onListener!=null){
                    onListener.onCamere();
                }
                break;
            case R.id.choose_album:
                if (onListener!=null){
                    onListener.onAlbum();
                }
                break;
            case R.id.choose_cancel:
                break;
        }
        isShowing = false;
        dismiss();
        AlphaUtil.setBackgroundAlpha(activity,1.0f);
    }

    public interface OnClickListener{
        void onCamere();
        void onAlbum();
    }
    public AlbumPopupWindow(Activity activity,OnClickListener onListener){
        this.onListener = onListener;
        this.activity = activity;
        View view = LayoutInflater.from(activity).inflate(R.layout.choose_avatar,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        camereButton = view.findViewById(R.id.choose_cam);
        albumButton = view.findViewById(R.id.choose_album);
        cancelButton = view.findViewById(R.id.choose_cancel);
        camereButton.setOnClickListener(this);
        albumButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        setTouchable(true);
        setOutsideTouchable(true);

        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.contextMenuAnim);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                AlphaUtil.setBackgroundAlpha(activity,1.0f);
                isShowing = false;
            }
        });

    }

    public void show(View view){
        if (!isShowing){
            showAtLocation(view, Gravity.BOTTOM,0,0);
            AlphaUtil.setBackgroundAlpha(activity,0.6f);
            isShowing = true;
        }else{
            isShowing = false;
        }


    }
}

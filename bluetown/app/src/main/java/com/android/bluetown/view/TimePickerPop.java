package com.android.bluetown.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.android.bluetown.R;
import com.android.bluetown.utils.Constant;

/**
 * Created by wangzj01 on 2015/4/24.
 */
public class TimePickerPop extends PopupWindow {
    private PopupWindow popupWindow;
    private View view;
    private Context context;
    private int displayWidth;
    private int displayHeight;
    public Handler mHandler;
    private PickerView minute_pv;
    private PickerView hour_pv;
    private Button ConfirmButton, CancelBut;
    private String GetDate = "";
    private String StrHour = "", StrMinute = "";
    private SimpleDateFormat HourFormat, MinuteFormat;

    public TimePickerPop(Context context, Handler handler) {
        this.context = context;
        this.mHandler = handler;
        init();
    }

    public void showWindow(View parent) {

        if (popupWindow == null) {
            Date date = new Date();
            HourFormat = new SimpleDateFormat("HH");
            MinuteFormat = new SimpleDateFormat("mm");
            StrHour = HourFormat.format(date);
            StrMinute = MinuteFormat.format(date);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.time_picker_pop, null);
            ConfirmButton = (Button) view.findViewById(R.id.confirm_button);
            CancelBut = (Button) view.findViewById(R.id.cancel_button);
            minute_pv = (PickerView) view.findViewById(R.id.minute_pv);
            hour_pv = (PickerView) view.findViewById(R.id.hour_pv);
            ConfirmButton.setOnClickListener(onClickListener);
            CancelBut.setOnClickListener(onClickListener);
            List<String> HourData = new ArrayList<String>();

            List<String> MinuteDate = new ArrayList<String>();
            System.out.println("<<<<<<<<<<<riqi" + StrHour + "<<<<" + StrMinute);
            for (int i = 0; i < 24; i++) {
                if (i < 10) {
                    HourData.add("0" + i);
                } else {
                    HourData.add("" + i);
                }

            }
            for (int i = 0; i < 60; i++) {
                MinuteDate.add(i < 10 ? "0" + i : "" + i);
            }
            hour_pv.setData(HourData);
            hour_pv.setSelected(StrHour);
            hour_pv.setOnSelectListener(new PickerView.onSelectListener() {

                @Override
                public void onSelect(String text) {
                    StrHour = text;
                }
            });
            minute_pv.setData(MinuteDate);

            minute_pv.setOnSelectListener(new PickerView.onSelectListener() {

                @Override
                public void onSelect(String text) {
                    StrMinute = text;

                }
            });
            minute_pv.setSelected(StrMinute);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, displayWidth,displayHeight);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        popupWindow.showAsDropDown(parent,0,dip2px(42));
//        popupWindow.showAsDropDown(parent);
//        popupWindow.showAtLocation(parent,Gravity.BOTTOM,0,100);

//        gridView.setOnItemClickListener(onItemClickListener);
        popupWindow.setOnDismissListener(new OnDismissListener(){
            @Override
            public void onDismiss() {

            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.cancel_button) {
                popupWindow.dismiss();
            } else if (id == R.id.confirm_button) {
                popupWindow.dismiss();
                Message msg = new Message();
                Bundle data = new Bundle();
                String GetDate = StrHour + ":" + StrMinute;
                data.putSerializable("GetDate", GetDate);
                msg.setData(data);
                msg.what = Constant.GET_DATE;
                mHandler.sendMessage(msg);
            }
        }
    };

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
}

package com.android.bluetown.popup;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.bluetown.R;
import com.android.bluetown.adapter.CartAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.utils.AlphaUtil;



/**
 * Created by Dafen on 2018/6/13.
 */

public class ShowShopCarPopup extends PopupWindow{
    private AlphaUtil alphaUtil;
    private Activity activity;
    private final ListView cartListView;
    private final TextView originalTotalprice;
    private final TextView currentTotalprice;
    private final TextView shopCardDishCount;
    private final TextView mDiscountRemindDialog;

    public ShowShopCarPopup(Activity activity, String type,View.OnClickListener listener1){
        View contentView = LayoutInflater.from(activity).inflate(R.layout.shop_card_layout,null);
        setContentView(contentView);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.contextMenuAnim);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        alphaUtil = new AlphaUtil();
        this.activity = activity;
        mDiscountRemindDialog = contentView.findViewById(R.id.tv_discount_remind);
        cartListView = (ListView) contentView.findViewById(R.id.lv_cart_list);
        originalTotalprice = (TextView) contentView
                .findViewById(R.id.originalprice);
        currentTotalprice = (TextView) contentView
                .findViewById(R.id.currentprice);
        shopCardDishCount = contentView
                .findViewById(R.id.shopCardDishCount);
        originalTotalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView seletedYes = (Button) contentView.findViewById(R.id.seletedYes);
        Button sleep = (Button) contentView.findViewById(R.id.sleep);
        seletedYes.setOnClickListener(listener1);
        if(type.equals("1")){
            seletedYes.setVisibility(View.GONE);
            sleep.setVisibility(View.VISIBLE);
        }else{
            seletedYes.setVisibility(View.VISIBLE);
            sleep.setVisibility(View.GONE);
        }

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                alphaUtil.setBackgroundAlpha(activity,1.0f);
            }
        });

//        setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (isShowing()) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    public void show(View view,String count,String originPrice, String currentPrice, CartAdapter cartAdapter){
        if (BlueTownApp.getDiscountRemind()==null){
            mDiscountRemindDialog.setVisibility(View.GONE);
        }else{
            mDiscountRemindDialog.setVisibility(View.VISIBLE);
            mDiscountRemindDialog.setText(BlueTownApp.getDiscountRemind());
        }
        cartListView.setAdapter(cartAdapter);
        shopCardDishCount.setVisibility(View.VISIBLE);
        shopCardDishCount.setText(count);
        originalTotalprice.setText(originPrice);
        currentTotalprice.setText(currentPrice);
        //获取需要在其上方显示的控件的位置信息
        showAtLocation(view,Gravity.BOTTOM,0,0);
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        //在控件上方显示
//        showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - getWidth() / 2, location[1] - getHeight());
        alphaUtil.setBackgroundAlpha(activity,0.6f);
    }
    public ListView getCartListView(){
        return cartListView;
    }

    public void setCarCountAndAmount(String count, String originalTotalPrice, String currentTotalPrice, Spanned spanned){
        shopCardDishCount.setText(count);
        originalTotalprice.setText(originalTotalPrice);
        currentTotalprice.setText(currentTotalPrice);
        if (spanned==null){
            mDiscountRemindDialog.setVisibility(View.GONE);
        }else{
            mDiscountRemindDialog.setVisibility(View.VISIBLE);
            mDiscountRemindDialog.setText(spanned);
        }

    }
}

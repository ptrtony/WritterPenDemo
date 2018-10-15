package com.android.bluetown.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.utils.DisplayUtils;

import java.util.List;

/**
 * Created by Dafen on 2018/7/19.
 */

public class DropDownMenu extends LinearLayout{
    private LinearLayout tabMenuView;
    private FrameLayout containerView;
    private FrameLayout popupMenuViews;
    private View maskView;
    private int current_tab_position;
    private int dividerColor;
    private int textSelectedColor;
    private int textUnselectedColor;
    private int maskColor;
    private int menuTextSize;
    private int menuSelectedIcon;
    private int menuUnselectedIcon;
    private float menuHeighPercent;
    int menuBackgroundColor1;
    int underlineColor = -3355444;
    private Context mContext;
    public DropDownMenu(Context context) {
        super(context, (AttributeSet)null);
        this.current_tab_position = -1;
        this.dividerColor = -3355444;
        this.textSelectedColor = -7795579;
        this.textUnselectedColor = -15658735;
        this.maskColor = -2004318072;
        this.menuTextSize = 14;
        this.menuHeighPercent = 0.5F;
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext= context;
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.current_tab_position = -1;
        this.dividerColor = -3355444;
        this.textSelectedColor = -7795579;
        this.textUnselectedColor = -15658735;
        this.maskColor = -2004318072;
        this.menuTextSize = 14;
        this.menuHeighPercent = 0.5F;
        this.setOrientation(1);
        byte menuBackgroundColor = -1;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
        this.dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);
        this.textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor,textSelectedColor);
        this.textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor,textUnselectedColor);
        menuBackgroundColor1 = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
        this.maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor,maskColor);
        this.menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);
        this.menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon, menuSelectedIcon);
        this.menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon, menuUnselectedIcon);
        this.menuHeighPercent = a.getFloat(R.styleable.DropDownMenu_ddmenuMenuHeightPercent, menuHeighPercent);
        a.recycle();

        this.tabMenuView = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        this.tabMenuView.setOrientation(0);
        this.tabMenuView.setBackgroundColor(menuBackgroundColor1);
        this.tabMenuView.setLayoutParams(params);
        this.addView(this.tabMenuView, 0);
        View underLine = new View(this.getContext());
        underLine.setLayoutParams(new LinearLayout.LayoutParams(-1, this.dpTpPx(1.0F)));
        underLine.setBackgroundColor(underlineColor);
        this.addView(underLine, 1);
        this.containerView = new FrameLayout(mContext);
        this.containerView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
        this.addView(this.containerView, 2);
    }

    public void setDropDownMenu(@NonNull List<View> popupViews, @NonNull View contentView) {

//        if(tabTexts.size() != popupViews.size()) {
//            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
//        } else {
//            if (this.tabMenuView.getChildCount()>0)this.tabMenuView.removeAllViews();
//            for(int i = 0; i < tabTexts.size(); ++i) {
//                this.addTab(tabTexts, i);
//            }

            if (this.containerView.getChildAt(0)!=null){
                this.containerView.removeViewAt(0);
            }

            this.containerView.addView(contentView, 0);
            this.maskView = new View(this.getContext());
            this.maskView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
            this.maskView.setBackgroundColor(this.maskColor);
            this.maskView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   DropDownMenu.this.closeMenu();
                }
            });

            if (this.containerView.getChildAt(1)!=null){
                this.containerView.removeViewAt(1);
            }
            this.containerView.addView(this.maskView, 1);
            this.maskView.setVisibility(8);
            if(this.containerView.getChildAt(2) != null) {
                this.containerView.removeViewAt(2);
            }

            this.popupMenuViews = new FrameLayout(this.getContext());
            this.popupMenuViews.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, (int)((float) DisplayUtils.getScreenSize(this.getContext()).y * this.menuHeighPercent)));
            this.popupMenuViews.setVisibility(8);
            this.containerView.addView(this.popupMenuViews, 2);
            if (popupMenuViews.getChildCount()>0)popupMenuViews.removeAllViews();
            for(int i = 0; i < popupViews.size(); ++i) {
                ((View)popupViews.get(i)).setLayoutParams(new android.view.ViewGroup.LayoutParams(-1, -2));
                this.popupMenuViews.addView((View)popupViews.get(i), i);
            }

//        }
    }


    public void setTabTexts(@NonNull List<String> tabTexts){
        for(int i = 0; i < tabTexts.size(); ++i) {
            this.addTab(tabTexts, i);
        }
    }


    private void addTab(@NonNull List<String> tabTexts, int i) {
        final RightDrawableTextView tab = new RightDrawableTextView(this.getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(17);
        tab.setTextSize(0, (float)this.menuTextSize);
        tab.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0F));
        tab.setTextColor(this.textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.getResources().getDrawable(this.menuUnselectedIcon), (Drawable)null);
        tab.setText((CharSequence)tabTexts.get(i));
        tab.setPadding(this.dpTpPx(5.0F), this.dpTpPx(12.0F), this.dpTpPx(5.0F), this.dpTpPx(12.0F));
        tab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DropDownMenu.this.switchMenu(tab);
            }
        });
        this.tabMenuView.addView(tab);
        if(i < tabTexts.size() - 1) {
            View view = new View(this.getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(this.dpTpPx(0.5F), -1));
            view.setBackgroundColor(this.dividerColor);
            this.tabMenuView.addView(view);
        }

    }

    public void setTabText(String text) {
        if(this.current_tab_position != -1) {
            ((TextView)this.tabMenuView.getChildAt(this.current_tab_position)).setText(text);
        }

    }

    public void setTabClickable(boolean clickable) {
        for(int i = 0; i < this.tabMenuView.getChildCount(); i += 2) {
            this.tabMenuView.getChildAt(i).setClickable(clickable);
        }

    }

    public void closeMenu() {
        if(this.current_tab_position != -1) {
            ((TextView)this.tabMenuView.getChildAt(this.current_tab_position)).setTextColor(this.textUnselectedColor);
            ((TextView)this.tabMenuView.getChildAt(this.current_tab_position)).setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.getResources().getDrawable(this.menuUnselectedIcon), (Drawable)null);
            this.popupMenuViews.setVisibility(8);
            this.popupMenuViews.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_menu_out));
            this.maskView.setVisibility(8);
            this.maskView.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_mask_out));
            this.current_tab_position = -1;
        }

    }

    public boolean isShowing() {
        return this.current_tab_position != -1;
    }

    private void switchMenu(View target) {
        System.out.println(this.current_tab_position);

        for(int i = 0; i < this.tabMenuView.getChildCount(); i += 2) {
            if(target == this.tabMenuView.getChildAt(i)) {
                if(this.current_tab_position == i) {
                    this.closeMenu();
                } else {
                    if(this.current_tab_position == -1) {
                        this.popupMenuViews.setVisibility(0);
                        this.popupMenuViews.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_menu_in));
                        this.maskView.setVisibility(0);
                        this.maskView.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_mask_in));
                        this.popupMenuViews.getChildAt(i / 2).setVisibility(0);
                    } else {
                        this.popupMenuViews.getChildAt(i / 2).setVisibility(0);
                    }

                    this.current_tab_position = i;
                    ((TextView)this.tabMenuView.getChildAt(i)).setTextColor(this.textSelectedColor);
                    ((TextView)this.tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.getResources().getDrawable(this.menuSelectedIcon), (Drawable)null);
                }
            } else {
                ((TextView)this.tabMenuView.getChildAt(i)).setTextColor(this.textUnselectedColor);
                ((TextView)this.tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.getResources().getDrawable(this.menuUnselectedIcon), (Drawable)null);
                this.popupMenuViews.getChildAt(i / 2).setVisibility(8);
            }
        }

    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        return (int)((double) TypedValue.applyDimension(1, value, dm) + 0.5D);
    }
}

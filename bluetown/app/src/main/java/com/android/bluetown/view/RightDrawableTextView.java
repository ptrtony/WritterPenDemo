package com.android.bluetown.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dafen on 2018/7/19.
 */

public class RightDrawableTextView extends TextView {

    private int mW, mH;

    public RightDrawableTextView(Context context) {
        super(context);
    }

    public RightDrawableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RightDrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mW = getWidth();
        mH = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            //获取右边图片
            Drawable drawableRight = drawables[2];
            if (drawableRight != null) {
                //获取文字占用长宽
                String txtContent = getText().toString();
                int textWidth = (int) getPaint().measureText(txtContent);
                int textHeight = (int) getPaint().getTextSize();
                //获取图片实际长宽
                int drawableWidth = drawableRight.getIntrinsicWidth();
                int drawableHeight = drawableRight.getIntrinsicHeight();

                //setBounds修改Drawable在View所占的位置和大小,对应参数同样的 左上右下()
                if (txtContent != null) {
                    if (txtContent.length() == 2) {
                        if (drawableHeight < textHeight) {
                            drawableRight.setBounds(((textWidth - mW) / 2) + textWidth,
                                    0,
                                    ((textWidth - mW) / 2) + textWidth + drawableWidth,
                                    drawableHeight);
                        } else {
                            drawableRight.setBounds(((textWidth - mW) / 2) + textWidth,
                                    (textHeight - drawableHeight) / 2,
                                    ((textWidth - mW) / 2) + textWidth + drawableWidth,
                                    (textHeight - drawableHeight) / 2 + drawableHeight);
                        }

                    } else if (txtContent.length() == 4) {
                        if (drawableHeight < textHeight) {
                            drawableRight.setBounds(((textWidth - mW) / 2) + textWidth / 2,
                                    0,
                                    ((textWidth - mW) / 2) + textWidth / 2 + drawableWidth, drawableHeight);
                        } else {
                            drawableRight.setBounds(((textWidth - mW) / 2) + textWidth / 2,
                                    (textHeight - drawableHeight) / 2,
                                    ((textWidth - mW) / 2) + textWidth / 2 + drawableWidth,
                                    (textHeight - drawableHeight) / 2 + drawableHeight);
                        }
                    }
                }
            }
        }
        super.onDraw(canvas);
    }
}

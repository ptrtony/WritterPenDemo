package com.android.bluetown.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Dafen on 2018/8/16.
 */

public class MarkButton extends Button{
    public MarkButton(Context context) {
        super(context);
    }

    public MarkButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

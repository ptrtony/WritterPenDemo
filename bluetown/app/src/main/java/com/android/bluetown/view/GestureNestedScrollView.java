package com.android.bluetown.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Dafen on 2018/5/28.
 */

public class GestureNestedScrollView extends NestedScrollView{
    public GestureNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public GestureNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){

    }


}

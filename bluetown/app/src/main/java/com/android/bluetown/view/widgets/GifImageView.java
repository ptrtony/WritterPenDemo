package com.android.bluetown.view.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.android.bluetown.R;

/**
 * Created by Dafen on 2018/6/24.
 */

public class GifImageView extends ImageView{
    public GifImageView(Context context) {
        this(context,null);
    }

    public GifImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GifImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
        setImageResource(R.drawable.refresh);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.widget_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        animation.setInterpolator(interpolator);
        startAnimation(animation);
    }
}

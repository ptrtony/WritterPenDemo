package com.android.bluetown.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.android.bluetown.view.widgets.pswEditText.PwdContract;

/**
 * Created by Dafen on 2018/6/5.
 */

public class PswEditText extends EditText implements View.OnTouchListener{
    /**
     * 显示暗文或明文的drawable图片
     */
    private PwdContract pwdContract;

    public PswEditText(Context context) {
        super(context);
        init();
    }

    public PswEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PswEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        pwdContract = PwdContract.getPwdContract(this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_UP){
            Boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                    && (event.getX() < ((getWidth() - getPaddingRight())));
            if (touchable){
                pwdContract.gone();
                pwdContract.visible();
            }
        }
        return super.onTouchEvent(event);
    }

}

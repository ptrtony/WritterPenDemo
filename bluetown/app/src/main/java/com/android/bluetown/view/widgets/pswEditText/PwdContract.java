package com.android.bluetown.view.widgets.pswEditText;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

import com.android.bluetown.R;

/**
 * Created by Dafen on 2018/6/5.
 */

public class PwdContract {
    private static PwdContract pwdContract;
    private EditText mEditText;
//    private Drawable mVisibleDrawable;
    private PwdContract(EditText editText){
        mEditText = editText;
//        mVisibleDrawable =  editText.getCompoundDrawables()[2];
        setIconVisible(isVisible);
    }
    public static PwdContract getPwdContract(EditText editText) {
        if (pwdContract == null){
            pwdContract = new PwdContract(editText);
        }

        return pwdContract;
    }

    public static boolean isVisible;
    private OnVisible onVisibleText;
    private void setVisibleText(OnVisible onVisibleText){
        this.onVisibleText = onVisibleText;
    }

    public void visible(){
        setVisibleText(new VisibleText());
        if (isVisible){
            onVisibleText.visible(mEditText);
        }
        setIconVisible(isVisible);
        isVisible = false;
    }

    public void gone(){
        setVisibleText(new GoneText());
        if (!isVisible){
            onVisibleText.gone(mEditText);
        }
        setIconVisible(isVisible);
        isVisible = true;
    }

    public void setIconVisible(boolean isVisible){
       Drawable right = isVisible?mEditText.getResources().getDrawable(R.drawable.icon_visibility): mEditText.getResources().getDrawable(R.drawable.icon_visibility_off);
        mEditText.setCompoundDrawables(mEditText.getCompoundDrawables()[0],
                mEditText.getCompoundDrawables()[1], right, mEditText.getCompoundDrawables()[3]);
    }
}

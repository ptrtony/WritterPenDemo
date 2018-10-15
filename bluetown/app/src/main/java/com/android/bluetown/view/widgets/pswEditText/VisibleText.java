package com.android.bluetown.view.widgets.pswEditText;

import android.text.InputType;
import android.widget.EditText;

/**
 * Created by Dafen on 2018/6/5.
 */

public class VisibleText implements OnVisible{

    @Override
    public void visible(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    @Override
    public void gone(EditText editText) {

    }
}

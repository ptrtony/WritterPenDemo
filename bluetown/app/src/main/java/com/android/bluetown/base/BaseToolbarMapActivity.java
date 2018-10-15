package com.android.bluetown.base;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.RecommendDish;

/**
 * Created by Dafen on 2018/8/14.
 */

public class BaseToolbarMapActivity extends BaseMapActivity {
    RelativeLayout mIvBack;
    TextView mTvTitle;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mIvBack = findViewById(R.id.toolbar_back);
        mTvTitle = findViewById(R.id.tv_title);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setTitle(CharSequence text){
        mTvTitle.setText(text);
    }

    public void setTitle(int resid){
        mTvTitle.setText(resid);
    }
}

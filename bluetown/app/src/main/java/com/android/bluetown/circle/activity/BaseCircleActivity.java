package com.android.bluetown.circle.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.bluetown.R;
import com.android.bluetown.view.VerticalSlideLayout;

public class BaseCircleActivity extends AppCompatActivity implements VerticalSlideLayout.PageChangeListener {
    private VerticalSlideLayout mVerticalSlide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_cirlce);
        mVerticalSlide = findViewById(R.id.activity_base_cirlce);
        mVerticalSlide.setPageChangeListener(this);

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.frameLayout_show_big_picture, frameLayout_show_big_picture)
//                .add(R.id.frameLayout_circle_details, mTwoFrament)
//                .commit();
    }

    @Override
    public void onPrevPage() {

    }

    @Override
    public void onNextPage() {

    }
}

package com.android.bluetown.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.BusinessAndEventBean;
import com.android.bluetown.home.main.model.act.ActionCenterDetailsActivity;
import com.android.bluetown.img.GlideRoundTransform;
import com.android.bluetown.utils.DisplayUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


/**
 * Created by Dafen on 2018/6/1.
 */

public class HomeEventAdapter extends PagerAdapter{
    private ArrayList<BusinessAndEventBean.HotActivitiesBean> eventViews;
    private Context mContext;
    public HomeEventAdapter(Context context){
        this.mContext = context;
    }

    public void setData(ArrayList<BusinessAndEventBean.HotActivitiesBean> eventViews){
        if (eventViews!=null){
            this.eventViews = (ArrayList<BusinessAndEventBean.HotActivitiesBean>) eventViews.clone();
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return eventViews.size()==0?0:eventViews.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_event,container,false);
        ImageView mIvHomeEvent = view.findViewById(R.id.iv_image);
        TextView mTvHomeContent = view.findViewById(R.id.tv_hot_event_content);
        TextView mTvHomeEventStatus = view.findViewById(R.id.tv_event_going);
        TextView mTvJoinNum = view.findViewById(R.id.tv_event_join_number);
        Button mBtnJoinUs = view.findViewById(R.id.btn_join_in);
        if (eventViews.get(position).ImageUrl==null||eventViews.get(position).ImageUrl.equals("")){
            Glide.with(mContext).load(R.drawable.ic_msg_empty).into(mIvHomeEvent);
        }else{
            Glide.with(mContext).load(eventViews.get(position).ImageUrl)
                    .override(DisplayUtils.dip2px(mContext,320)
                            ,DisplayUtils.dip2px(mContext,150))
                    .placeholder(R.drawable.ic_msg_empty)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_msg_empty)
                    .crossFade(500)
                    .transform(new GlideRoundTransform(mContext, DisplayUtils.dip2px(mContext,5)))
                    .skipMemoryCache(true)
                    .into(mIvHomeEvent);
        }
        mTvHomeContent.setText(eventViews.get(position).Name);
        switch (eventViews.get(position).Status) {
            case 1:
                mTvHomeEventStatus.setText("未开始");
                mBtnJoinUs.setText("未开始");
                break;
            case 2:
                mTvHomeEventStatus.setText("报名中");
                mBtnJoinUs.setText("马上报名");
                break;
            case 3:
                mTvHomeEventStatus.setText("活动截止");
                mBtnJoinUs.setText("活动截止");
                break;
        }
        mTvJoinNum.setText(eventViews.get(position).JoinNumber);
        mBtnJoinUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("aid", eventViews.get(position).Id);
                intent.setClass(mContext,
                        ActionCenterDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });

        mIvHomeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("aid", eventViews.get(position).Id);
                intent.setClass(mContext,
                        ActionCenterDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_event,container,false);
        container.removeView(view);
    }

}

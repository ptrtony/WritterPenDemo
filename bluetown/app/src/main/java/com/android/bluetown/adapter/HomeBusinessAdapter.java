package com.android.bluetown.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.BusinessAndEventBean;
import com.android.bluetown.home.main.model.act.SmartRestaurantDetailsActivity;
import com.android.bluetown.img.GlideRoundTransform;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.utils.DisplayUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import static com.android.bluetown.base.BaseWebActivity.LOAD_URL;

/**
 * Created by Dafen on 2018/6/7.
 */

public class HomeBusinessAdapter extends PagerAdapter{
    private ArrayList<BusinessAndEventBean.HotMerchantsBean> businessViews;
    private Context mContext;
    public HomeBusinessAdapter(Context context){
        this.mContext = context;
    }

    public void setData(ArrayList<BusinessAndEventBean.HotMerchantsBean> businessViews){
        if (businessViews!=null){
            this.businessViews = (ArrayList<BusinessAndEventBean.HotMerchantsBean>) businessViews.clone();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return businessViews.size()==0?0:businessViews.size();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_business,container,false);
        ImageView busienssImageView = view.findViewById(R.id.iv_image);
        TextView tvBusinessName = view.findViewById(R.id.tv_store_name);
        if (businessViews.get(position).ImageUrl==null||businessViews.get(position).ImageUrl.equals("")){
            Glide.with(mContext).load(R.drawable.ic_msg_empty).centerCrop().transform(new GlideRoundTransform(mContext,30)).into(busienssImageView);
        }else{
            Glide.with(mContext).load(businessViews.get(position).ImageUrl)
                    .placeholder(R.drawable.ic_msg_empty)
                    .override(DisplayUtils.dip2px(mContext,320)
                            ,DisplayUtils.dip2px(mContext,180))
                    .crossFade(500)
                    .transform(new GlideRoundTransform(mContext
                            , DisplayUtils.dip2px(mContext,5)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .error(R.drawable.ic_msg_empty)
                    .into(busienssImageView);
        }

        tvBusinessName.setText(businessViews.get(position).Name);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                BlueTownApp.DISH_TYPE = "food";
                StringBuilder sb = new StringBuilder();
                sb.append(Constant.WEB_BASE_URL+Constant.Interface.BUSINESS_DETAILS);
                sb.append("meid/"+businessViews.get(position).Id);
                intent.putExtra(LOAD_URL,sb.toString());
                intent.setClass(mContext,
                        SmartRestaurantDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_business,container,false);
        container.removeView(view);
    }

}

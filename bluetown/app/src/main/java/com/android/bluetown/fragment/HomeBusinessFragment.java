package com.android.bluetown.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.bluetown.R;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.BusinessAndEventBean;
import com.android.bluetown.surround.MerchantDetailsActivity;

import net.tsz.afinal.FinalBitmap;

/**
 * Created by Dafen on 2018/6/19.
 */

public class HomeBusinessFragment extends Fragment{
    private HomeBusinessFragment homeBusinessFragment;
    private static String HOME_BUSINESS = "HOME_BUSINESS";
    private ImageView busienssImageView;
    private TextView tvBusinessName;
    private FinalBitmap finalBitmap;
    public HomeBusinessFragment getNewFragment(BusinessAndEventBean.HotMerchantsBean hotMerchantsBean){
        homeBusinessFragment = new HomeBusinessFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(HOME_BUSINESS,hotMerchantsBean);
        homeBusinessFragment.setArguments(bundle);
        return homeBusinessFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBitmapLoader();
    }
    private void initBitmapLoader() {
        finalBitmap = FinalBitmap.create(getActivity());
        finalBitmap.configBitmapLoadThreadSize(3);// 定义线程数量
        finalBitmap.configDiskCachePath(this.getActivity()
                .getFilesDir().toString());// 设置缓存目录；
        finalBitmap.configDiskCacheSize(1024 * 1024 * 1);// 设置缓存大小
        finalBitmap.configLoadingImage(R.drawable.ic_msg_empty);
        finalBitmap.configLoadfailImage(R.drawable.ic_msg_empty);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=null;
        if (view==null){
            view = inflater.inflate(R.layout.item_home_business,container,false);
        }
        busienssImageView = view.findViewById(R.id.iv_image);
        tvBusinessName = view.findViewById(R.id.tv_store_name);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null!=getArguments()){
            Bundle bundle = getArguments();
            BusinessAndEventBean.HotMerchantsBean hotMerchantsBean = (BusinessAndEventBean.HotMerchantsBean) bundle.getSerializable(HOME_BUSINESS);
//            finalBitmap.configBitmapMaxHeight(ImageSizeUtil.getImageViewSize(busienssImageView).width);
//            finalBitmap.configBitmapMaxHeight(ImageSizeUtil.getImageViewSize(busienssImageView).height);
            finalBitmap.display(busienssImageView,hotMerchantsBean.ImageUrl);
            tvBusinessName.setText(hotMerchantsBean.Name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    BlueTownApp.DISH_TYPE = "food";
                    intent.putExtra("meid", hotMerchantsBean.Id);
                    intent.setClass(getActivity(),
                            MerchantDetailsActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}

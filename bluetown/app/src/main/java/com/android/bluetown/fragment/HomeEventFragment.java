package com.android.bluetown.fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.bluetown.R;
import com.android.bluetown.bean.BusinessAndEventBean;
import com.android.bluetown.home.main.model.act.ActionCenterDetailsActivity;
import com.android.bluetown.utils.ImageSizeUtil;
import net.tsz.afinal.FinalBitmap;

/**
 * Created by Dafen on 2018/6/19.
 */

public class HomeEventFragment extends Fragment {
    private static HomeEventFragment homeEventFragment;
    private static String HOME_EVENT = "HOME_EVENT";
    private ImageView mIvHomeEvent;
    private TextView mTvHomeContent;
    private TextView mTvHomeEventStatus;
    private TextView mTvJoinNum;
    private Button mBtnJoinUs;
    private FinalBitmap finalBitmap;
    private Bitmap bitmap;
    public HomeEventFragment getNewFragment(BusinessAndEventBean.HotActivitiesBean hotBean){
        homeEventFragment = new HomeEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(HOME_EVENT, hotBean);
        homeEventFragment.setArguments(bundle);
        return homeEventFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBitmapLoader();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=null;
        if (view==null){
            view = inflater.inflate(R.layout.item_home_event,container,false);
        }
        mIvHomeEvent = view.findViewById(R.id.iv_image);
        mTvHomeContent =  view.findViewById(R.id.tv_hot_event_content);
        mTvHomeEventStatus = view.findViewById(R.id.tv_event_going);
        mTvJoinNum = view.findViewById(R.id.tv_event_join_number);
        mBtnJoinUs = view.findViewById(R.id.btn_join_in);
        return view;
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null!=getArguments()){
            BusinessAndEventBean.HotActivitiesBean hotBean = (BusinessAndEventBean.HotActivitiesBean) getArguments().getSerializable(HOME_EVENT);
//            finalBitmap.configBitmapMaxWidth(ImageSizeUtil.getImageViewSize(mIvHomeEvent).width);
//            finalBitmap.configBitmapMaxHeight(ImageSizeUtil.getImageViewSize(mIvHomeEvent).height);

        }
    }

}

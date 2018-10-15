//package com.android.bluetown.adapter;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Html;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import com.android.bluetown.FindActivity;
//import com.android.bluetown.LoginActivity;
//import com.android.bluetown.MainActivity;
//import com.android.bluetown.MessageDetailsActivity;
//import com.android.bluetown.MessageListActivity;
//import com.android.bluetown.R;
//import com.android.bluetown.app.BlueTownApp;
//import com.android.bluetown.bean.MemberUser;
//import com.android.bluetown.custom.dialog.TipDialog;
//import com.android.bluetown.fragment.HomeBusinessFragment;
//import com.android.bluetown.fragment.HomeEventFragment;
//import com.android.bluetown.home.main.model.act.ActionCenterActivity;
//import com.android.bluetown.home.main.model.act.FleaMarketActivity;
//import com.android.bluetown.home.main.model.act.MakeFriendsActivity;
//import com.android.bluetown.home.main.model.act.RealTimeZoneActivity;
//import com.android.bluetown.home.main.model.act.SmartCateenActivity;
//import com.android.bluetown.home.main.model.act.SmartParkingActivity;
//import com.android.bluetown.model.HomeTypeContentModel;
//import com.android.bluetown.my.AuthenticationActivity;
//import com.android.bluetown.my.AuthenticationIngActivity;
//import com.android.bluetown.mywallet.activity.BliiListActivity;
//import com.android.bluetown.mywallet.activity.GesturePSWCheckActivity;
//import com.android.bluetown.mywallet.activity.MyWalletActivity;
//import com.android.bluetown.pref.SharePrefUtils;
//import com.android.bluetown.utils.DisplayUtils;
//import com.android.bluetown.utils.HomeUtils;
//import com.android.bluetown.view.DecoratorViewPager;
//import com.android.bluetown.view.NoScrollGridView;
//import com.android.bluetown.view.PinnedSectionListView;
//import net.tsz.afinal.FinalDb;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Dafen on 2018/6/24.
// */
//
//public class HomeSelectedListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter, AbsListView.OnScrollListener, View.OnClickListener {
//    public static final int FLOW_VIEW = 0;
//    public static final int CONTENT_VIEW = 1;
//    public static final int CAR_VIEW = 2;
//    private ArrayList<HomeTypeContentModel> modelLists;
//    private LayoutInflater inflater;
//    private Context mContext;
//    private boolean isScrolling;
//    private SharePrefUtils prefUtils;
//    private FinalDb db;
//    private String noticeId;
//    private AppCompatActivity activity;
//    public HomeSelectedListAdapter(AppCompatActivity activity,Context context) {
//        inflater = LayoutInflater.from(context);
//        this.mContext = context;
//        this.activity = activity;
//        db = FinalDb.create(context);
//        prefUtils = new SharePrefUtils(context);
//    }
//    public void setData( ArrayList<HomeTypeContentModel> mModelLists){
//        if (mModelLists.size()>0)
//        modelLists = (ArrayList<HomeTypeContentModel>) mModelLists.clone();
//        notifyDataSetChanged();
//    }
//    public boolean isScrolling() {
//        return isScrolling;
//    }
//
//    @Override
//    public boolean isItemViewTypePinned(int viewType) {
//        return FLOW_VIEW == viewType;
//    }
//
//    @Override
//    public int getCount() {
//        return modelLists.size() == 0 ? 0 : modelLists.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return 0;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (modelLists.get(position).type == 0) {
//            return FLOW_VIEW;
//        } else if (modelLists.get(position).type == 1) {
//            return CONTENT_VIEW;
//        } else if (modelLists.get(position).type == 2) {
//            return CAR_VIEW;
//        }
//        return -1;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        int viewType = getItemViewType(position);
//        if (convertView == null) {
//            convertView = switchType(viewType, parent, position);
//        }
//        return convertView;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 3;
//    }
//
//    private View switchType(int viewType, ViewGroup parent, int position) {
//        View convertView = null;
//        PayViewHolder payViewHolder = null;
//        ContentViewHolder contentViewHolder = null;
//        CarViewHolder carViewHolder = null;
//        switch (viewType) {
//            case FLOW_VIEW:
//                convertView = inflater.inflate(R.layout.item_flow_pay_remind, parent, false);
//                if (payViewHolder == null) {
//                    payViewHolder = new PayViewHolder(convertView);
//                    convertView.setTag(payViewHolder);
//                } else {
//                    payViewHolder = (PayViewHolder) convertView.getTag();
//                }
//                payViewHolder.mTvPayContent.setText(Html.fromHtml("<font color=#666666>" + modelLists.get(position).latestMessageBean.Content + "</font>" + "   " + "<font color=#999999>" +
//                        modelLists.get(position).latestMessageBean.GenerateTime + "</font>"));
//                break;
//            case CONTENT_VIEW:
//                convertView = inflater.inflate(R.layout.include_home, parent, false);
//                if (contentViewHolder == null) {
//                    contentViewHolder = new ContentViewHolder(convertView);
//                    convertView.setTag(contentViewHolder);
//                } else {
//                    contentViewHolder = (ContentViewHolder) convertView.getTag();
//                }
//                contentViewHolder.mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//                HomeUtils.setModelsLAdapter(mContext, contentViewHolder.mGridView, true, false, 8);
//                contentViewHolder.mGridView.setOnItemClickListener(mainModeOnItemClick);
//                contentViewHolder.mIvNotifyNotice.setOnClickListener(this);
//                contentViewHolder.mTvNotive.setOnClickListener(this);
//                contentViewHolder.mTvNotive.setText(modelLists.get(position).noticeBean.Title);
//                noticeId = modelLists.get(position).noticeBean.Id;
//
//                if (modelLists.get(position).HotActivities.size()<=0){
//                    contentViewHolder.mHotEventViewPager.setVisibility(View.GONE);
//                }else {
//                    contentViewHolder.mHotEventViewPager.setVisibility(View.VISIBLE);
//                    contentViewHolder.mHotEventViewPager.setAdapter(new FragmentPagerAdapter(activity.getSupportFragmentManager()) {
//                        @Override
//                        public Fragment getItem(int i) {
//                            return new HomeEventFragment().getNewFragment(modelLists.get(position).HotActivities.get(i));
//                        }
//
//                        @Override
//                        public int getCount() {
//                            return modelLists.get(position).HotActivities.size();
//                        }
//
//                    });
//                }
//                contentViewHolder.mHotEventViewPager.setOffscreenPageLimit(modelLists.get(position).HotActivities.size());
//                contentViewHolder.mHotEventViewPager.setPageMargin(DisplayUtils.dip2px(mContext,-8));
//                contentViewHolder.mLlHotBusiness.setOnClickListener(this);
//                if (modelLists.get(position).HotMerchants.size()<=0){
//                    contentViewHolder.mHotBusinessViewPager.setVisibility(View.GONE);
//                }else {
//                    contentViewHolder.mHotBusinessViewPager.setVisibility(View.VISIBLE);
//                    contentViewHolder.mHotBusinessViewPager.setAdapter(new FragmentPagerAdapter(activity.getSupportFragmentManager()) {
//                        @Override
//                        public Fragment getItem(int i) {
//                            return new HomeBusinessFragment().getNewFragment(modelLists.get(position).HotMerchants.get(i));
//                        }
//                        @Override
//                        public int getCount() {
//                            return modelLists.get(position).HotMerchants.size();
//                        }
//                    });
//                    contentViewHolder.mHotBusinessViewPager.setOffscreenPageLimit(modelLists.get(position).HotMerchants.size());
//                    contentViewHolder.mHotBusinessViewPager.setPageMargin(DisplayUtils.dip2px(mContext, 8));
//                }
//                contentViewHolder.mGropChat.setOnClickListener(this);
//                break;
//            case CAR_VIEW:
//                convertView = inflater.inflate(R.layout.include_car_location_message, parent, false);
//                if (carViewHolder==null){
//                    carViewHolder = new CarViewHolder(convertView);
//                    convertView.setTag(carViewHolder);
//                }else{
//                    carViewHolder = (CarViewHolder) convertView.getTag();
//                }
//                carViewHolder.mTvNoNum.setText(modelLists.get(position).Appointments.License);
//                carViewHolder.mTvCarTime.setText(modelLists.get(position).Appointments.CreateTime);
//                carViewHolder.mTvCarNum.setText(modelLists.get(position).Appointments.Telphone);
//                carViewHolder.mTvStartTime.setText(modelLists.get(position).Appointments.EnterTime);
//                carViewHolder.mTvEndTime.setText(modelLists.get(position).Appointments.LeaveTime);
//                carViewHolder.mTvChargeStatus.setText(modelLists.get(position).Appointments.StatusName);
//                break;
//
//        }
//        return convertView;
//    }
//
//    @Override
//    public void onClick(View v) {
//        String userId = "";
//        List<MemberUser> users = db.findAll(MemberUser.class);
//        if (users != null && users.size() != 0) {
//            MemberUser user = users.get(0);
//            if (user != null) {
//                userId = user.getMemberId();
//            }
//        }
//        switch (v.getId()){
//            case R.id.iv_notify_notice:
//                mContext.startActivity(new Intent(mContext,MessageListActivity.class));
//                break;
//            case R.id.tv_notice:
//                Intent intent1 = new Intent();
//                intent1.setClass(mContext,MessageDetailsActivity.class);
//                intent1.putExtra("hid",noticeId);
//                mContext.startActivity(intent1);
//                break;
//            case R.id.ll_business_list:
//                Intent intent = new Intent();
//                intent.putExtra("type","1");
//                intent.setClass(mContext,MainActivity.class);
//                mContext.startActivity(intent);
//                break;
//            case R.id.iv_group_chat:
//                if (!TextUtils.isEmpty(userId)) {
//                    // 交友
//                    mContext.startActivity(new Intent(mContext,
//                            MakeFriendsActivity.class));
//                } else {
//                    TipDialog.showDialogNoClose(mContext,
//                            R.string.tip, R.string.confirm,
//                            R.string.login_info_tip, LoginActivity.class);
//                }
//                break;
//        }
//    }
//
//    public class PayViewHolder {
//        TextView mTvPayContent;
//
//        public PayViewHolder(View view) {
//            mTvPayContent = view.findViewById(R.id.tv_home_pay_remind);
//        }
//    }
//
//    public class ContentViewHolder {
//        NoScrollGridView mGridView;
//        ImageView mIvNotifyNotice;
//        TextView mTvNotive;
//        LinearLayout mLlHotEvents;
//        DecoratorViewPager mHotEventViewPager;
//        LinearLayout mLlHotBusiness;
//        DecoratorViewPager mHotBusinessViewPager;
//        ImageView mGropChat;
//
//        public ContentViewHolder(View view) {
//            mGridView = view.findViewById(R.id.mainModelGridView);
//            mIvNotifyNotice = view.findViewById(R.id.iv_notify_notice);
//            mTvNotive = view.findViewById(R.id.tv_notice);
//            mLlHotEvents = view.findViewById(R.id.ll_hot_event_list);
//            mHotEventViewPager = view.findViewById(R.id.recycler_hot_event_list);
//            mLlHotBusiness = view.findViewById(R.id.ll_business_list);
//            mHotBusinessViewPager = view.findViewById(R.id.recycler_business_list);
//            mGropChat = view.findViewById(R.id.iv_group_chat);
//        }
//    }
//
//    public class CarViewHolder {
//        TextView mTvNoNum;
//        TextView mTvCarTime;
//        TextView mTvCarNum;
//        TextView mTvStartTime;
//        TextView mTvEndTime;
//        TextView mTvChargeStatus;
//
//        public CarViewHolder(View view) {
//            mTvNoNum = view.findViewById(R.id.tv_car_no_number);
//            mTvCarTime = view.findViewById(R.id.tv_car_intime);
//            mTvCarNum = view.findViewById(R.id.tv_car_name_and_number);
//            mTvStartTime = view.findViewById(R.id.tv_start_time);
//            mTvEndTime = view.findViewById(R.id.tv_end_time);
//            mTvChargeStatus = view.findViewById(R.id.tv_charge_status);
//        }
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
////        if (null != onAdapterScrollListener) {
////            onAdapterScrollListener.onScrollStateChanged(view, scrollState);
////        }
//
//        // 设置是否滚动的状态
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) { // 不滚动状态
//            isScrolling = false;
//            this.notifyDataSetChanged();
//        } else {
//            isScrolling = true;
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
////        if (null != onAdapterScrollListener) {
////            onAdapterScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
////        }
//    }
//
//    /**
//     * 主要版块的OnItemClickListener
//     */
//    private AdapterView.OnItemClickListener mainModeOnItemClick = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                long arg3) {
//            // TODO Auto-generated method stub
//            String userId = "";
//            List<MemberUser> users = db.findAll(MemberUser.class);
//            if (users != null && users.size() != 0) {
//                MemberUser user = users.get(0);
//                if (user != null) {
//                    userId = user.getMemberId();
//                }
//            }
//            switch (arg2) {
////				case 0:
////					// 园区企业
////					startActivity(new Intent(HomeActivity.this,
////							CompanyShowActivity.class));
////					break;
//                case 0:
//                    // 访客预约
//                    if (!TextUtils.isEmpty(userId)) {
//                        mContext.startActivity(new Intent(mContext,
//                                SmartParkingActivity.class));
//                    } else {
//                        TipDialog.showDialogNoClose(mContext,
//                                R.string.tip, R.string.confirm,
//                                R.string.login_info_tip, LoginActivity.class);
//                    }
//                    break;
////                case 1:
////                    // 车位包月
////                    if (!TextUtils.isEmpty(userId)) {
////                        if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "")
////                                .equals("1")) {
//////						startActivity(new Intent(HomeActivity.this,
//////								ParkingMonthlyDetailActivity.class));
////                            TipDialog.showDialog(HomeActivity.this,
////                                    R.string.tip, R.string.confirm,
////                                    R.string.noOpenModule);
////                        } else if (prefUtils.getString(SharePrefUtils.CHECKSTATE,
////                                "").equals("3")) {
////                            TipDialog.showDialogNoClose(HomeActivity.this,
////                                    R.string.tip, R.string.AuthenticationIng,
////                                    R.string.Authenticationinfo,
////                                    AuthenticationIngActivity.class);
////                        } else {
////                            TipDialog.showDialogNoClose(HomeActivity.this,
////                                    R.string.tip, R.string.gotoAuthentication,
////                                    R.string.Authenticationinfo,
////                                    AuthenticationActivity.class);
////                        }
////
////                    } else {
////                        TipDialog.showDialogNoClose(HomeActivity.this,
////                                R.string.tip, R.string.confirm,
////                                R.string.login_info_tip, LoginActivity.class);
////                    }
////                    break;
//                case 2:
//                    // 智慧餐厅
//                    mContext.startActivity(new Intent(mContext,
//                            SmartCateenActivity.class));
//                    break;
//                case 4:
//                    // 钱包
//                    setToBillListActivity(userId, new Intent(mContext,
//                            MyWalletActivity.class), 1);
//                    break;
//                case 5:
//                    setToBillListActivity(userId, new Intent(mContext,
//                            BliiListActivity.class), 3);
//
//                    break;
//                case 6:
//                    // 实时园区
//                    if (!TextUtils.isEmpty(userId)) {
//                        mContext.startActivity(new Intent(mContext,
//                                RealTimeZoneActivity.class));
//                    } else {
//                        TipDialog.showDialogNoClose(mContext,
//                                R.string.tip, R.string.confirm,
//                                R.string.login_info_tip, LoginActivity.class);
//                    }
//                    break;
//                case 3:
//                    // 活动中心
//                    mContext.startActivity(new Intent(mContext,
//                            ActionCenterActivity.class));
//                    // 点击查看清空活动中心的推送的消息数
//                    if (BlueTownApp.actionMsgCount != 0) {
//                        BlueTownApp.actionMsgCount = 0;
//                        Intent refreshintent = new Intent(
//                                "com.android.bm.refresh.new.msg.action");
//                        mContext.sendBroadcast(refreshintent);
//                    }
//                    break;
//                case 1:
//                    // 跳蚤市场
//                    mContext.startActivity(new Intent(mContext,
//                            FleaMarketActivity.class));
//                    // 点击查看清空跳蚤市场的推送的消息数
//                    if (BlueTownApp.fleaMarketMsgCount != 0) {
//                        BlueTownApp.fleaMarketMsgCount = 0;
//                        Intent refreshintent = new Intent(
//                                "com.android.bm.refresh.new.msg.action");
//                        mContext.sendBroadcast(refreshintent);
//                    }
//
//                    break;
////                case 6:
////                    if (!TextUtils.isEmpty(userId)) {
////                        // 交友
////                        startActivity(new Intent(HomeActivity.this,
////                                MakeFriendsActivity.class));
////                    } else {
////                        TipDialog.showDialogNoClose(HomeActivity.this,
////                                R.string.tip, R.string.confirm,
////                                R.string.login_info_tip, LoginActivity.class);
////                    }
////
////                    break;
////				case 10:
////					// 园区介绍
////					Intent intent = (new Intent(HomeActivity.this,
////							WebViewActivity.class));
////					intent.putExtra("URL", "http://www.smartparks.cn/about.html");
////					intent.putExtra("title", "园区介绍");
////					startActivity(intent);
////					break;
//                case 7:
//                    // 更多
//                    mContext.startActivity(new Intent(mContext, FindActivity.class));
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    };
//
//    private void setToBillListActivity(String userId, Intent intent2, int value) {
//        // 账单
//        if (!TextUtils.isEmpty(userId)) {
//            if (prefUtils.getString(SharePrefUtils.CHECKSTATE, "")
//                    .equals("1")) {
//                if (prefUtils
//                        .getString(SharePrefUtils.HANDPASSWORD, "")
//                        .equals("")
//                        || prefUtils.getString(
//                        SharePrefUtils.HANDPASSWORD, "") == null
//                        || prefUtils.getString(
//                        SharePrefUtils.HANDPASSWORD, "")
//                        .equals("null")) {
//                    mContext.startActivity(intent2);
//                } else {
//                    Intent intent = (new Intent(mContext,
//                            GesturePSWCheckActivity.class));
//                    intent.putExtra("type", value);
//                    mContext.startActivity(intent);
//                }
//            } else if (prefUtils.getString(SharePrefUtils.CHECKSTATE,
//                    "").equals("3")) {
//                TipDialog.showDialogNoClose(mContext,
//                        R.string.tip, R.string.AuthenticationIng,
//                        R.string.Authenticationinfo,
//                        AuthenticationIngActivity.class);
//            } else {
//                TipDialog.showDialogNoClose(mContext,
//                        R.string.tip, R.string.gotoAuthentication,
//                        R.string.Authenticationinfo,
//                        AuthenticationActivity.class);
//            }
//        } else {
//            TipDialog.showDialogNoClose(mContext,
//                    R.string.tip, R.string.confirm,
//                    R.string.login_info_tip, LoginActivity.class);
//        }
//    }
//}

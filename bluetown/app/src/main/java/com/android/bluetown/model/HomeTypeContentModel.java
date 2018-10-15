//package com.android.bluetown.model;
//
//import com.android.bluetown.bean.BusinessAndEventBean;
//
//import java.util.List;
//
///**
// * Created by Dafen on 2018/6/24.
// */
//
//public class HomeTypeContentModel {
//    public int type;
//    public BusinessAndEventBean.LatestMessageBean latestMessageBean;
//    public BusinessAndEventBean.NoticeBean noticeBean;
//    public List<BusinessAndEventBean.HotMerchantsBean> HotMerchants;
//    public List<BusinessAndEventBean.HotActivitiesBean> HotActivities;
//    public BusinessAndEventBean.AppointmentsBean Appointments;
//    public HomeTypeContentModel(BusinessAndEventBean.LatestMessageBean latestMessageBean,int type){
//        this.type = type;
//        this.latestMessageBean = latestMessageBean;
//    }
//
//    public HomeTypeContentModel(BusinessAndEventBean.NoticeBean noticeBean,List<BusinessAndEventBean.HotMerchantsBean> HotMerchants
//    ,List<BusinessAndEventBean.HotActivitiesBean> HotActivities,int type){
//        this.noticeBean = noticeBean;
//        this.HotMerchants = HotMerchants;
//        this.HotActivities = HotActivities;
//        this.type = type;
//
//    }
//
//    public HomeTypeContentModel(BusinessAndEventBean.AppointmentsBean appointmentsBean,int type){
//        this.Appointments = appointmentsBean;
//        this.type = type;
//    }
//
//
//}

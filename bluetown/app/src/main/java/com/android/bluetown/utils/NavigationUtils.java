//package com.android.bluetown.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
//import com.baidu.mapapi.navi.BaiduMapNavigation;
//import com.baidu.mapapi.navi.NaviParaOption;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Dafen on 2018/7/6.
// */
//
//public class NavigationUtils {
//
//    public void startNavi(Context context, LatLng startLocation, LatLng endLocation) {
//        //百度地图,从起点是LatLng ll_location = new LatLng("你的纬度latitude","你的经度longitude");
//        //终点是LatLng ll = new LatLng("你的纬度latitude","你的经度longitude");
//        NaviParaOption para = new NaviParaOption();
//        para.startPoint(startLocation);
//        para.startName("从这里开始");
//        para.endPoint(endLocation);
//        para.endName("到这里结束");
//        try {
//            BaiduMapNavigation.openBaiduMapNavi(para, context);
//        } catch (BaiduMapAppNotSupportNaviException e) {
//            e.printStackTrace();
//            ToastManager.getInstance(context).showText("您尚未安装百度地图或地图版本过低");
//        }
//    }
//
//    //高德地图,起点就是定位点
//    // 终点是LatLng ll = new LatLng("你的纬度latitude","你的经度longitude");
//    public void startNaviGao(Context context,long latitude,long longitude,String companyName) {
//        if (isAvilible(context, "com.autonavi.minimap")) {
//            try {
//                //sourceApplication
//                Intent intent = Intent.getIntent("androidamap://navi?sourceApplication="+companyName+"&poiname=我的目的地&lat=" + latitude + "&lon=" + longitude+ "&dev=0");
//                context.startActivity(intent);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        } else {
//            ToastManager.getInstance(context).showText("您尚未安装高德地图或地图版本过低");
//        }
//
//    }
//
//    /**
//     * 调用谷歌app
//     */
//    public void startNaviGoogle(Context context,long latitude,long longitude) {
//        if (isAvilible(context, "com.google.android.apps.maps")) {
//            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," +longitude);
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//            mapIntent.setPackage("com.google.android.apps.maps");
//            context.startActivity(mapIntent);
//        } else {
//            ToastManager.getInstance(context).showText("您尚未安装谷歌地图或地图版本过低");
//        }
//
//    }
//
//
//    /**
//     * 验证导航app是否安装
//     * @param context
//     * @param packageName
//     * @return
//     */
//    public static boolean isAvilible(Context context, String packageName) {
//        //获取packagemanager
//        final PackageManager packageManager = context.getPackageManager();
//        //获取所有已安装程序的包信息
//        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
//        //用于存储所有已安装程序的包名
//        List<String> packageNames = new ArrayList<String>();
//        //从pinfo中将包名字逐一取出，压入pName list中
//        if (packageInfos != null) {
//            for (int i = 0; i < packageInfos.size(); i++) {
//                String packName = packageInfos.get(i).packageName;
//                packageNames.add(packName);
//            }
//        }
//        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
//        return packageNames.contains(packageName);
//
//    }
//}
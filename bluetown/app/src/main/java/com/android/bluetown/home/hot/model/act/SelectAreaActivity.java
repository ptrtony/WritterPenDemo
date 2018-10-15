package com.android.bluetown.home.hot.model.act;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.SmartParkAdapter;
import com.android.bluetown.bean.RemindCarport;
import com.android.bluetown.custom.dialog.PromptDialog;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.home.main.model.act.GuestAppointmentCarSeatActivity;
import com.android.bluetown.home.main.model.act.SmartParkingActivity;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.mywallet.activity.RechargeActivity;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.ParkingLotsData;
import com.android.bluetown.result.RemindCarportResult;
import com.android.bluetown.utils.Constant;
import com.android.bluetown.view.ResizableImageView;
import com.android.bluetown.view.widgets.IFooterWrapper;

import java.util.ArrayList;
import java.util.List;



public class SelectAreaActivity extends TitleBarActivity implements ResizableImageView.OnSingleTapListener, View.OnClickListener {
    ResizableImageView mPhotoView;
    private String parkingALotsId, parkingBLotsId;
    private List<ParkingLotsData.BodyBean.ParkinglotsBean> resultBeans = new ArrayList<>();
    private String availableCountA;
    private String availableCountB;
    private boolean iscanable = false;
    private String errorResult = "";
    private ParkingLotsData.BodyBean.QualificationBean qualificationBean;
    private ParkingLotsData.BodyBean.ParkinglotsBean parkingAreaA;
    private ParkingLotsData.BodyBean.ParkinglotsBean parkingAreaB;
    private SharePrefUtils prefUtils;
    private String parkingId="";
    private String parkingNo="";
    private String parkingName="";
    private String reservedSpace="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        findViewById(R.id.ll_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_title)).setText("车位预约");
        mPhotoView = findViewById(R.id.photo_image_view);
        mPhotoView.setOnSingleTap(this);
        prefUtils = new SharePrefUtils(this);
        seatAreaApi();
    }





    private void parkingLotsData() {
        httpInstance.post(Constant.HOST_URL1+ Constant.Interface.ParkingLots, new AbsHttpStringResponseListener(SelectAreaActivity.this) {
            @Override
            public void onSuccess(int i, String s) {
                if (resultBeans.size() > 0) resultBeans.clear();
                ParkingLotsData parkingLotsData = (ParkingLotsData) AbJsonUtil.fromJson(s, ParkingLotsData.class);
                if (parkingLotsData.rep_code.equals(Constant.HTTP_SUCCESS)) {
                    resultBeans.addAll(parkingLotsData.body.parkinglots);
                    parkingALotsId = resultBeans.get(0).id;
                    parkingBLotsId = resultBeans.get(1).id;
                    parkingAreaA = parkingLotsData.body.parkinglots.get(0);
                    parkingAreaB = parkingLotsData.body.parkinglots.get(1);

                    qualificationBean = parkingLotsData.body.qualification;
                    if (parkingAreaA.enabled==1){
                        availableCountA = "(剩余车位" + parkingAreaA.available_count + ")";
                    }else{
                        availableCountA = "(未开放)";
                    }
                    if (parkingAreaB.enabled==1){
                        availableCountB = "(剩余车位" + parkingAreaB.available_count + ")";
                    }else{
                        availableCountB = "(未开放)";
                    }
                    mPhotoView.setAreaName(availableCountA,availableCountB);
                    iscanable = true;

                }else{
                    iscanable = false;
                    errorResult = parkingLotsData.rep_msg;
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                super.onFailure(i, s, throwable);
                throwable.printStackTrace();
                iscanable = false;
                errorResult =  throwable.getMessage().toString();
            }
        });
    }

    @Override
    public void initTitle() {
        setTitleState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSingleTap(int type) {
        if (type==2){
            TipDialog.showDialog(this,R.string.tip,"该区域未开放");
//            bundle.putString("parkingId",parkingALotsId);
//            bundle.putString("paringName",parkingAreaA.parkinglot_name);
//            intent.putExtras(bundle);
        }else if (type==1){
            getRemindCarport();
        }

//        if (iscanable){
//            if (type == 1) {
//                if (null!=qualificationBean.qua_code && "8".equals(qualificationBean.qua_code)){
//                    if (parkingAreaA.available_count!=0&&parkingAreaA.enabled==1){
//                        bundle.putString("parkingId",parkingALotsId);
//                        bundle.putString("paringName",parkingAreaA.parkinglot_name);
//                        intent.putExtras(bundle);
//                        startActivity(intent,bundle);
//                    }else if (parkingAreaA.available_count==0&&parkingAreaA.enabled==1){
//                        TipDialog.showDialog(SelectAreaActivity.this,R.string.confirm,R.string.car_seat_zero);
//                    }else if (parkingAreaA.enabled==0){
//                        TipDialog.showDialog(SelectAreaActivity.this,R.string.confirm,R.string.area_is_close);
//                    }
//                }else{
//                    TipDialog.showDialogStartNewActivity(SelectAreaActivity.this, R.string.tip,
//                            R.string.confirm,qualificationBean.qua_msg
//                            , RechargeActivity.class);
//                }

//
//            } else if (type == 2) {
//                if (null!=qualificationBean.qua_code && "8".equals(qualificationBean.qua_code)){
//                    if (parkingAreaB.available_count!=0&&parkingAreaB.enabled==1){
//                        bundle.putString("parkingId",parkingBLotsId);
//                        bundle.putString("paringName",parkingAreaB.parkinglot_name);
//                        intent.putExtras(bundle);
//                        startActivity(intent,bundle);
//                    }else if (parkingAreaB.available_count==0&&parkingAreaB.enabled==1){
//                        TipDialog.showDialog(SelectAreaActivity.this,R.string.confirm,R.string.car_seat_zero);
//                    }else if (parkingAreaB.enabled==0){
//                        TipDialog.showDialog(SelectAreaActivity.this,R.string.confirm,R.string.area_is_close);
//                    }
//                }else{
//                    TipDialog.showDialogStartNewActivity(SelectAreaActivity.this, R.string.tip,
//                            R.string.confirm,qualificationBean.qua_msg
//                            , RechargeActivity.class);
//                }
//
//            }
//        }else{
//            if (errorResult!=null){
//                TipDialog.showDialog(this,R.string.tip,R.string.confirm,errorResult);
//            }
//
//        }


    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private void getRemindCarport() {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(reservedSpace)&&!reservedSpace.equals("0")){
            Intent intent = new Intent(SelectAreaActivity.this, GuestAppointmentCarSeatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("parkingId",parkingId);
            bundle.putString("paringName",parkingName);
            bundle.putString("parkingNo",parkingNo);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            TipDialog.showDialog(SelectAreaActivity.this,R.string.confirm,R.string.car_seat_zero);
        }

    }

    private void seatAreaApi(){
        params.put("gardenId",
                prefUtils.getString(SharePrefUtils.GARDEN_ID, ""));
        httpInstance.post(
                Constant.HOST_URL + Constant.Interface.REMIND_CARPORT, params,
                new AbsHttpStringResponseListener(this, null) {
                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        RemindCarportResult result = (RemindCarportResult) AbJsonUtil
                                .fromJson(arg1, RemindCarportResult.class);
                        if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                            RemindCarport carportBean = result.getData().getStallList().get(0);
//                            if (carportBean.getRemainingReservedSpaces()!=null&&carportBean.getRemainingReservedSpaces().equals("0")){
                                reservedSpace = carportBean.getRemainingReservedSpaces();
                                parkingId = carportBean.getParkingLotNo();
                                parkingNo = carportBean.getMid();
                                parkingName = carportBean.getParkingName();
                                mPhotoView.setAreaName("(剩余车位数"+carportBean.getRemainingReservedSpaces()+")","(该区域未开放)");
//                            }else{
//                                TipDialog.showDialog(SelectAreaActivity.this,R.string.confirm,R.string.car_seat_zero);
//                            }
                        }
                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        // TODO Auto-generated method stub
                        super.onFailure(i, s, throwable);
                        TipDialog.showDialog(SelectAreaActivity.this,
                                R.string.tip, R.string.confirm, "第三方道闸系统故障！");
                    }
                });
    }
}

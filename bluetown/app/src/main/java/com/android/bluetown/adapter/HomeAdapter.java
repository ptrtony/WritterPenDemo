package com.android.bluetown.adapter;
import com.android.bluetown.R;
import com.android.bluetown.bean.BusinessAndEventBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


/**
 * Created by Dafen on 2018/6/9.
 */

public class HomeAdapter extends BaseQuickAdapter<BusinessAndEventBean.AppointmentsBean,BaseViewHolder>{
    public HomeAdapter() {
        super(R.layout.include_car_location_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusinessAndEventBean.AppointmentsBean item) {
        helper.setText(R.id.tv_car_no_number,"预计到访时间");
        helper.setText(R.id.tv_car_intime,"车牌号码");
        helper.setText(R.id.tv_start_time,item.PlanTime);
        helper.setText(R.id.tv_end_time,item.License);
        helper.setText(R.id.tv_charge_status,item.StatusName);
        helper.setText(R.id.tv_plan_time,item.CreateTime);
    }
}

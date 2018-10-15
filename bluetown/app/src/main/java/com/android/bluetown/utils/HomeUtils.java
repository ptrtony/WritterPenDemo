package com.android.bluetown.utils;

import java.util.ArrayList;

import android.content.Context;
import android.widget.GridView;

import com.android.bluetown.R;
import com.android.bluetown.adapter.HomeGridViewAdapter;
import com.android.bluetown.bean.HomeModelBean;

/**
 * 
 * @ClassName: HomeUtils
 * @Description:TODO(HomeActivity的相关常量)
 * @author: shenyz
 * @date: 2015年7月21日 下午4:46:52
 * 
 */
public class HomeUtils {
	/**
	 * 
	 * @param context
	 * @param gridView
	 * @param isShowMain
	 *            显示的是否为HomeActivity中的8个主要模块
	 * @param isCheck
	 *            是否显示Checkbox
	 * @param count
	 *            显示的模块的数目
	 */
	public static void setModelsLAdapter(Context context, GridView gridView,
			boolean isShowMain, boolean isCheck, int count) {
		ArrayList<HomeModelBean> modelsList = null;
		if (modelsList == null) {
			modelsList = new ArrayList<HomeModelBean>();
		}
		modelsList.clear();
		if (isCheck) {
			// 显示checkbox
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type1,
					R.string.household_appliances));
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type2,
					R.string.appliances));
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type3,
					R.string.mother_baby_products));
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type4,
					R.string.outdoor_activities));
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type5,
					R.string.ambrosia));
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type6,
					R.string.skin_beauty));
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type7,
					R.string.fashion_girl));
			modelsList.add(new HomeModelBean(R.drawable.ic_shop_type8,
					R.string.digital_burning_guest));

		} else {
			if (isShowMain) {
				// HomeActivity主模块8个
//				modelsList.add(new HomeModelBean(R.drawable.icon_company,
//						R.string.company_show));
				modelsList.add(new HomeModelBean(R.drawable.icon_reservation,
						R.string.guest_order));
//				modelsList.add(new HomeModelBean(R.drawable.icon_monthly_parking,
//						R.string.smart_parking));
				modelsList.add(new HomeModelBean(R.drawable.ic_flea_market,
						R.string.flea_market));
//				modelsList.add(new HomeModelBean(R.drawable.icon_dining_room,
//						R.string.smart_cateen));
				modelsList.add(new HomeModelBean(R.drawable.ic_find_main_1,
						R.string.company_show));
				modelsList.add(new HomeModelBean(R.drawable.icon_action_center,
						R.string.action_center));

				modelsList.add(new HomeModelBean(R.drawable.icon_wallet,
						R.string.wallet));
				modelsList.add(new HomeModelBean(R.drawable.icon_bill,
						R.string.bill_check));
//				modelsList.add(new HomeModelBean(R.drawable.icon_friends,
//						R.string.make_friends));
				modelsList.add(new HomeModelBean(R.drawable.icon_camera,
						R.string.real_time_zone));
//
//				modelsList.add(new HomeModelBean(R.drawable.ic_garden_intr,
//						R.string.garden_intr));
				modelsList.add(new HomeModelBean(R.drawable.icon_more,
						R.string.more));
				

			} else {
				// FindActivity常用模块8个模块
				if (count == 8) {
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_1,
							R.string.company_show));
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_2,
							R.string.smart_parking));
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_3,
							R.string.real_time_zone));
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_4,
							R.string.guest_order));
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_5,
							R.string.self_help_service));
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_6,
							R.string.flea_market));
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_7,
							R.string.action_center));
					modelsList.add(new HomeModelBean(R.drawable.ic_find_main_8,
							R.string.make_friends));
				} else if (count == 7) {
					// FindActivity更多模块7个模块（企业用户）
					modelsList.add(new HomeModelBean(R.drawable.icon_company,
						R.string.company_show1));
					modelsList.add(new HomeModelBean(R.drawable.icon_monthly_parking,
							R.string.smart_parking));
//					modelsList.add(new HomeModelBean(R.drawable.home_pic_1,
//							R.string.diary_search));
//					modelsList.add(new HomeModelBean(R.drawable.home_pic_2,
//							R.string.payment_management));
//					modelsList.add(new HomeModelBean(R.drawable.home_pic_3,
//							R.string.procedure_approval));
					modelsList.add(new HomeModelBean(R.drawable.icon_action_center,
							R.string.company_info_publish));

//					modelsList.add(new HomeModelBean(R.drawable.home_pic_5,
//							R.string.company_growup_help));
					modelsList.add(new HomeModelBean(R.drawable.icon_reservation,
							R.string.complaint_offer));
					modelsList.add(new HomeModelBean(R.drawable.icon_introduction,
							R.string.self_help_service));
					modelsList.add(new HomeModelBean(R.drawable.icon_friends,
							R.string.make_friends));
					modelsList.add(new HomeModelBean(R.drawable.icon_dining_room,
							R.string.smart_cateen));
					modelsList.add(new HomeModelBean(R.drawable.icon_camera,
							R.string.company_show));
					modelsList.add(new HomeModelBean(R.drawable.my_shouce,
							R.string.event_guide));
//					modelsList.add(new HomeModelBean(R.drawable.icon_company,
//						R.string.company_show));
//					modelsList.add(new HomeModelBean(R.drawable.icon_camera,
//						R.string.real_time_zone));
//					modelsList.add(new HomeModelBean(R.drawable.ic_flea_market,
//						R.string.flea_market));
//					modelsList.add(new HomeModelBean(R.drawable.ic_garden_intr,
//						R.string.garden_intr));

				} else {
					// FindActivity更多模块4个模块（普通、游客用户）
					modelsList.add(new HomeModelBean(R.drawable.home_pic_2,
							R.string.payment_management));

					modelsList.add(new HomeModelBean(R.drawable.home_pic_5,
							R.string.company_growup_help));
					modelsList.add(new HomeModelBean(R.drawable.home_pic_6,
							R.string.yellow_pages));
					modelsList.add(new HomeModelBean(R.drawable.home_pic_7,
							R.string.complaint_offer));
				}

			}
		}
		HomeGridViewAdapter adapter = new HomeGridViewAdapter(context,
				modelsList, isShowMain, isCheck);
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	/**
	 * 周边餐饮
	 * 
	 * @param context
	 * @param gridView
	 * @param model
	 */
	public static void setModelsLAdapter(Context context, GridView gridView,
			String model) {
		ArrayList<HomeModelBean> modelsList = null;
		if (modelsList == null) {
			modelsList = new ArrayList<HomeModelBean>();
		}
		modelsList.clear();
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_1,
				R.string.canteen_type_1));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_2,
				R.string.canteen_type_2));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_3,
				R.string.canteen_type_3));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_4,
				R.string.canteen_type_4));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_5,
				R.string.canteen_type_5));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_6,
				R.string.canteen_type_6));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_7,
				R.string.canteen_type_7));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_8,
				R.string.canteen_type_8));
		modelsList.add(new HomeModelBean(R.drawable.canteen_type_9,
				R.string.more));

		HomeGridViewAdapter adapter = new HomeGridViewAdapter(context,
				modelsList, model);
		gridView.setAdapter(adapter);

	}

}

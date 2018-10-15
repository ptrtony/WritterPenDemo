package com.android.bluetown.surround;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.DishSelectGoodAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.listener.OnFoodCallBackListener;

/**
 * 预定菜的列表
 * 
 * @author shenyz
 * 
 */
public class BookDishListActivity extends TitleBarActivity implements
		OnClickListener, OnFoodCallBackListener {
	private ListView mShowShowListView;
	private Button dishSelectOk;
	private TextView dishCount, totalPriceCount, totalOrigalCount;
	private String mid, merchantName, originalprice, currentprice, dinnerTime,
			tableName;
	private int totalCount;
	private List<RecommendDish> dishList;
	private DishSelectGoodAdapter adapter;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_dish_select_good);
		BlueTownExitHelper.addActivity(this);
		initUIView();
		initUIData();

	}

	/**
	 * 初始化组件
	 */
	private void initUIView() {
		Intent intent = getIntent();
		merchantName = intent.getStringExtra("merchantName");
		mid = intent.getStringExtra("meid");
		try {
			// 继续点菜和先订座后点菜
			dinnerTime = getIntent().getStringExtra("dinnerTime");
			tableName = getIntent().getStringExtra("tableName");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		originalprice = BlueTownApp.getOriginalPrice();
		currentprice = BlueTownApp.getDishesPrice();
		totalCount = BlueTownApp.getDishesCount();
		setTitleView(merchantName);
		mShowShowListView = (ListView) findViewById(R.id.lv_resaust_good);
		dishCount = (TextView) findViewById(R.id.tv_food_type);
		totalPriceCount = (TextView) findViewById(R.id.tv_food_new_prices);
		totalOrigalCount = (TextView) findViewById(R.id.tv_food_orginal_prices);
		dishSelectOk = (Button) findViewById(R.id.bt_resaustgood_sure);
		dishSelectOk.setOnClickListener(this);
	}

	/**
	 * 初始化界面组件
	 */
	private void initUIData() {
		dishList = BlueTownApp.getDishList();
		dishCount.setText("共计：" + totalCount + "个菜");
		totalPriceCount.setText("￥" + currentprice + "元");
		totalOrigalCount.setText("￥" + originalprice + "元");
		adapter = new DishSelectGoodAdapter(this, dishList, this);
		mShowShowListView.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_resaustgood_sure:
			String orderBy = BlueTownApp.ORDER_BY;
			if(totalCount==0){
				Toast.makeText(BookDishListActivity.this, "您还未点餐哦。",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 先点菜后订座-即详情点菜为“1”
			if (orderBy.equals("1")) {
				if (BlueTownApp.ORDER_TYPE.equals("continue")) {
					Intent intent = new Intent();
					intent.putExtra("merchantName", merchantName);
					intent.putExtra("meid", mid);
					intent.putExtra("dinnerTime", dinnerTime);
					intent.putExtra("tableName", tableName);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(BookDishListActivity.this,
							OnlineBookActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent();
					intent.putExtra("merchantName", merchantName);
					intent.putExtra("meid", mid);
					intent.putExtra("dinnerTime", dinnerTime);
					intent.putExtra("tableName", tableName);
					intent.setClass(BookDishListActivity.this,
							OnlineBookActivity.class);
					startActivity(intent);
					finish();
				}
			} else {
				// 先订座在点菜-即详情预定-提前点菜
				if (BlueTownApp.ORDER_TYPE.equals("continue")) {
					Intent intent = new Intent();
					intent.putExtra("merchantName", merchantName);
					intent.putExtra("meid", mid);
					intent.putExtra("dinnerTime", dinnerTime);
					intent.putExtra("tableName", tableName);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(BookDishListActivity.this,
							OnlineBookSeatActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent();
					intent.putExtra("merchantName", merchantName);
					intent.putExtra("meid", mid);
					intent.putExtra("dinnerTime", dinnerTime);
					intent.putExtra("tableName", tableName);
					intent.setClass(BookDishListActivity.this,
							OnlineBookSeatActivity.class);
					startActivity(intent);
					finish();
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void addFoodListener(RecommendDish dish, int operation) {
		// TODO Auto-generated method stub
		dishList = BlueTownApp.getDishList();
		for (RecommendDish item : dishList) {
			if (item.getDishesId().equals(dish.getDishesId())) {
				int index = dishList.indexOf(item);
				dishList.remove(item);
				dish.setDishesCount(dish.getDishesCount() + 1);
				dishList.add(index, dish);
				BlueTownApp.setDishList(dishList);
				break;
			}
			//BlueTownApp.setDishList(dishList);
		}

		adapter.notifyDataSetChanged();
		double dishOriPrice = Double.parseDouble(dish.getPrice());
		double dishPrice = Double.parseDouble(dish.getFavorablePrice());
		totalCount = totalCount + 1;
		double currentPrice = Double.parseDouble(currentprice) + dishPrice;
		double originalPrice = Double.parseDouble(originalprice) + dishOriPrice;
		formatPriceCount(currentPrice, originalPrice);
	}

	@Override
	public void minusFoodListener(RecommendDish dish, int operation) {
		// TODO Auto-generated method stub
		dishList = BlueTownApp.getDishList();
		for (RecommendDish item : dishList) {
			if (item.getDishesId().equals(dish.getDishesId())) {
				int index = dishList.indexOf(item);
				dishList.remove(item);
				dish.setDishesCount(dish.getDishesCount() - 1);
				dishList.add(index, dish);
				//BlueTownApp.setDishList(dishList);
				break;
			}
			BlueTownApp.setDishList(dishList);
		}
		adapter.notifyDataSetChanged();
		double dishOriPrice = Double.parseDouble(dish.getPrice());
		double dishPrice = Double.parseDouble(dish.getFavorablePrice());
		
		totalCount = totalCount - 1;
		double currentPrice = Double.parseDouble(currentprice) - dishPrice;
		double originalPrice = Double.parseDouble(originalprice) - dishOriPrice;
		formatPriceCount(currentPrice, originalPrice);
	}

	/**
	 * 格式化价格和菜的数量
	 * 
	 * @param currentPrice
	 * @param originalPrice
	 */
	private void formatPriceCount(double currentPrice, double originalPrice) {
		currentprice = getString(R.string.fee, currentPrice);
		originalprice = getString(R.string.fee, originalPrice);
		BlueTownApp.setDishesCount(totalCount);
		BlueTownApp.setDishesPrice(currentprice);
		BlueTownApp.setOriginalPrice(originalprice);
		dishCount.setText("共计：" + totalCount + "个菜");
		totalPriceCount.setText("￥" + currentprice + "元");
		totalOrigalCount.setText("￥" + originalprice + "元");
	}

	@Override
	public void dleFoodListener(RecommendDish dish) {
		// TODO Auto-generated method stub
		dishList = BlueTownApp.getDishList();
		int dishesCount = dish.getDishesCount();
		double dishOriPrice = Double.parseDouble(dish.getPrice());
		double dishPrice = Double.parseDouble(dish.getFavorablePrice());
		totalCount = totalCount - dishesCount;
		double currentPrice = Double.parseDouble(currentprice)
				- (dishPrice * dishesCount);
		double originalPrice = Double.parseDouble(originalprice)
				- (dishOriPrice * dishesCount);
		currentprice = getString(R.string.fee, currentPrice);
		originalprice = getString(R.string.fee, originalPrice);
		BlueTownApp.setDishesCount(totalCount);
		BlueTownApp.setDishesPrice(currentprice);
		BlueTownApp.setOriginalPrice(originalprice);
		originalprice = BlueTownApp.getOriginalPrice();
		currentprice = BlueTownApp.getDishesPrice();
		totalCount = BlueTownApp.getDishesCount();
		dishCount.setText("共计：" + totalCount + "个菜");
		totalPriceCount.setText("￥" + currentprice + "元");
		totalOrigalCount.setText("￥" + originalprice + "元");
		dish.setDishesCount(0);
		dishList.remove(dish);
		BlueTownApp.setDishList(dishList);
		adapter.notifyDataSetChanged();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

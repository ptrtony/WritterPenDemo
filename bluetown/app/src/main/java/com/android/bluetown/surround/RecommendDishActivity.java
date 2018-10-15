package com.android.bluetown.surround;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.CartAdapter;
import com.android.bluetown.adapter.RecommedDishTypeAdapter;
import com.android.bluetown.adapter.RecommendDishListAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.CartItem;
import com.android.bluetown.bean.MerchantDiscountBean;
import com.android.bluetown.bean.RecommedType;
import com.android.bluetown.bean.RecommendDish;
import com.android.bluetown.bean.SeleteDish;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.OnFoodCallBackListener;
import com.android.bluetown.popup.ShowShopCarPopup;
import com.android.bluetown.result.RecommedDishListResult;
import com.android.bluetown.result.RecommedTypeResult;
import com.android.bluetown.utils.Constant;

/**
 * 餐厅推荐菜
 *
 * @author shenyz
 */
@SuppressLint("InflateParams")
public class RecommendDishActivity extends TitleBarActivity implements
        OnClickListener, OnItemClickListener, OnFoodCallBackListener {
    private RelativeLayout cartLayout;
    private ListView dishClassifyname, selectDishContentList;
    // 推荐菜页面当前购物车的菜數量、选好了、当前选择类总共的菜数量的Button
    private Button seletedOk, dishTotalCount;
    // 推荐菜页面当前选择菜的总共的原价和现价，选菜清单总共的原价和现价
    private TextView originalprice, currentprice, dishCount;
    /**************
     * 展示购物车的相关组件
     *****************/
    private ListView cartListView;
    private TextView originalTotalprice, currentTotalprice, mDiscountRemind;
    private TextView shopCardDishCount;
    private Button seletedYes, sleep;
    private View contentView;
    // 显示订桌的信息
    private LinearLayout toastTitle;
    private TextView tableView;
    private TextView timeView;
    // flag食堂订单还是美食订单
    private String mid, merchantName, flag;
    // OnlineBookSeatActivity跳转携带的参数（orderType：0订座，1订座订菜(点击预订--然后点击先点菜，到了等吃)）
    private String orderType, dinnerTime, tableName;
    private List<RecommendDish> dishs;
    private RecommendDishListAdapter adapter;
    private CartAdapter cartAdapter;
    private int lastSelectTypeIndex;
    private Dialog dialog;
    private List<RecommendDish> cartDishList;
    // 我的订单-再订
    private List<SeleteDish> orderSelectList;
    private List<MerchantDiscountBean> beanList;
    private boolean isAdd = true;//添加菜
    Handler hander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case CartItem.ADD_OPERARION:
                    addFoodListener(dishs.get(msg.arg2), CartItem.ADD_OPERARION);
                    break;
                case CartItem.MINUS_OPERARION:
                    minusFoodListener(dishs.get(msg.arg2), CartItem.MINUS_OPERARION);
                    break;

                default:
                    break;
            }

        }

    };
    private ShowShopCarPopup showShopCarPopup;
    private double balance = 0;

    @Override
    public void initTitle() {
        // TODO Auto-generated method stub
        setBackImageView();
        setTitleView(R.string.recommend_dish);
        setTitleLayoutBg(R.color.title_bg_blue);
        righTextLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.ac_recommend_dish);
        initUIView();
        getRecommedDishType();
        if (getIntent().getStringExtra("isClosed")!=null){
            showShopCarPopup = new ShowShopCarPopup(this, getIntent().getStringExtra("isClosed"), this);
        }

    }

    /**
     * 初始化界面组件
     */
    private void initUIView() {
        BlueTownApp.setHanler(hander);
        Bundle bundle = getIntent().getExtras();
        mid = bundle.getString("meid");
        flag = BlueTownApp.DISH_TYPE;
        // 订菜类型（orderType订单类型（0只订座，1订座订菜）默认订座订菜）
        orderType = BlueTownApp.orderType;
        orderSelectList = BlueTownApp.getOrderDishList();
        merchantName = getIntent().getStringExtra("merchantName");

        try {

            // 继续点菜和先订座后点菜
            dinnerTime = getIntent().getStringExtra("dinnerTime");
            tableName = getIntent().getStringExtra("tableName");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        toastTitle = (LinearLayout) findViewById(R.id.layout_seat_title);
        tableView = (TextView) this.findViewById(R.id.toast_table_name);
        timeView = (TextView) this.findViewById(R.id.toast_table_time);
        cartLayout = (RelativeLayout) findViewById(R.id.cart_layout);
        mDiscountRemind = findViewById(R.id.tv_discount_remind);
        dishClassifyname = (ListView) findViewById(R.id.dishClassifyname);
        selectDishContentList = (ListView) findViewById(R.id.selectDishContentList);
        dishTotalCount = (Button) findViewById(R.id.dishTotalCount);
        dishCount = findViewById(R.id.dishCount);
        seletedOk = (Button) findViewById(R.id.seletedOk);
        sleep = (Button) findViewById(R.id.sleep);
        originalprice = (TextView) findViewById(R.id.originalprice);
        currentprice = (TextView) findViewById(R.id.currentprice);
        seletedOk.setOnClickListener(this);
        cartLayout.setOnClickListener(this);
        findViewById(R.id.ac_recommend_dish).setOnClickListener(this);
        dishClassifyname.setOnItemClickListener(this);
        selectDishContentList.setOnItemClickListener(this);
        if (getIntent().getStringExtra("isClosed")!=null&&getIntent().getStringExtra("isClosed").equals("1")) {
            seletedOk.setVisibility(View.GONE);
            sleep.setVisibility(View.VISIBLE);
        } else {
            seletedOk.setVisibility(View.VISIBLE);
            sleep.setVisibility(View.GONE);
        }
        // 预订-先订座-选择先点菜
        if (!TextUtils.isEmpty(orderType)) {
            if (orderType.equals("1") && dinnerTime != null
                    && !dinnerTime.isEmpty()) {
                toastTitle.setVisibility(View.VISIBLE);
                tableView.setText(tableName);
                timeView.setText(dinnerTime);
            } else {
                toastTitle.setVisibility(View.GONE);
            }

        } else {
            // 从食堂或美食直接点菜
            toastTitle.setVisibility(View.GONE);
        }
        originalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        initCartStatusData();

    }

    /**
     * 初始化购物车状态栏的数据（继续点菜、追加时）
     */
    private void initCartStatusData() {
        // 初始化购物车栏的数量
        cartDishList = BlueTownApp.getDishList();
        if (cartDishList != null && cartDishList.size() > 0) {
            String original = BlueTownApp.getOriginalPrice();
            String current = BlueTownApp.getDishesPrice();
            int dishNum = BlueTownApp.getDishesCount();
            originalprice.setText("￥" + original);
            currentprice.setText("￥" +current);
            dishCount.setVisibility(View.VISIBLE);
            dishCount.setText(dishNum + "");
        } else if (orderSelectList != null && orderSelectList.size() > 0) {
            String original = BlueTownApp.getOriginalPrice();
            String current = BlueTownApp.getDishesPrice();
            int dishNum = BlueTownApp.getDishesCount();
            originalprice.setText("￥" + original);
            currentprice.setText("￥" +current);
            dishCount.setVisibility(View.VISIBLE);
            dishCount.setText(dishNum + "");
        } else {
            originalprice.setText("￥0.00");
            currentprice.setText("￥0.00");
            dishCount.setVisibility(View.GONE);
        }
    }

    /**
     * 获取推荐菜类型
     *
     * @param userId
     */
    private void getRecommedDishType() {
        String url = "";
        params.put("meid", mid);
        // 食堂推荐菜类型
        if (flag.equals("canteen")) {
            url = Constant.HOST_URL
                    + Constant.Interface.CANTEEN_RECOMMEMD_DISH_TYPE;
        } else {
            // 美食推荐菜类型
            // flag="food"
            url = Constant.HOST_URL
                    + Constant.Interface.FOOD_RECOMMEMD_DISH_TYPE;
        }
        httpInstance.post(url, params, new AbsHttpResponseListener() {
            @Override
            public void onSuccess(int i, String s) {
                RecommedTypeResult result = (RecommedTypeResult) AbJsonUtil
                        .fromJson(s, RecommedTypeResult.class);
                if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                    ArrayList<RecommedType> dishTypes = new ArrayList<RecommedType>();
                    dishTypes.add(new RecommedType("", "推荐菜", "tuijian", ""));
                    dishTypes.addAll(result.getData());
                    RecommedDishTypeAdapter adapter = new RecommedDishTypeAdapter(
                            RecommendDishActivity.this, dishTypes);
                    dishClassifyname.setAdapter(adapter);
                    adapter.setDefSelec(lastSelectTypeIndex);
                    // 默认加载全部
                    getRecommedDishList(dishTypes.get(lastSelectTypeIndex)
                            .getTid());
                } else {
                    Toast.makeText(RecommendDishActivity.this,
                            result.getRepMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * 获取推荐菜列表
     * <p>
     * meid:商家id（必填） dishesId：菜单id（必填）
     */
    private void getRecommedDishList(String type) {
        String recommend = "";
        String url = "";
        params.put("meid", mid);
        if (!type.isEmpty() && type.equals("tuijian")) {
            recommend = "1";
            type = "";
        }
        params.put("typeId", type);
        params.put("recommend", recommend);
        // 食堂推荐菜列表
        if (flag.equals("canteen")) {
            url = Constant.HOST_URL + Constant.Interface.CANTEEN_RECOMMEMD_DISH;
        } else {
            // 没事推荐菜列表
            // flag="food"
            url = Constant.HOST_URL + Constant.Interface.FOOD_RECOMMEMD_DISH;
        }
        httpInstance.post(url, params, new AbsHttpResponseListener() {
            @Override
            public void onSuccess(int i, String s) {
                RecommedDishListResult result = (RecommedDishListResult) AbJsonUtil
                        .fromJson(s, RecommedDishListResult.class);
                if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
                    dishs = result.getData().getRows();
                    if (dishs != null && dishs.size() > 0) {
                        dishTotalCount.setText("全部(" + dishs.size() + ")");
                    } else {
                        dishTotalCount.setText("全部(0)");
                    }

                    adapter = new RecommendDishListAdapter(
                            RecommendDishActivity.this, dishs,
                            RecommendDishActivity.this);
                    selectDishContentList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (result.getRepCode().contains(Constant.HTTP_NO_DATA)) {
                    if (adapter != null) {
                        if (dishs == null) {
                            dishTotalCount.setText("全部(0)");
                        } else {
                            dishTotalCount.setText("全部(" + dishs.size() + ")");
                        }
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(RecommendDishActivity.this,
                            result.getRepMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecommendDishActivity.this,
                            result.getRepMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        BlueTownApp.setOrderDishList(null);
        cartDishList = BlueTownApp.getDishList();
        if (cartDishList == null) {
            cartDishList = new ArrayList<RecommendDish>();
        }
        switch (v.getId()) {
            case R.id.cart_layout:

                if (cartDishList != null && cartDishList.size() == 0) {
                    Toast.makeText(RecommendDishActivity.this, "您还未点餐哦。",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 填充数据
                    initCartViewData(cartDishList);
                }
                break;
            case R.id.seletedOk:
                if (cartDishList != null && cartDishList.size() == 0) {
                    Toast.makeText(RecommendDishActivity.this, "您还未点餐哦。",
                            Toast.LENGTH_SHORT).show();
                } else {
                    startBookDishListActivity();
                }
                break;
            case R.id.seletedYes:
                if (cartDishList != null && cartDishList.size() == 0) {
                    Toast.makeText(RecommendDishActivity.this, "您还未点餐哦。",
                            Toast.LENGTH_SHORT).show();
                } else {
                    startBookDishListActivity();
                }
                if (showShopCarPopup != null) {
                    showShopCarPopup.dismiss();
                }
                break;
            case R.id.ac_recommend_dish:
                if (showShopCarPopup != null && showShopCarPopup.isShowing()) {
                    showShopCarPopup.dismiss();
                }
                break;
            default:
                break;

        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (adapter != null) {
            if (BlueTownApp.getDishList() != null) {
                cartDishList = BlueTownApp.getDishList();
                adapter.notifyDataSetChanged();
                double price = Double.parseDouble(BlueTownApp
                        .getOriginalPrice());
                double currentPrice = Double.parseDouble(BlueTownApp
                        .getDishesPrice());
                setCartTotalCount(currentPrice, price,
                        BlueTownApp.getDishesCount());
            }

        }
    }

    /**
     * 跳转到选好菜单列表页面
     *
     * @param shopDishes
     */
    private void startBookDishListActivity() {
        String oTotalPrice = originalprice.getText().toString();
        String cTotalPrice = currentprice.getText().toString();
        String totalDishCount = dishCount.getText().toString();
        int totalCount = 0;
        if (!TextUtils.isEmpty(totalDishCount)) {
            totalCount = Integer.parseInt(totalDishCount);
        }
        oTotalPrice = oTotalPrice.substring(oTotalPrice.indexOf("￥") + 1);
        cTotalPrice = cTotalPrice.substring(cTotalPrice.indexOf("￥") + 1);
        BlueTownApp.setDishesCount(totalCount);
        BlueTownApp.setDishesPrice(cTotalPrice);
        BlueTownApp.setOriginalPrice(oTotalPrice);
        double price = Double.parseDouble(BlueTownApp.getOriginalPrice());
        double currentPrice = Double.parseDouble(BlueTownApp.getDishesPrice());
        setCartTotalCount(currentPrice, price, BlueTownApp.getDishesCount());
        Intent intent = new Intent();
        intent.setClass(this, BookDishListActivity.class);
        intent.putExtra("merchantName", merchantName);
        intent.putExtra("meid", mid);
        intent.putExtra("dinnerTime", dinnerTime);
        intent.putExtra("tableName", tableName);
        startActivity(intent);
    }

    /**
     * 展示购物车的数据
     */
    private void initCartViewData(List<RecommendDish> dishes) {
        cartAdapter = new CartAdapter(this, dishes, this);
        if (showShopCarPopup!=null){
            showShopCarPopup.show(findViewById(R.id.ac_recommend_dish), dishCount.getText().toString(), originalprice.getText().toString(),BlueTownApp.getDishesPrice(), cartAdapter);
        }

    }

//	private Dialog showDialog(Context context, final View contentView) {
//		final Dialog dialog = new Dialog(context, R.style.alert_dialog);
//		dialog.show();
//		mDiscountRemind.setVisibility(View.GONE);
//		Window window = dialog.getWindow();
//		WindowManager m = getWindowManager();
//		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
//		WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//		p.width = (int) (d.getWidth()); // 宽度设置
//		window.setAttributes(p); // 设置生效
//		window.setGravity(Gravity.BOTTOM);
//		dialog.setContentView(contentView);
//		return dialog;
//	}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        // 分类
        if (arg0.getId() == R.id.dishClassifyname) {
            if (lastSelectTypeIndex != arg2) {
                // 上次点击的分类
                arg1.setBackgroundColor(getResources().getColor(R.color.white));
                View tempObject = arg0.getChildAt(lastSelectTypeIndex);
                tempObject.setBackgroundColor(getResources().getColor(
                        R.color.invite_friend_title_color));
                // 当前选择的分类
                if (dishs != null) {
                    dishs.clear();
                }
                RecommedType recommedType = (RecommedType) arg0
                        .getItemAtPosition(arg2);
                getRecommedDishList(recommedType.getTid());
                lastSelectTypeIndex = arg2;
            } else {
                // 上次点击的分类
                arg1.setBackgroundColor(getResources().getColor(R.color.white));
                if (dishs != null) {
                    dishs.clear();
                }
                // 当前选择的分类
                RecommedType recommedType = (RecommedType) arg0
                        .getItemAtPosition(arg2);
                getRecommedDishList(recommedType.getTid());
                lastSelectTypeIndex = arg2;
            }

        } else {
            if (arg1.getId() == R.id.recommendDishImg) {
                Intent intent = new Intent();
                intent.putExtra("dishIndex", arg2);
                intent.putExtra("dishList", (Serializable) dishs);
                intent.setClass(RecommendDishActivity.this,
                        RecommendDishDetailsActivity.class);
                startActivity(intent);
            }


        }

    }

    @Override
    public void addFoodListener(RecommendDish dish, int operation) {
        // TODO Auto-generated method stub
        BlueTownApp.setOrderDishList(null);
        dishCount.setVisibility(View.VISIBLE);
        cartDishList = BlueTownApp.getDishList();
        if (cartDishList == null) {
            cartDishList = new ArrayList<RecommendDish>();
        }
        if (cartDishList != null) {
            if (cartDishList.contains(dish)) {
                for (RecommendDish item : cartDishList) {
                    if (item.getDishesId().equals(dish.getDishesId())) {
                        if (operation == CartItem.ADD_OPERARION) {
                            int index = cartDishList.indexOf(item);
                            cartDishList.remove(item);
                            dish.setDishesCount(dish.getDishesCount() + 1);
                            cartDishList.add(index, dish);

                        } else {
                            int index = cartDishList.indexOf(item);
                            cartDishList.remove(item);
                            cartDishList.add(index, dish);

                        }
                        break;
                    }
                }
                BlueTownApp.setDishList(cartDishList);
                if (operation == CartItem.ADD_OPERARION) {
                    adapter.notifyDataSetChanged();
                }
            } else {
                boolean isExsit = false;
                // 继续点菜
                if (BlueTownApp.ORDER_TYPE.equals("continue")) {
                    if (cartDishList != null && cartDishList.size() > 0) {
                        for (RecommendDish item : cartDishList) {
                            // 已有的菜，修改其个数
                            if (item.getDishesId().equals(dish.getDishesId())) {
                                if (operation == CartItem.ADD_OPERARION) {
                                    int index = cartDishList.indexOf(item);
                                    dish.setDishesCount(item.getDishesCount() + 1);
                                    cartDishList.remove(item);
                                    cartDishList.add(index, dish);
                                    BlueTownApp.setDishList(cartDishList);
                                } else {
                                    cartDishList.remove(item);
                                    cartDishList.add(dish);
                                    BlueTownApp.setDishList(cartDishList);
                                }
                                isExsit = true;
                                break;
                            }
                        }

                        if (!isExsit) {
                            // 点的新菜
                            if (operation == CartItem.ADD_OPERARION) {
                                dish.setDishesCount(dish.getDishesCount() + 1);
                                cartDishList.add(dish);
                                BlueTownApp.setDishList(cartDishList);
                            } else {
                                cartDishList.add(dish);
                                BlueTownApp.setDishList(cartDishList);
                            }
                            if (operation == CartItem.ADD_OPERARION) {
                                adapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                        // 正常点菜
                        if (operation == CartItem.ADD_OPERARION) {
                            dish.setDishesCount(dish.getDishesCount() + 1);
                            cartDishList.add(dish);
                            BlueTownApp.setDishList(cartDishList);
                        } else {
                            cartDishList.add(dish);
                            BlueTownApp.setDishList(cartDishList);
                        }
                        if (operation == CartItem.ADD_OPERARION) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (cartDishList != null && cartDishList.size() > 0) {
                        for (RecommendDish item : cartDishList) {
                            // 已有的菜，修改其个数
                            if (item.getDishesId().equals(dish.getDishesId())) {
                                if (operation == CartItem.ADD_OPERARION) {
                                    int index = cartDishList.indexOf(item);
                                    dish.setDishesCount(item.getDishesCount() + 1);
                                    cartDishList.remove(item);
                                    cartDishList.add(index, dish);
                                    //	BlueTownApp.setDishList(cartDishList);
                                } else {
                                    cartDishList.remove(item);
                                    cartDishList.add(dish);
                                    //	BlueTownApp.setDishList(cartDishList);
                                }
                                isExsit = true;
                                break;
                            }
                        }
                        if (!isExsit) {
                            // 点的新菜
                            if (operation == CartItem.ADD_OPERARION) {
                                dish.setDishesCount(dish.getDishesCount() + 1);
                                cartDishList.add(dish);
                                //	BlueTownApp.setDishList(cartDishList);
                            } else {
                                cartDishList.add(dish);
                                //	BlueTownApp.setDishList(cartDishList);
                            }
                            if (operation == CartItem.ADD_OPERARION) {
                                adapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                        // 正常点菜
                        if (operation == CartItem.ADD_OPERARION) {
                            dish.setDishesCount(dish.getDishesCount() + 1);
                            cartDishList.add(dish);
                            //BlueTownApp.setDishList(cartDishList);
                        } else {
                            cartDishList.add(dish);
                            //BlueTownApp.setDishList(cartDishList);
                        }
                        if (operation == CartItem.ADD_OPERARION) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    BlueTownApp.setDishList(cartDishList);
                }

            }
        }

        setCartData(dish, "add");
    }

    @Override
    public void minusFoodListener(RecommendDish dish, int operation) {
        // TODO Auto-generated method stub
        BlueTownApp.setOrderDishList(null);
        cartDishList = BlueTownApp.getDishList();
        //	if (cartDishList.contains(dish)) {
        for (RecommendDish item : cartDishList) {
            if (item.getDishesId().equals(dish.getDishesId())) {
                if (dish.getDishesCount() == 0) {
                    item.setDishesCount(0);
                    cartDishList.remove(item);
                    BlueTownApp.setDishList(cartDishList);
                } else {
                    if (operation == CartItem.MINUS_OPERARION) {
                        miniDish(dish, item);
                        BlueTownApp.setDishList(cartDishList);
                    } else if (operation == CartItem.CART_MINUS) {
                        miniDish(dish, item);
                        cartAdapter = new CartAdapter(this, cartDishList, this);
                        if (showShopCarPopup!=null)
                        showShopCarPopup.getCartListView().setAdapter(cartAdapter);
                        BlueTownApp.setDishList(cartDishList);
                    } else {
                        cartDishList.remove(item);
                        cartDishList.add(dish);
                        BlueTownApp.setDishList(cartDishList);
                    }

                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            BlueTownApp.setDishList(cartDishList);
        }

//		}

        setCartData(dish, "mini");
        if (dialog != null) {
            refreshCart();
        }
        if (operation == CartItem.MINUS_OPERARION
                || operation == CartItem.CART_MINUS) {
            adapter.notifyDataSetChanged();
        }

        if (operation == CartItem.CART_MINUS) {
            if (cartDishList != null && cartDishList.isEmpty()) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        }
    }

    /**
     * 减少菜的数量
     *
     * @param dish
     * @param item
     */
    private void miniDish(RecommendDish dish, RecommendDish item) {
        if (dish.getDishesCount() - 1 == 0) {
            item.setDishesCount(0);
            cartDishList.remove(item);
            BlueTownApp.setDishList(cartDishList);
        } else {
            int index = cartDishList.indexOf(item);
            cartDishList.remove(item);
            dish.setDishesCount(dish.getDishesCount() - 1);
            cartDishList.add(index, dish);
            BlueTownApp.setDishList(cartDishList);
        }
    }

    /**
     * 设置原价总和和优惠价总和，菜的总数量
     *
     * @param currentPrice
     * @param currentOriginalPrice
     * @param dishNumber
     */
    private void setCartTotalCount(double currentPrice,
                                   double currentOriginalPrice, int dishNumber) {
        originalprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String currentOriginalTotalPrice = "";
        String currentTotalPrice = "";
        if (dishNumber != 0) {
            // 保留两位小数
            currentOriginalTotalPrice = getString(R.string.fee,
                    currentOriginalPrice);
            currentTotalPrice = getString(R.string.fee, currentPrice);
            originalprice.setText("￥" + currentOriginalTotalPrice);
            currentprice.setText("￥" +BlueTownApp.getDishesPrice());
            if (showShopCarPopup != null) {
                showShopCarPopup.setCarCountAndAmount(dishNumber + "", "￥" + currentOriginalTotalPrice, "￥" + BlueTownApp.getDishesPrice(), BlueTownApp.getDiscountRemind());
            }

        } else {
            originalprice.setText("￥" + currentOriginalPrice + "0");
            currentprice.setText("￥" + currentPrice + "0");
            dishCount.setVisibility(View.GONE);
            if (showShopCarPopup != null) {
                showShopCarPopup.setCarCountAndAmount(dishNumber + "", "￥" + currentOriginalPrice + "0", "￥" + currentPrice + "0", BlueTownApp.getDiscountRemind());
                showShopCarPopup.dismiss();
            }
        }
        dishCount.setText(dishNumber + "");
    }

    @Override
    public void dleFoodListener(RecommendDish dish) {
        // TODO Auto-generated method stub
        cartDishList = BlueTownApp.getDishList();
        setCartData(dish, "delete");
        dish.setDishesCount(0);
        cartDishList.remove(dish);
        cartAdapter = new CartAdapter(this, cartDishList, this);
        if (showShopCarPopup!=null)
        showShopCarPopup.getCartListView().setAdapter(cartAdapter);
        refreshCart();
        adapter.notifyDataSetChanged();
        BlueTownApp.setDishList(cartDishList);
    }

    /**
     * 设置购物车栏数据的
     *
     * @param dish
     * @param operation
     */
    private void setCartData(RecommendDish dish, String operation) {
        // 菜现价即当前你的价格
        double price = Double.parseDouble(dish.getFavorablePrice());
        // 菜的原价
        double orginalPrice = Double.parseDouble(dish.getPrice());
        // 当前显示的原价
        String oriPriceStr = originalprice.getText().toString();
        // 当前显示的现价
        String cPriceStr = currentprice.getText().toString();
        // 购物车中的菜
        String dishNumberStr = dishCount.getText().toString();
        oriPriceStr = oriPriceStr.substring(oriPriceStr.indexOf("￥") + 1);
//        cPriceStr = cPriceStr.substring(cPriceStr.indexOf("￥") + 1);
        double currentPrice = 0;
        double currentOriginalPrice = Double.parseDouble(oriPriceStr);
        int dishNumber = 0;
        if (!TextUtils.isEmpty(dishNumberStr)) {
            dishNumber = Integer.parseInt(dishNumberStr);
        }
        if (operation.equals("add")) {
            currentOriginalPrice = currentOriginalPrice + price;
            dishNumber = dishNumber + 1;
            currentPrice = currentPrice(currentOriginalPrice);
        } else if (operation.equals("mini")) {
            currentOriginalPrice = currentOriginalPrice - price;
            currentPrice = currentPrice(currentOriginalPrice);
            if (dishNumber <= 1) {
                dishNumber = 0;
                dishCount.setVisibility(View.GONE);
            } else {
                dishCount.setVisibility(View.VISIBLE);
                dishNumber = dishNumber - 1;
            }
        } else {
            orginalPrice = orginalPrice * dish.getDishesCount();
//            price = price * dish.getDishesCount();
            currentOriginalPrice = currentOriginalPrice - orginalPrice;
            currentPrice = currentPrice(currentOriginalPrice);
            if (dishNumber <= 1) {
                dishNumber = 0;
                dishCount.setVisibility(View.GONE);
            } else {
                dishCount.setVisibility(View.VISIBLE);
                dishNumber = dishNumber - dish.getDishesCount();
            }
            isAdd = false;
        }
        BlueTownApp.setDishesCount(dishNumber);
        BlueTownApp.setDishesPrice(String.format("%.2f",currentPrice));
        BlueTownApp.setOriginalPrice(currentOriginalPrice + "");
        setCartTotalCount(currentPrice, currentOriginalPrice, dishNumber);
    }

    /**
     * 刷新购物车中原价总和和优惠价总和，菜的总数量
     */
    private void refreshCart() {
        if (cartDishList.isEmpty()) {
            showShopCarPopup.setCarCountAndAmount("0", originalprice.getText().toString(), "￥" + "0.00", BlueTownApp.getDiscountRemind());
            if (showShopCarPopup != null) showShopCarPopup.dismiss();
        } else {
            showShopCarPopup.setCarCountAndAmount(dishCount.getText().toString(), originalprice.getText().toString(), "￥" +BlueTownApp.getDishesPrice(), BlueTownApp.getDiscountRemind());
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    /**
     * 折现金额
     *
     * @return
     */

    public double currentPrice(double currentOriginalPrice){
        double currentPrice = 0;
        Spanned remind = null;
        beanList = BlueTownApp.getOrderDiscountList();
        if (beanList!=null&&beanList.size()>0){
            for (int i=0;i<beanList.size();i++) {
                if (currentOriginalPrice<Double.parseDouble(beanList.get(0).BaseLine)){
                    currentPrice = currentOriginalPrice;
                    mDiscountRemind.setVisibility(View.GONE);
                }else if (beanList.get(i).HasNext.equals("1")&&currentOriginalPrice>=Double.parseDouble(beanList.get(i).BaseLine)&&currentOriginalPrice<Double.parseDouble(beanList.get(i+1).BaseLine)){
                    currentPrice = currentOriginalPrice*Double.parseDouble(beanList.get(i).Discount)*0.1;
                    double nextDiscount = Double.parseDouble(beanList.get(i + 1).BaseLine) * (1 - Double.parseDouble(beanList.get(i + 1).Discount) * 0.1);
                    remind = Html.fromHtml("下单减" + String.format("%.2f", currentOriginalPrice * (1 - Double.parseDouble(beanList.get(i).Discount) * 0.1)) + "元,再买<font color='#EE6723'>" + String.format("%.2f", Double.parseDouble(beanList.get(i + 1).BaseLine) - currentOriginalPrice) + "元" + "</font>" + "可减" + "<font color='#EE6723'>" + String.format("%.2f", nextDiscount) + "元</font>");
                    mDiscountRemind.setVisibility(View.VISIBLE);
                    mDiscountRemind.setText(remind);

                }else if (beanList.get(i).HasNext.equals("0")&&currentOriginalPrice>=Double.parseDouble(beanList.get(i).BaseLine)){
                    currentPrice = currentOriginalPrice*Double.parseDouble(beanList.get(i).Discount)*0.1;
                    remind = Html.fromHtml("已满<font color='#EE6723'>" + beanList.get(i).BaseLine + "元</font>" + "可减<font color='#EE6723'>" + String.format("%.2f", currentOriginalPrice * (1 - Double.parseDouble(beanList.get(i).Discount) * 0.1)) + "元</font>");
                    mDiscountRemind.setVisibility(View.VISIBLE);
                    mDiscountRemind.setText(remind);
                }
            }
        }else{
            currentPrice = currentOriginalPrice;
        }
        BlueTownApp.setDiscountRemind(remind);
        return currentPrice;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (showShopCarPopup != null && showShopCarPopup.isShowing()) {
            showShopCarPopup.dismiss();
            showShopCarPopup = null;
        }
        return super.onTouchEvent(event);
    }
}

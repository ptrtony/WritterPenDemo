package com.android.bluetown.surround;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.bean.OrderParams;
import com.android.bluetown.fragment.OrderFragment;
import com.android.bluetown.utils.StatusBarUtils;

public class OrderListActivity extends FragmentActivity implements
		OnClickListener {
	// 标题
	private TextView orderTitle;
	// 返回键
	private ImageView backImg;
	/**
	 * 红色的选择线
	 */
	private View line_1;
	private View line_2;
	private View line_3;
	private View line_4;

	private Fragment messageFragment;// 全部
	private Fragment contactsFragment;// 交易中
	private Fragment newsFragment;// 已关闭
	private Fragment settingFragment;// 已完成

	/**
	 * 显示的view
	 */
	private View messageLayout;
	private View contactsLayout;
	private View newsLayout;
	private View settingLayout;

	/**
	 * 顶部的文字
	 */
	private TextView messageText;
	private TextView contactsText;
	private TextView newsText;
	private TextView settingText;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	/**
	 * 用于对判断是预订订单还是预订点菜
	 */
	private String flag, orderType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_order_list);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			StatusBarUtils.getInstance().initStatusBar(true,this);
		}
		// 初始化布局元素
		initViews();
		fragmentManager = getFragmentManager();
		// 第一次启动时选中第0个tab
		setTabSelection(0);
	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */
	private void initViews() {
		flag = getIntent().getStringExtra("flag");
		orderTitle = (TextView) findViewById(R.id.orderTitle);
		backImg = (ImageView) findViewById(R.id.backImg);
		line_1 = (View) findViewById(R.id.line_1);
		line_2 = (View) findViewById(R.id.line_2);
		line_3 = (View) findViewById(R.id.line_3);
		line_4 = (View) findViewById(R.id.line_4);

		line_1.setVisibility(View.GONE);
		line_2.setVisibility(View.GONE);
		line_3.setVisibility(View.GONE);
		line_4.setVisibility(View.GONE);

		messageLayout = findViewById(R.id.message_layout);
		contactsLayout = findViewById(R.id.contacts_layout);
		newsLayout = findViewById(R.id.news_layout);
		settingLayout = findViewById(R.id.setting_layout);

		messageText = (TextView) findViewById(R.id.message_text);
		contactsText = (TextView) findViewById(R.id.contacts_text);
		newsText = (TextView) findViewById(R.id.news_text);
		settingText = (TextView) findViewById(R.id.setting_text);
		backImg.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
		contactsLayout.setOnClickListener(this);
		newsLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		if (flag.equals("0")) {
			orderTitle.setText(R.string.my_order);
			orderType = flag;
		} else {
			orderTitle.setText(R.string.dish_order);
			orderType = flag;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_layout:
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(0);
			break;
		case R.id.contacts_layout:
			// 当点击了联系人tab时，选中第2个tab
			setTabSelection(1);
			break;
		case R.id.news_layout:
			// 当点击了动态tab时，选中第3个tab
			setTabSelection(2);
			break;
		case R.id.setting_layout:
			// 当点击了设置tab时，选中第4个tab
			setTabSelection(3);
			break;
		case R.id.backImg:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 */
	private void setTabSelection(int index) {
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			line_1.setVisibility(View.VISIBLE);
			line_2.setVisibility(View.GONE);
			line_3.setVisibility(View.GONE);
			line_4.setVisibility(View.GONE);
			// 当点击了消息tab时，改变控件的图片和文字颜色
			messageText.setTextColor(Color.parseColor("#FF7578"));
			if (messageFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				messageFragment = new OrderFragment(orderType,
						OrderParams.ORDER_ALL);
				transaction.add(R.id.content, messageFragment);

			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(messageFragment);
			}
			break;
		case 1:
			line_2.setVisibility(View.VISIBLE);

			line_1.setVisibility(View.GONE);
			line_3.setVisibility(View.GONE);
			line_4.setVisibility(View.GONE);

			// 当点击了联系人tab时，改变控件的图片和文字颜色
			contactsText.setTextColor(Color.parseColor("#FF7578"));
			if (contactsFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				contactsFragment = new OrderFragment(orderType,
						OrderParams.ORDER_TRADE);
				transaction.add(R.id.content, contactsFragment);

			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(contactsFragment);
			}
			break;
		case 2:
			line_3.setVisibility(View.VISIBLE);

			line_1.setVisibility(View.GONE);
			line_2.setVisibility(View.GONE);
			line_4.setVisibility(View.GONE);
			// 当点击了动态tab时，改变控件的图片和文字颜色
			newsText.setTextColor(Color.parseColor("#FF7578"));
			if (newsFragment == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				newsFragment = new OrderFragment(orderType,
						OrderParams.ORDER_CLOSED);
				transaction.add(R.id.content, newsFragment);

			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(newsFragment);
			}
			break;
		case 3:
			// 当点击了设置tab时，改变控件的图片和文字颜色
			line_4.setVisibility(View.VISIBLE);
			line_1.setVisibility(View.GONE);
			line_2.setVisibility(View.GONE);
			line_3.setVisibility(View.GONE);
			settingText.setTextColor(Color.parseColor("#FF7578"));
			if (settingFragment == null) {
				// 如果SettingFragment为空，则创建一个并添加到界面上
				settingFragment = new OrderFragment(orderType,
						OrderParams.ORDER_FINISHED);
				transaction.add(R.id.content, settingFragment);

			} else {
				// 如果SettingFragment不为空，则直接将它显示出来
				transaction.show(settingFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		messageText.setTextColor(Color.parseColor("#666666"));
		contactsText.setTextColor(Color.parseColor("#666666"));
		newsText.setTextColor(Color.parseColor("#666666"));
		settingText.setTextColor(Color.parseColor("#666666"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (messageFragment != null) {
			transaction.hide(messageFragment);
		}
		if (contactsFragment != null) {
			transaction.hide(contactsFragment);
		}
		if (newsFragment != null) {
			transaction.hide(newsFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}

	}
}
package com.android.bluetown.home.makefriends.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.FragmentViewPagerAdapter;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.fragment.FriendsFragment;
import com.android.bluetown.fragment.GroupsFragment;


/**
 * 群、好友
 * 
 * @author shenyz
 * 
 */
public class MakeFriendsActivity1 extends TitleBarActivity implements
		OnClickListener {
	private ViewPager mViewPager;
	private RadioGroup mListGroup;
	private RadioButton friendsRadioButton, groupRadioButton;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private RadioButton[] menus;
	private int currentIndex;
	private ImageViewOnClickListener imageViewOnClickListener;
	private String condition;
	private String userId;
	private FinalDb db;
	
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_makefriends1);
		BlueTownExitHelper.addActivity(this);
		initView();
		initMenu();
		setAdapter();
		setCurView(0);
		setCurDot(0);

	}
	

	/**
	 * 
	 * @Title: initTitle
	 * @Description:初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		righTextLayout.setVisibility(View.VISIBLE);
		setCustomSearchView(R.string.search_friend_hint);
		setRighTextView(R.string.search);
		setTitleLayoutBg(R.color.chat_tab_friend_color);
		mClearEditText.setOnClickListener(this);
		righTextLayout.setOnClickListener(this);
	}

	private void initView() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mListGroup = (RadioGroup) findViewById(R.id.userListRadioGroup);
		friendsRadioButton = (RadioButton) findViewById(R.id.friendsList);
		groupRadioButton = (RadioButton) findViewById(R.id.groupList);
		mViewPager.setOffscreenPageLimit(1);
		imageViewOnClickListener = new ImageViewOnClickListener();
		friendsRadioButton.setOnClickListener(imageViewOnClickListener);
		groupRadioButton.setOnClickListener(imageViewOnClickListener);
	}

	/**
	 * 
	 * @Title: initMenu
	 * @Description: TODO(初始化界面显示的Fragment)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initMenu() {
		// 防止Fragment重叠
		fragments.clear();
	//	fragments.add(new GroupsFragment(userId));
		fragments.add(new FriendsFragment(userId));
		if (menus == null) {
			menus = new RadioButton[fragments.size()];
			for (int i = 0; i < fragments.size(); i++) {
				menus[i] = (RadioButton) mListGroup.getChildAt(i);
				menus[i].setTag(i);
			}
			currentIndex = 0;
			menus[currentIndex].setChecked(true);
		}

	}

	/**
	 * 
	 * @Title: setAdapter
	 * @Description: TODO(设置适配器)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setAdapter() {
		if (fragments.size() > 0) {
			FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(
					getSupportFragmentManager(), mViewPager, fragments);
			adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
				@Override
				public void onExtraPageScrolled(int i, float v, int i2) {
					super.onExtraPageScrolled(i, v, i2); // To change body of
															// overridden
															// methods
															// use File |
															// Settings |
															// File Templates.
				}

				@Override
				public void onExtraPageSelected(int currentIndex) {
					setCurDot(currentIndex);
				}
			});
			adapter.notifyDataSetChanged();
		}

	}

	class ImageViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int position = 1;
			if (position == 0) {
				mClearEditText.setVisibility(View.GONE);
				righTextLayout.setVisibility(View.INVISIBLE);
				setTitleView(R.string.my_group);
				setTitleLayoutBg(R.color.chat_tab_friend_color);
			} else {
				righTextLayout.setVisibility(View.VISIBLE);
				setCustomSearchView(R.string.search_friend_hint);
				setRighTextView(R.string.search);
				setTitleLayoutBg(R.color.chat_tab_friend_color);
			}
			setCurView(position);
			setCurDot(position);

		}
	}

	/**
	 * 
	 * @Title: setCurView
	 * @Description: TODO(设置当前显示页面)
	 * @param @param position 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= fragments.size()) {
			return;
		}
		mViewPager.setCurrentItem(position);
	}

	/**
	 * 
	 * @Title: setCurDot
	 * @Description: TODO(设置当前显示position的状态)
	 * @param @param position 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setCurDot(int position) {
		if (position < 0 || position > fragments.size() - 1
				|| currentIndex == position) {
			return;
		}
		menus[currentIndex].setChecked(false);
		menus[position].setChecked(true);
		currentIndex = position;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.filter_edit:
			mClearEditText.setFocusable(true);
			mClearEditText.setFocusableInTouchMode(true);
			break;
		case R.id.rightTextLayout:
			condition = mClearEditText.getText().toString();
			Intent intent = new Intent(MakeFriendsActivity1.this,
					SearchFriendListActivity.class);
			intent.putExtra("condition", condition);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

package com.android.bluetown.home.makefriends.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.bean.FriendsBean;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.custom.dialog.TipDialog;
import com.android.bluetown.listener.AbsHttpStringResponseListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.FriendResult;
import com.android.bluetown.result.Result;
import com.android.bluetown.sortlistview.CharacterParser;
import com.android.bluetown.sortlistview.PinyinComparator;
import com.android.bluetown.sortlistview.SortAdapter;
import com.android.bluetown.sortlistview.SortModel;
import com.android.bluetown.utils.Constant;

/**
 * 邀请群成员
 * 
 * @author shenyz
 * 
 */
@SuppressLint({ "DefaultLocale", "InflateParams", "UseSparseArrays" })
public class InviteGroupMembersActivity extends TitleBarActivity implements
		OnClickListener, OnItemClickListener {
	private ListView sortListView;
	private LinearLayout selectedFriendLy;
	private Button finishSelected;
	private SortAdapter adapter;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	// 邀请好友的userId
	private StringBuffer midList = null;
	// 群名称
	private String flockName, mid;
	private SharePrefUtils mPrefUtils;
	// 当前点击的Item
	private SortModel currentSelectModel;
	// 当前选择item的CheckBox
	private CheckBox currentSelectBox;
	private FinalDb db;
	private String userId;
	private List<MemberUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_invite_group_members);
		initViews();
		
	}

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.chat_tab_message_color);
		setCustomSearchView(R.string.invite_group_member_search_hint);
		setRighTextView(R.string.search);
		righTextLayout.setOnClickListener(this);
		mClearEditText.setOnClickListener(this);
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	private void initViews() {
		db = FinalDb.create(this);
		users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				userId = user.getMemberId();
			}
		}
		flockName = getIntent().getStringExtra("flockName");
		mid = getIntent().getStringExtra("mid");
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		selectedFriendLy = (LinearLayout) findViewById(R.id.selectedFriendLy);
		finishSelected = (Button) findViewById(R.id.finishSelected);
		sortListView = (ListView) findViewById(R.id.invitFriendList);
		sortListView.setOnItemClickListener(this);
		finishSelected.setOnClickListener(this);
		// 获取所有还有的信息
		getData("");
	}

	/**
	 * 为ListView填充数据 userId：当前用户id（必填） type:必填（0:查看好友；1：查看所有用户） condition:查询条件
	 * genre：必填（0：获取好友列表，1：获取群好友列表） mid：群id（查询群列表必填）
	 * 
	 */
	private void getData(String codition) {
		
		params.put("userId", userId);
		// type:必填（0:查看好友；1：查看所有用户）
		params.put("type", "1");
		params.put("condition", codition);
		params.put("genre", "1");
		params.put("mid", mid);
		httpInstance.post(Constant.HOST_URL + Constant.Interface.FRIEND_LIST,
				params, new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						FriendResult result = (FriendResult) AbJsonUtil
								.fromJson(s, FriendResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							List<FriendsBean> list = result.getData()
									.getFriend().getRows();
							if (list != null && list.size() > 0) {
								SourceDateList = filledData(list);
								// 根据a-z进行排序源数据
								Collections.sort(SourceDateList,
										pinyinComparator);
								adapter = new SortAdapter(
										InviteGroupMembersActivity.this,
										SourceDateList);
								sortListView.setAdapter(adapter);
							}
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(InviteGroupMembersActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();
						}

					}
				});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(List<FriendsBean> friends) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < friends.size(); i++) {
			SortModel sortModel = new SortModel();
			FriendsBean mGroupsBean = friends.get(i);
			sortModel.setName(mGroupsBean.getNickName());
			sortModel.setSignaturele("");
			sortModel.setSelected(false);
			sortModel.setUserId(mGroupsBean.getUserId());
			sortModel.setFriendImg(mGroupsBean.getHeadImg());
			// 汉字转换成拼音
			if (!TextUtils.isEmpty(mGroupsBean.getNickName())) {
				String pinyin = characterParser.getSelling(mGroupsBean
						.getNickName());
				String sortString = pinyin.substring(0, 1).toUpperCase();

				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}

				mSortList.add(sortModel);

			}

		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		if (filterDateList != null && SourceDateList != null) {
			if (TextUtils.isEmpty(filterStr)) {
				filterDateList = SourceDateList;
			} else {
				filterDateList.clear();
				for (SortModel sortModel : SourceDateList) {
					String name = sortModel.getName();
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
									filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}

			}

			// 根据a-z进行排序
			Collections.sort(filterDateList, pinyinComparator);
			adapter.updateListView(filterDateList);
		}

	}

	/**
	 * 邀请好友加入群
	 * 
	 * @param userId
	 *            ：邀请人的id（必填）
	 * @param mid
	 *            ：加入的群id(必填)
	 * @param flockName
	 *            ：群名称（必填）
	 * @param friendId
	 *            ：邀请的用户id（必填 ，加入群的用户Id，多个用户用,隔开）
	 */
	private void inviteInGroup(String userId, String friendId,
			String flockName, String mid) {
		params.put("userId", userId);
		params.put("mid", mid);
		params.put("flockName", flockName);
		params.put("friendId", friendId);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.INVITATION_IN_GROUP, params,
				new AbsHttpStringResponseListener(this, null) {
					@Override
					public void onSuccess(int i, String s) {
						Result result = (Result) AbJsonUtil.fromJson(s,
								Result.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							Toast.makeText(context, result.getRepMsg(),
									Toast.LENGTH_SHORT).show();
							selectedFriendLy.removeAllViews();
							midList = null;
							SourceDateList.clear();
							adapter.notifyDataSetChanged();
							getData("");
						} else {
							Toast.makeText(InviteGroupMembersActivity.this,
									result.getRepMsg(), Toast.LENGTH_LONG)
									.show();
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.filter_edit:
			mClearEditText.setFocusable(true);
			mClearEditText.setFocusableInTouchMode(true);
			break;
		case R.id.finishSelected:
			if (midList != null) {
				String friendId = midList.toString();
				friendId = friendId.substring(0, friendId.lastIndexOf(","));
				
				inviteInGroup(userId, friendId, flockName, mid);
			}
			break;
		case R.id.rightTextLayout:
			String condition = mClearEditText.getText().toString();
			if (!TextUtils.isEmpty(condition)) {
				if (SourceDateList != null) {
					SourceDateList.clear();
				}
				getData(condition);
			} else {
				TipDialog
						.showDialog(InviteGroupMembersActivity.this,
								R.string.tip, R.string.dialog_ok,
								R.string.search_empty);
			}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
		if (midList == null) {
			midList = new StringBuffer();
		}
		resetCheckBox(arg0, arg1, arg2);
	}

	/**
	 * 重置checkbox的状态
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	private void resetCheckBox(AdapterView<?> arg0, View arg1, int arg2) {
		View itemView = adapter.getView(arg2, arg1, arg0);
		currentSelectBox = (CheckBox) itemView.findViewById(R.id.checkbox);
		currentSelectModel = (SortModel) adapter.getItem(arg2);
		if (currentSelectModel.isSelected()) {
			currentSelectModel.setSelected(false);
			currentSelectBox.setChecked(false);
		} else {
			currentSelectModel.setSelected(true);
			currentSelectBox.setChecked(true);
		}
		// 给selectedFriendLy添加选中的好友
		LayoutInflater mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.item_seleted_friend_image, null);
		if (currentSelectModel.isSelected()) {
			ImageView img = (ImageView) view.findViewById(R.id.img);
			if (!TextUtils.isEmpty(currentSelectModel.getFriendImg())) {
				finalBitmap.display(img, currentSelectModel.getFriendImg());
			} else {
				img.setImageResource(R.drawable.ic_msg_empty);
			}
			selectedFriendLy.addView(view);
			midList.append(currentSelectModel.getUserId() + ",");
			// 将该用户保存到数据库
			List<FriendsBean> friendList = db.findAllByWhere(FriendsBean.class,
					" userId=\"" + currentSelectModel.getUserId() + "\"");
			if (friendList.size() == 0) {
				FriendsBean friend = new FriendsBean();
				friend.setUserId(currentSelectModel.getUserId());
				friend.setHeadImg(currentSelectModel.getFriendImg());
				friend.setNickName(currentSelectModel.getName());
				db.save(friend);
			}
		} else {
			String friendIdList = midList.toString();
			String[] ids = friendIdList.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (currentSelectModel.getUserId().trim()
						.equalsIgnoreCase(ids[i])) {
					int start = friendIdList.indexOf(currentSelectModel
							.getUserId() + ",");
					midList = midList
							.delete(start,
									start
											+ ((currentSelectModel.getUserId() + ",")
													.length()));
					// 将该用户保存到数据库
					List<FriendsBean> friendList = db.findAllByWhere(
							FriendsBean.class, " userId=\""
									+ currentSelectModel.getUserId() + "\"");
					if (friendList.size() > 0) {
						db.deleteByWhere(FriendsBean.class, " userId=\""
								+ currentSelectModel.getUserId() + "\"");
					}
					selectedFriendLy.removeViewAt(i);
					selectedFriendLy.invalidate();
					break;
				}

			}

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
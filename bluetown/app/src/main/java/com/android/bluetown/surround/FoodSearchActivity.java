package com.android.bluetown.surround;

import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.adapter.SearchAdapter;
import com.android.bluetown.app.BlueTownApp;
import com.android.bluetown.bean.Merchant;
import com.android.bluetown.custom.dialog.TipDialog;

/**
 * 商家搜索
 * 
 * @author shenyz
 * 
 */
public class FoodSearchActivity extends TitleBarActivity implements
		OnClickListener {
	// 搜索
	private TextView tv_search;
	// 搜索关键字内容
	private EditText et_search_msg;
	// 历史记录、热门餐厅
	private GridView gvHistory;
	private List<Merchant> historyList;
	// 历史记录
	private SearchAdapter historyAdapter;
	private SQLiteDatabase db;
	private String searchValue;

	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleLayoutBg(R.color.title_bg_blue);
		setTitleView(R.string.search);
		righTextLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_search);
		initUIView();
	}

	/**
	 * 初始化界面组件
	 */
	private void initUIView() {
		// 创建数据库表
		db = Connector.getDatabase();

		searchValue=getIntent().getStringExtra("searchValue");
		tv_search = (TextView) findViewById(R.id.tv_search_layout);
		et_search_msg = (EditText) findViewById(R.id.et_search_msg);
		et_search_msg.setText(searchValue);
		gvHistory = (GridView) findViewById(R.id.gv_history);
		tv_search.setOnClickListener(this);
		gvHistory.setOnItemClickListener(onHistoryItemClickListener);
	}

	private void initHistoryData() {
		// TODO Auto-generated method stub
		historyList = DataSupport.findAll(Merchant.class);
		if (historyList != null && historyList.size() > 0) {
			historyAdapter = new SearchAdapter(this, historyList);
			gvHistory.setAdapter(historyAdapter);
			gvHistory.setOnItemClickListener(onHistoryItemClickListener);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initHistoryData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_search_layout:
			String keyword = et_search_msg.getText().toString();
			search(keyword);
			break;
		default:
			break;
		}

	}

	private OnItemClickListener onHistoryItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Merchant keyword = (Merchant) parent.getAdapter().getItem(position);
			search(keyword.getMerchantName());
		}
	};

	private void search(String keyword) {
		if (TextUtils.isEmpty(keyword) || keyword.trim().length() == 0) {
			TipDialog.showDialog(FoodSearchActivity.this, R.string.tip,
					R.string.confirm, R.string.search_content_hint);
			return;
		}
		List<Merchant> merchantList = DataSupport.where("merchantName=?",
						keyword).find(Merchant.class);
		if (merchantList == null || merchantList.size() == 0) {
			Merchant merchant = new Merchant();
			merchant.setMerchantName(keyword);
			Long date = new Date().getTime();
			merchant.setOpDate(String.valueOf(date));
			merchant.save();
		}
		Intent intent = new Intent(FoodSearchActivity.this,
				FoodMerchantActivity.class);
		BlueTownApp.DISH_TYPE = "food";
		intent.putExtra("search", keyword);
		startActivity(intent);
	}

}

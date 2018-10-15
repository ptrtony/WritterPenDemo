package com.android.bluetown;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbJsonUtil;
import com.android.bluetown.app.BlueTownExitHelper;
import com.android.bluetown.bean.IndexBanner;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.result.MessageDetailsResult;
import com.android.bluetown.utils.Constant;

//message详情
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MessageDetailsActivity extends TitleBarActivity {
	private TextView tv_name, puslishDate, deadLineDate;
	private WebView messageContent;
	private String hid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_message_details);
		BlueTownExitHelper.addActivity(this);
		initView();
		getData();
	}

	private void initView() {
		hid = getIntent().getStringExtra("hid");
		tv_name = (TextView) findViewById(R.id.tv_name);
		puslishDate = (TextView) findViewById(R.id.puslishDate);
		deadLineDate = (TextView) findViewById(R.id.deadLineDate);
		messageContent = (WebView) findViewById(R.id.messageContent);
	}

	private void getData() {
		params.put("hid", hid);
		httpInstance.post(Constant.HOST_URL
				+ Constant.Interface.MESSAGE_DETAILS, params,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int i, String s) {
						MessageDetailsResult result = (MessageDetailsResult) AbJsonUtil
								.fromJson(s, MessageDetailsResult.class);
						if (result.getRepCode().contains(Constant.HTTP_SUCCESS)) {
							dealResult(result);
						} else if (result.getRepCode().contains(
								Constant.HTTP_NO_DATA)) {
							Toast.makeText(MessageDetailsActivity.this,
									R.string.no_data, Toast.LENGTH_LONG).show();

						}

					}
				});
	}

	private void dealResult(MessageDetailsResult result) {
		IndexBanner centerItem = result.getData();
		tv_name.setText(centerItem.getTitle());
		puslishDate.setText(centerItem.getCreateDate());
		deadLineDate.setText("过期时间：" + centerItem.getPastDate());
		String content = centerItem.getContent();
		if (content != null && !content.isEmpty()) {
			messageContent.setVisibility(View.VISIBLE);
			content = "<div style=\"line-height:150%;font-size:10pt;\">"
					+ content + "</div>";
			content = content.replaceAll("color:black;", "color:#333333");
			Document doc_Dis = Jsoup.parse(content);
			Elements ele_Img = doc_Dis.getElementsByTag("img");
			if (ele_Img.size() != 0) {
				for (Element e_Img : ele_Img) {
					e_Img.attr("width", "100%");
					e_Img.attr("height", "auto");
				}
			}
			String newHtmlContent = doc_Dis.toString();
			messageContent.loadDataWithBaseURL("", newHtmlContent, "text/html",
					"UTF-8", "");
		} else {
			messageContent.setVisibility(View.GONE);
		}
	}

	public void initTitle() {
		setBackImageView();
		setTitleLayoutBg(R.color.app_blue);
		setTitleView("通知公告");
		rightImageLayout.setVisibility(View.INVISIBLE);
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}

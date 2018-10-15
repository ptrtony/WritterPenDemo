package com.android.bluetown.home.hot.model.act;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * 
 * @ClassName: ParkYellowDetailsActivity
 * @Description:TODO(HomeActivity-园区黄页-黄页详情)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:38:28
 * 
 */
public class ParkYellowDetailsActivity extends TitleBarActivity {
	private WebView mWebView;
	private String data;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_park_yellow_page_details);
		BlueTownExitHelper.addActivity(this);
		data = getIntent().getStringExtra("content");
		mWebView = (WebView) findViewById(R.id.parkYellowDetails);
		data = "<div style=\"line-height:150%;font-size:10pt;\">" + data
				+ "</div>";
		data = data.replaceAll("color:black;", "color:#333333;");
		Document doc_Dis = Jsoup.parse(data);
		Elements ele_Img = doc_Dis.getElementsByTag("img");
		if (ele_Img.size() != 0) {
			for (Element e_Img : ele_Img) {
				e_Img.attr("width", "100%");
				e_Img.attr("height", "auto");
			}
		}
		String newHtmlContent = doc_Dis.toString();
		mWebView.loadDataWithBaseURL("", newHtmlContent, "text/html",
				"UTF-8", "");
	}

	/**
	 * 
	 * @Title: initTitle
	 * @Description: 初始化标题栏
	 * @see com.android.bluetown.activity.TitleBarActivity#initTitle()
	 */
	@Override
	public void initTitle() {
		// TODO Auto-generated method stub
		setBackImageView();
		setTitleView(R.string.park_yellow_details);
		setTitleLayoutBg(R.color.title_bg_blue);
		rightImageLayout.setVisibility(View.INVISIBLE);

	}

}

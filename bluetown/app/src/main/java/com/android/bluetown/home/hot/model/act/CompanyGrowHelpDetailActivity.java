package com.android.bluetown.home.hot.model.act;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.bluetown.R;
import com.android.bluetown.TitleBarActivity;
import com.android.bluetown.app.BlueTownExitHelper;

/**
 * 
 * @ClassName: CompanyGrowHelpDetailActivity
 * @Description:TODO(HomeActivity-企业成长帮助-详情)
 * @author: shenyz
 * @date: 2015年7月31日 上午10:33:44
 * 
 */
public class CompanyGrowHelpDetailActivity extends TitleBarActivity {
	private WebView companyGrowHelpDetail;
	private TextView publishTitle, publishDate;
	private String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.ac_company_grow_help_details);
		BlueTownExitHelper.addActivity(this);
		initUIViews();
	}

	private void initUIViews() {
		data = getIntent().getStringExtra("content");
		String publishTitleContent = getIntent().getStringExtra("title");
		String date = getIntent().getStringExtra("date");
		companyGrowHelpDetail = (WebView) findViewById(R.id.companyGrowHelpDetail);
		publishTitle = (TextView) findViewById(R.id.publishTitle);
		publishDate = (TextView) findViewById(R.id.publishDate);
		publishTitle.setText(publishTitleContent);
		publishDate.setText(date);
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
		companyGrowHelpDetail.loadDataWithBaseURL("", newHtmlContent,
				"text/html", "UTF-8", "");
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
		setTitleView(R.string.company_growup_help);
		rightImageLayout.setVisibility(View.INVISIBLE);
		setTitleLayoutBg(R.color.title_bg_blue);
	}

}

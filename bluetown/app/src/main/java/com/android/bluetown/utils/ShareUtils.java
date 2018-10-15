package com.android.bluetown.utils;

import android.content.Context;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.bluetown.R;

public class ShareUtils {
	/**
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param imageUrl
	 */
	public static void showShare(Context context, String title, String content,
			String imageUrl) {
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.bluetowngroup.com/");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(content);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		oks.setImageUrl(imageUrl);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.bluetowngroup.com/");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(content);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.bluetowngroup.com/");
		// 启动分享GUI
		oks.show(context);
	}
}

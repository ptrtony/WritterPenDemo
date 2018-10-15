package com.android.bluetown.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.alipay.sdk.app.PayTask;
import com.android.bluetown.R;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.listener.AbsHttpResponseListener;
import com.android.bluetown.listener.AliPayListener;
import com.android.bluetown.pref.SharePrefUtils;
import com.android.bluetown.result.Result;
import com.android.bluetown.utils.Constant;

public class AlipayUtil {
	private Activity mContext;
	private String orderNum;
	private String payType;
	private String bid;
	private AbHttpUtil httpUtil;
	private AliPayListener listerner;
	private FinalDb db;
	private String userId;
	private String token;
	private String telephone;
	private SharePrefUtils prefUtils;
	private String des;
	private double money;
	/**
	 *
	 * @param mContext
	 *            上下文对象
	 * @param orderNum
	 *            订单号
	 * @param tag
	 */
	public AlipayUtil(Activity mContext, String orderNum, String payType,String bid) {
		this.mContext = mContext;
		this.orderNum = orderNum;
		this.payType = payType;
		this.bid = bid;
		listerner = (AliPayListener) mContext;
	}

//	 商户PID
	public static final String PARTNER = "2088621198116199";//正式库
//	 商户收款账号
	public static final String SELLER = "nbgxqzhy@foxmail.com";
//	 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJyYZh5vgrZPl0uXCAlwtQNw2KpbHPNXpwS8gAOZcCpg8R4J/YUoghjFyD/d/ybMJCWDbQuJ+uA/nvecBaX9DGPnNyLB65GOdz8jPyjzHZbYjvq95slsQg5njVwJuniw0XEVjX1GbZZTw0BhpF39of7KQE0VczOdmUs+ywzyOV6LAgMBAAECgYB6Sbz9qaFPE+qXujMphB2nuTlKF+sM0Xd5iGcD6JAiWCATkEOt+AtYoyOVVJzLxIG51d+TMkZ7VtOik/IJRQqlw+8kK6obSqeJWlfAgqyA9Ynig2bQ7wCCccPZjQ0IMlKfdXQB8u2siVFYkkH1eaurY8l1Sf5h6vRdslgQW5x8eQJBANoXCiVHzBS5En72LJsV/zZq0gF2Uq4vutuc0XZd3HDXVKyVfdXxKq0K+T0TchqjEMPJn/I42bu24hC5zo0aAScCQQC30Nuth8AHjpCX5P2NXxhUuGPA/VHnPHs91gs6mPGgzr/REbD7h6mtPjvACYb438L+zRs8SjP66cuuio9a1s39AkAK28DOkF+R3JEWLy2TXyoAzJq+JeUB9TBphMpNCWlTWPVHTn4ga8yDJxu3tcMlnl7UULKc/cWhef6RdJ7bGaNbAkAPrEtO3IhPHKTPjsLfzv/wGVjIF9Fn+gw4FYwXjvrrTzWjyfJhQAwDrY7CrQ1JIyspNqvf1CPifRTU6ROYF0CxAkEAlqhTaLVJ7h3+7/IvDnwMkrMRbCnSn57TRTFQCp0NI7J5YeZ2LTUfya3eICnYGG33cubW3EspF4bZ1asZuVJ1aQ==";
//	 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";



	//商户收款账号 测试库
//	public static final String SELLER = "henry_927@163.com";
//	//商户私钥，pkcs8格式  测试库
//	public static final String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrdsNsSoNTOnhcdPssFeA2FF5lKVxsXyhMK6OhcVN20R6jeoe9XrWtX4gg7hW6ujQxuFNuXNX6hxMvYzq5HKhyOIn1REnHh52f5oHO8KLoHMMaNGJ5NOtgdaznYjilPgBFXPfz1PdE/YPC/yYUXGOiCornVnKnTtR/VzSj6G1SdsCQ/4SlLJ0JStmf6W8BJ9ymrdzfLWMwXEosuT+r+0vw5nIxH3R9IQJvk4aQa++a+UKDPIAPCOROMLaMQq5beeUT4PNQeovcgqgNe6zMj8FDCZm5HVWDgONYYhK/scFAFUFY16PByUNQtTO9yndw6voaxlsjWDt3673Lv05fQZPNAgMBAAECggEAdFlfnIEngfeAKwT6V1z0yt2hfDXH0IX1u1I1c9yJjWuzxazkpRy6XU4g+m90cUPfHQn+GzqFvL2hyBsmguwuZWt2Oec/xVSCIjnxRbJGaQjzXznCxIqjWc4gKNaep4pd9rNyUZPmwnrgyRYICfLzbMaIoRgpLtywF0TepIaYgEaUksYL4LPvakk9K8OJxBGg4T6TtPUCSn4mVi8bx/qkwTQPbjWeggmilqhLB/bXQP7B3qEooX3pIr5yQfiIWfgrdozU2QI10phOIOabhdMwXiEzudRPqjESQ/IL0bkdSQUoA+AgeViWK41ytSuQi3SQtiAiP48cDamUrUX7ad9kJQKBgQDqthckz0utlf+o7qCmIt2woN1ggHBlm3N+9IJOPDGDezjpBk2VTR+9IoVKjMhAU3RBVYcqaAt+KdKntOp9Dn1AQ/rG65HDOaEb2kcGZHTqbCv3lQxuFz/8W8wS+JPwPuAkriZy7xgf745xXF9wI1EYrE3pUlCvEN+vOF18uB2MVwKBgQC7BBejQftwGMGrSjtnQa+dRyv4J/Rm2tH7R3kC6Ea+AyzZmiVJODaG3FwvkSpIiA5oohsXgDAAVWGUuwpraoPZ9VIhMV1fULp6vrS2utKQgnQ7SFCb58j6n0gknA7KsGIAXgl3Lsd/3RSxl0Hzi+dZ9tO37WyukyT7G5zyRrJKewKBgFvk3RzZWHznHIjMnPNO9VMhOGt+/gKaIVcUbiwpXw1mYjrB8ABDiOipKlysz/53bi3iSaBtygokX8iviZta3+kYdIgLVS+FUbGw/VvgWvIpVfGCVrKHwdKRMk4/KtTGGUXjNUSMnfpq9W8mxmsGOMsbMwnl45cXWSymOdwJze/TAoGBAJJCzhXRicsOoZDMOo8z8RupSPsIjH1Qj77dFAgxDvqWUyJQCu8rtoFcoABXoOEe/13XwiZsC56HKA0BB1Ig+MOiuDvbdrIP2fgbZAwxKnL4g9zFXr2/1hAkOHDhWDQvJEYzRWXfwQkAJ6oIqPzHowVaJBUJ6+DQlbCJ1QIqRlA3AoGBAIsT7QiJP9e1oEi8lhquQubySw+YooP3BSZphYjgMdj7/QMtgUEKIqfpsl4rvMUVWXewPfpSJ+eTAJF0GEb+/W77vbYOGhNO4Ei2sBncnACve8MsEWZXHbQgyJl+IqULPE9I3dG0IlSPYngaCG/G1xQrKUgQgRc+JCOJXlKWwUmY";
//	//公钥测试库
//	public static final String RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArBG9pMQbMQdrbFAyMKlcpwCZJfcm9zxBrf+fC5R8B+BEoX8IBrCrx+eIOJJmQTpgISAA3toJJPG108R9mRdwtrc/s5lSJPJGeSDt5NUIfOc+yDjfROIuXTMc51ZOC8yoH9R3GyWrWIIB0qQi7sX3P+caIN6NniDqqlD2siM9PCFWGV54UJoHJPj6fJ9qDYG0nL+Dzbm+mR3wwV3nANeT7WKBDNRbvOqMMDPskWzeehsAGQWdt+B7A801s96h0INaqpUc2JyksverU0YUFtV/oh21BKX67Yd8/wxKkdnLHFQleTHQ/9FD3eLw1ZtDLfmHTt0Y615jkIqXf2BzHfgPAQIDAQAB";
//	public static final String PARTNER = "2088102053103014";//测试库
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);

					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						checkPay(resultStatus,resultInfo);
					} else {
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							checkPay(resultStatus,resultInfo);
						} else {
							listerner.payFaild();
						}
					}
					break;
				}
				case SDK_CHECK_FLAG: {
					Toast.makeText(mContext, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
							.show();
					break;
				}
				default:
					break;
			}
		};
	};

	/**
	 * 检查支付状态
	 *
	 * @param status
	 */
	private void checkPay(String status,String resultInfo) {
		prefUtils = new SharePrefUtils(mContext);
		db = FinalDb.create(mContext);
		List<MemberUser> users = db.findAll(MemberUser.class);
		if (users != null && users.size() != 0) {
			MemberUser user = users.get(0);
			if (user != null) {
				token = prefUtils.getString(SharePrefUtils.TOKEN, "");
				userId = user.getMemberId();
				telephone=user.getUsername();
			}
		}
		if (httpUtil == null) {
			httpUtil = AbHttpUtil.getInstance(mContext);
		}
		AbRequestParams param = new AbRequestParams();
		// cid:订单id,payType:支付类型（0支付宝,1银联,2为微信支付）tradeStatus:交易状态
		param.put("bid", bid);
		param.put("userId", userId);
		param.put("alipayNum",getAlipayNum(mContext.getResources().getString(R.string.app_name),des,String.valueOf(money)));
		param.put("communicationToken", token);
		param.put("tradeStatus", status);
		httpUtil.post(Constant.HOST_URL+Constant.Interface.BillAction_successPayment, param,
				new AbsHttpResponseListener() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						// TODO Auto-generated method stub
						if (arg1 != null && arg1.length() > 0) {
							Result result = (Result) AbJsonUtil.fromJson(arg1,
									Result.class);
							if (result != null) {
								if (result.getRepCode().equals(Constant.HTTP_SUCCESS)) {
									listerner.paySuccess();
								} else {
									// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
									listerner.payFaild();
								}
							} else {
								listerner.payFaild();
							}
						}
					}
				});
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 *
	 */
	public void pay(String title, String des, double money) {
		this.des = des;
		this.money = money;
		// 订单
		String orderInfo = getOrderInfo(title, des, String.valueOf(money));

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(mContext);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, false);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 *
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(mContext);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 *
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(mContext);
		String version = payTask.getVersion();
		Toast.makeText(mContext, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 *
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNum + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\""
				+ Constant.HOST_URL+Constant.Interface.CALL_BACK_SERVER + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 *
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private String getAlipayNum(String project,String body,String price){
		StringBuilder sb = new StringBuilder();
		sb.append(getOrderInfo(project,body,price));
		sb.append("&success=\"true\"");
		sb.append("&"+getSignType());
		// 订单
		String orderInfo = getOrderInfo(project, body, price);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("&sign=\"" + sign + "\"&"
				+ getSignType());
		return sb.toString();
	}
}

package com.android.bluetown.network;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.ab.global.AbAppConfig;
import com.ab.global.AbAppException;
import com.ab.http.AbBinaryHttpResponseListener;
import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbGzipDecompressingEntity;
import com.ab.http.AbHttpResponseListener;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.http.entity.MultipartEntity;
import com.ab.http.ssl.EasySSLProtocolSocketFactory;
import com.ab.task.thread.AbThreadFactory;
import com.ab.util.AbAppUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbLogUtil;
import com.android.bluetown.bean.MemberUser;
import com.android.bluetown.pref.SharePrefUtils;
import net.tsz.afinal.FinalDb;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class BlueTownClient {
    private static Context mContext;
    public static Executor mExecutorService = null;
    private String encode = "UTF-8";
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 BIDUBrowser/6.x Safari/537.31";
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String USER_AGENT = "User-Agent";
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private CookieStore mCookieStore;
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    private static final int DEFAULT_MAX_RETRIES = 2;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int FAILURE_MESSAGE_CONNECT = 2;
    protected static final int FAILURE_MESSAGE_SERVICE = 3;
    protected static final int START_MESSAGE = 4;
    protected static final int FINISH_MESSAGE = 5;
    protected static final int PROGRESS_MESSAGE = 6;
    protected static final int RETRY_MESSAGE = 7;
    private SharePrefUtils sharePrefUtils;
    private String userId = "";
    private String communicationToken="";
    private int mTimeout = 10000;
    private boolean mIsOpenEasySSL = true;
    private DefaultHttpClient mHttpClient = null;
    private HttpContext mHttpContext = null;
    private FinalDb db;
    private HttpRequestRetryHandler mRequestRetryHandler = new HttpRequestRetryHandler() {
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if(executionCount >= 2) {
                AbLogUtil.d(BlueTownClient.mContext, "超过最大重试次数，不重试");
                return false;
            } else if(exception instanceof NoHttpResponseException) {
                AbLogUtil.d(BlueTownClient.mContext, "服务器丢掉了连接，重试");
                return true;
            } else if(exception instanceof SSLHandshakeException) {
                AbLogUtil.d(BlueTownClient.mContext, "ssl 异常 不重试");
                return false;
            } else {
                HttpRequest request = (HttpRequest)context.getAttribute("http.request");
                boolean idempotent = request instanceof HttpEntityEnclosingRequest;
                if(!idempotent) {
                    AbLogUtil.d(BlueTownClient.mContext, "请求被认为是幂等的，重试");
                    return true;
                } else {
                    return exception != null;
                }
            }
        }
    };

    public BlueTownClient(Context context) {
        mContext = context;
        mExecutorService = AbThreadFactory.getExecutorService();
        this.mHttpContext = new BasicHttpContext();
        sharePrefUtils = new SharePrefUtils(mContext);
        db = FinalDb.create(mContext);
        List<MemberUser> userLists = db.findAll(MemberUser.class);
        if (userLists!=null&&userLists.size()>0){
            userId = userLists.get(0).memberId;
        }
        communicationToken = sharePrefUtils.getString(SharePrefUtils.TOKEN,"");
    }

    public void get(final String url, final AbRequestParams params, final AbHttpResponseListener responseListener) {
        responseListener.setHandler(new BlueTownClient.ResponderHandler(responseListener));
        mExecutorService.execute(new Runnable() {
            public void run() {
                try {
                    BlueTownClient.this.doGet(url, params, responseListener);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    public void post(final String url, final AbRequestParams params, final AbHttpResponseListener responseListener) {
        responseListener.setHandler(new BlueTownClient.ResponderHandler(responseListener));
        mExecutorService.execute(new Runnable() {
            public void run() {
                try {
                    BlueTownClient.this.doPost(url, params, responseListener);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    private void doGet(String url, AbRequestParams params, AbHttpResponseListener responseListener) {
        try {
            responseListener.sendStartMessage();
            if(!AbAppUtil.isNetworkAvailable(mContext)) {
                Thread.sleep(200L);
                responseListener.sendFailureMessage(600, AbAppConfig.CONNECT_EXCEPTION, new AbAppException(AbAppConfig.CONNECT_EXCEPTION));
                return;
            }

            if(params != null) {
                if(url.indexOf("?") == -1) {
                    url = url + "?";
                }

                url = url + params.getParamString();
            }

            HttpGet e = new HttpGet(url);
            e.addHeader("User-Agent", this.userAgent);
            e.addHeader("Accept-Encoding", "gzip");
            if (userId!=null&& !TextUtils.isEmpty(userId)&&communicationToken!=null&&!TextUtils.isEmpty(communicationToken)){
                e.addHeader("userId",userId);
                e.addHeader("communicationToken",communicationToken);
            }
            DefaultHttpClient httpClient = this.getHttpClient();
            String response = (String)httpClient.execute(e, new BlueTownClient.RedirectionResponseHandler(url, responseListener), this.mHttpContext);
            AbLogUtil.i(mContext, "[HTTP GET]:" + url + ",result：" + response);
        } catch (Exception var10) {
            var10.printStackTrace();
            responseListener.sendFailureMessage(900, var10.getMessage(), new AbAppException(var10));
        } finally {
            responseListener.sendFinishMessage();
        }

    }

    private void doPost(String url, AbRequestParams params, AbHttpResponseListener responseListener) {
        try {
            responseListener.sendStartMessage();
            if(!AbAppUtil.isNetworkAvailable(mContext)) {
                Thread.sleep(200L);
                responseListener.sendFailureMessage(600, AbAppConfig.CONNECT_EXCEPTION, new AbAppException(AbAppConfig.CONNECT_EXCEPTION));
                return;
            }

            HttpPost e = new HttpPost(url);
            e.addHeader("User-Agent", this.userAgent);
            e.addHeader("Accept-Encoding", "gzip");
            if (userId!=null&& !TextUtils.isEmpty(userId)&&communicationToken!=null&&!TextUtils.isEmpty(communicationToken)){
                e.addHeader("userId",userId);
                e.addHeader("communicationToken",communicationToken);
            }
            boolean isContainFile = false;
            HttpEntity response;
            if(params != null) {
                response = params.getEntity();
                e.setEntity(response);
                if(params.getFileParams().size() > 0) {
                    isContainFile = true;
                }
            }

            response = null;
            DefaultHttpClient httpClient = this.getHttpClient();
            if(isContainFile) {
                e.addHeader("connection", "keep-alive");
                e.addHeader("Content-Type", "multipart/form-data; boundary=" + params.boundaryString());
                AbLogUtil.i(mContext, "[HTTP POST]:" + url + ",包含文件域!");
            }

            String response1 = (String)httpClient.execute(e, new BlueTownClient.RedirectionResponseHandler(url, responseListener), this.mHttpContext);
            AbLogUtil.i(mContext, "request：" + url + ",result：" + response1);
        } catch (Exception var11) {
            var11.printStackTrace();
            AbLogUtil.i(mContext, "[HTTP POST]:" + url + ",error：" + var11.getMessage());
            responseListener.sendFailureMessage(900, var11.getMessage(), new AbAppException(var11));
        } finally {
            responseListener.sendFinishMessage();
        }

    }

    public void doRequest(final String url, final AbRequestParams params, final AbStringHttpResponseListener responseListener) {
        responseListener.setHandler(new BlueTownClient.ResponderHandler(responseListener));
        mExecutorService.execute(new Runnable() {
            public void run() {
                HttpURLConnection urlConn = null;

                try {
                    responseListener.sendStartMessage();
                    if(AbAppUtil.isNetworkAvailable(BlueTownClient.mContext)) {
                        String e = null;
                        URL requestUrl = new URL(url);
                        urlConn = (HttpURLConnection)requestUrl.openConnection();
                        urlConn.setRequestMethod("POST");
                        urlConn.setConnectTimeout(BlueTownClient.this.mTimeout);
                        urlConn.setReadTimeout(BlueTownClient.this.mTimeout);
                        urlConn.setDoOutput(true);
                        if(params != null) {
                            urlConn.setRequestProperty("connection", "keep-alive");
                            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + params.boundaryString());
                            MultipartEntity reqEntity = params.getMultiPart();
                            reqEntity.writeTo(urlConn.getOutputStream());
                        } else {
                            urlConn.connect();
                        }

                        if(urlConn.getResponseCode() == 200) {
                            e = BlueTownClient.this.readString(urlConn.getInputStream());
                        } else {
                            e = BlueTownClient.this.readString(urlConn.getErrorStream());
                        }

                        e = URLEncoder.encode(e, BlueTownClient.this.encode);
                        urlConn.getInputStream().close();
                        responseListener.sendSuccessMessage(200, e);
                        return;
                    }

                    Thread.sleep(200L);
                    responseListener.sendFailureMessage(600, AbAppConfig.CONNECT_EXCEPTION, new AbAppException(AbAppConfig.CONNECT_EXCEPTION));
                } catch (Exception var8) {
                    var8.printStackTrace();
                    AbLogUtil.i(BlueTownClient.mContext, "[HTTP POST]:" + url + ",error：" + var8.getMessage());
                    responseListener.sendFailureMessage(900, var8.getMessage(), new AbAppException(var8));
                    return;
                } finally {
                    if(urlConn != null) {
                        urlConn.disconnect();
                    }
                    responseListener.sendFinishMessage();
                }

            }
        });
    }

    public void writeResponseData(Context context, HttpEntity entity, String name, AbFileHttpResponseListener responseListener) {
        if(entity != null) {
            if(responseListener.getFile() == null) {
                responseListener.setFile(context, name);
            }

            InputStream inStream = null;
            FileOutputStream outStream = null;

            try {
                inStream = entity.getContent();
                long e = entity.getContentLength();
                outStream = new FileOutputStream(responseListener.getFile());
                if(inStream != null) {
                    byte[] tmp = new byte[4096];
                    int count = 0;

                    int l;
                    while((l = inStream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                        count += l;
                        outStream.write(tmp, 0, l);
                        responseListener.sendProgressMessage((long)count, (long)((int)e));
                    }
                }

                responseListener.sendSuccessMessage(200);
            } catch (Exception var20) {
                var20.printStackTrace();
                responseListener.sendFailureMessage(602, AbAppConfig.SOCKET_TIMEOUT_EXCEPTION, var20);
            } finally {
                try {
                    if(inStream != null) {
                        inStream.close();
                    }

                    if(outStream != null) {
                        outStream.flush();
                        outStream.close();
                    }
                } catch (IOException var19) {
                    var19.printStackTrace();
                }

            }

        }
    }

    public void readResponseData(HttpEntity entity, AbBinaryHttpResponseListener responseListener) {
        if(entity != null) {
            InputStream inStream = null;
            ByteArrayOutputStream outSteam = null;

            try {
                inStream = entity.getContent();
                outSteam = new ByteArrayOutputStream();
                long e = entity.getContentLength();
                if(inStream != null) {
                    int count = 0;
                    byte[] tmp = new byte[4096];

                    int l;
                    while((l = inStream.read(tmp)) != -1) {
                        count += l;
                        outSteam.write(tmp, 0, l);
                        responseListener.sendProgressMessage((long)count, (long)((int)e));
                    }
                }

                responseListener.sendSuccessMessage(200, outSteam.toByteArray());
            } catch (Exception var18) {
                var18.printStackTrace();
                responseListener.sendFailureMessage(602, AbAppConfig.SOCKET_TIMEOUT_EXCEPTION, var18);
            } finally {
                try {
                    if(inStream != null) {
                        inStream.close();
                    }

                    if(outSteam != null) {
                        outSteam.close();
                    }
                } catch (Exception var17) {
                    var17.printStackTrace();
                }

            }

        }
    }

    public void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    public BasicHttpParams getHttpParams() {
        BasicHttpParams httpParams = new BasicHttpParams();
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(30);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
        ConnManagerParams.setTimeout(httpParams, (long)this.mTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        HttpConnectionParams.setSoTimeout(httpParams, this.mTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, this.mTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(httpParams, this.userAgent);
        HttpClientParams.setRedirecting(httpParams, false);
        HttpClientParams.setCookiePolicy(httpParams, "compatibility");
        httpParams.setParameter("http.route.default-proxy", (Object)null);
        return httpParams;
    }

    public DefaultHttpClient getHttpClient() {
        return this.mHttpClient != null?this.mHttpClient:this.createHttpClient();
    }

    public DefaultHttpClient createHttpClient() {
        BasicHttpParams httpParams = this.getHttpParams();
        if(this.mIsOpenEasySSL) {
            EasySSLProtocolSocketFactory easySSLProtocolSocketFactory = new EasySSLProtocolSocketFactory();
            SchemeRegistry supportedSchemes = new SchemeRegistry();
            PlainSocketFactory socketFactory = PlainSocketFactory.getSocketFactory();
            supportedSchemes.register(new Scheme("http", socketFactory, 80));
            supportedSchemes.register(new Scheme("https", easySSLProtocolSocketFactory, 443));
            ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
            this.mHttpClient = new DefaultHttpClient(connectionManager, httpParams);
        } else {
            this.mHttpClient = new DefaultHttpClient(httpParams);
        }

        this.mHttpClient.setHttpRequestRetryHandler(this.mRequestRetryHandler);
        this.mHttpClient.setCookieStore(this.mCookieStore);
        return this.mHttpClient;
    }

    public boolean isOpenEasySSL() {
        return this.mIsOpenEasySSL;
    }

    public void setOpenEasySSL(boolean isOpenEasySSL) {
        this.mIsOpenEasySSL = isOpenEasySSL;
    }

    private String readString(InputStream is) {
        StringBuffer rst = new StringBuffer();
        byte[] buffer = new byte[1048576];
        boolean len = false;

        int var7;
        try {
            while((var7 = is.read(buffer)) > 0) {
                for(int e = 0; e < var7; ++e) {
                    rst.append((char)buffer[e]);
                }
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return rst.toString();
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getEncode() {
        return this.encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void shutdown() {
        if(this.mHttpClient != null && this.mHttpClient.getConnectionManager() != null) {
            this.mHttpClient.getConnectionManager().shutdown();
        }

    }

    public CookieStore getCookieStore() {
        if(this.mHttpClient != null) {
            this.mCookieStore = this.mHttpClient.getCookieStore();
        }

        return this.mCookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.mCookieStore = cookieStore;
    }

    private class RedirectionResponseHandler implements ResponseHandler<String> {
        private AbHttpResponseListener mResponseListener = null;
        private String mUrl = null;

        public RedirectionResponseHandler(String url, AbHttpResponseListener responseListener) {
            this.mUrl = url;
            this.mResponseListener = responseListener;
        }

        public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            HttpUriRequest request = (HttpUriRequest)BlueTownClient.this.mHttpContext.getAttribute("http.request");
            int statusCode = response.getStatusLine().getStatusCode();
            Object entity = response.getEntity();
            String responseBody = null;
            Header locationHeader;
            String location;
            if(statusCode == 200) {
                if(entity != null) {
                    if(this.mResponseListener instanceof AbStringHttpResponseListener) {
                        locationHeader = ((HttpEntity)entity).getContentEncoding();
                        if(locationHeader != null) {
                            location = locationHeader.getValue();
                            if(location != null && location.contains("gzip")) {
                                entity = new AbGzipDecompressingEntity((HttpEntity)entity);
                            }
                        }

                        location = EntityUtils.getContentCharSet((HttpEntity)entity) == null?BlueTownClient.this.encode:EntityUtils.getContentCharSet((HttpEntity)entity);
                        responseBody = new String(EntityUtils.toByteArray((HttpEntity)entity), location);
                        ((AbStringHttpResponseListener)this.mResponseListener).sendSuccessMessage(statusCode, responseBody);
                    } else if(this.mResponseListener instanceof AbBinaryHttpResponseListener) {
                        responseBody = "Binary";
                        BlueTownClient.this.readResponseData((HttpEntity)entity, (AbBinaryHttpResponseListener)this.mResponseListener);
                    } else if(this.mResponseListener instanceof AbFileHttpResponseListener) {
                        String locationHeader1 = AbFileUtil.getCacheFileNameFromUrl(this.mUrl, response);
                        BlueTownClient.this.writeResponseData(BlueTownClient.mContext, (HttpEntity)entity, locationHeader1, (AbFileHttpResponseListener)this.mResponseListener);
                    }

                    try {
                        ((HttpEntity)entity).consumeContent();
                    } catch (Exception var8) {
                        var8.printStackTrace();
                    }

                    return responseBody;
                }
            } else if(statusCode != 302 && statusCode != 301) {
                if(statusCode == 404) {
                    this.mResponseListener.sendFailureMessage(statusCode, AbAppConfig.NOT_FOUND_EXCEPTION, new AbAppException(AbAppConfig.NOT_FOUND_EXCEPTION));
                } else if(statusCode == 403) {
                    this.mResponseListener.sendFailureMessage(statusCode, AbAppConfig.FORBIDDEN_EXCEPTION, new AbAppException(AbAppConfig.FORBIDDEN_EXCEPTION));
                } else {
                    this.mResponseListener.sendFailureMessage(statusCode, AbAppConfig.REMOTE_SERVICE_EXCEPTION, new AbAppException(AbAppConfig.REMOTE_SERVICE_EXCEPTION));
                }
            } else {
                locationHeader = response.getLastHeader("location");
                location = locationHeader.getValue();
                if(request.getMethod().equalsIgnoreCase("POST")) {
                    BlueTownClient.this.doPost(location, (AbRequestParams)null, this.mResponseListener);
                } else if(request.getMethod().equalsIgnoreCase("GET")) {
                    BlueTownClient.this.doGet(location, (AbRequestParams)null, this.mResponseListener);
                }
            }

            return null;
        }
    }

    private static class ResponderHandler extends Handler {
        private Object[] response;
        private AbHttpResponseListener responseListener;

        public ResponderHandler(AbHttpResponseListener responseListener) {
            this.responseListener = responseListener;
        }

        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    this.response = (Object[])msg.obj;
                    if(this.response != null) {
                        if(this.responseListener instanceof AbStringHttpResponseListener) {
                            if(this.response.length >= 2) {
                                ((AbStringHttpResponseListener)this.responseListener).onSuccess(((Integer)this.response[0]).intValue(), (String)this.response[1]);
                            } else {
                                AbLogUtil.e(BlueTownClient.mContext, "SUCCESS_MESSAGE " + AbAppConfig.MISSING_PARAMETERS);
                            }
                        } else if(this.responseListener instanceof AbBinaryHttpResponseListener) {
                            if(this.response.length >= 2) {
                                ((AbBinaryHttpResponseListener)this.responseListener).onSuccess(((Integer)this.response[0]).intValue(), (byte[])this.response[1]);
                            } else {
                                AbLogUtil.e(BlueTownClient.mContext, "SUCCESS_MESSAGE " + AbAppConfig.MISSING_PARAMETERS);
                            }
                        } else if(this.responseListener instanceof AbFileHttpResponseListener) {
                            if(this.response.length >= 1) {
                                AbFileHttpResponseListener exception1 = (AbFileHttpResponseListener)this.responseListener;
                                exception1.onSuccess(((Integer)this.response[0]).intValue(), exception1.getFile());
                            } else {
                                AbLogUtil.e(BlueTownClient.mContext, "SUCCESS_MESSAGE " + AbAppConfig.MISSING_PARAMETERS);
                            }
                        }
                    }
                    break;
                case 1:
                    this.response = (Object[])msg.obj;
                    if(this.response != null && this.response.length >= 3) {
                        AbAppException exception = new AbAppException((Exception)this.response[2]);
                        this.responseListener.onFailure(((Integer)this.response[0]).intValue(), (String)this.response[1], exception);
                    } else {
                        AbLogUtil.e(BlueTownClient.mContext, "FAILURE_MESSAGE " + AbAppConfig.MISSING_PARAMETERS);
                    }
                case 2:
                case 3:
                default:
                    break;
                case 4:
                    this.responseListener.onStart();
                    break;
                case 5:
                    this.responseListener.onFinish();
                    break;
                case 6:
                    this.response = (Object[])msg.obj;
                    if(this.response != null && this.response.length >= 2) {
                        this.responseListener.onProgress(((Long)this.response[0]).longValue(), ((Long)this.response[1]).longValue());
                    } else {
                        AbLogUtil.e(BlueTownClient.mContext, "PROGRESS_MESSAGE " + AbAppConfig.MISSING_PARAMETERS);
                    }
                    break;
                case 7:
                    this.responseListener.onRetry();
            }

        }
    }
}

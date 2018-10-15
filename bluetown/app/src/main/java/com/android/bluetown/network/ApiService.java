//package com.android.bluetown.network;
//
//import java.io.IOException;
//import java.util.Collections;
//
//import okhttp3.ConnectionSpec;
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.TlsVersion;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by Dafen on 2018/7/16.
// */
//
//public class ApiService {
//    private static  String newapi="";
//    private static  String oldapi="";
//    private static String communicationToken="";
//    private static String userid="";
//    public static void config(String baseNewUrl,String baseOldUrl){
//        ApiService.newapi = baseNewUrl;
//        ApiService.oldapi = baseOldUrl;
//    }
//
//
//    public static <T> T createOAuthService(final Class<T> OAuthApiService){
//
//    }
//    public static <T> T createNewService(final Class<T> apiService){
//        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(newapi);
//
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.TLS_1_2)
//                .build();
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectionSpecs(Collections.singletonList(spec))
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        // token 如果已经过期则进行刷新处理
////                        if (!refreshTokenHanding && tokenIsExpires()) refreshToken();
//
//                        // 检查当前是否在进行token刷新处理
////                        while (refreshTokenHanding) {
////                            try {
////                                Thread.sleep(500);
////                            } catch (InterruptedException e) {
////                                Thread.currentThread().interrupt();
////                            }
////                        }
//
////                        if (authInfo == null) {
////                            throw new OAuthInvalidException();
////                        }
//
//                        Request request = chain.request();
//                        Request newReq = request.newBuilder()
//                                .addHeader("userId",userid)
//                                .addHeader("communicationToken",communicationToken)
//                                .build();
//                        return chain.proceed(newReq);
//                    }
//                }).build();
//
//        builder.client(client);
//
//        return builder.build().create(apiService);
//    }
//}

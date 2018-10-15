//package com.android.bluetown.network;
//
//import com.android.bluetown.model.LoginInfoBean;
//
//import io.reactivex.Observable;
//import retrofit2.http.Field;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.POST;
//
///**
// * Created by Dafen on 2018/7/13.
// */
//
//public interface AuthApi {
//    @POST("/mobi/member/MobiMemberAction/LoginInfo.mobi")
//    @FormUrlEncoded()
//    Observable<LoginInfoBean> Login(@Field("telphone") String telphone, @Field("password")String password, @Field("pttId") String pttId);
//}

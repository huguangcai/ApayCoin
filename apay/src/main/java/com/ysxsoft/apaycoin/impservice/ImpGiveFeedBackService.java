package com.ysxsoft.apaycoin.impservice;


import com.ysxsoft.apaycoin.modle.GiveFeedBackBean;
import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 意见反馈接口
 * 日期： 2018/11/10 0010 09:55
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpGiveFeedBackService {
//    @Multipart
//    @POST("user/feedback")
//    Observable<GiveFeedBackBean> getCall(@Header("token") String token,
//                                         @Part("content") String content,
//                                         @PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("user/feedback")
    Observable<GiveFeedBackBean> getCall(@Header("token") String token,
                                         @Part("content") String content,
                                         @Part MultipartBody.Part file);


}

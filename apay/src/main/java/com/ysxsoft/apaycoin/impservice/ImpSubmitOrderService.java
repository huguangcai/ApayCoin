package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.SubmitOrderBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpSubmitOrderService {
    @POST("products/buyPro")
    Observable<SubmitOrderBean> getCall(@Header("token")String token,
                                        @Query("pid")String pid,
                                        @Query("pwd")String pwd,
                                        @Query("num")String num,
                                        @Query("aid")String aid,
                                        @Query("content")String content);
}

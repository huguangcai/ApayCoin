package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MyMallHeaderInfoBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface OnlineMallLookShopHeaderService {
    @POST("products/shopdea")
    Observable<MyMallHeaderInfoBean> getCall(@Header("token")String token,
                                             @Query("uid")String uid);
}

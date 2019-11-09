package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MyOrderRunCompleteBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpMyOrderRunCompleteService {
    @POST("products/myorder")
    Observable<MyOrderRunCompleteBean> getCall(@Header("token")String token,
                                               @Query("type")String type);
}

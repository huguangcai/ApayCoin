package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MyOrderDetialBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpMyOrderDetialService {
    @POST("products/orderdea")
    Observable<MyOrderDetialBean> getCall(@Header("token")String token,
                                          @Query("oid")String oid);

}

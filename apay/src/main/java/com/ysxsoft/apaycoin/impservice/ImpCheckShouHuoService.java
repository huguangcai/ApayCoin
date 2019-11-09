package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.CheckShouHuoBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpCheckShouHuoService {
//    @POST("shops/qrshoukuan")
    @POST("products/qrshoukuan")
    Observable<CheckShouHuoBean> getCall(@Header("token")String token,
                                         @Query("oid")String oid,
                                         @Query("pwd") String pwd);
}

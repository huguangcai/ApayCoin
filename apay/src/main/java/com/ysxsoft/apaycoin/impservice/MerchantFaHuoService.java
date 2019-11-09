package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MerchantFaHuoBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface MerchantFaHuoService {
    @POST("shops/qrfh")
    Observable<MerchantFaHuoBean> getCall(@Header("token")String token,
                                          @Query("oid")String oid);
}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MerchantOrderRunAndCompleteBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpMerchantOrderRunAndCompleteService {
    @POST("shops/myshopOrder")
    Observable<MerchantOrderRunAndCompleteBean> getCall(@Header("token")String token,
                                                        @Query("type")String type);
 }

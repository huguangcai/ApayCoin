package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.BusinessInstructionsBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface ImpBusinessInstructionsService {
    @POST("user/getShopDec")
    Observable<BusinessInstructionsBean> getCall(@Header("token")String token);
}

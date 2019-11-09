package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.UpDataVersionCodeBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpUpDataVersionCodeService {
    @POST("index/getVersion")
    Observable<UpDataVersionCodeBean> getCall(@Query("type")String type);
}

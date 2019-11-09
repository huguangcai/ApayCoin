package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.GetCreditNumBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 信用恢复  恢复一个需要多少apay币
 */
public interface ImpGetCreditNumService {
    @POST("user/huifu")
    Observable<GetCreditNumBean> getCall(@Header("token")String token);
}

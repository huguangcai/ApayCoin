package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.RecoverCreditBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 恢复信用的接口
 */
public interface ImpRecoverCreditService {
    @POST("pays/payCredit")
    Observable<RecoverCreditBean> getCall(@Header("token")String token,
                                          @Query("num")String num,
                                          @Query("pwd")String pwd);
}

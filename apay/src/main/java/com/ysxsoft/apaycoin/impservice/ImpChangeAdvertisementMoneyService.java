package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ChangeAdvertisementMoneyBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 扫码转广告币接口
 * 日期： 2018/11/23 0023 17:14
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpChangeAdvertisementMoneyService {

    @POST("pays/outgold")
    Observable<ChangeAdvertisementMoneyBean> getCall(@Header("token") String token,
                                                     @Query("gold") String gold,
                                                     @Query("pwd") String pwd,
                                                     @Query("wallet") String wallet);

}


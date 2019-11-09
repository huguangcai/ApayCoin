package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.CheckGatheringMoneyBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 确认收款接口
 * 日期： 2018/11/19 0019 15:31
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpCheckGatheringMoneyservice {
    @POST("homes/qrshoukuan")
    Observable<CheckGatheringMoneyBean> getCall(@Header("token") String token,
                                                @Query("oid") String oid,
                                                @Query("pwd") String pwd);
}

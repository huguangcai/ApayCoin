package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.LookOrderBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 查看订单接口
 * 日期： 2018/11/19 0019 15:37
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpLookOrderService {
    @POST("homes/orderDea")
    Observable<LookOrderBean> getCall(@Header("token") String token,
                                      @Query("oid") String oid);
}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.CancleOrderBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 取消订单接口
 * 日期： 2018/11/19 0019 17:23
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpCancleOrderService {
    @POST("pays/cancelOrder")
    Observable<CancleOrderBean> getCall(@Header("token") String token,
                                        @Query("oid") String oid);
}

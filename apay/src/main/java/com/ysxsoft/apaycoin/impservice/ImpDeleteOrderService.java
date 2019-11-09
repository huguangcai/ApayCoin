package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.DeleteOrderBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 删除订单接口
 * 日期： 2018/11/21 0021 11:03
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpDeleteOrderService {
    @GET("homes/delOrder")
    Observable<DeleteOrderBean> getCall(@Header("token") String token,
                                        @Query("oid") String oid);
}

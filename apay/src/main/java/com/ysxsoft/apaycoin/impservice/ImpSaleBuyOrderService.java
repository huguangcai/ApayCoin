package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.SaleBuyOrderBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 销售购买订单接口
 * 日期： 2018/11/16 0016 11:28
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpSaleBuyOrderService {
    @POST("homes/myOrder")
    Observable<SaleBuyOrderBean> getCall(@Header("token") String token,
                                         @Query("type") String type,
                                         @Query("flag") String flag);
}

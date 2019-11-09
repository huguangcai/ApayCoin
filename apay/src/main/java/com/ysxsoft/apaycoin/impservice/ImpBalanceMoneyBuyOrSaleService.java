package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.BalanceMoneyBuyOrSaleBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 余额购买和出售接口
 * 日期： 2018/11/16 0016 15:01
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpBalanceMoneyBuyOrSaleService {
    @GET("homes/getOrderList")
    Observable<BalanceMoneyBuyOrSaleBean> getCall(@Header("token") String token,
                                                  @Query("type") String type,
                                                  @Query("page")String page);

}

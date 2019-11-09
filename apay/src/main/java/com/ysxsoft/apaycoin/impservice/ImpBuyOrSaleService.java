package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.BuyOrSaleBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 购买和销售接口
 * 日期： 2018/11/19 0019 08:53
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpBuyOrSaleService {
    @POST("pays/myZhifu")
    Observable<BuyOrSaleBean> getCall(@Header("token") String token,
                                      @Query("oid") String oid,
                                      @Query("zf_type") String zf_type,
                                      @Query("pwd") String pwd);
}

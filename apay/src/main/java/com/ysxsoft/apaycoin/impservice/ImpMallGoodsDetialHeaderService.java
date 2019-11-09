package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MallGoodsDetialHeaderBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 商家的商品信息接口
 * 日期： 2018/11/26 0026 11:12
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpMallGoodsDetialHeaderService {
    @GET("shops/getProMsg")
    Observable<MallGoodsDetialHeaderBean> getCall(@Header("token") String token,
                                                  @Query("pid") String pid);
}

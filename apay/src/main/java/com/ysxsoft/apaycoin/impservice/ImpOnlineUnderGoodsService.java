package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.OnlineUnderGoodsBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 上架或下架的商品
 * 日期： 2018/11/26 0026 10:29
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpOnlineUnderGoodsService {
    @POST("shops/profabu")
    Observable<OnlineUnderGoodsBean> getCall(@Header("token") String token,
                                             @Query("pid") String pid,
                                             @Query("flag") String flag);
}

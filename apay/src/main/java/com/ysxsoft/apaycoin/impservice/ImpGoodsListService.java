package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.GoodsListBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 我的店铺  商品列表接口
 * 日期： 2018/11/26 0026 09:13
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpGoodsListService {
    @POST("shops/myProList")
    Observable<GoodsListBean> getCall(@Header("token") String token,
                                      @Query("page") String page);
}

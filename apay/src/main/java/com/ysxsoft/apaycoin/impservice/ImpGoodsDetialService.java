package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.GoodsDetialBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 商品基本信息修改
 * 日期： 2018/11/26 0026 15:06
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpGoodsDetialService {
    @POST("shops/proeditjiben")
    Observable<GoodsDetialBean> getCall(@Header("token") String token,
                                        @Query("title") String title,
                                        @Query("money") String money,
                                        @Query("stock") String stock,
                                        @Query("pid") String pid);
}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.GoodsTypeBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述： 商品分类
 * 日期： 2018/11/24 0024 11:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpGoodsTypeService {
    @POST("homes/getshopClass")
    Observable<GoodsTypeBean> getCall(@Header("token") String token);
}

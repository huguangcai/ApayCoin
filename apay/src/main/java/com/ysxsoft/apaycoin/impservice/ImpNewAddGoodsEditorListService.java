package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.NewAddGoodsEditorListBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 产品列表详情
 * 日期： 2018/11/26 0026 15:36
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpNewAddGoodsEditorListService {
    @POST("shops/getprodeamsg")
    Observable<NewAddGoodsEditorListBean> getCall(@Header("token") String token, @Query("pid") String pid);
}

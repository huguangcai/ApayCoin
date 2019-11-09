package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.EditMyBankCardBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 编辑我的银行卡接口
 * 日期： 2018/11/8 0008 11:49
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@Deprecated
public interface ImpEditMyBankCardService {
    @POST("user/editMyCard")
    Observable<EditMyBankCardBean> getEditMybankInfo(
            @Header("token") String token,
            @Query("name") String name,
            @Query("card_num") String card_num,
            @Query("khh") String khh,
            @Query("cid") String cid);

}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.DeleteMyBankCardBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 删除我的银行卡
 * 日期： 2018/11/8 0008 11:58
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@Deprecated
public interface ImpdeleteMyBankCardService {
    @POST("user/delMyCard")
    Observable<DeleteMyBankCardBean> getDeleteInfo(
            @Header("token") String token,
            @Query("cid") String cid);
}

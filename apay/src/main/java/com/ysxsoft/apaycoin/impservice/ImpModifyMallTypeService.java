package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ModifyMallTypeBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 修改店铺分类的接口
 */
public interface ImpModifyMallTypeService {
    @POST("shops/setshopclass")
    Observable<ModifyMallTypeBean> getCall(@Header("token")String token,
                                           @Query("cid")String cid);
}

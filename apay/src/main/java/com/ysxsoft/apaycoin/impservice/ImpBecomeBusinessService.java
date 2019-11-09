package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.BecomeBusinessBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 申请成为商家接口
 * 日期： 2018/11/24 0024 13:46
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpBecomeBusinessService {
    @POST("homes/shenqing")
    Observable<BecomeBusinessBean> getCall(@Header("token") String token,
                                           @Query("name") String shopname,
                                           @Query("line") String line,
                                           @Query("phone") String phone,
                                           @Query("cid") String cid,
                                           @Query("pwd") String pwd);
}

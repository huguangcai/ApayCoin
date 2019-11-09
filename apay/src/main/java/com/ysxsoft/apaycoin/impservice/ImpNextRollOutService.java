package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.NextRollOutBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 下一步的转出接口
 * 日期： 2018/11/12 0012 16:28
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpNextRollOutService {
    @POST("pays/outMoney")
    Observable<NextRollOutBean> getCall(@Header("token") String token,
                                        @Query("f_uid") String f_uid,
                                        @Query("money") String money,
                                        @Query("phone") String phone,
                                        @Query("pwd") String pwd);
}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.RegisterBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 注册的接口
 * 日期： 2018/11/6 0006 10:45
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpRegisterService {
    @POST("index/registers")
    Observable<RegisterBean> getRegister(@Query("username") String username,
                                         @Query("password") String password,
                                         @Query("code") String code,
                                         @Query("invitation") String invitation,
                                         @Query("pay_pwd") String pay_pwd,
                                         @Query("nickname") String nickname);
}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean;

import java.io.File;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 修改登录密码接口
 * 日期： 2018/11/10 0010 11:35
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpModifyLoginPwdService {
    @GET("user/setUser")
    Observable<ModifyLoginPwdBean> getCall(@Header("token") String token,
                                           @Query("pay_pwd") String pay_pwd,
                                           @Query("repay_pwd") String repay_pwd,
                                           @Query("password") String password,
                                           @Query("repassword") String repassword,
                                           @Query("nickname") String nickname,
                                           @Query("icon") File icon);
}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ForgetLoginPayPwdBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 忘记登录支付密码接口
 * 日期： 2018/11/20 0020 11:19
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpForgetLoginPayPwdService {
    @GET("index/forgetPwd")
    Observable<ForgetLoginPayPwdBean> getCall(@Query("mobile") String mobile,
                                              @Query("code") String code,
                                              @Query("newpasssword") String newpasssword,
                                              @Query("type") String type);
}

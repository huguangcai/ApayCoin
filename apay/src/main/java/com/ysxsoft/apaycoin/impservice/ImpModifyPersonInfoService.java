package com.ysxsoft.apaycoin.impservice;

import android.database.Observable;

import com.ysxsoft.apaycoin.modle.ModifyPersonInfoBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * 描述： 修改个人信息
 * 日期： 2018/11/8 0008 11:41
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@Deprecated
public interface ImpModifyPersonInfoService {
    @GET("user/setUser")
    Observable<ModifyPersonInfoBean> getModifyInfo(
            @Header("token") String token,
            @Query("pay_pwd") String pay_pwd,
            @Query("repay_pwd") String repay_pwd,
            @Query("password") String password,
            @Query("repassword") String repassword,
            @Query("nickname") String nickname,
            @Query("icon") String icon);
}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean;

import java.io.File;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpModifyLoginPasswordService {
    @GET("user/setUser")
    Observable<ModifyLoginPwdBean> getCall(@Header("token") String token,
                                           @Query("password") String password,
                                           @Query("repassword") String repassword);
}

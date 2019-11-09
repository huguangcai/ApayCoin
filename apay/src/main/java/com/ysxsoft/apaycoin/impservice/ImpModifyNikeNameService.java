package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 修改用户名
 * 日期： 2018/11/20 0020 14:27
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpModifyNikeNameService {
    @POST("user/setUser")
    Observable<ModifyLoginPwdBean> getCall(@Header("token") String token,
                                           @Query("nickname") String nickname
    );
}

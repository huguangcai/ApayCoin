package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ConvertibilityBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 兑换接口
 * 日期： 2018/11/12 0012 16:47
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpConvertibilityService {
    @POST("pays/changeQuan")
    Observable<ConvertibilityBean> getCall(@Header("token") String token,
//                                           @Query("f_uid") String f_uid,
                                           @Query("money") String money,
                                           @Query("pwd") String pwd);
}

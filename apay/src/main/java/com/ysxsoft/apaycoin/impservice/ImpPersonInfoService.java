package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.DeleteInfoBean;
import com.ysxsoft.apaycoin.modle.PersonInfoBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;

/**
 * 描述： 个人信息接口
 * 日期： 2018/11/20 0020 08:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpPersonInfoService {
    @POST("homes/UserMsg")
    Observable<PersonInfoBean> getCall(@Header("token") String token,
                                       @Query("page") String page);


}

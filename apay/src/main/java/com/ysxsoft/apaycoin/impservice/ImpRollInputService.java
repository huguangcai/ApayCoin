package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.RollInputBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述： 转入接口
 * 日期： 2018/11/9 0009 15:16
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpRollInputService {
    @POST("user/getMyInMoneyQr")
    Observable<RollInputBean> getCall(@Header("token") String token);
}

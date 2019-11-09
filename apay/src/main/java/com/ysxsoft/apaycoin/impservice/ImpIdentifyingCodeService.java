package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.IdentifyingCodeBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 获取验证码的接口
 * 日期： 2018/11/6 0006 10:33
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpIdentifyingCodeService {
    @POST("index/getcode")
    Observable<IdentifyingCodeBean> getIdentifyingCode(@Query("phone") String phone);
}

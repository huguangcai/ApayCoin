package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.AddressManagerBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述： 地址管理接口
 * 日期： 2018/11/15 0015 13:50
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpAddressManagerService {
    @POST("user/getMyArea")
    Observable<AddressManagerBean> getCall(@Header("token") String token);
}

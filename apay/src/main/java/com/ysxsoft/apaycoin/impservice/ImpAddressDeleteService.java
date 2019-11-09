package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.AddressDeleteBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 地址删除接口
 * 日期： 2018/11/15 0015 14:18
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpAddressDeleteService {
    @GET("user/delMyArea")
    Observable<AddressDeleteBean> getCall(@Header("token") String token,
                                          @Query("aid") String aid);

}

package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.AddMyAddressBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 增加我的地址
 * 日期： 2018/11/15 0015 14:39
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpAddMyAddressService {
    @POST("user/myareaAdd")
    Observable<AddMyAddressBean> getCall(@Header("token") String token,
                                         @Query("provice") String provice,
                                         @Query("city") String city,
                                         @Query("area") String area,
                                         @Query("address") String address,
                                         @Query("linkname") String linkname,
                                         @Query("phone") String phone);
}

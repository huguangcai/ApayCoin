package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.AddMyAddressBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 地址编辑和设置默认接口
 * 日期： 2018/11/15 0015 15:51
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpAddressListEditNormalService {
    @POST("user/editMyArea")
    Observable<AddMyAddressBean> getCall(@Header("token") String token,
                                         @Query("aid") String aid,
                                         @Query("address") String address,
                                         @Query("is_ture") String is_ture,
                                         @Query("provice") String provice,
                                         @Query("city") String city,
                                         @Query("area") String area,
                                         @Query("linkname") String linkname,
                                         @Query("phone") String phone);
}

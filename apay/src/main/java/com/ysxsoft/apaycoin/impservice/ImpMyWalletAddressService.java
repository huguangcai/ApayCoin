package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MyWalletAddressBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述： 我的钱包地址
 * 日期： 2018/11/23 0023 15:44
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpMyWalletAddressService {
    @POST("user/getMyW")
    Observable<MyWalletAddressBean> getCall(@Header("token") String token);
}

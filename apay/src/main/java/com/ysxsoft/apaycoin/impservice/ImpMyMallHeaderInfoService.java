package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.MyMallHeaderInfoBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * 描述： 我的店铺头部信息接口
 * 日期： 2018/11/24 0024 14:05
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpMyMallHeaderInfoService {
    @GET("shops/mymoney")
    Observable<MyMallHeaderInfoBean> getCall(@Header("token") String token);
}

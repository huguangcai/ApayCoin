package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.CrowdfingBuyBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 众筹购买
 * 日期： 2018/11/23 0023 09:12
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpCrowdfingBuyService {
    @POST("crowdfunding/payVpay")
    Observable<CrowdfingBuyBean> getCall(@Header("token") String token,
                                         @Query("cid") String cid,
                                         @Query("num") String num,
                                         @Query("pwd") String pwd);
}

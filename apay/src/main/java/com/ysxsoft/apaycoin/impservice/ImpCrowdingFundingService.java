package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.CrowdingFundingBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 众筹接口
 * 日期： 2018/11/21 0021 09:30
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpCrowdingFundingService {
    @GET("homes/zhongchoulist")
    Observable<CrowdingFundingBean> getCall(@Header("token") String token,
                                            @Query("type") String type,
                                            @Query("page") String page);
}

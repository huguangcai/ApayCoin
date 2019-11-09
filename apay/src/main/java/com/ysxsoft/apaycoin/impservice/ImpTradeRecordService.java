package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.TradeRecordBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 交易记录接口
 * 日期： 2018/11/19 0019 17:32
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpTradeRecordService {
    @POST("homes/tranLog")
    Observable<TradeRecordBean> getCall(@Header("token") String token,
                                        @Query("page") String page);
}

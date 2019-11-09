package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.TicketRecordBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 券记录接口
 * 日期： 2018/11/14 0014 10:29
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface TicketRecordService {
    @POST("pays/quansHistory")
    Observable<TicketRecordBean> getCall(@Header("token") String token,
                                         @Query("page") String page);
}

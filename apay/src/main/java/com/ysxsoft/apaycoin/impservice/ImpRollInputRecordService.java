package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.RollInputRecordBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 转入记录接口
 * 日期： 2018/11/13 0013 14:32
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpRollInputRecordService {
    @POST("pays/moneyInHistory")
    Observable<RollInputRecordBean> getCall(@Header("token") String token,
                                            @Query("page") String page);

}

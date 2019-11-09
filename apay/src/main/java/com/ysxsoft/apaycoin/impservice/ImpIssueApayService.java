package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.IssueApayBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 发布接口
 * 日期： 2018/11/16 0016 10:37
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpIssueApayService {
    @GET("pays/pubShOrder")
    Observable<IssueApayBean> getCall(@Header("token") String token,
                                      @Query("type") String type,
                                      @Query("zf_type") String zf_type,
                                      @Query("gold") String gold,
                                      @Query("money") String money,
                                      @Query("pwd") String pwd);
}

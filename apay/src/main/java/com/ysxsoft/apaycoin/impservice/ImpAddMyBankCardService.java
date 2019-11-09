package com.ysxsoft.apaycoin.impservice;


import com.ysxsoft.apaycoin.modle.AddMyBankCardBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 添加我的银行卡接口
 * 日期： 2018/11/8 0008 11:37
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpAddMyBankCardService {
    //    @Headers("token:OTcyMzUxNTQxMzkwMDIzLGFmZDgzOWI0MTgxYzZhY2NiYmI1YjY1ZThmYTMxMGZjMTZkMWE3YWU=")
//    @POST("user/myCardAdd")
    @GET("user/editMyCard")
    Observable<AddMyBankCardBean> getMyCardInfo(@Header("token") String token,
                                                @Query("khh") String khh,
                                                @Query("khr") String neme,
                                                @Query("carnum") String card_num,
                                                @Query("alipay") String alipay,
                                                @Query("pwd") String pwd);
}

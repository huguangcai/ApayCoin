package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.GoMakeMoneyBean;

import okhttp3.MultipartBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 去打款接口
 * 日期： 2018/11/19 0019 16:26
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpGoMakeMoneyService {
    @Multipart
    @POST("homes/qrmoney")
    Observable<GoMakeMoneyBean> getCall(@Header("token") String token,
                                        @Query("oid") String oid,
                                        @Part MultipartBody.Part pic);
}

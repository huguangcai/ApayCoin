package com.ysxsoft.apaycoin.impservice;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 五分钟 五小时 日线折线图接口
 */
public interface ImpBrokenLineSumService {
    @POST("homes/line")
    Call<ResponseBody> getCall(@Header("token")String token,
                               @Query("type")String type);
}

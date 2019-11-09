package com.ysxsoft.apaycoin.impservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 描述： 五日走势图接口
 * 日期： 2018/11/20 0020 14:55
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpNumAssetTuService {
    @POST("homes/wutian")
//    Observable<String> getCall(@Header("token")String token);
    Call<ResponseBody> getCall(@Header("token") String token);
}

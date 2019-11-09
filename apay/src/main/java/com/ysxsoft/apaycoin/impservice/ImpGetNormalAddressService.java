package com.ysxsoft.apaycoin.impservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface ImpGetNormalAddressService {
    @POST("user/mraddress")
//    Observable<GetNormalAddressBean> getCall(@Header("token") String token);
    Call<ResponseBody> getCall(@Header("token") String token);
 }

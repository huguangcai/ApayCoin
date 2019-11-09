package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.AddLunBoImageBean;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpAddLunBoImageService {
    @Multipart
    @POST("shops/problaneradd")
    Observable<AddLunBoImageBean> getCal(@Header("token")String token,
                                         @Query("pid") String pid,
                                         @Part MultipartBody.Part file);
}

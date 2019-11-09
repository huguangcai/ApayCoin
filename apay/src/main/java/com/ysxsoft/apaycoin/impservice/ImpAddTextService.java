package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.AddTextOrImageBean;

import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpAddTextService {
    @POST("shops/proadd")
    Observable<AddTextOrImageBean> getCall(@Header("token")String token,
                                           @Query("pid")String pid,
                                           @Query("type")String type,
                                           @Query("vals")String vals);
}

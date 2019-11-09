package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.DeleteAndModifyLunBoBean;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpDeleteAndModifyLunBoService {
    @Multipart
    @POST("shops/problaner")
    Observable<DeleteAndModifyLunBoBean> getCall(@Header("token")String token,
                                                 @Part("pid") String pid,
                                                 @Part("type")String type,
                                                 @Part MultipartBody.Part file);
}
